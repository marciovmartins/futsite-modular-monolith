package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.AmateurSoccerGroupRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.PlayerRepository
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class UsercoreAmateurSoccerGroupGraphqlController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
    private val playerRepository: PlayerRepository,
) {
    @SchemaMapping(typeName = "AmateurSoccerGroup", field = "userData")
    fun amateurSoccerGroupUserData(
        amateurSoccerGroupGraphQL: AmateurSoccerGroupGraphqlController.AmateurSoccerGroupGraphQL
    ): AmateurSoccerGroupUserDataGraphQL? {
        return amateurSoccerGroupRepository.findById(amateurSoccerGroupGraphQL.id)
            .map { toGraphQL(it) }
            .orElse(null)
    }

    @SchemaMapping(typeName = "GamedayMatchPlayerStatistic", field = "userData")
    fun gamedayMatchPlayerStatisticUserData(
        gamedayMatchPlayerStatisticGraphQL: AmateurSoccerGroupGraphqlController.GamedayMatchPlayerStatisticGraphQL,
    ): GamedayMatchPlayerStatisticUserData? {
        return playerRepository.findById(gamedayMatchPlayerStatisticGraphQL.playerId)
            .map { GamedayMatchPlayerStatisticUserData(playerId = it.playerId, name = it.name) }
            .orElse(null)
    }

    private fun toGraphQL(amateurSoccerGroup: AmateurSoccerGroup): AmateurSoccerGroupUserDataGraphQL {
        return AmateurSoccerGroupUserDataGraphQL(
            id = amateurSoccerGroup.amateurSoccerGroupId,
            name = amateurSoccerGroup.name,
        )
    }

    data class AmateurSoccerGroupUserDataGraphQL(
        val id: UUID,
        val name: String,
    )

    data class GamedayMatchPlayerStatisticUserData(
        val playerId: UUID,
        val name: String,
    )
}