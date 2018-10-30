import java.util

object Chap4 {

  var scores = scala.collection.immutable.SortedMap("Alice" -> 10,
    "Bob" -> 3,
    "Cindy" -> 8)
  scores += ("Joe" -> 15)
  scores("Joe")
  scores += ("Jill" -> 5)
  scores("Jill")
  scores -= "Joe"
  scores.keySet
  for (v <- scores.values) println(v)
  for ((k, v) <- scores) yield (v, k)
  import scala.collection.JavaConversions.mapAsScalaMap
  val scores2 : scala.collection.mutable.Map[String, Int] =
    new util.TreeMap[String, Int]()
  for ((k, v) <- scores)
    scores2 += (k -> v)
  import scala.collection.JavaConversions.propertiesAsScalaMap
  val props : scala.collection.Map[String, String] = System.getProperties
  import scala.collection.JavaConversions.mapAsJavaMap
  import java.awt.font.TextAttribute._
  val attrs = Map(FAMILY -> "Serif", SIZE -> 12)
  val font = new java.awt.Font(attrs)
  val t = (1, 3.14, "Fred")
//  val second = t._2
  val (first, second, third) = t
//  val (first, _) = t

  "New York".partition(_.isUpper)
  val symbols = Array("<", "-", ">")
  val counts = Array(2, 10, 2)
  val pairs = symbols.zip(counts)
  for ((s,n) <- pairs) Console.print(s * n)
}