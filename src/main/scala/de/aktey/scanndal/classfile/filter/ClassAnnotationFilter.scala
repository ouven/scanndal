package de.aktey.scanndal.classfile.filter


/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 24.07.13
 * Time: 23:23
 */

/**
 * test class files for a class annotation A
 * @tparam A the annotation class
 */
class ClassAnnotationFilter[A: Manifest] extends ClassAnnotationNameFilter(manifest[A].erasure.getCanonicalName)