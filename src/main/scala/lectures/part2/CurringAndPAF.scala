package lectures.part2

object CurringAndPAF extends App {
  // curried functions
  val superAdder: Int => Int => Int = x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method
  val add4: Int => Int = curriedAdder(4) // lifting = ETA-EXPANSION

  // functions != method (JVM limitation)
  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(x => inc(x)) // ETA-EXPANSION

  //Partial function application
  val add5 = curriedAdder(5) _ // Int => Int

  val simpleAddFunction: (Int, Int) => Int = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y
  //  val curriedFunction: Int => Int => Int = x => y => x + y

  // add7 exercise
  val add7 = (x: Int) => simpleAddMethod(x, 7)
  val add7_2 = simpleAddMethod.curried(7)
  val add7_6 = simpleAddMethod(7, _: Int)

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_)

  val add7_5 = simpleAddMethod(7, _: Int)

  def concatenate(x: String, y: String, z: String): String = x + y + z
  val insertName: String => String =
    concatenate("Hi, I'm ", _: String, ", how are you?")
  println(insertName("Ben"))

  def curriedFormatter(formatString: String)(number: Double): String =
    formatString.format(number)

  val numberList = List(2.6734, Math.PI, Math.E, 1, 2432452.52524, 2e-13)

  val simpleFormat = curriedFormatter("%4.2f") _
  val fancyFormat = curriedFormatter("%8.6f") _
  val moreFancyFormat = curriedFormatter("%14.12f")

  println(numberList.map(simpleFormat))
  println(numberList.map(fancyFormat))
  println(numberList.map(moreFancyFormat))
  println(numberList.map(curriedFormatter("%4.2f")))

  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42
  val paf = parenMethod _

  byName(method)
  byName(parenMethod())
  byName(42)
//  byName(() => 42)
  byName((() => 42)())
//  byName(paf)
//  byName(parenMethod)
//  byFunction(method)
//  byFunction(parenMethod())
  byFunction(paf)
  byFunction(() => 42)
//  byFunction((() => 42)())
  byFunction(parenMethod)
}
