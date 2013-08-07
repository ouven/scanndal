package de.aktey.scanndal

import org.specs2.mutable.Specification

/**
 * User: ruben
 * Date: 20.07.13
 * Time: 11:40
 */
class ScannerTest extends Specification {

	"scanndal " should {
		"find classes in packages" in {
			Scanndal("de.aktey.scanndal.scannertest").scan.size must_== 1
		}
	}

}
