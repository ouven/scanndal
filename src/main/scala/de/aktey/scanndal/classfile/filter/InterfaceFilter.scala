package de.aktey.scanndal.classfile.filter

import scala.reflect._

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 09.08.13
 * Time: 23:33
 */
/**

 * test class files if they directly implement an interface
 * @tparam I the annotation class
 */
class InterfaceFilter[I: ClassTag] extends InterfaceNameFilter(classTag[I].runtimeClass.getCanonicalName)
