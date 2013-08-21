package de.aktey.scanndal.classfile.filter

import scala.reflect._

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 15:28
 */

/**
 * test class files if they directly extend a superclass
 * @tparam S the superclass
 */
class SuperClassFilter[S: ClassTag] extends SuperClassNameFilter(classTag[S].runtimeClass.getCanonicalName)
