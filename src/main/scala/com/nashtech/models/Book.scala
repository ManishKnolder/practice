package com.nashtech.models

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
case class Book(id: Int,
                title: String,
                price: Int,
                isbn: BigInt,
                authorId: Int)

object BookJson{
  implicit val bookFormat: RootJsonFormat[Book] = jsonFormat5(Book)
}