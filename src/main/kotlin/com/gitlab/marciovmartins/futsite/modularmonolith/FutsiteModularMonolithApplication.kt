package com.gitlab.marciovmartins.futsite.modularmonolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(considerNestedRepositories = true)
class FutsiteModularMonolithApplication

fun main(args: Array<String>) {
    runApplication<FutsiteModularMonolithApplication>(*args)
}
