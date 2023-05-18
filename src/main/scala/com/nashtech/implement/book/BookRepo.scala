package com.nashtech.implement.book

import com.nashtech.models.{Book, Error}

trait BookRepo {
  def create(book: Book): Either[Error, List[Book]]

  def get(id: Int): Option[Book]

  def getAll(): Either[Error, List[Book]]

  def put(book: Book): Either[Error, Unit]

  def delete(id: Int): Option[List[Book]]

}
