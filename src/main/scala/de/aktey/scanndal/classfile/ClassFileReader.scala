package de.aktey.scanndal.classfile

import java.io.{InputStream, DataInputStream}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 18.07.13
 * Time: 19:51
 *
 * assemble a class file
 */
object ClassFileReader {
	// read a class file from an input stream
	def readFromInputStream(data: InputStream): ClassFile = readFromDataInputStream(new DataInputStream(data))

	// read a class file from a data input stream
	def readFromDataInputStream(data: DataInputStream): ClassFile = {
		//read an array of bytes with length len
		@inline def readByteArray(len: Int): Array[Byte] = {
			val b = Array.ofDim[Byte](len)
			data.readFully(b)
			b
		}

		//read an array of elements E with length len
		//f reads the next element E
		@inline def readArray[E: Manifest](len: Int)(f: => E) = (0 until len).map(_ => f).toArray

		// C0FEBABE
		val magic = data.readInt

		// class file version
		val minorVersion = data.readUnsignedShort
		val majorVersion = data.readUnsignedShort

		val constantPool = {
			// the first read byte of an entry tells the type of the entry
			def readConstantPoolEntry = data.readByte() match {
				case 1 => ConstantPoolEntryUtf8(readByteArray(data.readUnsignedShort))
				case 3 => ConstantPoolEntryInteger(data.readInt)
				case 4 => ConstantPoolEntryFloat(data.readInt)
				case 5 => ConstantPoolEntryLong(data.readInt, data.readInt)
				case 6 => ConstantPoolEntryDouble(data.readInt, data.readInt)
				case 7 => ConstantPoolEntryClass(data.readUnsignedShort)
				case 8 => ConstantPoolEntryString(data.readUnsignedShort)
				case 9 => ConstantPoolEntryFieldref(data.readUnsignedShort, data.readUnsignedShort)
				case 10 => ConstantPoolEntryMethodref(data.readUnsignedShort, data.readUnsignedShort)
				case 11 => ConstantPoolEntryInterfaceMethodref(data.readUnsignedShort, data.readUnsignedShort)
				case 12 => ConstantPoolEntryNameAndType(data.readUnsignedShort, data.readUnsignedShort)
				case 15 => ConstantPoolEntryMethodHandle(data.readUnsignedByte, data.readUnsignedShort)
				case 16 => ConstantPoolEntryMethodType(data.readUnsignedShort)
				case 18 => ConstantPoolEntryInvokeDynamic(data.readUnsignedShort, data.readUnsignedShort)
			}

			def _read(length: Int, constants: List[ConstantPoolEntry]): List[ConstantPoolEntry] = {
				if (length > 0) {
					val constant = readConstantPoolEntry
					// Quote from the JVM specification:
					//  "All eight byte constants take up two spots in the constant pool.
					// If this is the n'th byte in the constant pool, then the next item
					// will be numbered n+2"
					val (indexModifier, poolEntries) = constant match {
						case _: ConstantPoolEntryDouble => (2, null :: constant :: constants)
						case _: ConstantPoolEntryLong => (2, null :: constant :: constants)
						case _ => (1, constant :: constants)
					}
					_read(length - indexModifier, poolEntries)
				} else {
					constants
				}
			}

			// constant_pool[0] is unused by the compiler and may be used freely
			// by the implementation.
			_read(data.readUnsignedShort - 1, List[ConstantPoolEntry](null)).reverse.toArray
		}

		// read attributes of the class, fields or methods
		def readAttributes: Array[Attribute] = readArray(data.readUnsignedShort) {
			// recursive structure definition with readElementValue
			def readAnnotation = Annotation(
				typeIndex = data.readUnsignedShort,
				elementValuePairs = readArray(data.readUnsignedShort) {
					ElementValuePair(
						nameIndex = data.readUnsignedShort,
						value = readElementValue
					)
				}
			)

			// recursive structure definition with readAnnotation
			def readElementValue: ElementValue = {
				// element type is
				val tag = data.readByte
				tag match {
					case 'B' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'C' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'D' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'F' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'I' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'J' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'S' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'Z' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 's' => PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'c' => ClassElementValue(tag, data.readUnsignedShort)
					case 'e' => EnumElementValue(tag, data.readUnsignedShort, data.readUnsignedShort)
					case '@' =>
						val anno = readAnnotation
						AnnotationElementValue(tag, anno.typeIndex, anno.elementValuePairs)
					case '[' => ArrayElementValue(
						tag = tag,
						values = readArray(data.readUnsignedShort)(readElementValue)
					)
				}
			}

			// the type index
			// the entry in the constant pool has tell the type of the attribute
			val idx = data.readUnsignedShort
			val typ = {
				val cpe = constantPool(idx).asInstanceOf[ConstantPoolEntryUtf8]
				new String(cpe.bytes, "UTF-8")
			}
			typ match {
				case "RuntimeVisibleAnnotations" =>
					// attribute length in bytes is implicit
					data.skipBytes(4)
					new RuntimeVisibleAnnotationsAttribute(
						attributeNameIndex = idx,
						typ = typ,
						annotations = readArray(data.readUnsignedShort)(readAnnotation)
					)
				// all attributes are in this form
				case _ => DefaultAttribute(idx, typ, readByteArray(data.readInt))
			}
		}

		val accessFlags = data.readUnsignedShort
		val thisClass = data.readUnsignedShort
		val superClass = data.readUnsignedShort
		val interfaces = readArray(data.readUnsignedShort)(data.readUnsignedShort)

		val fields = readArray(data.readUnsignedShort) {
			Field(
				accessFlags = data.readUnsignedShort,
				nameIndex = data.readUnsignedShort,
				descriptorIndex = data.readUnsignedShort,
				attributes = readAttributes
			)
		}

		val methods = readArray(data.readUnsignedShort) {
			Method(
				accessFlags = data.readUnsignedShort,
				nameIndex = data.readUnsignedShort,
				descriptorIndex = data.readUnsignedShort,
				attributes = readAttributes
			)
		}

		val attributes = readAttributes

		ClassFile(
			magic,
			minorVersion,
			majorVersion,
			constantPool,
			accessFlags,
			thisClass,
			superClass,
			interfaces,
			fields,
			methods,
			attributes
		)
	}
}

