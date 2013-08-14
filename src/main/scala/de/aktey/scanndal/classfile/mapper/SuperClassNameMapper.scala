package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 20.07.13
 * Time: 12:06
 */

/**
 * maps to the canonical class name of the superclass
 */
class SuperClassNameMapper extends Mapper[String] with ClassFileInterpretation {
	def apply(cf: ClassFile) = toCanonicalName(cf.superClassName)
}
