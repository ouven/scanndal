package de.aktey.scanndal.classfile.filter


/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 09.08.13
 * Time: 23:33
 */
class InterfaceFilter[T: Manifest] extends InterfaceNameFilter(manifest[T].erasure.getCanonicalName)
