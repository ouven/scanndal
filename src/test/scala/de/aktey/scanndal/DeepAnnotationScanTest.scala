package de.aktey.scanndal

import org.specs2.mutable.Specification
import de.aktey.scanndal.deepannotationscantest.{NotInheritedAnnotation, InheritedAnnotation}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 13.08.13
 * Time: 07:23
 */
class DeepAnnotationScanTest extends Specification {

	"If the AnnotationFilter is typed by an annotation, " +
		"that is annotated with @Inherited, the scan " should {
		"find classes that have this annotation or extend a class with that annotation." in {
			val das = new DeepAnnotationScan[InheritedAnnotation]("de.aktey.scanndal.deepannotationscantest.")

			das.scan.size must_== 2
		}
	}

	"If the AnnotationFilter is typed by an annotation, " +
		"that is not annotated with @Inherited, the scan " should {
		"only find classes that have this annotation." in {
			val das = new DeepAnnotationScan[NotInheritedAnnotation]("de.aktey.scanndal.deepannotationscantest.")

			das.scan.size must_== 1
		}
	}

}
