package com.gitlab.marciovmartins.futsite.modularmonolith.shared.common.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.FutsiteModularMonolithApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = [FutsiteModularMonolithApplication::class],
    considerNestedRepositories = true
)
class JpaConfiguration