

package de.scalable.database.tables

import java.time.LocalDateTime
import java.util.UUID

import de.scalable.database.ScalablePostgresProfile.api._
import de.scalable.model._
import slick.lifted.ProvenShape
import slick.sql.SqlProfile.ColumnOption.NotNull

class SongTable(tag: Tag)
  extends BaseTableLong[Song](tag, "song") {

  def streamingServiceID: Rep[String] = column[String]("streaming_service_id", NotNull)
  def name: Rep[String] = column[String]("name", NotNull)
  def artist: Rep[String] = column[String]("artist", NotNull)
  def album: Rep[String] = column[String]("album", NotNull)
  def albumCoverUrl: Rep[String] = column[String]("album_cover_url")

  // scalastyle:off method.name
  def * : ProvenShape[Song] =
    (id, streamingServiceID, name, artist, album, albumCoverUrl) <>
      (Song.tupled, Song.unapply)

  // scalastyle:on method.name
}