

package de.scalable.model

import java.time.LocalDateTime
import java.util.UUID

case class Song(id: Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String){

  def toReturn(upvotes:Int, downvotes: Int, played: Boolean): SongReturn =
    SongReturn(id, streamingServiceID, name, artist, album, albumCoverUrl, upvotes, downvotes, played)
}

case class SongToAdd(streamingServiceID: String,
                     name: String,
                     artist: String,
                     album: String,
                     albumCoverUrl: String){
  def toSong(): Song =
    Song(0L, streamingServiceID, name, artist, album, albumCoverUrl)
}


case class SongReturn(id:Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String,
                upvotes: Int,
                downvotes: Int,
                played: Boolean){

  def voteDiff(): Int = upvotes - downvotes
}

