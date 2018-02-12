package de.scalable.database.queries

import de.scalable.database.ScalableDB
import de.scalable.model.Song
import org.slf4j.{Logger, LoggerFactory}

object PartyQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueries")

  protected def getSongIDsForKey(partyKey: String) ={
    partyQuery.filter(_.key == partyKey)
  }

  def insertSong(song: Song) = {

  }
}
