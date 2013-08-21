package de.aktey.scanndal

import de.aktey.scanndal.classfile.mapper._
import de.aktey.scanndal.classfile.filter._
import scala.reflect._
import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 12.08.13
 * Time: 22:41
 */

/**
 * Find all classes, that extend a class or extend
 * a class, that extends this class
 * Find all classes (and interfaces), that implement an interface or extend
 * a class, that implements this interface.
 *
 * (Because of the tree structure, one cannot use a simple
 * ClassAnnotationFilter.)
 *
 * @param scanPackage packages to scan in
 * @tparam A the Annotation
 */
class DeepTypeScan[A: ClassTag](scanPackage: String = null) {
	val typeName = classTag[A].runtimeClass.getCanonicalName
	val implementsA = new InterfaceFilter[A]()
	val extendsA = new SuperClassFilter[A]()
	val isInterface = new ClassAccessFlagFilter(ClassAccessFlag.INTERFACE)

	val idMapper = new IdentityMapper()
	val classNameOf = new ClassNameMapper()
	val splitMapper = new Tuple2Mapper(idMapper, new Tuple2Mapper(new SuperClassNameMapper(), idMapper))
	val scanndal = Scanndal(scanPackage)

	def scan: Iterable[ClassFile] = {
		// create two streams with one scan
		val (classFiles, superTuples) = scanndal.scan.map(splitMapper).unzip

		// build a function, that maps a superclass to all
		// its direct children
		val superMap = superTuples
			.groupBy(_._1)
			.mapValues(_.map(_._2).toList)
			.withDefaultValue(Nil)

		// go down the hierarchy
		def descendantOrSelf(classes: Stream[ClassFile]) = {
			def merge(result: Stream[ClassFile], children: Stream[ClassFile]): Stream[ClassFile] = {
				if (children.isEmpty) result
				else result #::: merge(children, children.map(classNameOf).flatMap(superMap))
			}
			merge(classes, classes.map(classNameOf).flatMap(superMap))
		}

		val extendingClasses = if (classTag[A].runtimeClass.isInterface) {
			// find all interfaces, that extend this interface
			val interfaces = typeName #:: descendantOrSelf(classFiles
				.filter(isInterface)
				.filter(implementsA))
				.map(classNameOf)
			// find all classes, that implement one of this interfaces
			val implementsAny = interfaces
				.map(new InterfaceNameFilter(_))
				.fold(new FalseFilter)(_ || _)
			classFiles.filter(implementsAny)
		} else {
			classFiles.filter(extendsA)
		}

		// find all classes, that extend one of this classes
		descendantOrSelf(extendingClasses)
	}
}
