# 7. Setup Identity and Access Mgmt for the Apis

Date: 2022-10-26

## Status

Accepted

## Context

For identity and security, the backend api should be protected by some authentication and authorisation process to avoid
unwanted external access to user data and also for unwanted access between different valid users. One user cannot have
access to data from another user.

To conform with [GDPR (europe)](https://gdpr-info.eu/)
or [LGPD (brazil)](https://www.gov.br/cidadania/pt-br/acesso-a-informacao/lgpd) the user data should be stored and
protected properly with limited access and in a external component.

## Decision

We will use [Keycloak](https://www.keycloak.org/) to authenticate and authorise end-users to login, logout and manage
their personal data.

## Consequences

Protect user data against unwanted access or exposure.

Use a solid, stable and reliable identity and access mgmt tool maintained by RedHat.

No in-house implementation and maintenance besides regular version upgrade.