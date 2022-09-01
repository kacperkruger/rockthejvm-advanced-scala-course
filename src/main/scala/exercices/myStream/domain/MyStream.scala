package exercices.myStream.domain

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsList(n: Int): List[A] = take(n).toList()

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc else tail.toList(head :: acc)
}

object MyStream {
  def from[A](start: A)(generate: A => A): MyStream[A] =
    new NotEmptyStream[A](start, MyStream.from(generate(start))(generate))
}
