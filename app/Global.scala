import play.api.GlobalSettings
import play.api.mvc.RequestHeader
import info.schleichardt.play2.basicauth.CredentialsFromConfChecker
import info.schleichardt.play2.basicauth.Authenticator
import utils.Config

import play.api.Logger


object Global extends GlobalSettings {

  override def onRouteRequest(request: RequestHeader) = {

    if (request.path == "/healthcheck") {
        super.onRouteRequest(request)
    }
    else if (Config.baseAuthenticationEnabled) {
        val requireBasicAuthentication = Authenticator(new CredentialsFromConfChecker)
        requireBasicAuthentication(request, () => super.onRouteRequest(request))
    }
    else if (Config.getAuthenticationEnabled) {
        val requireGetAuthentication = Authenticator(new CredentialsFromConfChecker, "get")
        requireGetAuthentication(request, () => super.onRouteRequest(request))
    }
    else{
      super.onRouteRequest(request)
    }
  }
}
