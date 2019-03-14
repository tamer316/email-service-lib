package dev.choppers.services

import javax.inject.{Inject, Singleton}

import dev.choppers.model.api.EmailProtocol._
import dev.choppers.repositories.EmailRepository
import dev.choppers.ses.SESClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class EmailService @Inject()(val emailRepository: EmailRepository,
                             val sesClient: SESClient) {

  def sendEmail(email: Email): Future[Unit] = {
    sesClient.send(email) flatMap { result =>
      emailRepository.insert(email)
    }
  }
}