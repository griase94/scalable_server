

package de.scalable.routes


import de.scalable.model.Song
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

object PartyActorLogic {

  private val log: Logger = LoggerFactory.getLogger("ScalableActorLogic")

  def echo(echo: String) = {
    Future.successful(echo)
  }

  def addSong(song: Song, partyKey:String) = {

  }

}

