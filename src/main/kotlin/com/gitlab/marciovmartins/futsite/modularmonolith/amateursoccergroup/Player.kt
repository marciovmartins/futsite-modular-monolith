package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "amateur_soccer_group_players")
class Player {
    @Id
    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    lateinit var playerId: UUID

    @JsonIgnore
    @Column(nullable = false, insertable = true, updatable = false)
    lateinit var amateurSoccerGroupId: UUID
}