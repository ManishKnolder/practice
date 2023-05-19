package com.nashtech.request

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.http.scaladsl.server.Route
import com.nashtech.implement.author.AuthorImplementation
import com.nashtech.models.Author
import com.nashtech.models.AuthorJson.authorFormat
import com.nashtech.validator.AuthorValidator
import play.api.libs.json.Json

object AuthorRequest extends App {
  implicit val parseJson = Json.format[Author]
  private val host = "localhost"
  private val port = 8092
  implicit val system = ActorSystem("HTTP_SERVER")
  implicit val ec = system.dispatcher
  private val authorImplementation = new AuthorImplementation(new AuthorValidator)

  private val listOfAuthors = List(
    Json.parse {
      """
        |{
        | "id" : 101,
        | "firstName" : "Manish",
        | "lastName" : "Mishra",
        | "gender" : "Male",
        | "emailId" : "mm0255275@gmail.com"
        |}
        |""".stripMargin
    }.as[Author],
    Json.parse {
      """
        |{
        | "id" : 102,
        | "firstName" : "Ajit",
        | "lastName" : "Kumar",
        | "gender" : "Male",
        | "emailId" : "ajit@knoldus.com"
        |}
        |""".stripMargin
    }.as[Author]
  )
  listOfAuthors.map(author => authorImplementation.create(author))
  val routes: Route = {
    pathPrefix("api") {
      path("authors") {
        post {
          entity(as[Author]) { author =>
            authorImplementation.create(author)
            complete("Created Author")
          }
        } ~
          get {
            val success = authorImplementation.getAll()
            success match {
              case Right(value) => complete(StatusCodes.OK, s"${value.map(author => Json.toJson(author))}")
              case Left(value) => complete(StatusCodes.BadRequest, s"$value")
            }
          }
      } ~
        path("author" / IntNumber) { authorId =>
          get {
            val success = authorImplementation.get(authorId)
            success match {
              case Some(value) => complete(StatusCodes.OK, s"${Json.toJson(value)}")
              case None => complete(StatusCodes.NotFound)
            }
          }
        } ~
        path("update-authors" / IntNumber) { authorId =>
          patch {
            entity(as[Author]) { author =>
              val check = authorImplementation.get(authorId)
              check match {
                case Some(_) =>
                  val updatedAuthor = authorImplementation.put(author.copy(firstName = "Rakesh", lastName = "Kumar"))
                  updatedAuthor match {
                    case Left(value) => complete(StatusCodes.NotFound, s"$value")
                    case Right(()) => complete(StatusCodes.OK)
                  }
                case None => complete(StatusCodes.NotFound)
              }
            }
          } ~
            get {
              complete(StatusCodes.OK, s"${authorImplementation.getAll()}")
            }
        } ~
        path("delete" / IntNumber) { authorId =>
          delete {
            val deleteAuthor = authorImplementation.delete(authorId)
            deleteAuthor match {
              case Some(value) => complete(StatusCodes.OK, s"$value")
              case None => complete(StatusCodes.BadRequest)
            }
          }
        }
    }
  }
  val bindingFuture = Http().newServerAt(host, port).bind(routes)
  println("Server available @ : http://localhost:8092/api")
  sys.addShutdownHook {
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

