# JPA Best Practices

Based on Vlad Mihalcea and focused on practical JPA and Hibernate usage in this project.

## Entity Identity

- `equals` should compare a non-null id
- `hashCode` should be constant
- Never use mutable fields in `hashCode`

## Entity Design

- Avoid public setters
- Prefer constructors and domain methods
- Preserve entity invariants through behavior methods

## Audit Fields

Every table should usually have:

- `created_at`
- `updated_at`

Optional fields:

- `created_by`
- `updated_by`
- `deleted`

## Association Design

### OneToMany

- Prefer bidirectional mapping
- Parent uses `mappedBy`
- Child owns the foreign key
- Keep both sides in sync with helper methods
- Do not map large collections when direct child queries are better

### ManyToMany

- Always use `Set`
- Cascade only `PERSIST` and `MERGE`
- Never use `CascadeType.REMOVE` or `CascadeType.ALL`
- Prefer a join entity when the relationship may carry metadata

### OneToOne

- Prefer child-to-parent mapping with `@MapsId`
- Child primary key should match parent foreign key
- Avoid parent-side `@OneToOne` when possible

## Fetch Strategy

- Always declare fetch type explicitly
- Use `FetchType.LAZY` for `@ManyToOne` and `@OneToOne` unless there is a strong reason not to

## N+1 Prevention

When service logic would trigger repeated lazy loads:

- Fix it with `JOIN FETCH`, or
- Use DTO projection for read-only queries

## Query Design

- Avoid loading full entities when not needed
- Prefer DTO projection
- Prefer pagination with `Pageable`
- Avoid `findAll()` except for small master tables

## getReferenceById

- Use `getReferenceById` when only a foreign key reference is needed
- Use `findById` when entity existence or fields must be validated

## Composite Keys

- Use `@Embeddable` and `@EmbeddedId`
- Do not use `@IdClass`
