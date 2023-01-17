package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@Suppress("unused")
@AnalyzeClasses(packages = ["com.gitlab.marciovmartins.futsite.modularmonolith"])
class MyArchitectureTest {
    @ArchTest
    val domainRules: ArchRule = ArchRuleDefinition.classes()
        .that().resideInAPackage("..domain..")
        .should().accessClassesThat().resideInAPackage("..domain..")

    @ArchTest
    val usecaseRules: ArchRule = ArchRuleDefinition.classes()
        .that().resideInAPackage("..application..")
        .should().accessClassesThat().resideInAnyPackage("..application..", "..domain..")
}