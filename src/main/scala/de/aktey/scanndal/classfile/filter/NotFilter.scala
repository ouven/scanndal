package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 01.08.13
 * Time: 23:11
 */

/**
 * inverts the logic of a filter
 * @param filter the filter invert
 */
class NotFilter(filter: Filter) extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = !filter(cf)
}
