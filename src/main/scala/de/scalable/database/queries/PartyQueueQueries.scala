package de.scalable.database.queries


import java.time.LocalDateTime

import de.scalable.model.{PartyQueueEntry, Song}
import org.slf4j.{Logger, LoggerFactory}
import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._

import scala.concurrent.Future

object PartyQueueQueries extends ScalableDB {

  private def insertSongQuery(entry: PartyQueueEntry) =
    (partyQueueQuery returning partyQueueQuery) += entry

  val log: Logger = LoggerFactory.getLogger("PartyQueueQueries")

  def getEntriesForParty(partyID: Long): Future[Seq[PartyQueueEntry]] = {
    val query = partyQueueQuery.filter(_.id === partyID)
    db.run(query.result)
  }

  def addSongToQueue(songID: Long, partyID: Long): Future[PartyQueueEntry] = {
    val now = LocalDateTime.now
    val newEntry = PartyQueueEntry(0L,partyID,songID,0,0,now)
    db.run(insertSongQuery(newEntry))
  }

}

