package de.aktey.scanndal.classfile.filter


/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 15:28
 *
 * filters for all classes with the direct super type S
 */
class SuperClassFilter[S: Manifest] extends SuperClassNameFilter(manifest[S].erasure.getCanonicalName)
