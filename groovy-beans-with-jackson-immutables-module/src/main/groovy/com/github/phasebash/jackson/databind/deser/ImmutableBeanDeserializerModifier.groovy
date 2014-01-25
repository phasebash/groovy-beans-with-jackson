package com.github.phasebash.jackson.databind.deser

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.github.phasebash.jackson.databind.deser.std.GroovyImmutableStdValueInstantiator

/**
 * A BeanDeserializerModifier which adds a GroovyImmutableStdValueInstantiator to the builder.
 */
class ImmutableBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    BeanDeserializerBuilder updateBuilder(DeserializationConfig config,
                                          BeanDescription beanDesc,
                                          BeanDeserializerBuilder builder) {
        BeanDescription beanDescription = config.introspectDirectClassAnnotations(beanDesc.type)

        if (beanDescription.classAnnotations.get(groovy.transform.Immutable)) {
            builder.setValueInstantiator(new GroovyImmutableStdValueInstantiator(config, beanDesc.class))
        }

        super.updateBuilder(config, beanDesc, builder)
    }
}
