# Scanndal
Scanndal is a library, which enables software developers to scan
the classpath for classes without loading them via a classloader.

Main design goal:
 * To have no transient maven dependencies, except the scala-library.

Most libraries of this kind utilize an opcode engineering library like
asm, bcel or javaassist and any logging tool and other frameworks.
The most common case, I used classpath scanning was, when I wrote some
kind of runners, which bring their own dependencies into the pool, so
solving version clashes became daily business.

The library is highly influenced by ronmamos reflections library at
googlecode (https://code.google.com/p/reflections/) and the apache bcel
project.

## Latest release
Scanndal is available in the maven central repository.
```xml
<!-- usage with maven -->
<dependency>
    <groupId>de.aktey.scanndal</groupId>
    <artifactId>scanndal_2.9.3</artifactId>
    <version>0.8</version>
</dependency>
```
```scala
// usage with sbt
libraryDependencies += "de.aktey.scanndal" %% "scanndal" % "0.8"
```

#### release 0.8
* add inital scanner
* enable to filter by class annotations
* enable to filter by direct superclass 
* enable to filter by interface
* add mapper to map to classname
* add mapper to map to java.lang.Class
