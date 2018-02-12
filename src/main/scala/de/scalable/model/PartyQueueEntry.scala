package de.scalable.model

import java.time.LocalDateTime

case class PartyQueueEntry(id: Long,
                           partyID: Long,
                           songID: Long,
                           upvotes:Int,
                           downvotes:Int,
                           played:Boolean,
                           createdAt: LocalDateTime)
