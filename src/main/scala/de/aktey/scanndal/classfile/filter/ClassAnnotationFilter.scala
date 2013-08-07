package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, RuntimeVisibleAnnotationsAttribute, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 24.07.13
 * Time: 23:23
 */

/**
 * test class files for a class annotation A
 * @tparam A the annotation class
 */
class ClassAnnotationFilter[A: Manifest] extends Filter with ClassFileInterpretation {
	val annotationTypeDescriptor = "L" + fromCanonicalName(manifest[A].erasure.getCanonicalName) + ";"

	/**
	 * @param cf classfile to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = cf.attributes.exists {
		case attr: RuntimeVisibleAnnotationsAttribute =>
			attr.annotations.exists {
				anno => cf.cpUtf8String(anno.typeIndex) == annotationTypeDescriptor
			}
		case _ => false
	}
}
