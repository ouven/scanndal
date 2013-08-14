package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 12.08.13
 * Ti_e: 22:43
 */

/**
 * Combine two mappers to a single one, that maps a class
 * file to a tuple of single mapper results
 *
 * @param _1 first mapper
 * @param _2 second mapper
 * @tparam A first mapper result type
 * @tparam B second mapper result type
 */
class Tuple2Mapper[A, B](val _1: Mapper[A], val _2: Mapper[B]) extends Mapper[(A, B)] {
	override def apply(classFile: ClassFile) = (_1(classFile), _2(classFile))
}

/**
 * @see Tuple2Mapper
 */
class Tuple3Mapper[A, B, C](val _1: Mapper[A], val _2: Mapper[B], val _3: Mapper[C]) extends Mapper[(A, B, C)] {
	override def apply(classFile: ClassFile) = (_1(classFile), _2(classFile), _3(classFile))
}

/**
 * @see Tuple2Mapper
 */
class Tuple4Mapper[A, B, C, D](val _1: Mapper[A], val _2: Mapper[B], val _3: Mapper[C], val _4: Mapper[D]) extends Mapper[(A, B, C, D)] {
	override def apply(classFile: ClassFile) = (_1(classFile), _2(classFile), _3(classFile), _4(classFile))
}
