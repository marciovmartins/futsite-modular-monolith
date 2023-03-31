package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
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
import java.time.Instant
import java.util.UUID

/**
 * It is an aggregate that registers the information about matches and the players statistics
 */
@Entity(name = "gamedays")
class Gameday(
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    var gamedayId: UUID? = null,

    @Column(insertable = true, updatable = false)
    var amateurSoccerGroupId: UUID,

    @Column(name = "gameday_date", insertable = true, updatable = false)
    var date: Instant,

    @JoinColumn(name = "gameday_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var matches: List<Match>,
) {
    @Entity(name = "gamedays_matches")
    data class Match(
        @Id
        @JsonIgnore
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var matchId: Long? = null,

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "match_id", nullable = false)
        var players: Set<PlayerStatistic>,
    ) {
        /**
         * Individual player statistic for a match
         */
        @Entity(name = "gamedays_player_statistic")
        data class PlayerStatistic(
            @Id
            @JsonIgnore
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            var playerStatisticId: Long? = null,

            var playerId: UUID,

            @Enumerated(EnumType.STRING)
            var team: Team,

            var goalsInFavor: UByte,

            var goalsAgainst: UByte,

            var yellowCards: UByte,

            var blueCards: UByte,

            var redCards: UByte,
        )

        enum class Team {
            A, B
        }
    }
}
