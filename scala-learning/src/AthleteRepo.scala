/**
 * Created by barclay.dunn on 8/14/15.
 */
object AthleteRepo {

  val ATHLETES: List[Athlete] = List[Athlete](
    Athlete("Bob", 190, "basketball"),
    Athlete("Frank", 130, "basketball"),
    Athlete("Jane", 190, "chess")
  )

  def main(args: Array[String]): Unit = {
    printAll(ATHLETES).foreach(println)
  }

  def rare(athlete: Athlete): Boolean = {
    athlete.height > 180 && athlete.profession != "basketball" ||
      athlete.height < 180 && athlete.profession == "basketball"
  }

  def printAll(athletes: List[Athlete]): List[String] = {
    ATHLETES.filter(rare).map(_.name)
  }
}

class Athlete() {
  var name: String = ""
  var height: Int = 0
  var profession: String = ""

  def this(_name: String, _height: Int, _profession: String) = {
    this()
    this.name = _name
    this.height = _height
    this.profession = _profession
  }
}

object Athlete {
  def apply(name: String, height: Int, profession: String): Athlete = {
    new Athlete(name, height, profession)
  }
}
