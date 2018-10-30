import scala.collection.mutable

object Chap13Exercises {
  // 1
  val word = "Mississippi"
  val wordWithIndexes = word.zipWithIndex
  wordWithIndexes.foldLeft(mutable.Map[Char, Set[Int]]()) {
    (m, k) => {
      val charInWord: Char = k._1
      val index: Int = k._2
      val originalValue: Set[Int] = m.getOrElse(charInWord, Set.empty)
      val newValue = originalValue + index
      m + (charInWord -> newValue)
    }
  }
  // 2
  wordWithIndexes.foldLeft(Map[Char, List[Int]]()) {
    (m, k) => {
      val charInWord: Char = k._1
      val index: Int = k._2
      val originalValue: List[Int] = m.getOrElse(charInWord, List.empty)
      val newValue = originalValue :+ index
      m + (charInWord -> newValue)
    }
  }
  // 3
  def removeZeroesFromLinkedList(linkedList: mutable.LinkedList[Int]): mutable.LinkedList[Int] = {
    if (linkedList == Nil) mutable.LinkedList.empty
    else if (linkedList.head == 0) removeZeroesFromLinkedList(linkedList.tail)
    else linkedList.head +: removeZeroesFromLinkedList(linkedList.tail)
  }
  val integersWithZeroes = mutable.LinkedList[Int](1, 2, 0, 5, 8, -1, 0, -3, 4)
  removeZeroesFromLinkedList(integersWithZeroes)
  // 4
  def findFirstGroupsLocationInMap(group: Array[String], locationMap: Map[String, Int]): Array[Int] = {
    val groupInLocMap = group.filter(locationMap.keySet.contains(_))
    groupInLocMap.map(locationMap(_))
  }
  val groupOfGuys = Array("Tom", "Fred", "Harry")
  val locMap = Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5)
  findFirstGroupsLocationInMap(groupOfGuys, locMap)
  //5
  def makeString(iterable: scala.collection.immutable.Traversable[String], separator: String): String = {
    val output: String = ""
    iterable.reduceLeft(_ + separator + _)
  }

  val severalStrings = scala.collection.immutable.Seq[String]("Four score and seven years ago",
    "our fathers brought forth on this continent, a new nation,",
    "conceived in Liberty,", "and dedicated to the proposition",
    "that all men are created equal.")
  makeString(severalStrings, " ")
  // 6
  val lst = List[Int](1, 2, 3, 5, 7, 8, 0)
  (lst /: List[Int]())(_ :+ _) // foldLeft
  (lst :\ List[Int]())(_ :: _) // foldRight
  (lst /: List[Int]())((r, c) => c :: r)
  (lst :\ List[Int]())((r, c) => c :+ r)
  lst.foldLeft(List[Int]())((r, c) => c :: r)
  lst.foldRight(List[Int]())((r, c) => c :+ r)

  // 7
  val prices = List[Double](5.0, 20.0, 9.95)
  val quantities = List[Int](10, 2, 1)
//  (prices zip quantities) map { p => p._1 * p._2 }
  def calcPrices(prices: List[Double], quantities: List[Int]): List[Double] =
    (prices zip quantities) map { ((d: Double, i: Int) => d * i).tupled }
  calcPrices(prices, quantities)

  // 8
  def twoDim(in: Array[Double], numCols: Int) = {
    in.grouped(numCols).toArray
  }
  val input = Array[Double](1, 2, 3, 4, 5, 6)
  twoDim(input, 3)

  // 9

}