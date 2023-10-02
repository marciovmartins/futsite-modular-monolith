package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.GamedayRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.Instant
import java.util.Optional
import java.util.UUID
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.AmateurSoccerGroup as AmateurSoccerGroupUserData
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.AmateurSoccerGroupRepository as UsercoreAmateurSoccerGroupRepository

@Controller
class AmateurSoccerGroupGraphqlController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
    private val usercoreAmateurSoccerGroupRepository: UsercoreAmateurSoccerGroupRepository,
    private val gamedayRepository: GamedayRepository,
) {
    @QueryMapping
    fun allAmateurSoccerGroups(): AllAmateurSoccerGroupsGraphQL<List<AmateurSoccerGroupGraphQL>> {
        val allAmateurSoccerGroupModel = amateurSoccerGroupRepository.findAll()
        return AllAmateurSoccerGroupsGraphQL(
            data = allAmateurSoccerGroupModel.map { toGraphQL(it) },
            actions = listOf(
                "self",
                "create",
                "create-user-data",
            )
        )
    }

    @QueryMapping
    fun amateurSoccerGroupById(@Argument id: UUID): AmateurSoccerGroupGraphQL? {
        return amateurSoccerGroupRepository.findById(id)
            .map { toGraphQL(it) }
            .orElse(null)
    }

    @QueryMapping
    fun gamedays(
        @Argument amateurSoccerGroupId: UUID,
        @Argument page: Optional<Int>,
        @Argument size: Optional<Int>,
        @Argument sort: Optional<String>,
        @Argument direction: Optional<Sort.Direction>,
    ): GamedaysGraphQL {
        val pageable = PageRequest.of(
            page.orElse(0),
            size.orElse(20),
            direction.orElse(Sort.DEFAULT_DIRECTION),
            sort.orElse("")
        )
        val allGamedaysModel = gamedayRepository.findByAmateurSoccerGroupId(amateurSoccerGroupId, pageable)
        return GamedaysGraphQL(
            data = allGamedaysModel.content.map { toGraphQL(it) },
            actions = listOfNotNull(
                "self",
                "create-gameday",
                "get-amateur-soccer-group",
                if (!allGamedaysModel.isFirst) "first"
                else null,
                if (!allGamedaysModel.isLast) "last"
                else null,
                if (allGamedaysModel.hasPrevious()) "prev"
                else null,
                if (allGamedaysModel.hasNext()) "next"
                else null,
            ),
            page = allGamedaysModel.let {
                PageGraphQL(it.size.toLong(), it.totalElements, it.totalPages.toLong(), it.number.toLong())
            }
        )
    }

    @MutationMapping
    fun createAmateurSoccerGroup(
        @Argument id: UUID,
        @Argument name: String,
    ): AmateurSoccerGroupGraphQL {
        val amateurSoccerGroupToCreate = AmateurSoccerGroup().apply {
            this.amateurSoccerGroupId = id
        }
        amateurSoccerGroupRepository.save(amateurSoccerGroupToCreate)

        val amateurSoccerGroupUserDataToCreate = AmateurSoccerGroupUserData().apply {
            this.amateurSoccerGroupId = id
            this.name = name
        }
        usercoreAmateurSoccerGroupRepository.save(amateurSoccerGroupUserDataToCreate)

        return AmateurSoccerGroupGraphQL(
            id = id,
            actions = listOf("self")
        )
    }

    private fun toGraphQL(amateurSoccerGroup: AmateurSoccerGroup): AmateurSoccerGroupGraphQL {
        return AmateurSoccerGroupGraphQL(
            id = amateurSoccerGroup.amateurSoccerGroupId!!,
            actions = listOf(
                "self",
                "get-amateur-soccer-groups",
                "get-user-data",
                "get-gamedays",
                "create-gameday",
                "calculate-ranking",
                "get-players",
                "create-player",
            )
        )
    }

    private fun toGraphQL(gameday: Gameday): GamedayGraphQL {
        return GamedayGraphQL(
            id = gameday.gamedayId!!,
            amateurSoccerGroupId = gameday.amateurSoccerGroupId!!,
            date = gameday.date,
            matches = gameday.matches.map { toGraphQL(it) },
            actions = listOf("self")
        )
    }

    private fun toGraphQL(gamedayMatch: Gameday.Match): GamedayMatchGraphQL {
        return GamedayMatchGraphQL(
            players = gamedayMatch.players.map { toGraphQL(it) }
        )
    }

    private fun toGraphQL(gamedayMatchPlayer: Gameday.Match.PlayerStatistic): GamedayMatchPlayerStatisticGraphQL {
        return GamedayMatchPlayerStatisticGraphQL(
            playerId = gamedayMatchPlayer.playerId,
            team = gamedayMatchPlayer.team.name,
            goalsInFavor = gamedayMatchPlayer.goalsInFavor.toInt(),
            ownGoals = gamedayMatchPlayer.ownGoals.toInt(),
            yellowCards = gamedayMatchPlayer.yellowCards.toInt(),
            blueCards = gamedayMatchPlayer.blueCards.toInt(),
            redCards = gamedayMatchPlayer.redCards.toInt(),
        )
    }

    data class AllAmateurSoccerGroupsGraphQL<T>(
        val data: T,
        val actions: List<String>,
    )

    data class AmateurSoccerGroupGraphQL(
        val id: UUID,
        val actions: List<String>,
    )

    data class GamedaysGraphQL(
        val data: List<GamedayGraphQL>,
        val actions: List<String>,
        val page: PageGraphQL,
    )

    data class GamedayGraphQL(
        val id: UUID,
        val amateurSoccerGroupId: UUID,
        val date: Instant,
        val matches: List<GamedayMatchGraphQL>,
        val actions: List<String>,
    )

    data class GamedayMatchGraphQL(
        val players: List<GamedayMatchPlayerStatisticGraphQL>,
    )

    data class GamedayMatchPlayerStatisticGraphQL(
        val playerId: UUID,
        val team: String,
        val goalsInFavor: Int,
        val ownGoals: Int,
        val yellowCards: Int,
        val blueCards: Int,
        val redCards: Int,
    )

    data class PageGraphQL(
        val size: Long,
        val totalElements: Long,
        val totalPages: Long,
        val number: Long,
    )

}