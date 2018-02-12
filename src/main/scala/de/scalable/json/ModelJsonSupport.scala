

package de.scalable.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.scalable.model._
import spray.json.RootJsonFormat

import scala.language.implicitConversions


trait ModelJsonSupport extends SprayJsonSupport with AdditionalJsonTypes {

  implicit val songFormat: RootJsonFormat[Song] = jsonFormat6(Song)
  implicit val songToCreateFormat: RootJsonFormat[SongToAdd] = jsonFormat5(SongToAdd)
  implicit val songReturnFormat: RootJsonFormat[SongReturn] = jsonFormat9(SongReturn)
  implicit val songPlayedFormat: RootJsonFormat[SongPlayed] = jsonFormat2(SongPlayed)
  implicit val partyFormat: RootJsonFormat[Party] = jsonFormat4(Party)
  implicit val PartyQueueEntryFormat: RootJsonFormat[PartyQueueEntry] = jsonFormat6(PartyQueueEntry)
  implicit val PartyVoteFormat: RootJsonFormat[PartyVote] = jsonFormat3(PartyVote)

}
