import scala.math.{signum, abs}
/**
 * Created by barclay.dunn on 8/14/15.
 */
object Chap11Ex {
  // 3.
  class Fraction(n: Int, d: Int) {
    private val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d)
    private val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d)
    override def toString = num + "/" + den
    def sign(a: Int) = if (a > 0) 1 else if (a < 0) -1 else 0
    def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)
    def +(other: Fraction) = new Fraction(num * other.den + other.num * den, den * other.den)
    def -(other: Fraction) = new Fraction(num * other.den - other.num * den, den * other.den)
    def *(other: Fraction) = new Fraction(num * other.num, den * other.den)
    def /(other: Fraction) = new Fraction(num * other.den, den * other.num)
  }
  val onethird = new Fraction(1, 3)
  val onehalf = new Fraction(1, 2)
  onethird / onehalf
  onehalf / onethird
  onethird * onehalf
  onethird - onehalf
  onethird + onehalf
  // 4.
  class Money(d: Int, c: Int) {
    private val dols: Int  = if (c > 100) d + (c % 100) else d
    private val cents: Int = if (c > 100) abs(c % 100) else abs(c)

    override def toString = (if (signum(d) < 0) "-" else "") + "$" + abs(dols) + "." + (if (cents < 10) "0" else "") + cents

    def toDouble = dols + (cents.toDouble/100)

    def +(other: Money) = new Money(dols + other.dols + ((cents + other.cents) / 100), (cents + other.cents) % 100)

    def -(other: Money) = new Money(dols - other.dols + ((cents - other.cents) / 100), (cents - other.cents) % 100)

    def *(other: Double) = {
      val multMoney: Double = this.toDouble * other
      val multDols: Int = multMoney.toInt
      val multCents: Int = (scala.math.round(multMoney * 100) % 100).toInt
      new Money(multDols, multCents)
    }

    def /(other: Double) = {
      val divMoney: Double = this.toDouble / other
      val divDols: Int = divMoney.toInt
      val divCents: Int = (scala.math.round(divMoney * 100) % 100).toInt
      new Money(divDols, divCents)
    }

    def ==(other: Money): Boolean = (dols == other.dols && cents == other.cents)

    def <(other: Money) = {
      val thisDouble: Int = (this.toDouble * 100).toInt
      val otherDouble: Int = (other.toDouble * 100).toInt
      thisDouble < otherDouble
    }
  }
  val twothirty = new Money(2, 30)
  val negtwothirty = new Money(-2, 30)
  val sevenseventyone = new Money(7, 71)
  twothirty + sevenseventyone
  sevenseventyone - twothirty
  twothirty - sevenseventyone
  twothirty * 7.71
  twothirty / 7.71
  twothirty == negtwothirty
  twothirty == new Money(2, 30)
  sevenseventyone < twothirty
  twothirty < new Money(2, 30) * 1.00
}
