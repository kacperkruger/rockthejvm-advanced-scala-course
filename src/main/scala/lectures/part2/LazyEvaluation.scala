package lectures.part2

object LazyEvaluation extends App {
  lazy val x: Int = throw new RuntimeException

  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
//  println(if (simpleCondition && lazyCondition) "yes" else "no")

  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val lazyN = n
    lazyN + lazyN + lazyN + 1
  }
  def retrieveMagicValue = {
    Thread.sleep(1000)
    42
  }

//  println(byNameMethod(retrieveMagicValue))

  //filtering with lazy val
  def lessThen30(x: Int): Boolean = {
    println(s"$x less then 30??")
    x < 30
  }

  def greaterThen20(x: Int): Boolean = {
    println(s"$x grater then 20??")
    x > 20
  }

  val numbers = List(1, 25, 6, 2, 746, 34, 96)
  val lt30 = numbers.filter(lessThen30)
  val gt20 = lt30.filter(greaterThen20)
//  println(gt20)

  val lt30lazy = numbers.withFilter(lessThen30)
  val gt20lazy = lt30lazy.withFilter(greaterThen20)
  println("-" * 100)
  println(gt20lazy)
  gt20lazy.foreach(println)

  // for-comprehension uses withFilter
  for {
    a <- List(1, 2, 3) if a % 3 == 0
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 3 == 0).map(_ + 1)
}
