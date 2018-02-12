

package de.scalable.database.tables

import java.time.LocalDateTime

import de.scalable.database.ScalablePostgresProfile.api._
import de.scalable.model._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import slick.sql.SqlProfile.ColumnOption.NotNull

class PartyQueueTable(tag: Tag)
  extends BaseTableLong[PartyQueueEntry](tag, "party_queue") {

  def partyID: Rep[Long] = column[Long]("party_id", NotNull)
  def songID: Rep[Long] = column[Long]("song_id", NotNull)
  def upvotes: Rep[Int] = column[Int]("upvotes", NotNull, O.Default(0))
  def downvotes: Rep[Int] = column[Int]("downvotes",NotNull, O.Default(0))

  def createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at", NotNull)

  // scalastyle:off method.name
  def * : ProvenShape[PartyQueueEntry] =
    (id, partyID, songID, upvotes, downvotes, createdAt) <>
      (PartyQueueEntry.tupled, PartyQueueEntry.unapply)

  // scalastyle:on method.name
  def partyQueuePartyIDFk: ForeignKeyQuery[PartyTable, Party] = foreignKey(
    "party_queue_party_id_fk", partyID, TableQuery[PartyTable])(_.id)
  def partyQueueSongIDFk: ForeignKeyQuery[SongTable, Song] = foreignKey(
    "party_queue_party_id_fk", songID, TableQuery[SongTable])(_.id)
}