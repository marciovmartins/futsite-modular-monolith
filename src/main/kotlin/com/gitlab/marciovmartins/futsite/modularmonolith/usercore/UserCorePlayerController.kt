package com.gitlab.marciovmartins.futsite.modularmonolith.usercore

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController(value = "userCorePlayerController")
@RequestMapping(value = ["/api/user-core/players"])
class UserCorePlayerController(
    private val playerRepository: PlayerRepository,
) {
    @PostMapping
    fun create(
        @RequestBody player: Player?,
    ): ResponseEntity<*> {
        if (player == null) return ResponseEntity.badRequest().body("Unable to create")

        upsertPlayer(player, player.playerId)

        val location = linkTo(methodOn(UserCorePlayerController::class.java).show(player.playerId)!!)
            .withSelfRel()
            .toUri()

        return ResponseEntity.ok(playerModel(player))
    }

    @PutMapping("/{playerId}")
    fun upsert(
        @PathVariable playerId: UUID,
        @RequestBody player: Player?,
    ): ResponseEntity<*> {
        if (player == null) return ResponseEntity.badRequest().body("Unable to create")
        upsertPlayer(player, playerId)
        return ResponseEntity.ok(playerModel(player))
    }

    @GetMapping("/{playerId}")
    fun show(
        @PathVariable playerId: UUID,
    ): EntityModel<Player>? {
        return playerRepository.findById(playerId)
            .map { playerModel(it) }
            .orElse(null)
    }

    @GetMapping
    fun getByAmateurSoccerGroupId(
        @RequestParam(required = true) amateurSoccerGroupId: UUID,
    ): CollectionModel<*> {
        val playersModel = playerRepository.findByAmateurSoccerGroupId(amateurSoccerGroupId)
            .map { playerModel(it) }

        return CollectionModel.of(
            playersModel,
            linkTo(methodOn(UserCorePlayerController::class.java).getByAmateurSoccerGroupId(amateurSoccerGroupId))
                .withRel("getByAmateurSoccerGroupId"),
        );
    }

    private fun upsertPlayer(player: Player, playerId: UUID) {
        player.playerId = playerId
        playerRepository.save(player)
    }

    private fun playerModel(player: Player): EntityModel<Player> {
        return EntityModel.of(
            player,
            linkTo(methodOn(UserCorePlayerController::class.java).upsert(player.playerId, player))
                .withRel("set-player"),
            linkTo(methodOn(UserCorePlayerController::class.java).show(player.playerId)!!)
                .withSelfRel(),
        )
    }
}