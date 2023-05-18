package com.nashtech.validator

import com.nashtech.models.Book

class BookValidator {
  def isBookValidated(book: Book): Boolean = {
    if (book.title.length > 20) false
    else true
  }
}
