package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.Player
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.PlayerRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class PlayerGraphQLController(
    private val playerRepository: PlayerRepository,
) {
    @QueryMapping
    fun playersByAmateurSoccerGroupId(@Argument amateurSoccerGroupId: UUID): PlayersGraphQL {
        val allPlayerModel = playerRepository.findByAmateurSoccerGroupId(amateurSoccerGroupId)
        return PlayersGraphQL(
            data = allPlayerModel.map { toGraphQL(it) },
            actions = listOf("self", "create")
        )
    }

    private fun toGraphQL(playerEntityModel: Player): PlayerGraphQL {
        return PlayerGraphQL(
            id = playerEntityModel.playerId,
            actions = listOf(
                "get-amateur-soccer-group",
                "set-player-user-data",
                "get-player-user-data",
            ),
        )
    }

    data class PlayersGraphQL(
        val data: List<PlayerGraphQL>,
        val actions: List<String>,
    )

    data class PlayerGraphQL(
        val id: UUID,
        val actions: List<String>,
    )
}