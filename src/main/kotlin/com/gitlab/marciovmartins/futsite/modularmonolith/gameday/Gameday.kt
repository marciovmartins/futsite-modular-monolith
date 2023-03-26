package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure.PastOrPresent
import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure.Property
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.rest.core.annotation.RestResource
import java.time.Instant
import java.util.UUID

/**
 * It is an aggregate that registers the information about matches and the players statistics
 */
@Entity(name = "gamedays")
@RestResource(path = "gameDays")
class Gameday(
    @Id
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    var gamedayId: UUID? = null,

    @NotNull
    @Column(insertable = true, updatable = false)
    var amateurSoccerGroupId: UUID,

    @NotNull
    @PastOrPresent(
        from = "2020-01-01T00:00:00Z",
        title = "Invalid Date",
        message = "The game day date should not be before {from}",
        properties = [
            Property("minimumAllowedDate", "2020-01-01T00:00:00Z")
        ]
    )
    @Column(name = "gameday_date", insertable = true, updatable = false)
    var date: Instant,

    @NotEmpty
    @JoinColumn(name = "gameday_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var matches: List<Match>,
) {
    @Entity(name = "gamedays_matches")
    data class Match(
        @Id
        @JsonIgnore
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val matchId: Long,

        @NotEmpty
        @Size(min = 2, max = 44)
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "match_id", nullable = false)
        val players: Set<PlayerStatistic>,
    ) {
        /**
         * Individual player statistic for a match
         */
        @Entity(name = "gamedays_player_statistic")
        data class PlayerStatistic(
            @Id
            @JsonIgnore
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            val playerStatisticId: Long,

            val playerId: UUID,

            @Enumerated(EnumType.STRING)
            val team: Team,

            val goalsInFavor: UByte,

            val goalsAgainst: UByte,

            val yellowCards: UByte,

            val blueCards: UByte,

            val redCards: UByte,
        )

        enum class Team {
            A, B
        }
    }
}
