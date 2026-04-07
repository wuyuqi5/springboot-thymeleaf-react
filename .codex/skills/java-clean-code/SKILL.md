---
name: java-clean-code
description: Use when reviewing, writing, refactoring, or fixing Java or Spring code in this project. Apply this project's conventions for exceptions, controllers, services, DTOs, repositories, and architecture. Read references/java-code-style.md for all Java changes. Read references/jpa-best-practices.md when editing entities or repositories.
---

# Java Clean Code

## Workflow

1. Read the target Java or Spring files first.
2. Read `references/java-code-style.md`.
3. If the task touches entities or repositories, also read `references/jpa-best-practices.md`.
4. Check the code in this order:
   - Exceptions
   - Controller
   - Service
   - Entity/Repository
   - DTO
   - Style
5. Make the required changes.
6. Report what changed and why.

## Review Checklist

### Exceptions

- Do not use `IllegalArgumentException`, `RuntimeException`, or `new Exception(...)`
- Use project exceptions such as `BizException` or `Preconditions`
- Do not use `.orElseThrow()` without an explicit exception
- Do not add try/catch blocks in controllers
- If an error code is missing, add it to `BizErrorCode`

### Controller

- No repository injection
- No business logic
- No try/catch
- No formatting or display logic
- Admin endpoints must use `@PreAuthorize("hasAnyRole('ROLE_ADMIN')")`

Controllers should only handle HTTP, request and response mapping, and view binding.

### Service

- Default service classes to `@Transactional(readOnly = true)`
- Write methods should override with `@Transactional`
- Do not put `@Transactional` on private methods
- Prevent N+1 by fixing repository queries, not service code
- Complex services may use interface plus implementation
- Simple services may stay as a single concrete class

### Entity and Repository

Read `references/jpa-best-practices.md` before changing entity or repository code.

### DTO

- Use `Dto`, `Command`, `Query`, and `Form` suffixes appropriately
- DTOs transport data only and should not hold domain behavior
- Mapping logic may live inside DTOs
- DTO projection is acceptable for read-only queries
