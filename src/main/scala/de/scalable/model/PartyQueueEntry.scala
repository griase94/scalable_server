package de.scalable.model

import java.time.LocalDateTime

case class PartyQueueEntry(id: Long,
                           partyKey: String,
                           songID: Long,
                           upvotes:Int,
                           downvotes:Int,
                           played:Boolean)