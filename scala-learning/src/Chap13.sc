import java.awt.Color
import scala.collection.immutable.SortedSet
object Chap13 {
  Iterable(0xFF, 0xFF00, 0xFF0000)
  Set(Color.RED, Color.GREEN, Color.BLUE)
  Map(Color.RED -> 0xFF0000,
    Color.GREEN -> 0xFF00,
    Color.BLUE -> 0xFF
  )
  SortedSet("Hello", "World")
  def digits(n : Int) : Set[Int] =
    if (n < 0) digits(-n)
    else if (n < 10) Set(n)
    else digits(n / 10) + (n % 10)
  digits(5)
  digits(54678)
  digits(-55300)
  digits(53)
  val lst = scala.collection.mutable.LinkedList(1, -2, 7, -9)
  var cur = lst
  while (cur != Nil) {
    if (cur.elem < 0) cur.elem = 0
    cur = cur.next
  }
  lst
  cur = lst
  while (cur != Nil && cur.next != Nil) {
    cur.next = cur.next.next
    cur = cur.next
  }
  lst
  Vector(1,2,3) :+ 5
  5 +: Vector(1,2,3)
  val names = List("Peter", "Paul", "Mary")
  names.map(_.toUpperCase)
  def ulcase(s: String) =
    Vector(s.toUpperCase(), s.toLowerCase())
  names.map(ulcase)
  names.flatMap(ulcase)
  val freq = scala.collection.mutable.Map[Char, Int]()
//  for (c <- "Mississippi")
//    freq(c) = freq.getOrElse(c, 0) + 1
//  freq
  (Map[Char, Int]() /: "Mississippi") {
    (m, c) => m + (c -> (m.getOrElse(c, 0) + 1))
  }
  (1 to 10).scanLeft(0)(_ + _)
  val prices = List(5.0, 20.0, 9.95)
  val quantities = List(10, 2, 1)
  (prices zip quantities) map { p => p._1 * p._2}
  List(5.0, 20.0, 9.95).zipAll(List(10, 2), 0.0, 1)
  "Scala".zipWithIndex.max._2
  def numsFrom(n: BigInt): Stream[BigInt] = n #:: numsFrom(n + 1)
  val tenOrMore = numsFrom(10)
  tenOrMore.take(6).force
  val squares = numsFrom(1).map(x => x * x)
  squares.take(4).force
  squares(5)
  squares
//  (0 to 1000).map(pow(10, _)).map(1 / _)
  import scala.math.pow
  (0 to 1000).view.map(pow(10, _)).map(1 / _)
  import scala.collection.JavaConversions.propertiesAsScalaMap
  val props: scala.collection.mutable.Map[String, String] = System.getProperties
  props.keySet.par.count(_ != null)
  import scala.collection.parallel.immutable.ParVector
  val counts = ParVector[String]()
  // ??????
  for (i <- (0 until 100).par) {
    counts :+ i + " "
  }
  counts
  for (i <- (0 until 100).par) yield i + " "

  
}