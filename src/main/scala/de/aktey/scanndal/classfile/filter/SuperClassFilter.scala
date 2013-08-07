package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 15:28
 *
 * filters for all classes with the direct super type S
 */
class SuperClassFilter[S: Manifest] extends Filter with ClassFileInterpretation {
	val superTypeInfo = fromCanonicalName(manifest[S].erasure.getCanonicalName)

	def apply(cf: ClassFile) = cf.superClassName == superTypeInfo
}
