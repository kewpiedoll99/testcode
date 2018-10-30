
object AthleteHipo extends App {

  val ATHLETES: List[Athlete2] = List(
    Athlete2("Bob", 190, "basketball"),
    Athlete2("Frank", 130, "basketball"),
    Athlete2("Jane", 190, "chess")
  )
  getRareAthleteNames(ATHLETES).foreach(println)

  def rare(athlete: Athlete2): Boolean = {
    val isTall = athlete.height > 180
    val isBasketballPlayer = athlete.profession == "basketball"

    isTall != isBasketballPlayer
  }

  def getRareAthleteNames(athletes: List[Athlete2]) = {
//    ATHLETES.filter(rare).map(_.name)
    ATHLETES.filter(rare).map(_.toString) // toString comes for free with "case" class
  }
}

case class Athlete2(name: String, height: Int, profession: String)

