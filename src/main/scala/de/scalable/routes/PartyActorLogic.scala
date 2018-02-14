

package de.scalable.routes


import java.time.LocalDateTime

import de.scalable.database.queries.{PartyQueries, PhotoFeedQueries, SongQueries}
import de.scalable.model.{Party, PartyVote, Song, SongToAdd}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future
import scala.util.Random

object PartyActorLogic {

  val partyPasswords = Seq("lion", "giraffe", "wolf", "caterpillar", "elephant", "snake", "tiger")
  val randomNumber = new Random

  private val log: Logger = LoggerFactory.getLogger("ScalableActorLogic")

  def echo(echo: String) = {
    Future.successful(echo)
  }

  def addSong(song: SongToAdd, partyKey:String) = {
    SongQueries.insertSong(song,partyKey)
  }

  def createParty(name:String) ={
    val now = LocalDateTime.now()
    val key = generateKey(6).toUpperCase
    val password = partyPasswords(randomNumber.nextInt(partyPasswords.length))
    println(key + " - " + password)
    val party = Party(key,name,password,now)

    PartyQueries.insertParty(party)
  }

  def getSongsForParty(partyID:String) = {
    PhotoFeedQueries.getSongsForParty(partyID)
  }

  def voteForSong(vote:PartyVote) = {
    vote match {
      case PartyVote(key,songID,true)   => PhotoFeedQueries.upvoteSongForParty(songID,key)
      case PartyVote(key,songID,false)  => PhotoFeedQueries.downvoteSongForParty(songID,key)
    }
  }
  def setSongPlayed(songID:Long, partyKey:String) = {
    PhotoFeedQueries.setSongPlayed(songID, partyKey)
  }

  private def generateKey(length: Int): String = {
    val randomAlphanumeric = Random.alphanumeric
    randomAlphanumeric take length mkString
  }



}

