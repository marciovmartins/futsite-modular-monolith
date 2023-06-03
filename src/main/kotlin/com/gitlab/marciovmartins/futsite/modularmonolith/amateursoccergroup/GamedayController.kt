package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.UserCorePlayerController
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(
    path = ["/api/amateurSoccerGroups/{amateurSoccerGroupId}/gamedays"]
)
class GamedayController(
    private val gamedayRepository: GamedayRepository,
    private val pagedResourcesAssembler: PagedResourcesAssembler<Gameday>,
) {
    @GetMapping
    fun showAll(
        @PathVariable amateurSoccerGroupId: UUID,
        pageable: Pageable?,
    ): PagedModel<EntityModel<Gameday>> {
        val allGamedaysModel = gamedayRepository.findByAmateurSoccerGroupId(amateurSoccerGroupId, pageable)

        return pagedResourcesAssembler.toModel(allGamedaysModel) {
            EntityModel.of(
                it,
                linkTo(methodOn(GamedayController::class.java).show(amateurSoccerGroupId, it.gamedayId!!)!!)
                    .withSelfRel(),
            )
        }
            .add(
                linkTo(methodOn(GamedayController::class.java).showAll(amateurSoccerGroupId, pageable))
                    .withRel("create-gameday")
                    .andAffordance(afford(methodOn(GamedayController::class.java).create(amateurSoccerGroupId, null)))
            )
            .add(
                linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroupId)!!)
                    .withRel("get-amateur-soccer-group")
            ) as PagedModel<EntityModel<Gameday>>
    }

    @PostMapping
    fun create(
        @PathVariable amateurSoccerGroupId: UUID,
        @RequestBody gameday: Gameday?,
    ): ResponseEntity<*> {
        if (gameday == null) return ResponseEntity.badRequest().body("Unable to create")

        gameday.amateurSoccerGroupId = amateurSoccerGroupId

        gamedayRepository.save(gameday)

        val selfLink = linkTo(methodOn(GamedayController::class.java).show(amateurSoccerGroupId, gameday.gamedayId!!)!!)
            .withSelfRel()

        return ResponseEntity.created(selfLink.toUri()).body(
            EntityModel.of(
                gameday,
                selfLink,
                linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroupId)!!)
                    .withRel("get-amateur-soccer-group")
            )
        )
    }

    @GetMapping("/{gamedayId}")
    fun show(
        @PathVariable amateurSoccerGroupId: UUID,
        @PathVariable gamedayId: UUID,
    ): EntityModel<*>? {
        val gameday = gamedayRepository.findByAmateurSoccerGroupIdAndGamedayId(amateurSoccerGroupId, gamedayId)

        val links = mutableListOf(
            linkTo(methodOn(GamedayController::class.java).show(amateurSoccerGroupId, gamedayId)!!)
                .withSelfRel(),
            linkTo(methodOn(GamedayController::class.java).showAll(amateurSoccerGroupId, null))
                .withRel("get-gamedays"),
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroupId)!!)
                .withRel("get-amateur-soccer-group"),
        )

        gameday?.matches?.forEach { match ->
            match.players.forEach { playerStatistic ->
                val playerId = playerStatistic.playerId
                links.add(
                    linkTo(methodOn(PlayerController::class.java).show(amateurSoccerGroupId, playerId)!!)
                        .withRel("get-player-$playerId")
                )
                links.add(
                    linkTo(methodOn(UserCorePlayerController::class.java).show(playerId)!!)
                        .withRel("get-player-user-data-$playerId")
                )
            }
        }

        return gameday?.let { EntityModel.of(it, links) }
    }
}