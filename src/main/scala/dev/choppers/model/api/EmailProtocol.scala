package dev.choppers.model.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import dev.choppers.akka.http.json.JavaDateTimeJsonProtocol
import dev.choppers.ses.model.{Address, Content, Email => SesEmail}
import spray.json._

object EmailProtocol extends SprayJsonSupport with DefaultJsonProtocol with JavaDateTimeJsonProtocol with Validators {

  case class InternetAddress(address: String, personal: Option[String] = None, charset: Option[String] = None)

  case class Email(source: InternetAddress, recipient: InternetAddress, subject: String, html: String,
                   text: String, replyTo: Option[InternetAddress] = None) {
    requireEmail("Source", source.address)
    requireEmail("Recipient", recipient.address)
    requireNonEmptyText("Subject", subject)
    replyTo map { rt => requireEmail("Reply To", rt.address) }
    requireNonEmptyText("Html", html)
    requireNonEmptyText("Text", text)
  }

  implicit val internetAddressFormat = jsonFormat3(InternetAddress.apply)
  implicit val emailFormat = jsonFormat6(Email.apply)

  implicit def toAddress(internetAddress: InternetAddress): Address = {
    Address(address = internetAddress.address,
      personal = internetAddress.personal,
      charset = internetAddress.charset.fold("UTF-8")(c => c))
  }

  implicit def toSesEmail(email: Email): SesEmail = {
    SesEmail(subject = Content(email.subject), bodyHtml = Some(Content(email.html)),
      bodyText = Some(Content(email.text)), to = Seq(email.recipient),
      source = email.source, replyTo = email.replyTo.fold(Seq.empty[Address])(r => Seq(r)))
  }
}