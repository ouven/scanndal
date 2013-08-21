package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 09.08.13
 * Time: 23:33
 */

/**
 * test class files if they directly implement an interface
 * @param canonicalName the canonical name of the interface
 */
class InterfaceNameFilter(canonicalName: String) extends Filter with ClassFileInterpretation {
	val interfaceName = fromCanonicalName(canonicalName)

	def apply(classFile: ClassFile) = classFile
		.interfaces.toStream
		.map(classFile.cpClass)
		.map(classFile.cpUtf8String)
		.exists(_ == interfaceName)
}
