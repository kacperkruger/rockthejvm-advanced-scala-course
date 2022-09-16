package lectures.part3

import scala.annotation.tailrec

object ConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x = 2)

    thread1.start()
    thread2.start()
    println(x) // race condition
  }

  case class BankAccount(var amount: Int)

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.amount -= price
  }

  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account
      .synchronized { // does not allow multiple threads to run the critical section at the same time
        account.amount -= price // critical section
      }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "phone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000)
        println(s"bank is broken :// ${account.amount}")
    }
  }

  def inceptionSleep(max: Int, i: Int = 1): Thread = {
    new Thread {
      if (i < max)
        val newThread = inceptionSleep(max, i + 1)
        newThread.start()
        newThread.join()

      println(s"hello from thread $i")
    }
  }

  def minMax(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    println(x)
  }

  // max = 100
  // min = 1

  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // solution: join the worker thread
    awesomeThread.join()
    println(message)
  }

  def main(args: Array[String]): Unit = {
    inceptionSleep(30).start()
  }
}
