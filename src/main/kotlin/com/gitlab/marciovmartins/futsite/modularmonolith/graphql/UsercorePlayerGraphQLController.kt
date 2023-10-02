package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.PlayerRepository
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class UsercorePlayerGraphQLController(
    private val playerRepository: PlayerRepository,
) {
    @SchemaMapping(typeName = "Player", field = "userData")
    fun playerUserData(playerGraphQL: PlayerGraphQLController.PlayerGraphQL): PlayerUserDataGraphQL? {
        return playerRepository.findById(playerGraphQL.id)
            .map { PlayerUserDataGraphQL(playerId = it.playerId, name = it.name) }
            .orElse(null)
    }

    @SchemaMapping(typeName = "PlayerStatistic", field = "userData")
    fun playerStatisticUserData(playerStatisticGraphQL: CalculateRankingGraphQLController.PlayerStatisticGraphQL): PlayerUserDataGraphQL? {
        return playerRepository.findById(playerStatisticGraphQL.playerId)
            .map { PlayerUserDataGraphQL(playerId = it.playerId, name = it.name) }
            .orElse(null)
    }

    data class PlayerUserDataGraphQL(
        val playerId: UUID,
        val name: String,
    )
}