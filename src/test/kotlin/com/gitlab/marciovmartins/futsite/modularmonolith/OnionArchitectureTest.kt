package com.gitlab.marciovmartins.futsite.modularmonolith

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@Suppress("unused")
@AnalyzeClasses(packages = ["com.gitlab.marciovmartins.futsite.modularmonolith"])
class OnionArchitectureTest {
    @ArchTest
    val domainRules: ArchRule = ArchRuleDefinition.classes()
        .that().resideInAPackage("..domain..")
        .should().accessClassesThat().areInnerClasses()
        .orShould().accessClassesThat().resideInAnyPackage("..domain..", "java..")

    @ArchTest
    val usecaseRules: ArchRule = ArchRuleDefinition.classes()
        .that().resideInAPackage("..application..")
        .should().accessClassesThat().areInnerClasses()
        .orShould().accessClassesThat().resideInAnyPackage("..application..", "..domain..", "java..")
}