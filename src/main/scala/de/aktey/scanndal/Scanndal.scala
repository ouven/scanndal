package de.aktey.scanndal

import java.io._
import java.net.{JarURLConnection, URL, URLClassLoader}
import java.util.jar.{JarEntry, JarFile}
import de.aktey.scanndal.classfile.{ClassFileReader, ClassFile}

/**
 * Created with IntelliJ IDEA.
 * User: rwagner
 * Date: 28.06.13
 */
object Scanndal {

	/**
	 * Create a scanner for a package. All classes in the package
	 * and its subpackages will be scanned.
	 * @param scanPackage package to scan
	 * @return new scanndal scanner
	 */
	def apply(scanPackage: String) = new Scanndal(scanPackage)

	/**
	 * Create a scanner for all package. All classes in the root package
	 * and its subpackages will be scanned.
	 * @return new scanndal scanner
	 */
	def apply() = new Scanndal()

	val defaultClassLoaders = Set(
		Thread.currentThread.getContextClassLoader,
		classOf[Scanndal].getClassLoader
	).toStream
}

/**
 * Create a scanner for a package. All classes in the package
 * and its subpackages will be scanned.
 * @param scanPackage package to scan
 */
class Scanndal(scanPackage: String = null) {

	import collection.JavaConversions._
	import Scanndal._

	def scan = for {
		loader <- defaultClassLoaders
		if loader.isInstanceOf[URLClassLoader]
		url <- urlIterator(loader.asInstanceOf[URLClassLoader])
		classFile <- scanUrl(url)
	} yield classFile

	/**
	 * iterate over all urls of a URLClassLoader
	 * @param ucl the class loader
	 * @return url stream
	 */
	private def urlIterator(ucl: URLClassLoader): Stream[URL] =
		if (scanPackage == null || scanPackage.isEmpty) ucl.getURLs.toStream
		else ucl.getResources(scanPackage.replace('.', '/')).toStream

	/**
	 * recursively scan all class files in a file/directory
	 * @param f directory or file
	 * @return class file stream
	 */
	def scanFile(f: File): Stream[ClassFile] = {

		@inline def toStream(f: File) = new BufferedInputStream(new FileInputStream(f), 1024)

		def getClassFiles(file: File): Stream[File] = {
			if (file.isDirectory) for {
				subFile <- file.listFiles.toStream
				if subFile.canRead
				if !subFile.getName.startsWith(".")
				subSubFile <- getClassFiles(subFile)
			} yield subSubFile
			else file #:: Stream.empty[File]
		}
		getClassFiles(f)
			.filter(_.getName.endsWith(".class"))
			.map(toStream)
			.map(ClassFileReader.readFromInputStream)
	}

	/**
	 * scan all class files in a jar file
	 * @param jarFile jar to scan
	 * @return class file stream
	 */
	def scanJar(jarFile: JarFile): Stream[ClassFile] = {
		def withJarEntryStream[T](jarEntry: JarEntry)(f: InputStream => T): T = {
			val stream = jarFile.getInputStream(jarEntry)
			try f(stream) finally stream.close()
		}

		jarFile.entries.toStream
			.filterNot(_.isDirectory)
			.filter(_.getName.endsWith(".class"))
			.map(withJarEntryStream(_) {
			stream => ClassFileReader.readFromInputStream(new BufferedInputStream(stream, 1024))
		})
	}

	/**
	 * scan all class files at a URL
	 * @param url url to scan
	 * @return class file stream
	 */
	def scanUrl(url: URL): Stream[ClassFile] = url.getProtocol match {
		case "file" =>
			val f = new File(url.toURI)
			if (f.isDirectory) scanFile(f)
			else scanJar(new JarFile(f))
		case "jar" =>
			scanJar(
				url.openConnection()
					.asInstanceOf[JarURLConnection]
					.getJarFile
			)
		case _ =>
			Stream.empty
	}
}
