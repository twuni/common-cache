# Twuni Commons - Cache

This library provides fundamental data structures and algorithms for applications that use caching.

## Building

This project uses the [Maven][1] build system. Check out the source code, navigate to its root
directory, and run `mvn install`.

 [1]: http://maven.apache.org/

## Using

To use this library in your Maven application or library, add the following dependency:

```xml
    <dependency>
      <groupId>org.twuni</groupId>
      <artifactId>common-cache</artifactId>
      <version>0.2.1</version>
    </dependency>
```

Pre-built versions of this and other Twuni Commons libraries are available in the Twuni
Maven Repository. To use it, add the following block to your application's **pom.xml**:

```xml
    <repositories>
      <repository>
        <id>twuni-releases</id>
        <url>https://maven.twuni.org/repository/libs-release</url>
      </repository>
    </repositories>
```

If you do not use the Maven build system, you can download them directly from
[https://maven.twuni.org/repository/libs-release/org/twuni/common-cache][2].

 [2]: https://maven.twuni.org/repository/libs-release/org/twuni/common-cache
