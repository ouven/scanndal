package de.aktey.scanndal.classfile.mapper

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 14.08.13
 * Time: 00:13
 */

/**
 * maps classfiles to themselfs
 */
class IdentityMapper extends Mapper[ClassFile] {
	def apply(classFile: ClassFile) = classFile
}
