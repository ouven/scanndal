package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 16.07.13
 * Time: 06:48
 */

/**
 * filter base trait
 */
trait Filter extends Function[ClassFile, Boolean] {

	/**
	 * combines two filters with a logical and
	 * @param that other filter
	 * @return
	 */
	def &&(that: Filter): Filter = new AndFilter(this, that)

	/**
	 * combines two filters with a logical and
	 * @param that other filter
	 * @return
	 */
	def ||(that: Filter): Filter = new OrFilter(this, that)

	/**
	 * inverts a filter
	 * @return
	 */
	def unary_! : Filter = new NotFilter(this)
}

/**
 * inverts the logic of a filter
 * @param filter the filter invert
 */
sealed class NotFilter(filter: Filter) extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = !filter(cf)
}

/**
 * combines two filter with a logical and
 * @param filter1 the first filter to combine
 * @param filter2 the second filter to combine
 */
sealed class AndFilter(filter1: Filter, filter2: Filter) extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = filter1(cf) && filter1(cf)
}

/**
 * combines two filter with a logical or
 * @param filter1 the first filter to combine
 * @param filter2 the second filter to combine
 */
sealed class OrFilter(filter1: Filter, filter2: Filter) extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = filter1(cf) || filter2(cf)
}

/**
 * a zero filter for the or operation
 */
class FalseFilter() extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = false
}

/**
 * a zero filter for the and operation
 */
class TrueFilter() extends Filter {
	/**
	 * @param cf class file to test
	 * @return true to keep
	 */
	def apply(cf: ClassFile) = true
}
