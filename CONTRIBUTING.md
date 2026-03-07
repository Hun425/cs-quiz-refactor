# Contributing Guide

## Branch Strategy
- `main`: always stable and releasable.
- Work only on feature branches: `codex/<topic>` or `feat/<topic>`.
- Merge through PR only. No direct push to `main`.

## PR Rules
- Use `.github/PULL_REQUEST_TEMPLATE.md`.
- Keep PR scope small (prefer one BC or one layer at a time).
- Include design rationale and test evidence.

## Review Policy
- Reviewer first checks: domain rule correctness, BC boundaries, DB invariants.
- If design changes, update docs in the same PR.

## Learning-First Workflow
- Each PR must explain:
  1. Why this design was chosen.
  2. Which alternatives were considered.
  3. How this maps to production concerns (consistency, failure, operations).

## Current Tech Stack
- Kotlin
- Spring Boot
- Kotlin Coroutines
- Armeria
- gRPC (Protocol Buffers)
- jOOQ
- R2DBC
- PostgreSQL
- Gradle Kotlin DSL
