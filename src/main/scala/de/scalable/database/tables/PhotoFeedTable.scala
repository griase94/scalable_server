

package de.scalable.database.tables

import de.scalable.database.ScalablePostgresProfile.api._
import de.scalable.model._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import slick.sql.SqlProfile.ColumnOption.NotNull

class PhotoFeedTable(tag: Tag)
  extends BaseTableLong[PhotoFeedEntry](tag, "photo_feed") with VotableTable[PhotoFeedEntry] {

  def partyID: Rep[String] = column[String]("party_id", NotNull)
  def photoID: Rep[Long] = column[Long]("photo_id", NotNull)
  def url: Rep[String] = column[String]("url", NotNull)


  // scalastyle:off method.name
  def * : ProvenShape[PhotoFeedEntry] =
    (id,partyID, photoID , upvotes, downvotes) <>
      (PhotoFeedEntry.tupled, PhotoFeedEntry.unapply)
}