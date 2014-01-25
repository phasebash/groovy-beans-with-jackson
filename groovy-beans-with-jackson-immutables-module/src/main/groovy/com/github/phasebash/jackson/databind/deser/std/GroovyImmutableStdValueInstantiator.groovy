package com.github.phasebash.jackson.databind.deser.std

import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator

/**
 * A StdValueInstantiator which can work with Groovy @Immutable beans.
 */
class GroovyImmutableStdValueInstantiator extends StdValueInstantiator {

    private static final Map EMPTY_MAP = [:].asImmutable() as HashMap

    Class<?> clazz

    GroovyImmutableStdValueInstantiator(DeserializationConfig config, Class<?> valueType) {
        super(config, valueType)
        clazz = valueType
    }

    @Override
    boolean canCreateUsingDefault() {
        true
    }

    @Override
    Object createUsingDefault(DeserializationContext ctxt) {
        clazz.newInstance(EMPTY_MAP)
    }
}
