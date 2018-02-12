

package de.scalable.model

import java.time.LocalDateTime
import java.util.UUID

case class Song(id: Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String,
                createdAt: LocalDateTime){

  def toReturn(upvotes:Int, downvotes: Int, played: Boolean): SongReturn =
    SongReturn(id, streamingServiceID, name, artist, album, albumCoverUrl, upvotes, downvotes, played, createdAt)
}

case class SongReturn(id:Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String,
                upvotes: Int,
                downvotes: Int,
                played: Boolean,
                createdAt: LocalDateTime)

