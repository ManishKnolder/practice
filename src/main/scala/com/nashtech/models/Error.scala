package com.nashtech.models

trait Error

case class AlreadyExist(message: String) extends Error

case class EmptyList(message: String) extends Error

case class IdMisMatch(message: String) extends Error

case class NotValidated(message: String) extends Error
