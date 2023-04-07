package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
class PlayerId(
    var value: UUID
)
