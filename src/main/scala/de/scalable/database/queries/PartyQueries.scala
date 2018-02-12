package de.scalable.database.queries

import de.scalable.model.{Party}
import org.slf4j.{Logger, LoggerFactory}
import de.scalable.database.ScalableDB
import de.scalable.database.ScalablePostgresProfile.api._


import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future



object PartyQueries extends ScalableDB {

  val log: Logger = LoggerFactory.getLogger("PartyQueries")

  private def insertPartyQuery(party: Party) =
    (partyQuery returning partyQuery.map(_.id) into ((party,id) => party.copy(id=id))) += party


  def insertParty(party: Party): Future[Party] ={
    db.run(insertPartyQuery(party))
  }


}
