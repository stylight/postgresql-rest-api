package utils

import com.typesafe.config.ConfigFactory

/**
 * Created by Engin Yoeyen on 04/10/14.
 */
object Config {
  private val config = ConfigFactory.load()

  val baseUrl = config.getString("application.baseUrl")

  val baseAuthenticationEnabled = config.hasPath("basic.auth")
  val getAuthenticationEnabled = config.hasPath("get.auth")

  override def toString = s"Config($config)"
}
