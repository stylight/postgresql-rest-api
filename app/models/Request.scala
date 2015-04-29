package models

/**
 * Created by Engin Yoeyen on 06/10/14.
 */
case class Request(name: String,
  func:Option[String] = None,
  limit:Option[String] = None,
  offset:Option[String] = None,
  by:Option[String] = None,
  order:Option[String] = None,
  column:Option[String] = None,
  filter:Option[String] = None,
  language:Option[String] = None,
  field:Option[String] = None,
  format:Option[String] = None)