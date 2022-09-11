package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.usecase.GetHelloWorld
import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.usecase.HelloWorldDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController(
    private val getHelloWorld: GetHelloWorld
) {
    @GetMapping("/api/v1/helloworld")
    fun helloWorldName(@RequestParam(value = "name") name: String?): HelloWorldDTO {
        return getHelloWorld.with(name)
    }
}