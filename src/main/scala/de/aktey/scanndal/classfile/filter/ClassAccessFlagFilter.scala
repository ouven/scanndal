package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 16.07.13
 * Time: 06:48
 */
object ClassAccessFlag {
	val PUBLIC = 0x0001
	val PRIVATE = 0x0002
	val PROTECTED = 0x004
	val STATIC = 0x008
	val FINAL = 0x0010
	val SUPER = 0x0020
	val INTERFACE = 0x0200
	val ABSTRACT = 0x0400
	val SYNTHETIC = 0x1000
	val ANNOTATION = 0x2000
	val ENUM = 0x4000
}

object FieldAccessFlag {
	val PUBLIC = 0x0001
	val PRIVATE = 0x0002
	val PROTECTED = 0x004
	val STATIC = 0x008
	val FINAL = 0x0010
	val VOLATILE = 0x0040
	val TRANSIENT = 0x0080
	val SYNTHETIC = 0x1000
	val ENUM = 0x4000
}

object MethodAccessFlag {
	val PUBLIC = 0x0001
	val PRIVATE = 0x0002
	val PROTECTED = 0x004
	val STATIC = 0x008
	val FINAL = 0x0010
	val SYNCHRONIZED = 0x0020
	val BRIDGE = 0x0040
	val VARARGS = 0x0080
	val NATIVE = 0x0100
	val ABSTRACT = 0x0400
	val STRICT = 0x0800
	val SYNTHETIC = 0x1000
}

/**
 * test class files for with certain access flags
 *
 * @param flagBits combine flags with bit operations
 */
class ClassAccessFlagFilter(flagBits: Int) extends Filter {

	/**
	 * @param classFile current classfile
	 * @return true for keep
	 */
	override def apply(classFile: ClassFile): Boolean = {
		(classFile.accessFlags & flagBits) > 0
	}
}
