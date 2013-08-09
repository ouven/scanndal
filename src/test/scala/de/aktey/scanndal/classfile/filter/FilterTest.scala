package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.Scanndal
import org.specs2.mutable.Specification
import de.aktey.scanndal.annotationfiltertest.MyRuntimeAnnotation
import de.aktey.scanndal.superclassfiltertest.{SuperSuperClass, SuperClass}
import de.aktey.scanndal.interfacefiltertest.{TestTrait2, TestTrait1, TestInterface}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 22.07.13
 * Time: 21:03
 */
class FilterTest extends Specification {

  "A scanndal result " should {
    "be filterable by class access flags" in {
      Scanndal("de.aktey.scanndal.accessfiltertest")
        .scan
        .count(new ClassAccessFlagFilter(ClassAccessFlag.PUBLIC)) must_== 1
    }
  }

  "A scanndal result " should {
    "be filterable by annotations" in {
      Scanndal("de.aktey.scanndal.annotationfiltertest")
        .scan
        .count(new ClassAnnotationFilter[MyRuntimeAnnotation]()) must_== 1
    }
  }

  "A scanndal result " should {
    val scanResult = Scanndal("de.aktey.scanndal.superclassfiltertest").scan.toList

    "be filterable by super classes" in {
      scanResult.count(new SuperClassFilter[SuperClass]()) must_== 1
    }

    "not find deep super classes" in {
      scanResult.count(new SuperClassFilter[SuperSuperClass]()) must_== 1
    }
  }

  "A scanndal result " should {
    val scanResult = Scanndal("de.aktey.scanndal.interfacefiltertest").scan.toList

    "be filterable by an interface" in {
      scanResult.count(new InterfaceFilter[TestInterface]()) must_== 1
    }

    "be filterable by a trait with implementations" in {
      scanResult.count(new InterfaceFilter[TestTrait2]()) must_== 1
    }

    "be filterable by a trait without implementations" in {
      scanResult.count(new InterfaceFilter[TestTrait1]()) must_== 1
    }
  }

}
