package lectures.part3

import java.util.concurrent.Executors

object Intro extends App {
  val aThread = new Thread(() => println("something in parallel"))

  aThread.start() // gives a signal to the JVM to start a thread
  aThread.join() // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() =>
    (1 to 5).foreach(_ => println("goodbye"))
  )
  threadHello.start()
  threadGoodbye.start()
  //different runs produce different results

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after one second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after two seconds")
  })

  pool.shutdown()
//  pool.execute(() => println("should not appear")) // throw exception in the calling thread

//  pool.shutdownNow()
  println(pool.isShutdown)
}
