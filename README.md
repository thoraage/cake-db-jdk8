# Cake Pattern in JDK8

Spring has recently released support for Scala (see http://blog.springsource.org/2012/12/10/introducing-spring-scala/).
This of course can't go unheeded and there is only one correct way to counter this: Outlining the Cake Pattern for Java
;-)

In order to create unnecessary confusion I try to illustrate the principles by implementing an extremely simple cake
database with web frontend through the Cake Pattern.

## What is the Cake Pattern?

The Cake Pattern is dependency injection's type safe and annotation/xml-free cousin. It works by creating traits for
each module. Traits in Scala are basically interfaces with state and methods. These traits are then layered on top of
each other into a concrete class or object; a cake. You can combine these modules in different ways to create different
cakes according to different demands.

## Why?

Java developers have been toiling away writing XML-files and flimsy annotations for years. It's time that they get a
taste of the goodness that static typing were supposed to give them.

## How?

JDK8 includes among other handsome features something called virtual extension methods. This opens for adding code into
interfaces and since you can add as many interfaces as you like you now have a limited multiple inheritance. Since we
still can't have state in interfaces I've implemented that through the SingletonModule interface and the
SingletonModuleImpl class.

In order to use the code in this project now (22. december 2012) you will need to download the lambda version of JDK8.
You'll find it here: http://jdk8.java.net/lambda/

## How it's done?

If we start from the top it looks like this:

```java
    class CakeStack extends SingletonModuleImpl implements CakeConfigurationModule, JdbcDbModule, CakePageHandlerModule, JettyWebHandlerModule {
        @Override
        public void initialize() {
            JdbcDbModule.super.initialize();
            JettyWebHandlerModule.super.initialize();
        }
    }
```

This represent a complete runnable stack. To start using this stack we need only instantiate it and start the WebHandler:

```java
    new CakeStack().getWebHandler().start();
```

The CakeStack contain only concrete implementations of all the modules and we could change easily change the
implementations when needed. For example by inserting a MongoDbModule instead of the JdbcDbModule. This will in  no way
change how the CakePageHandlerModule can get cakes from the the database by calling '???.getDb().getCakes()'.

Each module will have a method that return the needed dependency. Adding code to interfaces enables us to add as many
modules as we please. Each module on the other hand is implemented as generic as possible:

```java
    public interface JdbcDbModule extends DbModule, DbConfigurationModule, SingletonModule {
        class JdbcDb implements Db {
            ...
        }
        @Override
        default void initialize() {
            getSingleton().put(JdbcDbModule.class, new JdbcDb());
        }
        @Override
        default Db getDb() {
            return getSingleton().get(JdbcDbModule.class);
        }
    }
```

Here we request the DbConfigurationModule, but we have no dependency on how it is implemented. That makes it simple to
have one implementation for test and a completely different for production. We could for example have an implementation
with dynamic proxies and property files where we would define the JDBC driver class like this:

```properties
    dbConfiguration.databaseDriverClass=org.h2.Driver
```

The concrete class


* Inner class doesn't real have a relationship to the interface so we inject it as module
* Ordering important; most basic first
* SingletonModuleImpl: Might need other implementations
* How to initialise: Scala can declare variables in the scope
* Dirty shameful details added to the framework package

<div id="disqus_thread"></div>
<script type="text/javascript">var disqus_shortname = 'cakedbjdk8poc';(function() {var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);})();</script>
<noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
<a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
