package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.{ClassFileInterpretation, ClassFile}
import scala.reflect._

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 09.08.13
 * Time: 23:33
 */
class InterfaceFilter[T: ClassTag] extends Filter with ClassFileInterpretation {
  val interfaceName = fromCanonicalName(classTag[T].runtimeClass.getCanonicalName)

  def apply(classFile: ClassFile) = classFile
    .interfaces.toStream
    .map(classFile.cpClass)
    .map(classFile.cpUtf8String)
    .exists(_ == interfaceName)
}
