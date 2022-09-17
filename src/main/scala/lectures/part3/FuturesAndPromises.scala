package lectures.part3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

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
}
