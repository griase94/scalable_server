package de.scalable.database.queries


import java.time.LocalDateTime

import de.scalable.model.{PartyQueueEntry, Song, SongReturn}
import org.slf4j.{Logger, LoggerFactory}
import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._

import scala.util.{Failure, Success}
import scala.concurrent.Future

object PartyQueueQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueueQueries")

  private def insertEntryQuery(entry: PartyQueueEntry) =
    (partyQueueQuery returning partyQueueQuery) += entry



  def getEntriesForPartyQuery(partyID: Long) = {
    for {
      entries <- partyQueueQuery.filter(_.id === partyID).result
      songs <- songQuery.filter(_.id inSet entries.map(_.id)).result
    } yield (entries, songs)
  }

  def getEntriesForParty(partyID: Long): Future[Seq[SongReturn]] ={
    db.run(getEntriesForPartyQuery(partyID).asTry) flatMap {
      case Success((entries, songs)) => {
        val entryMap = entries.map(e => (e.songID, e)).toMap
        //TODO geht das so!?
        val result = songs.map(s =>{
          val entry = entryMap.get(s.id)
          entry match {
            case Some(e) => s.toReturn(e.upvotes,e.downvotes,e.played)
          }
        })
        Future.successful(result)
      }
      case Failure(exception) => {
        exception.printStackTrace()
        Future.failed(new Exception(s"Get entries for party failed ${exception.getMessage}"))
      }
    }
  }

  def addSongToQueue(songID: Long, partyID: Long): Future[PartyQueueEntry] = {
    val now = LocalDateTime.now
    val newEntry = PartyQueueEntry(0L,partyID,songID,0,0,false,now)
    db.run(insertEntryQuery(newEntry))
  }

  //Incrementing not supported by slick https://github.com/slick/slick/issues/497
  def upvoteSongForParty(songID:Long, partyID:Long):Future[Int] = {
    db.run(sqlu"UPDATE scalable.party_queue SET upvotes = upvotes + 1 WHERE party_id = $partyID AND song_id = $songID;")
  }

  def downvoteSongForParty(songID:Long, partyID:Long):Future[Int] = {
    db.run(sqlu"UPDATE scalable.party_queue SET upvotes = upvotes + 1 WHERE party_id = $partyID AND song_id = $songID;")
  }

}

