package com.github.phasebash.jackson.databind

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test

import groovy.transform.Immutable

/**
 * Integration-level testing of the GroovyImmutableModule and the Jackson ObjectMapper.
 */
class GroovyImmutableModuleTest {

    // the module under test.
    Module module

    // used for testing but not under test.
    ObjectMapper auxiliaryMapper

    @Before
    void setUp() {
        module = new GroovyImmutableModule()
        auxiliaryMapper = new ObjectMapper()
    }

    @Test(expected = JsonMappingException)
    void 'should throw without module registered'() {
        plain().readValue('{"a":"a long lost land"}', Bean)
    }

    @Test
    void 'should not throw when module registered'() {
        registered().readValue('{"a": "a long lost road"}', Bean)
    }

    @Test
    void 'should completely deserialize an Immutable with depth 0'() {
        Map prototype = [
            a: 'one',
            b: 100,
            c: false,
            d: 1.1F,
            e: [1, '3', false],
            f: [bar: 'baz']
        ]

        String s = json(prototype)

        Bean b = registered().readValue(s, Bean)

        prototype.properties.each {
            assert prototype[it] == b."${it}"
        }
    }

    @Test
    void 'should completely deserialize an Immutbale with depth 2'() {
        Map prototype = [
            a: 'a string',
            bean: [
                a: 'one',
                b: 100,
                c: false,
                d: 1.1F,
                e: [1, '3', false],
                f: [bar: 'baz']
            ]
        ]

        prototype.nested = new HashMap(prototype)

        String s = json(prototype)

        Nested n = registered().readValue(s, Nested)

        assert n.a == 'a string'
        assert n.nested
        assert n.nested.a == 'a string'
        assert n.nested.bean
        assert n.nested.bean.a == 'one'
    }

    @Test
    void 'should not die when deserializing non-Immutable without HashMap constructor'() {
        registered().readValue(json([a: 'one', b: 'two']), NoMapConstructorHasAnnotations)
    }

    @Test(expected = JsonMappingException)
    void 'should die when deserilizing non-Immutable without HashMap constructor and without annotations'() {
        registered().readValue(json([a: 'one', b: 'two']), NoMapConstructorLacksAnnotations)
    }

    private json(def map) {
        auxiliaryMapper.writeValueAsString(map)
    }

    private ObjectMapper registered() {
        plain().registerModule(module)
    }

    private static ObjectMapper plain() {
        new ObjectMapper()
    }

    @Immutable
    static class Bean {
        String a
        int b
        boolean c
        float d
        List e
        Map f
    }

    @Immutable
    static class Nested {
        String a
        Bean bean
        Nested nested
    }

    static class NoMapConstructorHasAnnotations {
        String a
        String b

        NoMapConstructorHasAnnotations(@JsonProperty(value = 'a') String a, @JsonProperty(value = 'b') String b) {
            this.a = a
            this.b = b
        }
    }

    static class NoMapConstructorLacksAnnotations {
        String a
        String b

        NoMapConstructorLacksAnnotations(String a, String b) {
            this.a = a
            this.b = b
        }
    }
}
