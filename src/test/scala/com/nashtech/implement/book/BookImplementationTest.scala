package com.nashtech.implement.book

import com.nashtech.models.{Book, EmptyList, IdMisMatch}
import com.nashtech.validator.BookValidator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class BookImplementationTest extends AnyFlatSpec with Matchers {
  val bookImplementation = new BookImplementation(new BookValidator)

  "get method" should "return None because list is empty" in {
    val id = 202301
    val actualOutput = bookImplementation.get(id)
    val expectedOutput = None
    expectedOutput shouldBe actualOutput
  }

  "get All method" should "return list of empty" in {
    val actualOutput = bookImplementation.getAll()
    val expectedOutput = Left(EmptyList("List of Book is Empty"))
    expectedOutput shouldBe actualOutput
  }

  "put" should "return None because list is empty" in {
    val book = Book(202301, "Scala Check", 600, 21221, 121)
    val actualOutput = bookImplementation.put(book)
    val expectedOutput = Left(IdMisMatch("The Book ID is not similar to exist Book ID"))
    expectedOutput shouldBe actualOutput
  }

  "delete method" should "return None because list is empty" in {
    val id = 202301
    val actualOutput = bookImplementation.delete(id)
    val expectedOutput = None
    expectedOutput shouldBe actualOutput
  }

  "create method" should "create not match with any new book" in {
    val book = Book(2023, "Scala Something", 200, 2190, 101)
    val actualOutput = bookImplementation.create(book)
    val expectedOutput = Right(List(Book(20231, "Scala ", 200, 2190, 101)))
    assert(expectedOutput != actualOutput)
  }

  "create method" should "create new book" in {
    val book = Book(202310, "Scala Basics", 200, 2190, 101)
    val actualOutput = bookImplementation.create(book)
    val expectedOutput = Right(List(Book(2023, "Scala Something", 200, 2190, 101), Book(202310, "Scala Basics", 200, 2190, 101)))
    expectedOutput shouldBe actualOutput
  }

  "get method" should "return Book if id found" in {
    val id = 202301
    val actualOutput = bookImplementation.get(id)
    val expectedOutput = None
    expectedOutput shouldBe actualOutput
  }

  "get method" should "return Book  id found" in {
    val id = 202310
    val actualOutput = bookImplementation.get(id)
    val expectedOutput = Some(Book(202310, "Scala Basics", 200, 2190, 101))
    expectedOutput shouldBe actualOutput
  }

  "get All method" should "return list of list" in {
    val actualOutput = bookImplementation.getAll()
    val expectedOutput = Right(List(Book(2023, "Scala Something", 200, 2190, 101), Book(202310, "Scala Basics", 200, 2190, 101)))
    expectedOutput shouldBe actualOutput
  }

  "put" should "return updated list because list is contain" in {
    val book = Book(202310, "Scala Check", 600, 21221, 121)
    val actualOutput = bookImplementation.put(book)
    val expectedOutput = Left(IdMisMatch("The Book ID is not similar to exist Book ID"))
    assert(expectedOutput != actualOutput)
  }

  "delete method" should "return new list" in {
    val id = 202310
    val actualOutput = bookImplementation.delete(id)
    val expectedOutput = Some(List(Book(2023, "Scala Something", 200, 2190, 101)))
    expectedOutput shouldBe actualOutput
  }

  "filterBook" should "return empty LList" in {
    val actualOutput = bookImplementation.filterBooks(Some("101"), None, None)
    val expectedOutput = List()
    assert(actualOutput == expectedOutput)
  }
}
