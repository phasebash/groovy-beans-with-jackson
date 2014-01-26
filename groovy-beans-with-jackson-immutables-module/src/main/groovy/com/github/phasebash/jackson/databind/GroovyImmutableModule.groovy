package com.github.phasebash.jackson.databind

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.github.phasebash.jackson.databind.deser.ImmutableBeanDeserializerModifier



/**
 * A Jackson Module which makes Jackson able to instantiate Groovy @Immutable beans.
 */
class GroovyImmutableModule extends Module {

    private final Version version = new Version(0, 1, 0, null,
            'com.github.phasebash.jackson',
            'groovy-beans-with-jackson-immutables-module')

    @Override
    String getModuleName() {
        getClass().simpleName
    }

    @Override
    Version version() {
        version
    }

    @Override
    void setupModule(Module.SetupContext context) {
        context.addBeanDeserializerModifier(new ImmutableBeanDeserializerModifier())
    }
}
