package com.nashtech.implement.book


import com.nashtech.models.{AlreadyExist, Book, EmptyList, Error, IdMisMatch, NotValidated}
import com.nashtech.validator.BookValidator

import scala.collection.mutable.ListBuffer

class BookImplementation(titleValidate: BookValidator) extends BookRepo {
  private var listOfBook: ListBuffer[Book] = ListBuffer[Book]().empty

  override def create(book: Book): Either[Error, List[Book]] = {
    if (titleValidate.isBookValidated(book)) {
      val check = listOfBook.find(list => list.id == book.id || list.title == book.title)
      check match {
        case Some(_) => Left(AlreadyExist("Having similar book ID, Cannot create new Book"))
        case None =>
          Right((listOfBook += book).toList)
      }
    }
    else Left(NotValidated("Book length is more than 20 characters"))

  }

  override def get(id: Int): Option[Book] = {
    listOfBook.find(list => list.id == id)
  }

  override def getAll(): Either[Error, List[Book]] = {
    if (listOfBook.isEmpty) Left(EmptyList("List of Book is Empty"))
    else Right(listOfBook.toList)
  }

  override def put(book: Book): Either[Error, Unit] = {
    val check = listOfBook.find(list => list.id == book.id)
    check match {
      case Some(_) =>
        val result = listOfBook.map { list =>
          if (list.id == book.id) book
          else list
        }
        listOfBook=result
        Right(println("Updated"))
      case None => Left(IdMisMatch("The Book ID is not similar to exist Book ID"))
    }
  }

  override def delete(id: Int): Option[List[Book]] = {
    val check = listOfBook.find(list => list.id == id)
    check match {
      case Some(value) => Some((listOfBook -= value).toList)
      case None => None
    }
  }
}
