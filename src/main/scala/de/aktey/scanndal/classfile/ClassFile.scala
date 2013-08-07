package de.aktey.scanndal.classfile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 16.07.13
 * Time: 21:29
 *
 * structural elements in a class file
 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html">docs.oracle.com</a>
 */
class ClassFile(val magic: Int,
				val minorVersion: Int,
				val majorVersion: Int,
				val constantPool: Array[ConstantPoolEntry],
				val accessFlags: Int,
				val thisClass: Int,
				val superClass: Int,
				val interfaces: Array[Int],
				val fields: Array[Field],
				val methods: Array[Method],
				val attributes: Array[_ <: Attribute])

class Field(val accessFlags: Int,
			val nameIndex: Int,
			val descriptorIndex: Int,
			val attributes: Array[_ <: Attribute])

class Method(val accessFlags: Int,
			 val nameIndex: Int,
			 val descriptorIndex: Int,
			 val attributes: Array[_ <: Attribute])

trait ConstantPoolEntry

class ConstantPoolEntryClass(val nameIdx: Int) extends ConstantPoolEntry

class ConstantPoolEntryFieldref(val nameIdx: Int,
								val nameAndTypeIndex: Int) extends ConstantPoolEntry

class ConstantPoolEntryMethodref(val nameIdx: Int,
								 val nameAndTypeIndex: Int) extends ConstantPoolEntry

class ConstantPoolEntryInterfaceMethodref(val nameIdx: Int,
										  val nameAndTypeIndex: Int) extends ConstantPoolEntry

class ConstantPoolEntryString(val stringIdx: Int) extends ConstantPoolEntry

class ConstantPoolEntryInteger(val bytes: Int) extends ConstantPoolEntry

class ConstantPoolEntryFloat(val bytes: Int) extends ConstantPoolEntry

class ConstantPoolEntryLong(val highBytes: Int,
							val lowBytes: Int) extends ConstantPoolEntry

class ConstantPoolEntryDouble(val highBytes: Int,
							  val lowBytes: Int) extends ConstantPoolEntry

class ConstantPoolEntryNameAndType(val nameIdx: Int,
								   val descriptionIdx: Int) extends ConstantPoolEntry

class ConstantPoolEntryUtf8(val bytes: Array[Byte]) extends ConstantPoolEntry

trait Attribute {
	def attributeNameIndex: Int

	def typ: String
}

class DefaultAttribute(val attributeNameIndex: Int,
					   val typ: String,
					   val info: Array[Byte]) extends Attribute

class RuntimeVisibleAnnotationsAttribute(val attributeNameIndex: Int,
										 val typ: String,
										 val annotations: Array[Annotation]) extends Attribute

class Annotation(val typeIndex: Int,
				 val elementValuePairs: Array[ElementValuePair])

class ElementValuePair(val nameIndex: Int,
					   val value: ElementValue)

trait ElementValue {
	def tag: Byte
}

class PrimitiveElementValue(val tag: Byte,
							val constValueIndex: Int) extends ElementValue

class EnumElementValue(val tag: Byte,
					   val typeNameIndex: Int,
					   val constNameIndex: Int) extends ElementValue

class ClassElementValue(val tag: Byte,
						val classInfoIndex: Int) extends ElementValue

class AnnotationElementValue(val tag: Byte,
							 override val typeIndex: Int,
							 override val elementValuePairs: Array[ElementValuePair])
  extends Annotation(typeIndex, elementValuePairs)
  with ElementValue

class ArrayElementValue(val tag: Byte,
						values: Array[ElementValue]) extends ElementValue


