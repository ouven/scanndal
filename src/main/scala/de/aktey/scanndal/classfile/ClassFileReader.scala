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
				case 1 => new ConstantPoolEntryUtf8(readByteArray(data.readUnsignedShort))
				case 7 => new ConstantPoolEntryClass(data.readUnsignedShort)
				case 9 => new ConstantPoolEntryFieldref(data.readUnsignedShort, data.readUnsignedShort)
				case 10 => new ConstantPoolEntryMethodref(data.readUnsignedShort, data.readUnsignedShort)
				case 11 => new ConstantPoolEntryInterfaceMethodref(data.readUnsignedShort, data.readUnsignedShort)
				case 8 => new ConstantPoolEntryString(data.readUnsignedShort)
				case 3 => new ConstantPoolEntryInteger(data.readInt)
				case 4 => new ConstantPoolEntryFloat(data.readInt)
				case 5 => new ConstantPoolEntryLong(data.readInt, data.readInt)
				case 6 => new ConstantPoolEntryDouble(data.readInt, data.readInt)
				case 12 => new ConstantPoolEntryNameAndType(data.readUnsignedShort, data.readUnsignedShort)
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
			def readAnnotation = new Annotation(
				typeIndex = data.readUnsignedShort,
				elementValuePairs = readArray(data.readUnsignedShort) {
					new ElementValuePair(
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
					case 'B' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'C' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'D' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'F' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'I' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'J' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'S' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'Z' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 's' => new PrimitiveElementValue(tag, data.readUnsignedShort)
					case 'c' => new ClassElementValue(tag, data.readUnsignedShort)
					case 'e' => new EnumElementValue(tag, data.readUnsignedShort, data.readUnsignedShort)
					case '@' =>
						val anno = readAnnotation
						new AnnotationElementValue(tag, anno.typeIndex, anno.elementValuePairs)
					case '[' => new ArrayElementValue(
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
				case _ => new DefaultAttribute(idx, typ, readByteArray(data.readInt))
			}
		}

		val accessFlags = data.readUnsignedShort
		val thisClass = data.readUnsignedShort
		val superClass = data.readUnsignedShort
		val interfaces = readArray(data.readUnsignedShort)(data.readUnsignedShort)

		val fields = readArray(data.readUnsignedShort) {
			new Field(
				accessFlags = data.readUnsignedShort,
				nameIndex = data.readUnsignedShort,
				descriptorIndex = data.readUnsignedShort,
				attributes = readAttributes
			)
		}

		val methods = readArray(data.readUnsignedShort) {
			new Method(
				accessFlags = data.readUnsignedShort,
				nameIndex = data.readUnsignedShort,
				descriptorIndex = data.readUnsignedShort,
				attributes = readAttributes
			)
		}

		val attributes = readAttributes

		new ClassFile(
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

