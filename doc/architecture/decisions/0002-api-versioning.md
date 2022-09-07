# 2. Api Versioning

Date: 2022-09-05

## Status

Accepted

## Context

An api versioning is necessary to avoid break the api contract after changes in the api resource payload as adding,
changing or removing properties or query parameters.

## Decision

Each resource SHOULD have an incremental major version for any change in the payload or query parameters.

The base resource path should be `/api/{version}/{path}`.

Examples:

`POST /api/v1/ranking/calculate`

`GET /api/v3/amateurSoccerGroup/807d94b1-dd68-482b-901d-4ff528f453d2`

`POST /api/v1/users?amateurSoccerGroup=30603c23-9b7b-4c45-a7be-a6fc62dbe3a4`

`POST /api/v2/users?amateurSoccerGroupId=30603c23-9b7b-4c45-a7be-a6fc62dbe3a4`

## Consequences

It will avoid breaking changes to the api. This introduces some complexity maintaining the apis, it is necessary more
coordination with the client components. Keep extra attention to deprecated resources and schedule it for removal.