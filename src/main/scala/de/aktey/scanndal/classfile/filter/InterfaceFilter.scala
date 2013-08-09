package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 09.08.13
 * Time: 23:33
 */
class InterfaceFilter[T: Manifest] extends Filter with ClassFileInterpretation {
  val interfaceName = fromCanonicalName(manifest[T].erasure.getCanonicalName)

  def apply(classFile: ClassFile) = classFile
    .interfaces.toStream
    .map(classFile.cpClass)
    .map(classFile.cpUtf8String)
    .exists(_ == interfaceName)
}
