package de.aktey.scanndal.classfile


/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 15:59
 */

/**
 * mixin with helpers to process class file contents
 */
trait ClassFileInterpretation {

	/**
	 * extend a de.aktey.scanndal.classfile.ClassFile
	 */
	implicit def extendClassFile(classFile: ClassFile) = new ExtendedClassFile(classFile)

	/**
	 * extend a de.aktey.scanndal.classfile.ConstantPoolEntryUtf8
	 */
	implicit def extendConstantPoolEntryUtf8(cpe: ConstantPoolEntryUtf8) = new ExtendedConstantPoolEntryUtf8(cpe)

	@inline def toCanonicalName(cpUtf8Entry: String) = cpUtf8Entry.replace('/', '.')

	@inline def fromCanonicalName(canonicalName: String) = canonicalName.replace('.', '/')

	/**
	 * @param classFile class file to extend
	 */
	class ExtendedClassFile(classFile: ClassFile) {
		/**
		 * get an index of a class name from the constant pool
		 *
		 * @param idx index of the entry
		 * @return constant pool index of the class name
		 * @throws de.aktey.scanndal.classfile.ClassFileInterpretationException if entry is null or is not a de.aktey.scanndal.classfile.ConstantPoolEntryClass
		 */
		def cpClass(idx: Int) = classFile.constantPool(idx) match {
			case cpe: ConstantPoolEntryClass if cpe != null => cpe.nameIdx
			case e => throw new ClassFileInterpretationException(
				"constant pool entry with index %d has to be a %s, but was %s".format(
					idx,
					classOf[ConstantPoolEntryClass],
					if (e == null) "null" else e.getClass
				)
			)
		}

		/**
		 * get a string from a  de.aktey.scanndal.classfile.ConstantPoolEntryUtf8
		 * from the constant pool.
		 *
		 * @param idx index of the entry
		 * @return represented sting
		 * @throws de.aktey.scanndal.classfile.ClassFileInterpretationException if entry is null or not a de.aktey.scanndal.classfile.ConstantPoolEntryUtf8
		 */
		def cpUtf8String(idx: Int) = classFile.constantPool(idx) match {
			case cpe: ConstantPoolEntryUtf8 if cpe != null => new ExtendedConstantPoolEntryUtf8(cpe).string
			case e => throw new ClassFileInterpretationException(
				"constant pool entry with index %d has to be a %s, but was %s".format(
					idx,
					classOf[ConstantPoolEntryUtf8],
					if (e == null) "null" else e.getClass
				)
			)
		}

		/**
		 * @return index of the super class name
		 */
		def superClassNameIndex = cpClass(classFile.superClass)

		/**
		 * @return super class name
		 */
		def superClassName = cpUtf8String(superClassNameIndex)

		/**
		 * @return index of the class name
		 */
		def thisClassNameIndex = cpClass(classFile.thisClass)

		/**
		 * @return class name
		 */
		def thisClassName = cpUtf8String(thisClassNameIndex)
	}

	/**
	 * @param constantPoolEntry utf8 pool entry to extend
	 */
	class ExtendedConstantPoolEntryUtf8(constantPoolEntry: ConstantPoolEntryUtf8) {
		/**
		 * @return represented String utf-8 decoded
		 */
		def string = new String(constantPoolEntry.bytes, "UTF-8")
	}

}

class ClassFileInterpretationException(val message: String) extends RuntimeException(message)
