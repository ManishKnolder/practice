package com.nashtech.implement.author

import com.nashtech.models.{AlreadyExist, Author, EmptyList, IdMisMatch, NotValidated}
import com.nashtech.validator.AuthorValidator

import scala.collection.mutable.ListBuffer
import com.nashtech.models.Error

class AuthorImplementation(authorValidator: AuthorValidator) extends AuthorRepo {
  private val listOfAuthor: ListBuffer[Author] = ListBuffer[Author]().empty

  override def create(author: Author): Either[Error, List[Author]] = {
    if (authorValidator.isAuthorValidated(author)) {
      val check = listOfAuthor.find(list => list.id == author.id || (list.firstName + list.lastName) == (author.firstName + author.lastName))
      check match {
        case Some(_) => Left(AlreadyExist("Having similar author ID, cannot create new Author"))
        case None => Right((listOfAuthor += author).toList)
      }
    }
    else Left(NotValidated("Author name length is more than 20 characters"))
  }

  override def get(id: Int): Option[Author] = {
    listOfAuthor.find(list => list.id == id)
  }

  override def getAll(): Either[Error, List[Author]] = {
    if (listOfAuthor.isEmpty) Left(EmptyList("List of Author is Empty"))
    else Right(listOfAuthor.toList)
  }

  override def put(author: Author): Either[Error, Unit] = {
    val check = listOfAuthor.find(list => list.id == author.id)
    check match {
      case Some(_) =>
        listOfAuthor.map { list =>
          if (list.id == author.id) author
          else list
        }
        Right(println("Updated"))
      case None => Left(IdMisMatch("Author Id doesn't match "))
    }
  }

  override def delete(id: Int): Option[List[Author]] = {
    val check = listOfAuthor.find(list => list.id == id)
    check match {
      case Some(value) => Some((listOfAuthor -= value).toList)
      case None => None
    }
  }
}