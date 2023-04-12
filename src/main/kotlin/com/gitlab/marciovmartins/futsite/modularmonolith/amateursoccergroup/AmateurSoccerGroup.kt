package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import jakarta.persistence.Embeddable
import java.util.UUID

class AmateurSoccerGroup {
    @Embeddable
    class AmateurSoccerGroupId(
        var value: UUID,
    )
}
