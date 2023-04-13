package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "amateur_soccer_group")
class AmateurSoccerGroup(
    @Id
    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    var amateurSoccerGroupId: UUID? = null,

    @Column(nullable = false, insertable = true, updatable = true)
    var name: String,
) {
    @Embeddable
    class AmateurSoccerGroupId(
        var value: UUID,
    )
}
