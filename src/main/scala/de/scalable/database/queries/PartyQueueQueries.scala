package de.scalable.database.queries



import de.scalable.model.{PartyQueueEntry, Song, SongReturn}
import org.slf4j.{Logger, LoggerFactory}
import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._


import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}
import scala.concurrent.Future

object PartyQueueQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueueQueries")

  def insertQueueEntryQuery(entry: PartyQueueEntry) =
    (partyQueueQuery returning partyQueueQuery) += entry



  def getEntriesForPartyQuery(partyID: String) = {
    for {
      entries <- partyQueueQuery.filter(_.partyID === partyID).result
      songs <- songQuery.filter(_.id inSet entries.map(_.id)).result
    } yield (entries, songs)
  }

  def getSongsForParty(partyID: String): Future[Seq[SongReturn]] ={
    db.run(getEntriesForPartyQuery(partyID).asTry) flatMap {
      case Success((entries, songs)) => {
        val entryMap = entries.map(e => (e.songID, e)).toMap

        //TODO geht das so!?
        val result = songs.filter(x => entryMap.keySet.contains(x.id)).map(s =>{
          val e = entryMap.get(s.id).get
          s.toReturn(e.upvotes,e.downvotes,e.played)
        }).sortWith(_.voteDiff() > _.voteDiff())
        println(result.map(_.voteDiff()))
        Future.successful(result)
      }
      case Failure(exception) => {
        exception.printStackTrace()
        Future.failed(new Exception(s"Get entries for party failed ${exception.getMessage}"))
      }
    }
  }

  def setSongPlayed(songID: Long, partyID: String):Future[Int] = {
    val q = for { e <- partyQueueQuery if (e.songID === songID &&  e.partyID === partyID)} yield e.played
    db.run(q.update(true))
  }

  //Incrementing not supported by slick https://github.com/slick/slick/issues/497
  def upvoteSongForParty(songID:Long, partyID:String):Future[Int] = {
    db.run(sqlu"UPDATE scalable.party_queue SET upvotes = upvotes + 1 WHERE party_id = $partyID AND song_id = $songID;")
  }

  def downvoteSongForParty(songID:Long, partyID:String):Future[Int] = {
    db.run(sqlu"UPDATE scalable.party_queue SET downvotes = downvotes + 1 WHERE party_id = $partyID AND song_id = $songID;")
  }

}

