package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 19:49
 */

/**
 * maps to loaded classes of type T
 * @tparam T result type after mapping
 */
class ClassMapper[T] extends Mapper[Class[T]] {
	private val delegate = new ClassNameMapper

	override def apply(classFile: ClassFile) = Class
	  .forName(delegate.apply(classFile))
	  .asInstanceOf[Class[T]]
}
