

package de.scalable.database.queries


import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._
import de.scalable.model._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future


object SongQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("SongQueries")

  private def insertSongQuery(song: Song) =
    (songQuery returning songQuery) += song

  def insertSong(song: Song): Future[Song] = {
    db.run(insertSongQuery(song))
  }

  def getSongsForParty(key:String): Unit ={

  }
}
