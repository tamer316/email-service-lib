package dev.choppers.model.persistence

import java.time.Instant

import dev.choppers.model.api.EmailProtocol.{Email, InternetAddress}
import reactivemongo.bson._

object EmailEntity {

  case class InternetAddressEntity(address: String, personal: Option[String] = None, charset: Option[String] = None)

  case class EmailEntity(_id: BSONObjectID = BSONObjectID.generate,
                         source: InternetAddressEntity,
                         recipient: InternetAddressEntity,
                         subject: String,
                         html: String,
                         text: String,
                         replyTo: Option[InternetAddressEntity],
                         createdDate: Instant = Instant.now)

  implicit def toInternetAddressEntity(internetAddress: InternetAddress): InternetAddressEntity = {
    InternetAddressEntity(address = internetAddress.address, personal = internetAddress.personal, charset = internetAddress.charset)
  }

  implicit def toEmailEntity(email: Email): EmailEntity = {
    EmailEntity(source = email.source, recipient = email.recipient, subject = email.subject,
      html = email.html, text = email.text, replyTo = email.replyTo.map(ia => ia))
  }
}
