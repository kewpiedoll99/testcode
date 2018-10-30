object Chap3 {

  import scala.collection.mutable.ArrayBuffer
  import scala.collection.mutable.Buffer
  val b = ArrayBuffer[Int]()
  b += 1
  b += (1, 2, 3, 5)
  b ++= Array(8, 13, 21)
  b.trimEnd(5)
  b.insert(2, 6)
  b.insert(2, 7, 8, 9)
  b.remove(2)
  b.remove(2, 3)
  b.toArray
  val a = ArrayBuffer[Int](1, -3, 2, -1, 3, -5, 4, -8)
//  for (i <- 0 until a.length)
//  for (i <- 0 until (a.length, 2))
  for (i <- (0 until a.length).reverse)
    println(i + ": " + a(i))
  for (elem <- a)
    println(elem)
  val result = for (elem <- a) yield 2 * elem
  for (elem <- a if elem % 2 == 0) yield 2 * elem
  a.filter(_ % 2 == 0).map(2 * _)
  a.sum
  a.min
  a.max
  val quote = ArrayBuffer("Mary", "had", "a", "little", "lamb")
  quote.sorted
  val aA = a.toArray
  scala.util.Sorting.quickSort(aA)
  aA.mkString("{'", "', '", "'}")
  a.count(_ > 0)

  val matrix = Array.ofDim[Double](3,4)
  val triangle = new Array[Array[Int]](10)
  for (i <- 0 until triangle.length)
    triangle(i) = new Array[Int](i + 1)
//  triangle
  import scala.collection.JavaConversions.bufferAsJavaList
  val command = ArrayBuffer("ls", "-al", "/home/cay")
  val pb = new ProcessBuilder(command)
  import scala.collection.JavaConversions.asScalaBuffer
  val cmd : Buffer[String] = pb.command()
}
