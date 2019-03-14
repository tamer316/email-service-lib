package dev.choppers.services

import com.amazonaws.services.simpleemail.model.SendEmailResult
import dev.choppers.model.api.EmailProtocol.{Email, InternetAddress}
import dev.choppers.model.persistence.EmailEntity.EmailEntity
import dev.choppers.repositories.EmailRepository
import dev.choppers.ses.SESClient
import dev.choppers.ses.model.{Address, Content, Email => SesEmail}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class EmailServiceSpec extends Specification with Mockito {

  private trait Context extends Scope {
    val sesClient = mock[SESClient]
    val emailRepository = mock[EmailRepository]
    val emailService = new EmailService(emailRepository, sesClient)
  }

  "sendEmail" should {
    "Return Failure if Client returns Failure" in new Context {
      val email = Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"), "Subject", "Html", "Text")
      sesClient.send(any[SesEmail]) returns Future.failed(new Exception())

      Await.result(emailService.sendEmail(email), 1 second) must throwA[Exception]

      there was one(sesClient).send(any[SesEmail])
      there were noCallsTo(emailRepository)
    }

    "Return Failure if Repository returns Failure" in new Context {
      val email = Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"), "Subject", "Html", "Text")
      sesClient.send(any[SesEmail]) returns Future.successful(new SendEmailResult)
      emailRepository.insert(any[EmailEntity]) returns Future.failed(new Exception)

      Await.result(emailService.sendEmail(email), 1 second) must throwA[Exception]

      there was one(sesClient).send(any[SesEmail])
      there was one(emailRepository).insert(any[EmailEntity])
    }

    "Send and save an Email successfully" in new Context {
      val email = Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"), "Subject", "Html", "Text")
      sesClient.send(any[SesEmail]) returns Future.successful(new SendEmailResult())
      emailRepository.insert(any[EmailEntity]) returns Future.successful({})

      val res = Await.result(emailService.sendEmail(email), 1 second)

      res mustEqual {}

      val sesEmailCaptor = capture[SesEmail]
      val emailEntityCaptor = capture[EmailEntity]

      there was one(sesClient).send(sesEmailCaptor)
      val sesEmail = sesEmailCaptor.value
      sesEmail.source mustEqual Address("source@email.com")
      sesEmail.to mustEqual Seq(Address("recipient@email.com"))
      sesEmail.subject mustEqual Content("Subject")
      sesEmail.bodyHtml mustEqual Some(Content("Html"))
      sesEmail.bodyText mustEqual Some(Content("Text"))

      there was one(emailRepository).insert(emailEntityCaptor)
      val emailEntity = emailEntityCaptor.value
      emailEntity.source.address mustEqual "source@email.com"
      emailEntity.recipient.address mustEqual "recipient@email.com"
      emailEntity.subject mustEqual "Subject"
      emailEntity.html mustEqual "Html"
      emailEntity.text mustEqual "Text"
    }
  }
}
