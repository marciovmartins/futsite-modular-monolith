# 9. Architecture Design

Date: 2023-01-15

## Status

Accepted

## Context

Define the architectural designs for the application. Needed to facilitate the new developers onboarding. Decrease the
mental load.

## Decision

We will use the onion architecture for complex parts of the software with 3 layers (domain, application and
infrastructure packages).

The domain can only contain business logic and should not contain infrastructure information (e.g: Spring Boot
annotations)

The usecase/application should be the domain orchestration, calling classes and interfaces from the domain and mapping
information from the infrastructure to the domain and mapping back from the domain to the DTOs or primitive classes.

E.g.:

- ..modularmonolith.players.domain.Player
- ..modularmonolith.players.domain.PlayerToCreate
- ..modularmonolith.players.domain.PlayerRepository
- ..modularmonolith.players.application.CreatePlayer
- ..modularmonolith.players.application.PlayerMapper
- ..modularmonolith.players.infrastructure.PlayerSpringDataRepository
- ..modularmonolith.players.infrastructure.PlayerController

We will use two layers architecture for not complex parts of the software but with the presentation data different from
the domain structure (domain and infrastructure packages).

E.g.:

- ..modularmonolith.players.domain.Player
- ..modularmonolith.players.domain.PlayerRepository
- ..modularmonolith.players.infrastructure.PlayerSpringDataRepository
- ..modularmonolith.players.infrastructure.PlayerController

We will use one layer architecture for CRUDs where the domain structure is the same of the presented data.

E.g.:

- ..modularmonolith.players.PlayerEntity
- ..modularmonolith.players.PlayerSpringRestRepository

It is allowed to have subpackages inside each layer. Pay attention to do not introduce unnecessary complexity.

## Consequences

Defined way to build the application. Shared way between different, old and new developers.
