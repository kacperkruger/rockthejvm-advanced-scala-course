package lectures.part3

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration.*

object FuturesAndPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(3000)
    44
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // (global) which is passed by the compiler

//  println(aFuture.value) // Option[Try[Int]]

//  println("waiting for the future")
  aFuture.onComplete {
    case Failure(exception) => println(exception)
    case Success(value)     => println(value)
  } // call by SOME thread

  Thread.sleep(3000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = println(
      s"[${this.name}] poking ${anotherProfile.name}"
    )
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.3-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching form database
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(500))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

  }
  // client: mark poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
//  mark.onComplete {
//    case Failure(exception) => exception.printStackTrace()
//    case Success(markProfile) =>
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Failure(exception)   => exception.printStackTrace()
//        case Success(billProfile) => markProfile.poke(billProfile)
//      }
//  }

  // functional composition of futures
  // map, flatMap, filter
  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend =
    mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted =
    marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown_id").recover {
    case _: Throwable => Profile("fb.id.0-dummy", "Forever Alone")
  }

  val aFetchProfileNoMatterWhat =
    SocialNetwork.fetchProfile("unknown_id").recoverWith { case _: Throwable =>
      SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

  val fallbackResult = SocialNetwork
    .fetchProfile("unknown_id")
    .fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // online banking app
  case class User(name: String)
  case class Transaction(
      sender: String,
      receiver: String,
      amount: Double,
      status: String
  )

  object BankingApp {
    val name = "Rock the JVM Banking"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(400)
      User(name)
    }

    def createTransaction(
        user: User,
        merchantName: String,
        amount: Double
    ): Future[Transaction] = Future {
      Thread.sleep(1001)
      Transaction(user.name, merchantName, amount, "Success")
    }

    def purchase(
        username: String,
        item: String,
        merchantName: String,
        cost: Double
    ): String = {
      // fetch user from DB
      // create transaction
      // wait for the transaction to finish

      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(
        transactionStatusFuture,
        5.seconds
      ) // implicit conversions -> pimp my library
    }
  }

  println(BankingApp.purchase("Daniel", "iPhone", "rock the jvm store", 3000))

  // promises

  val promise = Promise[Int]() // "controller" over future
  val future = promise.future

  future.onComplete {
    case Success(r) => println(s"[consumer] i've received $r")
    case Failure(e) => e.printStackTrace()
  }

  val producer = new Thread(() => {
    println("[producer] crunching numbers")
    Thread.sleep(500)
    // "fulfilling the promise
    promise.success(44)
    println("[producer] done")
  })

  Thread.sleep(1000)

  producer.start()
}
