package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.Scanndal
import de.aktey.scanndal.classmappertest.ClassMapperTestClass
import org.scalatest.{Matchers, WordSpec}

/**
  * Created with IntelliJ IDEA.
  * User: ruben
  * Date: 04.08.13
  * Time: 19:47
  */
class MapperTest extends WordSpec with Matchers {

  "The ClassMapper " should {
    "map all result class files to a loaded java class" in {
      Scanndal("de.aktey.scanndal.classmappertest")
        .scan
        .map(new ClassMapper[ClassMapperTestClass])
        .headOption
        .map(_.newInstance())
        .map(_.foo()) should contain("bar")
    }
  }

  "The ClassNameMapper " should {
    "map all result class files class names" in {
      Scanndal("de.aktey.scanndal.classnamemappertest")
        .scan
        .map(new ClassNameMapper)
        .toList should be(List("de.aktey.scanndal.classnamemappertest.WhatsMyName"))
    }
  }

}
