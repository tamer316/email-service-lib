package dev.choppers.repositories

import javax.inject.Singleton

import dev.choppers.model.persistence.EmailEntity.{EmailEntity, InternetAddressEntity}
import dev.choppers.mongo.{MongoDB, Repository}
import reactivemongo.bson._

@Singleton
class EmailRepository extends Repository[EmailEntity] with MongoDB {
  val collectionName = "emails"

  implicit val internetAddressReader: BSONDocumentReader[InternetAddressEntity] = Macros.reader[InternetAddressEntity]

  implicit val internetAddressWriter: BSONDocumentWriter[InternetAddressEntity] = Macros.writer[InternetAddressEntity]

  implicit val reader: BSONDocumentReader[EmailEntity] = Macros.reader[EmailEntity]

  implicit val writer: BSONDocumentWriter[EmailEntity] = Macros.writer[EmailEntity]
}