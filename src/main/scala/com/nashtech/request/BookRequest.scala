package com.nashtech.request

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.nashtech.implement.book.BookImplementation
import com.nashtech.models.Book
import com.nashtech.models.BookJson.bookFormat
import com.nashtech.validator.BookValidator
import play.api.libs.json.Json

object BookRequest extends App {
  implicit val parseJson = Json.format[Book]
  private val host = "localhost"
  private val port = 8090
  implicit val system = ActorSystem("HTTP_SERVER")
  implicit val ec = system.dispatcher
  val bookImplementation = new BookImplementation(new BookValidator)
  val listOfBook = List(
    Json.parse(
      """
        |{
        | "id" : 202301,
        | "title" : "Scala Basics",
        | "price" : 200,
        | "isbn" : 121416,
        | "authorId" : 101
        | }
        |""".stripMargin
    ).as[Book],
    Json.parse(
      """
        |{
        | "id" : 202302,
        | "title" : "Scala Intermediate",
        | "price" : 400,
        | "isbn" : 121190,
        | "authorId" : 102
        | }
        |""".stripMargin
    ).as[Book]
  )
  listOfBook.map(book => bookImplementation.create(book))
  val routes: Route = {
    pathPrefix("api") {
      path("books") {
        post {
          entity(as[Book]) { book =>
            bookImplementation.create(book)
            complete("Created Book")
          }
        } ~
          get {
            val success = bookImplementation.getAll()
            success match {
              case Right(value) => complete(StatusCodes.OK, s"${value.map(book => Json.toJson(book))}")
              case Left(value) => complete(StatusCodes.NotFound, s"$value")
            }
          }
      } ~
        /*update*/
        path("book" / IntNumber) { bookId =>
          get {
            val success = bookImplementation.get(bookId)
            success match {
              case Some(value) => complete(StatusCodes.OK, s"${Json.toJson(value)}")
              case None => complete(StatusCodes.NotFound)
            }
          }
        } ~
        /*update*/
        path("update-book" / IntNumber) { id =>
          patch {
            entity(as[Book]) { book =>
              val check = bookImplementation.get(id)
              check match {
                case Some(_) =>
                  val updateBook = bookImplementation.put(book.copy(title = "ROCKY"))
                  updateBook match {
                    case Left(value) => complete(StatusCodes.NotFound, s"$value")
                    case Right(()) => complete(StatusCodes.OK)
                  }
                case None => complete(StatusCodes.NotFound)
              }

            }
          } ~
            get {
              complete(StatusCodes.OK, s"${bookImplementation.getAll()}")
            }
        }
    }
  }
  val bindingFuture = Http().newServerAt(host, port).bind(routes)
  println("Server available @ : http://localhost:8090/api")
  sys.addShutdownHook {
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

