package de.scalable.model

case class Vote(partyID: String,
                songID: Long,
                positive: Boolean,
                voteType: String)


object VoteTypes {
  val SONG = "SONG"
  val PHOTO = "PHOTO"
}