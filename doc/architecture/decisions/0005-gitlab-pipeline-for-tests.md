# 5. Gitlab pipeline for Tests

Date: 2022-09-05

## Status

Accepted

## Context

The unit tests, integration tests for the main branch, feature branches, hotfix, should be visible and easy to run.

## Decision

We will use gitlab.com to run the pipelines with local runner in my local private kubernetes cluster.

## Consequences

It will be possible to run pipelines without cost limitation. Learn about gitlab runners in a microk8s kubernetes
cluster with 3 nodes.