package de.aktey.scanndal

import de.aktey.scanndal.classfile.mapper._
import de.aktey.scanndal.classfile.filter.ClassAnnotationFilter
import java.lang.annotation.Inherited
import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 12.08.13
 * Time: 22:41
 */

/**
 * Find all classes, that have a certain annotation.
 * If that annotation is annotated with @Inherited, find also all childclasses
 * of the annotated classes.
 *
 * (Because of the tree structure, one cannot use a simple
 * ClassAnnotationFilter.)
 *
 * @param scanPackage packages to scan in
 * @tparam A the Annotation
 */
class DeepAnnotationScan[A: Manifest](scanPackage: String = null) {

	// do a deep scan if the annotation is inherited
	private val deep = manifest[A]
		.erasure
		.getAnnotation(classOf[Inherited]) != null

	val annotationFilter = new ClassAnnotationFilter[A]()

	def scan: Iterable[ClassFile] = if (deep) {
		val idMapper = new IdentityMapper()
		val splitMapper = new Tuple2Mapper(idMapper, new Tuple2Mapper(new SuperClassNameMapper(), idMapper))

		// create two streams with one scan
		val (classFiles, superTuples) = Scanndal(scanPackage).scan.map(splitMapper).unzip
		val annotatedClasses = classFiles.filter(annotationFilter)

		// build a function, that maps a superclass to all
		// its direct children
		val superMap = superTuples
			.groupBy(_._1)
			.mapValues(_.map(_._2).toList)
			.withDefaultValue(Nil)

		// go down the hierarchy
		def descendantOrSelf(classes: Stream[ClassFile]) = {
			val classNameOf = new ClassNameMapper()
			def merge(result: Stream[ClassFile], children: Stream[ClassFile]): Stream[ClassFile] = {
				if (children.isEmpty) result
				else result #::: merge(children, children.map(classNameOf).flatMap(superMap))
			}
			merge(classes, classes.map(classNameOf).flatMap(superMap))
		}

		descendantOrSelf(annotatedClasses)
	} else {
		// the annotation is not inheerited
		Scanndal(scanPackage).scan.filter(annotationFilter)
	}
}
