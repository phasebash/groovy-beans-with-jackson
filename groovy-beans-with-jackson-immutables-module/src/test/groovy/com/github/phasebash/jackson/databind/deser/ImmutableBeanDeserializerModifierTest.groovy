package com.github.phasebash.jackson.databind.deser

import static org.hamcrest.Matchers.isA

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.util.Annotations
import com.github.phasebash.jackson.databind.deser.std.GroovyImmutableStdValueInstantiator
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

/**
 * Tests for the ImmutableBeanDeserializerModifier.
 */
@WithGMock
class ImmutableBeanDeserializerModifierTest {

    ImmutableBeanDeserializerModifier modifier

    BeanDescription mockBeanDesc
    DeserializationConfig mockContext
    BeanDeserializerBuilder mockBuilder
    Annotations mockAnnotations
    AnnotatedClass mockClassInfo


    @Before
    void setUp() {
        modifier = new ImmutableBeanDeserializerModifier()

        mockBeanDesc = mock(BeanDescription)
        mockContext = mock(DeserializationConfig)
        mockBuilder = mock(BeanDeserializerBuilder)
        mockAnnotations = mock(Annotations)
        mockClassInfo = mock(AnnotatedClass)
    }

    @Test
    void 'should only delegate to super when BeanDescription does not match criteria'() {
        mockAnnotations.get(groovy.transform.Immutable).returns(false).once()
        mockBeanDesc.getClassAnnotations().returns(mockAnnotations).once()

        play {
            assert modifier.updateBuilder(mockContext, mockBeanDesc, mockBuilder)
        }
    }

    @Test
    void 'should augment builder when BeanDescription matches criteria'() {
        final Class<?> annotatedClass = HashMap
        mockAnnotations.get(groovy.transform.Immutable).returns(true).once()
        mockBeanDesc.getClassAnnotations().returns(mockAnnotations).once()

        mockClassInfo.getAnnotated().returns(annotatedClass).once()
        mockBeanDesc.getClassInfo().returns(mockClassInfo).once()

        mockBuilder.setValueInstantiator(isA(GroovyImmutableStdValueInstantiator)).once()

        play {
            assert modifier.updateBuilder(mockContext, mockBeanDesc, mockBuilder)
        }
    }
}
