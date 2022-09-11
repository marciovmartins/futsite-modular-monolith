package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.usecase

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.domain.HelloWorld

object HelloWorldMapper {
    fun HelloWorld.toDTO() = HelloWorldDTO(
        message = "Hello world, ${name.value}!",
        name = name.value,
    )
}