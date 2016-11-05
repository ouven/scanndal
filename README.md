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
