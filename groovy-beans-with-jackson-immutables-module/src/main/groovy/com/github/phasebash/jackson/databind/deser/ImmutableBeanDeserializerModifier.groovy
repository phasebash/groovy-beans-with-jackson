package com.github.phasebash.jackson.databind.deser

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.github.phasebash.jackson.databind.deser.std.GroovyImmutableStdValueInstantiator
import groovy.transform.Immutable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A BeanDeserializerModifier which adds a GroovyImmutableStdValueInstantiator to the builder.
 */
class ImmutableBeanDeserializerModifier extends BeanDeserializerModifier {

    private static final Logger LOG = LoggerFactory.getLogger(ImmutableBeanDeserializerModifier)

    /**
     * If the Given BeanDescription has a class annotation of groovy.transform.Immutable,
     * a GroovyImmutableStdValueInstantiator will be set on the BeanDeserializerBuilder.
     *
     * Calls superclass afterwards.
     *
     * @param config The DeserializationConfig.
     * @param beanDesc The BeanDescription, used to introspect class annotations.
     * @param builder The BeanDeserializerBuilder, a new ValueInstantiator will be set if the above state is true.
     *
     * @return Delegated to superclass.
     */
    @Override
    BeanDeserializerBuilder updateBuilder(DeserializationConfig config,
                                          BeanDescription beanDesc,
                                          BeanDeserializerBuilder builder) {
        if (is_immutable(beanDesc)) {
            AnnotatedClass classInfo = beanDesc.classInfo

            LOG.trace("Updating builder '{} to instantiate @Immutable class '{}'.", builder, classInfo)

            enable_value_instantiator(builder, config, classInfo)
        }

        super.updateBuilder(config, beanDesc, builder)
    }

    private static boolean is_immutable(BeanDescription beanDesc) {
        beanDesc.classAnnotations.get(Immutable)
    }

    private static void enable_value_instantiator(BeanDeserializerBuilder builder,
                                                  DeserializationConfig config,
                                                  AnnotatedClass classInfo) {
        builder.valueInstantiator = new GroovyImmutableStdValueInstantiator(config, classInfo.annotated)
    }
}
