package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 15:28
 */

/**
 * test class files if they directly extend a superclass
 * @param canonicalName the canonical name of the superclass
 */
class SuperClassNameFilter(canonicalName: String) extends Filter with ClassFileInterpretation {
	val superTypeInfo = fromCanonicalName(canonicalName)

	def apply(cf: ClassFile) = cf.superClassName == superTypeInfo
}
