package de.aktey.scanndal

import de.aktey.scanndal.deepannotationscantest.{InheritedAnnotation, NotInheritedAnnotation}
import org.scalatest.{Matchers, WordSpec}

/**
  * Created with IntelliJ IDEA.
  * User: ruben
  * Date: 13.08.13
  * Time: 07:23
  */
class DeepAnnotationScanTest extends WordSpec with Matchers {

  "If the AnnotationFilter is typed by an annotation, " +
    "that is annotated with @Inherited, the scan " should {
    "find classes that have this annotation or extend a class with that annotation." in {
      val das = new DeepAnnotationScan[InheritedAnnotation]("de.aktey.scanndal.deepannotationscantest.")

      das.scan.size should be(2)
    }
  }

  "If the AnnotationFilter is typed by an annotation, " +
    "that is not annotated with @Inherited, the scan " should {
    "only find classes that have this annotation." in {
      val das = new DeepAnnotationScan[NotInheritedAnnotation]("de.aktey.scanndal.deepannotationscantest.")

      das.scan.size should be(1)
    }
  }

}
