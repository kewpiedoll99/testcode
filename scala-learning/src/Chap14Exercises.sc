object Chap14Exercises {

//  def leafSum(tree: Any, sum: Int): Int = {
//    tree match {
//      case List => {
//        tree.head match {
//          case List => leafSum (tree.head) + leafSum (tree.tail) + sum
//          case Int => tree.head + leafSum (tree.tail) + sum
//          case _ => sum
//        }
//      }
//      case Int => tree + sum
//      case _ => sum
//    }
//  }
//  leafSum((3, 8), 2, (5))

  def leafSum(tree: BinaryTree, sum: Int = 0): Int = {
    tree match {
      // iterate through elements in node, leafSum them
      case n: Node => n.nodes.foldLeft(sum)(leafSum(_) + leafSum(_))
      case l: Leaf => l.value + sum
      case _ => 0
    }
  }
  sealed abstract class BinaryTree
  case class Leaf(value: Int) extends BinaryTree
  case class Node(nodes: BinaryTree*) extends BinaryTree
  val tree = Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))
  leafSum(tree)
}
