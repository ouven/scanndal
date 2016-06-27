[![Maven Central](https://img.shields.io/maven-central/v/de.aktey.scanndal/scanndal_2.11.svg?maxAge=2592000)](http://search.maven.org/#search%7Cga%7C1%7Cde.aktey.scanndal)

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


#### release 0.10
* support Java 8 class files (thx @nspekat) 

#### release 0.9
* find classes with @inherited annotations
* find classes Deep of a certain type - no matter
  if the type is from an inherited interface in an inherited class
* combine mappers to a single TupleMapper
* combine filters logically to one filter

#### release 0.8
* add inital scanner
* enable to filter by class annotations
* enable to filter by direct superclass 
* enable to filter by interface
* add mapper to map to classname
* add mapper to map to java.lang.Class
