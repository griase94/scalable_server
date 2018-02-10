

package de.scalable.database.tables

import java.time.LocalDateTime

import de.scalable.database.ScalablePostgresProfile.api._
import de.scalable.model._
import slick.lifted.ProvenShape
import slick.sql.SqlProfile.ColumnOption.NotNull

class PartyTable(tag: Tag)
  extends BaseTableLong[Party](tag, "party") {

  def key: Rep[String] = column[String]("key", NotNull)
  def name: Rep[String] = column[String]("name", NotNull)

  def createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at", NotNull)

  // scalastyle:off method.name
  def * : ProvenShape[Party] =
    (id, key, name, createdAt) <>
      (Party.tupled, Party.unapply)

  // scalastyle:on method.name
}