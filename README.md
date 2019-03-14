Email Service
=============
Application built with the following (main) technologies:

* Scala
* SBT
* Akka Http
* Specs2
* Amazon SES

Description
-----------
A Restful Service for sending Emails using Amazon SES
  
Testing
---------
- Run Unit tests
  ```
  sbt test
  ```
- Run Integration tests
  ```
  sbt it:test
  ```
  
- Run all tests
  ```
  sbt test it:test
  ```
  
- Run one test
  ```
  sbt test-only *EmailServiceRoutesSpec
  ```

Code Coverage
-------------
SBT-scoverage a SBT auto plugin: https://github.com/scoverage/sbt-scoverage
- Run tests with coverage enabled by entering:
  ```
  sbt clean coverage test
  ```

After the tests have finished, find the coverage reports inside target/scala-2.11/scoverage-report