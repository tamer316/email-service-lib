package dev.choppers.routes

import javax.inject.{Inject, Singleton}

import akka.http.scaladsl.model.StatusCodes
import dev.choppers.akka.http.Routing
import dev.choppers.model.api.EmailProtocol._
import dev.choppers.services._
import grizzled.slf4j.Logging

@Singleton
class EmailServiceRoutes @Inject()(emailService: EmailService) extends Routing with Logging {

  val route = {
    logRequestResult("email-service") {
      pathPrefix("email") {
        (post & entity(as[Email])) { email =>
          onSuccess(emailService.sendEmail(email)) {
            complete(StatusCodes.OK)
          }
        }
      }
    }
  }
}
