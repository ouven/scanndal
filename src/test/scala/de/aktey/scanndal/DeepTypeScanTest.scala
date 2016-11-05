package de.aktey.scanndal

import de.aktey.scanndal.deeptypescantest.{AnInterface, AnOtherInterface, SuperSuperClass}
import org.scalatest.{Matchers, WordSpec}

/**
  * Created with IntelliJ IDEA.
  * User: ruben
  * Date: 13.08.13
  * Time: 07:23
  */
class DeepTypeScanTest extends WordSpec with Matchers {

  "If the Scanner is typed by a class, the scan " should {
    "find classes, that extend this class or extend a class, " +
      "that extends this class." in {
      val dts = new DeepTypeScan[SuperSuperClass]("de.aktey.scanndal.deeptypescantest.")
      dts.scan.size should be(2)
    }
  }


  "If the Scanner is typed by an interface, the scan " should {
    "find classes, that implement this interface or extend a " +
      "class, that implements this interface." in {
      val dts = new DeepTypeScan[AnOtherInterface]("de.aktey.scanndal.deeptypescantest.")
      dts.scan.size should be(2)
    }
  }

  "If the Scanner is typed by an interface A, that is extended by an Interface B, the scan " should {
    "find classes, that implement interface B or extend a " +
      "class, that implements interface B." in {
      val dts = new DeepTypeScan[AnInterface]("de.aktey.scanndal.deeptypescantest.")
      dts.scan.size should be(3)
    }
  }

}
