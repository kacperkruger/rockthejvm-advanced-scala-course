package exercices

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean = false
  override def +(elem: A): MySet[A] = new NotEmptySet[A](elem, this)

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  override def map[B](f: A => B): MySet[B] = new EmptySet[B]
  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  override def filter(predicate: A => Boolean): MySet[A] = this
  override def foreach(f: A => Unit): Unit = ()
}
class NotEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean =
    if (head == elem) true else tail.contains(elem)

  override def +(elem: A): MySet[A] =
    if (this.contains(elem)) this else new NotEmptySet[A](elem, this)

  override def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head

  override def map[B](f: A => B): MySet[B] = tail.map(f) + f(head)

  override def flatMap[B](f: A => MySet[B]): MySet[B] =
    tail.flatMap(f) ++ f(head)

  override def filter(predicate: A => Boolean): MySet[A] =
    if (predicate(head)) tail.filter(predicate) + head
    else tail.filter(predicate)

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
}

object MySet {
  def apply[A](elems: A*): MySet[A] = {
    buildSet[A](elems, new EmptySet[A])
  }

  @tailrec private def buildSet[A](valSeq: Seq[A], acc: MySet[A]): MySet[A] =
    if (valSeq.isEmpty) acc else buildSet[A](valSeq.tail, acc + valSeq.head)
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3)
  ((s + 5) ++ MySet(-1, -2) + 3).foreach(println)
  val newSet = s.flatMap(x => MySet(x, x * 10))
}
