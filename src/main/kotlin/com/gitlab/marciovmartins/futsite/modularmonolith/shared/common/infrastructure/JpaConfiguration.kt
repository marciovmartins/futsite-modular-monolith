package com.gitlab.marciovmartins.futsite.modularmonolith.shared.common.infrastructure

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(considerNestedRepositories = true)
class JpaConfiguration