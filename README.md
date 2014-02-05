groovy-beans-with-jackson
=========================

Implementations of the Jackson API to support Groovy Beans,
particularly [@Immutable](http://groovy.codehaus.org/gapi/groovy/transform/Immutable.html)s

## Current Status ##
[![Build Status](https://travis-ci.org/phasebash/groovy-beans-with-jackson.png)]
(https://travis-ci.org/phasebash/groovy-beans-with-jackson)

## Use Cases ##
[Groovy Beans](http://groovy.codehaus.org/Groovy+Beans) are a particularly attractive way of developing Java Beans. Yet
when making the most of what Groovy provides, particularly the
[@Immutable](http://groovy.codehaus.org/gapi/groovy/transform/Immutable.html) transform, this presents challenges during
deserialization as Jackson will not be able to find a default constructor (or any annotations if your classes are
fully Groovy).

Consider the following Groovy Bean.

    @Immutable
    class BeanBag {
        Integer count
        String texture
        Zipper zipper
    }

Without modification, this stacktrace might look familiar:

    com.fasterxml.jackson.databind.JsonMappingException:
    No suitable constructor found for type
    [simple type, class BeanBag]:
    can not instantiate from JSON object (need to add/enable type information?)

As a first naive step, one could provide a private constructor:

    @Immutable
    class BeanBag {
        private BeanBag() {}
        Integer count
        String texture
        Zipper zipper
    }

And would could expect to receive this message:

    Groovyc: Explicit constructors not allowed for @Immutable class: BeanBag

Yet, the @Immutable transform prohibits this, any other constructors are also forbidden.

## In Use ##
In order to deserialize @Immutable GroovyBeans without fuss, simply provide the ObjectMapper with a Jackson Module
which provides the necessary ValueInstantiator to allow Jackson to instantiate GroovyBeans by their generated
HashMap constructors.

    ObjectMapper objectMapper = new ObjectMapper()
    objectMapper.registerModule(new GroovyImmutableModule())

    // this is now possible.
    BeanBag beanBag = objectMapper.readValue('{"count": 100, "texture": "smooth"}', BeanBag)

## Coordinates ##
You may make use of this module with the following coordinates:
### Maven ###
    <dependency>
        <groupId>com.github.phasebash.jackson</groupId>
        <artifactId>groovy-beans-with-jackson-immutables-module</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>

### Gradle ###
    compile(
        [groupId: 'com.github.phasebash.jackson', name: 'groovy-beans-with-jackson-immutables-module', version: 'master-SNAPSHOT']
    )

A 1.0 release is on the way.

## Questions, Feedback? ##
Feel free to submit an issue ticket through github or contact me directly.  I will help you.

## License ##
The [groovy-beans-with-jackson](https://github.com/phasebash/groovy-beans-with-jackson)
has been released under [Apache 2.0](https://github.com/phasebash/groovy-beans-with-jackson/blob/master/LICENSE.md)
as per all it's dependencies.

