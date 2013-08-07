package de.aktey.scanndal.classfile.filter

import de.aktey.scanndal.classfile.ClassFile

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 16.07.13
 * Time: 06:48
 */

/**
 * filter base trait
 */
trait Filter extends Function[ClassFile, Boolean]
