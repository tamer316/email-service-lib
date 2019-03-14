package dev.choppers.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.ValidationRejection
import akka.http.scaladsl.testkit.Specs2RouteTest
import dev.choppers.model.api.EmailProtocol._
import dev.choppers.services.EmailService
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import spray.json.{JsObject, JsString}

import scala.concurrent.Future

class EmailServiceRoutesSpec extends Specification with Specs2RouteTest with Mockito {

  private trait Context extends Scope {
    val emailService = mock[EmailService]
    val emailServiceRoutes = new EmailServiceRoutes(emailService)
  }

  "EmailServiceRoutes" should {
    "Send an email successfully" in new Context {
      val email = Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"), "Subject", "Html", "Text")
      emailService.sendEmail(email) returns Future.successful({})
      Post(s"/email", email) ~> emailServiceRoutes.route ~> check {
        status mustEqual StatusCodes.OK
      }
      there was one(emailService).sendEmail(email)
    }

    "Fail if source address is not a valid email" in new Context {
      val email = JsObject("source" -> JsObject("address" -> JsString("source")),
      "recipient" -> JsObject("address" -> JsString("recipient@email.com")),
      "subject" -> JsString("Subject"), "html" -> JsString("Html"), "text" -> JsString("Text"))
      Post(s"/email", email) ~> emailServiceRoutes.route ~> check {
        rejection must beAnInstanceOf[ValidationRejection]
      }
      there was noCallsTo(emailService)
    }
  }
}
