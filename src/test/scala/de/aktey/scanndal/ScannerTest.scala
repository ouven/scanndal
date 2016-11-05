package de.aktey.scanndal

import org.scalatest.{Matchers, WordSpec}

/**
  * User: ruben
  * Date: 20.07.13
  * Time: 11:40
  */
class ScannerTest extends WordSpec with Matchers {

  "scanndal " should {
    "find classes in packages" in {
      Scanndal("de.aktey.scanndal.scannertest").scan.size should be(1)
    }
  }

}
