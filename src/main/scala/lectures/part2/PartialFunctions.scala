package lectures.part2

object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) =>
    x match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

//  println(aPartialFunction(2))

  //PF Utilities

//  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
//  println(lifted(2)) // Some(56)
//  println(lifted(3)) // None

  val pfChain = aPartialFunction.orElse[Int, Int] { case 45 =>
    67
  }

//  println(pfChain(45)) // 67

  //pf extends normal functions
  val aTotalFunction: Int => Int = { case 1 =>
    99
  }

  // HOF accept pf
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 72
    case 3 => 13
  }

//  println(aMappedList)

  val aPartialFunction2: PartialFunction[Int, Int] =
    new PartialFunction[Int, Int] {
      override def apply(v1: Int): Int = v1 * v1

      override def isDefinedAt(x: Int): Boolean = x > 0
    }

//  println(aPartialFunction2.isDefinedAt(1))

  val aChatBot: PartialFunction[String, String] = {
    case "Hello"             => "Hi"
    case "Bye"               => "Bye bye"
    case "How are you doing" => "Good, and you?"
  }

  scala.io.Source.stdin.getLines().map(aChatBot).foreach(println)
}
