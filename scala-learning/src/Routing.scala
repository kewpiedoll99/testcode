/**
 * Created by barclay.dunn on 8/14/15.
 */
object Routing extends App {
//  println(getRoute("JFK", " ", "LAX"))
  println(getRoute("JFK", "DEN", "LAX"))
  println(getRoute("JFK", "DEN", "SFO", "LAX"))

  def getRoute(args: String*) = {
    args.map(checkInput).mkString("->")
  }

  def checkInput(input: String) = {
    val trimmed = input.trim
    if (trimmed.isEmpty)
      throw new IllegalArgumentException
    trimmed
  }
}
