/***
  * This code has been retrieved from following repo
  * https://github.com/schleichardt/Play-2-Basic-Auth
  *
  * */

package info.schleichardt.play2.basicauth

import play.api.mvc.{AnyContent, RequestHeader, Action, Handler}
import play.api.mvc.Results._
import play.api.Play
import play.api.libs.Crypto
import org.apache.commons.codec.binary.Base64.encodeBase64

import play.api.Logger

case class Credentials(userName: String, password: String)

trait CredentialChecker {
  def authorized(credentials: Option[Credentials], authType: String): Boolean
}

class CredentialsFromConfChecker extends CredentialChecker {
  import CredentialsFromConfChecker._

  override def authorized(credentials: Option[Credentials], authType: String) = {

    if (credentials.isDefined) {
      val authConf = Play.current.configuration.getConfig("%s.auth".format(authType))

      authConf.flatMap(_.getString(credentials.get.userName)).exists(_ == credentials.get.password)
    } else {
      false
    }
  }
}

object CredentialsFromConfChecker {
  import BasicAuth._
  def hashCredentialsWithApplicationSecret(credentials: Credentials): String = {
    //TODO use function cache?
    Crypto.sign(encodeCredentials(credentials))
  }

  //useful to generate with a console the hashes
  //info.schleichardt.play2.basicauth.CredentialsFromConfCheck.hashCredentialsWithApplicationSecret("username", "password", "application secret")
  def hashCredentialsWithApplicationSecret(userName: String, password: String, secret: String): String = {
    val credentials = Credentials(userName, password)
    Crypto.sign(encodeCredentials(credentials), secret.getBytes)
  }
}

case class Authenticator(checker: CredentialChecker, authType: String = "basic", message: String = "Authentication needed") extends Function2[RequestHeader, () => Option[Handler], Option[Handler]] {

  import BasicAuth._
  import GetAuth._

  override def apply(request: RequestHeader, defaultHandler: () => Option[Handler]): Option[Handler] = {

    var mayBeCredentials: Option[Credentials] = Option(Credentials("", ""))

    authType match {
      case "get" => {
        mayBeCredentials = extractAuthDataFromGet(request)
      }
      case _ => {
        mayBeCredentials = extractAuthDataFromHeader(request.headers.get("Authorization"))
      }
    }

    if(checker.authorized(mayBeCredentials, authType)){
      defaultHandler()
    } else {
       unauthorizedHandlerOption(message)
    }
  }
}

object GetAuth {
  def extractAuthDataFromGet(request: RequestHeader): Option[Credentials] = {

    Option(Credentials(request.getQueryString("user").getOrElse(""),
      request.getQueryString("passwd").getOrElse("")))
  }
}

object BasicAuth {
  def encodeCredentials(credentials: Credentials): String = {
    val formatted = credentials.userName + ":" + credentials.password
    new String(encodeBase64(formatted.getBytes))
  }

  def extractAuthDataFromHeader(headerOption: Option[String]): Option[Credentials] = {
    //inspired from guillaumebort  https://gist.github.com/2328236 24.09.2012
    headerOption.flatMap {
      authorization =>
        authorization.split(" ").drop(1).headOption.flatMap {
          encoded =>
            new String(org.apache.commons.codec.binary.Base64.decodeBase64(encoded.getBytes)).split(":").toList match {
              case userName :: password :: Nil if userName.length > 0 && password.length > 0 => Option(Credentials(userName, password))
              case _ => None
            }
        }
    }
  }

  def unauthorizedHandlerOption(message: String): Option[Action[AnyContent]] = Option(Action {
    Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="%s"""".format(message))
  })
}
