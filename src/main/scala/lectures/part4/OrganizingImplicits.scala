package lectures.part4

object OrganizingImplicits extends App {
  implicit val reverseOrder: Ordering[Int] = Ordering.fromLessThan(_ > _)
//  implicit val normalOrder: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1, 4, 5, 3, 2).sorted)

  // scala.Predef

  /*
    Implicits (used as implicits parameters):
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  case class Person(name: String, age: Int)

  implicit val personOrder: Ordering[Person] =
    Ordering.fromLessThan(_.name < _.name)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 20),
    Person("John", 25)
  )
  println(persons.sorted)

  /*
    Implicits scope:
      - normal scope = local scope
      - imported scope
      - companion objects of all types involved in the method signature
   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPrice: Ordering[Purchase] =
      Ordering.fromLessThan((purchase1, purchase2) =>
        purchase1.nUnits * purchase1.unitPrice < purchase2.nUnits * purchase2.unitPrice
      )
  }

  object CountOrdering {
    implicit val unitCount: Ordering[Purchase] =
      Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object PriceOrdering {
    implicit val unitPrice: Ordering[Purchase] =
      Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  val purchases = List(
    Purchase(10, 5),
    Purchase(20, 1),
    Purchase(100, 0.5)
  )
  println(purchases.sorted)

}
