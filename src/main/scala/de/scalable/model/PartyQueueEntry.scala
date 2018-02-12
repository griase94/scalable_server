package de.scalable.model

import java.time.LocalDateTime

case class PartyQueueEntry(id: Long,
                           partyID: Long,
                           SongID: Long,
                           upvotes:Int,
                           downvotes:Int,
                           createdAt: LocalDateTime)
