import scala.collection.immutable.HashMap
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.propertiesAsScalaMap
import scala.collection.mutable.ArrayBuffer
object Chap4Exercises {
  val gizmos = Map("iPod" -> 250, "beeper" -> 125, "Google Glass" -> 850)
  for ((k, v) <- gizmos) yield (k, v * .9)

//  val wordCounts : scala.collection.mutable.Map[String, Int] = new scala.collection.mutable.HashMap[String, Int]
//  val in = new java.util.Scanner(new java.io.File("/Users/barclay.dunn/Documents/gettysburg.txt"))
//  while (in.hasNext()) {
//    val line = in.next()
//    for (word <- line.split(" ")) {
//      if (wordCounts.keySet.contains(word))
//        wordCounts(word) = wordCounts(word) + 1
//      else
//        wordCounts(word) = 1
//    }
//  }
//
//  for ((k,v) <- wordCounts) println(k + ": " + v)
//  var wordCounts : Map[String, Int] = new scala.collection.immutable.HashMap[String, Int]
//  var wordCounts : scala.collection.immutable.Map[String, Int] = new scala.collection.immutable.TreeMap[String, Int]
  var wordCounts : scala.collection.mutable.Map[String, Int] = new java.util.TreeMap[String, Int]
  val in = new java.util.Scanner(new java.io.File("/Users/barclay.dunn/Documents/gettysburg.txt"))
  while (in.hasNext()) {
    val line = in.next()
    for (word <- line.split(" ")) {
//      var newCount = 1
//      if (wordCounts.keySet.contains(word))
//        newCount = wordCounts(word) + 1
//      wordCounts = wordCounts + (word -> newCount)
      wordCounts(word) = wordCounts.getOrElse(word, 0) + 1
    }
  }
  for ((k,v) <- wordCounts) println(k + ": " + v)
//
  val weekdays = scala.collection.mutable.LinkedHashMap(
      "Monday" -> java.util.Calendar.MONDAY,
      "Tuesday" -> java.util.Calendar.TUESDAY,
      "Wednesday" -> java.util.Calendar.WEDNESDAY,
      "Thursday" -> java.util.Calendar.THURSDAY,
      "Friday" -> java.util.Calendar.FRIDAY,
      "Saturday" -> java.util.Calendar.SATURDAY,
      "Sunday" -> java.util.Calendar.SUNDAY
    )
  for (k <- weekdays.keySet) println(k)
//
  val props : scala.collection.Map[String, String] = System.getProperties
  val propsKeyLengths = for ((k,v) <- props) yield (k, k.length)
  val longestKeyLength : Int = propsKeyLengths.values.max
  for ((k,v) <- props) {
    val prop : String = k.padTo(longestKeyLength, " ").mkString("")
    println(prop + " | " + v)
  }
//
  def minmax(values : Array[Int]) = {
    (values.min, values.max)
  }
  val a = ArrayBuffer[Int](1, -3, 2, -1, 3, -5, 4, -8)
  minmax(a.toArray)
//
  def lteqgt(values : Array[Int], v : Int) = {
    (a.count(_ < v), a.count(_ == v), a.count(_ > v))
  }
  lteqgt(a.toArray, 3)
//
  "Hello".zip("World")
}
