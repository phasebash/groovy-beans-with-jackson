package com.github.phasebash.jackson.databind.deser.std

import org.junit.Test

import groovy.transform.Immutable

/**
 * Tests for the GroovyImmutableStdValueInstantiator.  This class assumes to be working with a type that it is built
 * to handle.  The invariant in play is provided by the ImmutableBeanDeserializerModifier.
 */
class GroovyImmutableStdValueInstantiatorTest {

    GroovyImmutableStdValueInstantiator instatiator


    @Test
    void 'should construct the given groovy type with map constructor'() {
        instatiator = new GroovyImmutableStdValueInstantiator(null, Bean)

        assert instatiator.canCreateUsingDefault()
        assert instatiator.createUsingDefault(null)
        assert instatiator.createUsingDefault(null) instanceof Bean
    }

    @Test
    void 'should report as being able to create given default'() {
        assert new GroovyImmutableStdValueInstantiator(null, Bean).canCreateUsingDefault()
    }

    @Test
    void 'should create an object with default values'() {
        def bean = new GroovyImmutableStdValueInstantiator(null, WithDefaults).createUsingDefault(null)

        assert bean.a == 'mewtew'
        assert bean.b == null
    }

    @Test
    void 'should not actually be bound to @Immutable, only the constructor it provides'() {
        def bean = new GroovyImmutableStdValueInstantiator(null, PsuedoBean).createUsingDefault(null)

        assert bean
        assert !bean.a
        assert bean.b == 'constructor set value'
    }

    @Immutable
    static class Bean {
        String a
        String b
    }

    @Immutable
    static class WithDefaults {
        String a = 'mewtew'
        String b
    }

    static class PsuedoBean {
        def a
        def b

        PsuedoBean(Map props) {
            this.a = props.a
            this.b = 'constructor set value'
        }
    }
}
