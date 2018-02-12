package de.scalable.database.queries

import de.scalable.database.ScalableDB
import de.scalable.model.{Party, Song}
import org.slf4j.{Logger, LoggerFactory}
import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._

import scala.concurrent.Future



object PartyQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueries")

  private def insertPartyQuery(party: Party) =
    (partyQuery returning partyQuery) += party

  def insertParty(party: Party): Future[Party] ={
    db.run(insertPartyQuery(party))
  }

  def getPartyFromKey(partyKey: String):Future[Option[Party]] = {
    val query = partyQuery.filter(_.key === partyKey)
    db.run(query.result.headOption)
  }

}
