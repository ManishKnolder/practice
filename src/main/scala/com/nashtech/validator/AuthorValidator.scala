package com.nashtech.validator

import com.nashtech.models.Author

class AuthorValidator {
  def isAuthorValidated(author: Author): Boolean = {
    if ((author.firstName + author.lastName).length > 20) false
    else true
  }
}
