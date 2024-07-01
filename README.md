# Introduction

If you are only interested in how to use  this library, go to [Getting Started](#getting-started). In case you are interested in some background and context, just start with the next chapter.

# Versioned endpoints

There are several ways of doing versioned endpoints. I prefer solutions with the following characteristics:
1. the version is part of the url
2. consumers use one version number for all endpoints (so they do not have to keep track of different version for every endpoint)
3. adding a new version should be simple
4. old versions should still work (backwards compatibility)
5. a minimal amount of extra code

This library enables you to use versioned endpoints with the mentioned characteristics in Spring Boot. This is done by slightly modified annotations.

# Explaining the problem

Let's start with an example of a simple REST endpoint.

    @GetMapping("/v1/random")
    public String random1() {
        return String.format("%s", Math.random());
    }

Notice that this example satisfies the first characteristic.

Assume that there are other endpoints as well. The endpoints will also start with /v1, because the second characteristics says that the consumer only has to use one version number for all endpoints.

Now we would like to introduce a new version of the random endpoint above. This would look like this:

    @GetMapping("/v2/random")
    public String random2() {
        return String.format("Random number: %s", Math.random());
    }

Remember that we use one version number for every endpoint (characteristic 2) and that old versions should still work (characteristic 3). As a consequence we have to add a v2 endpoint for every other existing endpoint. That sounds like a lot of work!

# Simple solution

A simple, but verbose solution for this problem is the following approach:

    @GetMapping("/v{version}/random")
    public String random2(@PathVariable int version) {
        return switch (version) {
            case 1 -> random1();
            default -> random2();
        };
    }

A @PathVariable is introduced, and now we use a switch expression to get the right response for the requested version. The default branch is always used for the latest version. This way we can relatively easy add versions without a lot of duplication. Please notice that we have to introduce the PathVariable and the switch expression for every other endpoint as well. So still a lot of work! But luckily this is a one time operation.

Once every endpoint has this mechanism in place it works and it has the first four characteristics. It does not really have the fifth characteristic, because this solution introduces quit some extra code. It needs a PathVariable in the URL and as argument of the method. Furthermore, it introduces a switch expression. 

# Better solution

This library provides an alternative to this solution that also satisfies the fifth characteristic: a minimal amount of extra code. Please have a look at the following example.

    @GetVersionMapping(value = "/random", version = 1)
    public String random1() {
        return String.format("%s", Math.random());
    }

    @GetVersionMapping(value = "/random", version = 2)
    public String random2() {
        return String.format("Random number: %s", Math.random());
    }

A slight modified version of the GetMapping annotation is introduced: the GetVersionMapping annotation. Next to specifying a path (or value) you can also specify a version. It will prefix the version number, so the first endpoint becomes '/1/random' and the second becomes '/2/random'. 

This works really wel with low version numbers, but this is still a lot of work when you have a lot of version numbers, because you have to add an annotation for every new version number for every endpoint.

# Best solution

To solve this last problem ranged versions are introduces. Please have a look at this example.

    @GetVersionMapping(value = "/random", versions = {1, 10})
    public String random1() {
        return String.format("%s", Math.random());
    }

    @GetVersionMapping(value = "/random", versions = {11, 20})
    public String random11() {
        return String.format("Random number: %s", Math.random());
    }

This concludes the explanation why this library is created. The remainder of the readme aims to give more details.

# Getting started

Follow the three steps below to quickly get started.

## Add the dependency to the POM

To use this library in a Maven project add the following dependancy:

        <dependency>
            <groupId>io.github.boukenijhuis</groupId>
            <artifactId>dynamic-version-url</artifactId>
            <version>0.0.1</version>
        </dependency>

For Gradle add the following line:

    compile "io.github.boukenijhuis:dynamic-version-url:0.0.1"

Please check if the version in the dependency above is the latest version!

## Add a custom mapping handler

Add the following class to your project:

    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
    import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
    
    @Configuration
    public class WebMvcConfig extends DelegatingWebMvcConfiguration 
    {
        @Override
        public RequestMappingHandlerMapping createRequestMappingHandlerMapping() 
        {
            return new ApiVersionRequestMappingHandlerMapping("v");
        }
    }

The name of the class can be changed to your liking. For more information about the "v" parameter, please check the [Reference](#Reference) section. 

## Add new annotations

Start adding the new versioned annotations to your project. See [Best solution](#best-solution) for examples.

# Reference

- optional postfix string for version numbers
- every (?) request method is supported: GET, POST, PUT, DELETE, PATCH
- version vs versions
- default version

Errors:
- overlapping version numbers
- more than two version numbers


# Best practice

- start with add 'Version' to the annotation
- specify the version
- start using ranges
- use a constant to denote the oldest & newest version (with example)

# Future

- add postfix option for version numbers



