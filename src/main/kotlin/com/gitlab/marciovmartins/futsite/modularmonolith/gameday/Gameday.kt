package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.Player.PlayerId
import jakarta.persistence.AttributeOverride
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import org.hibernate.annotations.FetchMode
import java.time.Instant
import java.util.UUID

/**
 * It is an aggregate that registers the information about matches and the players statistics
 */
@Entity(name = "gamedays")
class Gameday(
    @Id
    @JsonIgnore
    @Column(name = "gameday_id", unique = true, nullable = false, insertable = true, updatable = false)
    protected var id: UUID? = null,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "gameday_id", insertable = false, updatable = false))
    var gamedayId: GamedayId,

    @Embedded
    @JsonIgnore
    @AttributeOverride(
        name = "value",
        column = Column(name = "amateur_soccer_group_id", insertable = true, updatable = false)
    )
    var amateurSoccerGroupId: AmateurSoccerGroupId,

    @Column(name = "gameday_date", insertable = true, updatable = false)
    var date: Instant,

    @JoinColumn(name = "gameday_id", nullable = false)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(FetchMode.SELECT)
    var matches: List<Match>,
) {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    fun setGamedayId(value: UUID) {
        this.id = value
    }

    @JsonProperty("amateurSoccerGroupId")
    fun getAmateurSoccerGroupId(): UUID = amateurSoccerGroupId.value

    @Embeddable
    class GamedayId(
        var value: UUID,
    )

    @Entity(name = "gamedays_matches")
    data class Match(
        @Id
        @JsonIgnore
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var matchId: Long? = null,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinColumn(name = "match_id", nullable = false)
        @org.hibernate.annotations.Fetch(FetchMode.SELECT)
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

            @Embedded
            @JsonIgnore
            @AttributeOverride(name = "value", column = Column(name = "player_id"))
            var playerId: PlayerId,

            @Enumerated(EnumType.STRING)
            var team: Team,

            var goalsInFavor: UByte,

            var ownGoals: UByte,

            var yellowCards: UByte,

            var blueCards: UByte,

            var redCards: UByte,
        ) {
            @JsonProperty("playerId")
            fun getPlayerId(): UUID = playerId.value
        }

        enum class Team {
            A, B
        }
    }
}
