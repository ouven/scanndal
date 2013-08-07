package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 20.07.13
 * Time: 12:01
 */

/**
 * @tparam T result type after mapping
 */
trait Mapper[T] extends Function[ClassFile, T]
