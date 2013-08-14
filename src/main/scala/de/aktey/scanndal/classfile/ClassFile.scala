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
case class ClassFile(magic: Int,
										 minorVersion: Int,
										 majorVersion: Int,
										 constantPool: Array[ConstantPoolEntry],
										 accessFlags: Int,
										 thisClass: Int,
										 superClass: Int,
										 interfaces: Array[Int],
										 fields: Array[Field],
										 methods: Array[Method],
										 attributes: Array[Attribute])

case class Field(accessFlags: Int,
								 nameIndex: Int,
								 descriptorIndex: Int,
								 attributes: Array[Attribute])

case class Method(accessFlags: Int,
									nameIndex: Int,
									descriptorIndex: Int,
									attributes: Array[Attribute])

trait ConstantPoolEntry

case class ConstantPoolEntryClass(nameIdx: Int) extends ConstantPoolEntry

case class ConstantPoolEntryFieldref(nameIdx: Int,
																		 nameAndTypeIndex: Int) extends ConstantPoolEntry

case class ConstantPoolEntryMethodref(nameIdx: Int,
																			nameAndTypeIndex: Int) extends ConstantPoolEntry

case class ConstantPoolEntryInterfaceMethodref(nameIdx: Int,
																							 nameAndTypeIndex: Int) extends ConstantPoolEntry

case class ConstantPoolEntryString(stringIdx: Int) extends ConstantPoolEntry

case class ConstantPoolEntryInteger(bytes: Int) extends ConstantPoolEntry

case class ConstantPoolEntryFloat(bytes: Int) extends ConstantPoolEntry

case class ConstantPoolEntryLong(highBytes: Int,
																 lowBytes: Int) extends ConstantPoolEntry

case class ConstantPoolEntryDouble(highBytes: Int,
																	 lowBytes: Int) extends ConstantPoolEntry

case class ConstantPoolEntryNameAndType(nameIdx: Int,
																				descriptionIdx: Int) extends ConstantPoolEntry

case class ConstantPoolEntryUtf8(bytes: Array[Byte]) extends ConstantPoolEntry

trait Attribute {
	def attributeNameIndex: Int

	def typ: String
}

case class DefaultAttribute(attributeNameIndex: Int,
														typ: String,
														info: Array[Byte]) extends Attribute

case class RuntimeVisibleAnnotationsAttribute(attributeNameIndex: Int,
																							typ: String,
																							annotations: Array[Annotation]) extends Attribute

case class Annotation(typeIndex: Int,
											elementValuePairs: Array[ElementValuePair])

case class ElementValuePair(nameIndex: Int,
														value: ElementValue)

trait ElementValue {
	def tag: Byte
}

case class PrimitiveElementValue(tag: Byte,
																 constValueIndex: Int) extends ElementValue

case class EnumElementValue(tag: Byte,
														typeNameIndex: Int,
														constNameIndex: Int) extends ElementValue

case class ClassElementValue(tag: Byte,
														 classInfoIndex: Int) extends ElementValue

case class AnnotationElementValue(tag: Byte,
																	typeIndex: Int,
																	elementValuePairs: Array[ElementValuePair]) extends ElementValue

case class ArrayElementValue(tag: Byte,
														 values: Array[ElementValue]) extends ElementValue


