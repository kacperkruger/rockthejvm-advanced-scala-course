package lectures.part5

object Variance extends App {
  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  class Cage[T]

  // covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // invariance
  class ICage[T]
//  val icage: ICage[Animal] = new ICage[Cat]
//  val x: Int = "hello world"

  // contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCge[+T](val animal: T) // covariant position

//  class ContravarientCage[-T](val animal: T)

//  class CovariantVariableCage[+T](var animal: T) // types of vars in contravariant position

//  class ContravariantVariableCage[-T](var animal: T) // COVARIANT POSITION

  class InvariantVariableCage[T](var animal: T)

//  trait AnotherCovariantCage[+T] {
//    def addAnimal(animal: T) // CONTRAVARIANT POSITION
//  }

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = ??? // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val moreMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types

  class PetShop[-T] {
//    def get: T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = ???

  }
  val shop: PetShop[Dog] = new PetShop[Animal]
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule:
      - method arguments are in CONTRAVARIANT position
      - return types are in COVARIANT position
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class InvariantParking[T](things: List[T]) {
    def park(vehicle: T): InvariantParkingIList[T] = ???
    def impound(vehicles: List[T]): InvariantParkingIList[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => InvariantParking[S]): InvariantParking[S] = ???
  }

  class CovariantParking[+T](things: List[T]) {
    def park[S >: T](vehicle: S): CovariantParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CovariantParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CovariantParking[S]): CovariantParking[S] = ???
  }

  class ContravariantParking[-T](things: List[T]) {
    def park(vehicle: T): ContravariantParking[T] = ???
    def impound(vehicles: List[T]): ContravariantParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](
        f: R => ContravariantParking[S]
    ): ContravariantParking[S] = ???
  }

  class InvariantParkingIList[T](things: IList[T]) {
    def park(vehicle: T): InvariantParkingIList[T] = ???
    def impound(vehicles: IList[T]): InvariantParkingIList[T] = ???
    def checkVehicles(conditions: String): IList[T] = ???
  }

  class CovariantParkingIList[+T](things: IList[T]) {
    def park[S >: T](vehicle: S): CovariantParkingIList[S] = ???
    def impound[S >: T](vehicles: IList[S]): CovariantParkingIList[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class ContravariantParkingIList[-T](things: IList[T]) {
    def park(vehicle: T): ContravariantParkingIList[T] = ???
    def impound[S <: T](vehicles: IList[S]): ContravariantParkingIList[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

}
