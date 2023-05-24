package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupController
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.PlayerController
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.CalculateRanking
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.UserCorePlayerController
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(
    path = ["/api/v1/amateurSoccerGroups/{amateurSoccerGroupId}/ranking"],
)
class CalculateRankingController(
    private val calculateRanking: CalculateRanking,
) {
    @PostMapping
    fun calculateRanking(
        @PathVariable amateurSoccerGroupId: UUID,
        @RequestBody period: RankingDTO.Period,
    ): EntityModel<*> {
        val rankingDTO = calculateRanking.with(amateurSoccerGroupId, period)

        val links = mutableListOf(
            linkTo(methodOn(CalculateRankingController::class.java).calculateRanking(amateurSoccerGroupId, period))
                .withSelfRel(),
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroupId)!!)
                .withRel("get-amateur-soccer-group"),
        )
        links += rankingDTO.playerStatistics.map { it.playerId }.map {
            linkTo(methodOn(PlayerController::class.java).show(amateurSoccerGroupId, it)!!)
                .withRel("get-player-$it")
        }
        links += rankingDTO.playerStatistics.map { it.playerId }.map {
            linkTo(methodOn(UserCorePlayerController::class.java).show(it)!!)
                .withRel("get-player-user-data-$it")
        }

        return EntityModel.of(rankingDTO, links)
    }
}
