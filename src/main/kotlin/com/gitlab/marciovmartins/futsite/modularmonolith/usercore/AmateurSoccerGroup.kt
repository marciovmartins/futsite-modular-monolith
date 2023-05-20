package com.gitlab.marciovmartins.futsite.modularmonolith.usercore

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "user_core_amateur_soccer_group")
class AmateurSoccerGroup {
    @Id
    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    lateinit var amateurSoccerGroupId: UUID

    @Column(nullable = false, insertable = true, updatable = true)
    lateinit var name: String
}
