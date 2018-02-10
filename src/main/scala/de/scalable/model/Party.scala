package de.scalable.model

import java.time.LocalDateTime

case class Party(id: Long,
                  key: String,
                  name: String,
                  createdAt: LocalDateTime)
