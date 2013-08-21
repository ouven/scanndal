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
 * @param canonicalName the canonical name of the annotation
 */
class ClassAnnotationNameFilter(canonicalName: String) extends Filter with ClassFileInterpretation {
	val annotationTypeDescriptor = "L" + fromCanonicalName(canonicalName) + ";"

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
