package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.domain.AmateurSoccerGroup.AmateurSoccerGroupId
import java.time.Instant
import java.util.UUID

class Gameday(
    val id: GamedayId,
    val amateurSoccerGroupId: AmateurSoccerGroupId,
    val date: Date,
    val matches: List<Match>,
) {
    init {
        if (matches.isEmpty()) throw EmptyMatchesException()
    }

    data class GamedayId(val value: UUID)

    data class Date(val value: Instant) {
        init {
            if (value.isBefore(Instant.parse("2020-01-01T00:00:00Z"))) throw InvalidDateException(value)
            if (value.isAfter(Instant.now())) throw InvalidDateException(value)
        }

        class InvalidDateException(value: Instant) : InvalidGamedayException("date", value)
    }

    data class Match(
        val players: Set<Player>,
    ) {
        data class Player(
            val playerId: PlayerId,
            val team: Team,
            val goalsInFavor: GoalsInFavor,
            val goalsAgainst: GoalsAgainst,
            val yellowCards: YellowCards,
            val blueCards: BlueCards,
            val redCards: RedCards,
        ) {
            data class PlayerId(val value: UUID)
            data class GoalsInFavor(val value: UByte)
            data class GoalsAgainst(val value: UByte)
            data class YellowCards(val value: UByte)
            data class BlueCards(val value: UByte)
            data class RedCards(val value: UByte)
        }

        enum class Team {
            A, B
        }
    }

    abstract class InvalidGamedayException(
        val propertyName: String,
        val propertyValue: Any? = null,
    ) : Exception()

    class EmptyMatchesException : InvalidGamedayException("matches")
}
