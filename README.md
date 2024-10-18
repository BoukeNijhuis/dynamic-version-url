# Introduction

If you are only interested in how to use  this library, go to [Getting Started](#getting-started). In case you are interested in some background and context, just start with the next chapter.

# Versioned endpoints

There are several ways of doing versioned endpoints. I prefer solutions with the following characteristics:
1. the version is part of the url
2. consumers use one version number for all endpoints 
3. old versions should still work (backwards compatibility)
4. adding a new version should be simple/fast
5. and require a minimal amount of extra code

The reason for the second characteristic is as follows: the consumer does not have to keep track of different version for every endpoint. The solution should be as simple as possible for the consumer.

This library enables you to use versioned endpoints with the mentioned characteristics in Spring Boot. This is done by slightly modified annotations.

# Explaining the problem

Let's start with an example of a simple REST endpoint.

    @GetMapping("/v1/a")
    public String a1() {
        return "a1";
    }

Notice that this example satisfies the first characteristic.

Assume that there are other endpoints as well. The endpoints will also start with /v1, because the second characteristics says that the consumer only has to use one version number for all endpoints.

Now we would like to introduce a new version of the random endpoint above. This would look like this:

    @GetMapping("/v2/a")
    public String a2() {
        return "a2";
    }

Remember that we use one version number for every endpoint (characteristic 2) and that old versions should still work (characteristic 3). As a consequence we have to add a v2 endpoint for every other existing endpoint. That sounds like a lot of work!

# Simple solution

A simple, but verbose solution for this problem is the following approach:

    @GetMapping("/v{version}/a")
    public String a(@PathVariable int version) {
        return switch (version) {
            case 1 -> a1();
            default -> a2();
        };
    }

A @PathVariable is introduced, and now we use a switch expression to get the right response for the requested version. The default branch is always used for the latest version. This way we can relatively easy add versions without a lot of duplication. Please notice that we have to introduce the PathVariable and the switch expression for every other endpoint as well. So still a lot of work! But luckily this is a one time operation.

Once every endpoint has this mechanism in place it works and it has the first four characteristics. It does not really have the fifth characteristic, because this solution introduces quite some extra code. It needs a PathVariable in the URL and as argument of the method. Furthermore, it introduces a switch expression. 

# Better solution

This library provides an alternative to this solution that also satisfies the fifth characteristic: a minimal amount of extra code. Please have a look at the following example.

    @GetVersionMapping(value = "/a", version = 1)
    public String a1() {
        return "a1";
    }

    @GetVersionMapping(value = "/a", version = 2)
    public String a2() {
        return "a2";
    }

A slight modified version of the GetMapping annotation is introduced: the GetVersionMapping annotation. Next to specifying a path (or value) you can also specify a version. It will prefix the version number, so the first endpoint becomes '/1/random' and the second becomes '/2/random'. 

This works really wel with low version numbers, but this is still a lot of work when you have a lot of version numbers, because you have to add an annotation for every new version number for every endpoint.

# Even better solution

To solve this last problem ranged versions are introduces. Please have a look at this example.

    @GetVersionMapping(value = "/a", versions = {1, 10})
    public String a1() {
        return "a1";
    }

    @GetVersionMapping(value = "/a", versions = {11, 20})
    public String a11() {
        return "a11";
    }

This is already pretty good, but imagine having a lot of endpoints. In case of adding a version you have to change all those endpoints. Pffff, tiring! And it is not fast (characteristic 4).

# Best solution

It turns out there is a better way by introducing a constant for the newest (or latest) version. When every endpoint uses this constant as upper limit, a change become almost effortless. Please take a look at this example.

    private static final int LATEST_VERSION = 10;

    @GetVersionMapping(value = "/a", versions = {1, LATEST_VERSION})
    public String a() {
        return "a";
    }

Every change takes only two small steps:
* the change itself (which will introduce a new method with a version mapping annotation)
* an increment of the constant

The latter makes sure that every other endpoint also supports the new version number.

Let's see if this solution satisfies all characteristics mentioned at the start of this text.

1. the version is part of the url ✅
2. consumers use one version number for all endpoints ✅
3. old versions should still work ✅
4. adding a new version should be simple/fast ✅
5. and require a minimal amount of extra code ✅

This concludes the explanation why this library is created. The remainder of the readme aims to give more details.

# Getting started

Follow the three steps below to quickly get started.

## Add the dependency to the POM

To use this library in a Maven project add the following dependancy:

        <dependency>
            <groupId>io.github.boukenijhuis</groupId>
            <artifactId>dynamic-version-url</artifactId>
            <version>0.0.3</version>
        </dependency>

For Gradle add the following line:

    compile "io.github.boukenijhuis:dynamic-version-url:0.0.3"

Please check if the version in the dependency above is the latest version!

## Change the version prefix string

By default, the version is prefixed with the character 'v'. You can override this with the following property: 
  
    version.prefix

This can be done in the application.properties in the resources directory.

## Add new annotations

Start adding the new versioned annotations to your project. See [Best solution](#best-solution) for an example.

# Reference

- copy of existing mapping annotation
- optional prefix string for version numbers
- the following request method is supported: GET, POST, PUT, DELETE, PATCH
- version vs versions
- default version

The range mechanism breaks the Spring boot mechanism that checks for ambiguous mappings. Therefor it will not detect overlapping version ranges. It does work for single version mappings, but not for ranged versions mappings. The reason that is does not work is the fact that the check only expects single version mappings. Which is logical because vanilla Spring does not know about ranged versions.

Errors:
- overlapping version numbers -> Ambiguous version mapping found with the following URL: URL
- more than two version numbers -> Too many versions (VERSIONS) specified on ANNOTATION with path PATH.


# Migration guid

- add library to POM
- add a custom mapping handler
- add 'Version' in the annotation name
- add the version attribute in the annotation
- postfix method names with the first version
- start using ranges
- use a constant to denote the newest version (with example)

# Future

- add postfix option for version numbers



