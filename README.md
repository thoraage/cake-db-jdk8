Spring has recently released support for Scala (see http://blog.springsource.org/2012/12/10/introducing-spring-scala/). This of course can't go unheeded and there is only one correct way to counter this: Outlining the Cake Pattern for Java ;-)

In order to create unnecessary confusion I try to illustrate the principles by implementing an extremely simple cake database with web frontend. You'll find the repository for this at: https://github.com/thoraage/cake-db-jdk8

In order to use the code in this project now (7. January 2013) you will need to download the lambda version of JDK8. You'll find it here: http://jdk8.java.net/lambda/

## What is the Cake Pattern?

The Cake Pattern is dependency injection's type safe and annotation/xml-free cousin. It works by creating traits for each module. Traits in Scala are basically interfaces with state and methods. These traits can be layered on top of each other into a concrete class or object; a cake. You can combine these modules in any way you'd like to create different cakes according to different demands.

## Why?

Java developers have been toiling away writing XML-files and flimsy annotations for years. It's time that they get a taste of the goodness that static typing were supposed to give them.

## How?

JDK8 includes among other handsome features something called virtual extension methods. This opens for adding code into interfaces. If you know Scala you can think of it as Scala's traits on sedatives. However, since you can add as many interfaces as you like you now have a limited multiple inheritance. Here is an example of how such an interface might look:

```java
public interface AnInterface {
    default void sayHello() {
        System.out.println("Hello");
    }
}
```

## Problem with State

Since you still can't have state in interfaces I've implemented that through the _SingletonModule_ interface and the _SingletonModuleImpl_ class. This is not optimal as the class will devour our potential "real" inheritance. However, I've not found any competing use for it; yet. The _SingletonModule_ looks like this:

```java
public interface SingletonModule {
    interface Singleton {
        <M extends SingletonModule, T> T get(Class<M> clazz);
        <M extends SingletonModule, T> void put(Class<M> clazz, T t);
    }
    void initialize();
    Singleton getSingleton();
}
```

I've created an initialisation step here so that I don't have to worry about thread safety of lazy singletons. It also asserts fast failure. You can see how we put and get singletons in the _CakeJdbcDbModule_:

```java
public interface CakeJdbcDbModule extends DbModule, DbConfigurationModule, SingletonModule {
    class JdbcDb implements Db {
        ...
    }
    @Override
    default void initialize() {
        getSingleton().put(CakeJdbcDbModule.class, new JdbcDb(this));
    }
    @Override
    default Db getDb() {
        return getSingleton().get(CakeJdbcDbModule.class);
    }
}
```

Sadly the initialisation methods will have to be initialised from the top for each module with initialisation needs.

## The Stack

If we start from the top it looks like this:

```java
class CakeStack extends SingletonModuleImpl implements CakeConfigurationModule, CakeJdbcDbModule, CakePageHandlerModule, JettyWebHandlerModule {
    @Override
    public void initialize() {
        CakeJdbcDbModule.super.initialize();
        JettyWebHandlerModule.super.initialize();
    }
}
```

This represent a complete runnable stack. Here you can also observe the initialisation in action. Let us concentrate on the most important modules:

* _CakeConfigurationModule_ - Contains configuration parameters
* _CakeJdbcDbModule_ - Provides access to the database
* _CakePageHandlerModule_ - Handles page requests
* _JettyWebHandlerModule_ - Starts a web server and directs requests to the page handler

All these modules have to be added in the order of their dependencies. _JettyWebHandlerModule_ last since it depends on _CakePageHandlerModule_ and _CakeConfigurationModule_. _CakeConfigurationModule_ first as it is not dependent on anything.

To start using this stack we need only instantiate it and start the _WebHandler_:

```java
new CakeStack().getWebHandler().start();
```

The _CakeStack_ refer only concrete implementations of all the modules we need and we could change easily exchange the implementations when needed. For example by inserting a _CakeMongoDbModule_ instead of the _CakeJdbcDbModule_. This will in no way affect how the _CakePageHandlerModule_ get cakes from the database by calling 'module.getDb().getCakes()'.

## Creating a Module

Each module is implementing a method that return the needed module implementation. Default methods on the interfaces enable us to add as many modules as we please. Each module on the other hand is implemented as generic as possible:

```java
public interface CakePageHandlerModule extends PageHandlerModule, DbModule {
    class CakePageHandler implements PageHandler {
        ...
    }
    @Override
    default PageHandler getPageHandler() {
        return new CakePageHandler(this);
    }
}
```

In this example we extend the _PageHandlerModule_ and the _DbModule_. The _PageHandlerModule_ is the responsibility of this module so we implement that. The _DbModule_ however, we just request and leave for someone else to provide an implementation for us. This makes it simple to have one implementation for test and a completely different for production.

I have modelled _CakePageHandler_ as a nested class of the _CakePageHandlerModule_-interface. Notice however that I need to pass the module in as a constructor argument. A nested class B in a class A will have access to A through 'A.this'. A nested class in an interface on the other hand is more akin to a static nested class and does not really have a relation to its outer class. In order to underline the connection between the entities I've chosen to keep the module implementation inside the module interface anyway.

## Conclusion

All-in-all I'm quite happy about how this experiment unfolded. It has some drawbacks compared to applying it in Scala. Particularly the way Scala traits hold state and how nested classes in traits can directly access the trait. That aside, I believe it presents a compelling way to build an application and it would be really interesting to see how this would play out in a real project.