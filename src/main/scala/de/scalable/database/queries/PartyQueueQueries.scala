package de.scalable.database.queries

import de.scalable.database.ScalableDB
import de.scalable.model.Song
import org.slf4j.{Logger, LoggerFactory}

object PartyQueueQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueueQueries")

  private def getSongIDsForKey(partyKey: String) ={
    partyQueueQuery
  }

  def insertSong(song: Song) = {

  }
}

