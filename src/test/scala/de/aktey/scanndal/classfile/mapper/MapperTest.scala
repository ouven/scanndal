package de.aktey.scanndal.classfile.mapper

import org.specs2.mutable.Specification
import de.aktey.scanndal.Scanndal
import de.aktey.scanndal.classmappertest.ClassMapperTestClass

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 19:47
 */
class MapperTest extends Specification {

	"The ClassMapper " should {
		"map all result class files to a loaded java class" in {
			Scanndal("de.aktey.scanndal.classmappertest")
				.scan
				.map(new ClassMapper[ClassMapperTestClass])
				.headOption
				.map(_.newInstance())
				.map(_.foo()) must_== Some("bar")
		}
	}

	"The ClassNameMapper " should {
		"map all result class files class names" in {
			Scanndal("de.aktey.scanndal.classnamemappertest")
				.scan
				.map(new ClassNameMapper)
				.toList must_== List("de.aktey.scanndal.classnamemappertest.WhatsMyName")
		}
	}

}
