package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import com.gitlab.marciovmartins.futsite.modularmonolith.usercore.UserCorePlayerController as UserCorePlayerController

@RestController
@RequestMapping(value = ["/api/amateurSoccerGroups/{amateurSoccerGroupId}/players"])
class PlayerController(
    private val playerRepository: PlayerRepository,
) {
    @PostMapping
    fun create(
        @PathVariable amateurSoccerGroupId: UUID,
        @RequestBody player: Player?,
    ): ResponseEntity<*> {
        if (player == null) return ResponseEntity.badRequest().body("Unable to create")

        player.amateurSoccerGroupId = amateurSoccerGroupId

        playerRepository.save(player)

        val location = linkTo(methodOn(PlayerController::class.java).show(amateurSoccerGroupId, player.playerId)!!)
            .withSelfRel()
            .toUri()

        return ResponseEntity.created(location).body(playerModel(player))
    }

    @GetMapping
    fun showAll(
        @PathVariable amateurSoccerGroupId: UUID,
    ): CollectionModel<EntityModel<*>> {
        val allPlayerModel = playerRepository.findByAmateurSoccerGroupId(amateurSoccerGroupId).map {
            playerModel(it)
        }

        return CollectionModel.of(
            allPlayerModel,
            linkTo(methodOn(PlayerController::class.java).showAll(amateurSoccerGroupId)).withSelfRel()
                .andAffordance(afford(methodOn(PlayerController::class.java).create(amateurSoccerGroupId, null)))
                .withRel("create"),
        )
    }

    @GetMapping("/{playerId}")
    fun show(
        @PathVariable amateurSoccerGroupId: UUID,
        @PathVariable playerId: UUID,
    ): EntityModel<Player>? {
        return playerRepository.findById(playerId)
            .map { playerModel(it) }
            .orElse(null)
    }

    private fun playerModel(player: Player): EntityModel<Player> {
        return EntityModel.of(
            player,
            linkTo(methodOn(PlayerController::class.java).show(player.amateurSoccerGroupId, player.playerId)!!)
                .withSelfRel(),
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(player.amateurSoccerGroupId)!!)
                .withRel("get-amateur-soccer-group"),
            linkTo(methodOn(UserCorePlayerController::class.java).upsert(player.playerId, null))
                .withRel("set-player-user-data"),
            linkTo(methodOn(UserCorePlayerController::class.java).show(player.playerId)!!)
                .withRel("get-player-user-data")
        )
    }
}