package com.nashtech.models

import spray.json.DefaultJsonProtocol._

case class Author(id: Int,
                  firstName: String,
                  lastName: String,
                  gender: String,
                  emailId: String)

object AuthorJson {
  implicit val authorFormat = jsonFormat5(Author)
}
