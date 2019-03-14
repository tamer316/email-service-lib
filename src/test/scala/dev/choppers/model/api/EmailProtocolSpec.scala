package dev.choppers.model.api

import dev.choppers.model.api.EmailProtocol.{Email, InternetAddress}
import org.specs2.mutable.Specification

class EmailProtocolSpec extends Specification {

  "Email creation" should {
    "Fail if Source address is not a valid email" in {
      Email(InternetAddress("source"), InternetAddress("recipient@email.com"),
        "Subject", "Html", "Text") should throwA[IllegalArgumentException]
    }

    "Fail if Recipient address is not a valid email" in {
      Email(InternetAddress("source@email.com"), InternetAddress("recipient"),
        "Subject", "Html", "Text") should throwA[IllegalArgumentException]
    }

    "Fail if Subject is empty" in {
      Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"),
        "", "Html", "Text") should throwA[IllegalArgumentException]
    }

    "Fail if Html is empty" in {
      Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"),
        "Subject", "", "Text") should throwA[IllegalArgumentException]
    }

    "Fail if Text is empty" in {
      Email(InternetAddress("source@email.com"), InternetAddress("recipient@email.com"),
        "Subject", "Html", "") should throwA[IllegalArgumentException]
    }
  }
}
