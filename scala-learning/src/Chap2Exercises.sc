object Chap2Exercises {

//  def multUnis(args: Char*) : Long = {
//    var prod : Long = 1
//    if (args.length == 0) prod = 1
//    else if (args.length == 1) {
//      val arg : Char = args(0)
//      prod = arg.toLong
//    } else {
//      prod = args.head.toLong * multUnis(args.tail : _*)
//    }
//    prod
//  }

  def multUnis(args: Char*) : Long = {
    if (args.isEmpty) 1
    else {
      args.head.toLong * multUnis(args.tail : _*)
    }
  }
  multUnis("Hello".toCharArray : _*)

//  def power(x : Double, n : Int) : Double = {
//    var prod : Double = 1
//    if (n % 2 == 0 && n > 0) prod = power(x, n/2) * power(x, n/2)
//    else if (n > 0) prod = x * power(x, n-1)
//    else if (n == 0) prod = 1
//    else if (n < 0) prod = 1 / power(x, -n)
//    prod
//  }

  def power(x : Double, n : Int) : Double = {
    if (n % 2 == 0 && n > 0) {
      val result = power(x, n/2)
      result * result
    }
    else if (n > 0) x * power(x, n-1)
    else if (n == 0) 1
    else 1 / power(x, -n)
  }
  power(5,2)
  power(6.02, -1)
  power(2, -2)
}