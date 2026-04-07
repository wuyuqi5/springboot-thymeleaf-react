# Java Code Style

All rules in this document must be followed.

## File Structure

Order:

- package
- imports
- class

Rules:

- One top-level class per file
- File name must match class name
- Exactly one blank line between sections

## Imports

Rules:

- Wildcard imports must not be used
- Static imports come before normal imports
- Separate groups with one blank line
- Imports must be sorted in ASCII order
- Imports must not be line wrapped

## Indentation

- Use 2 spaces
- Do not use tabs

## Column Limit

- Maximum line length is 100 characters
- Exceptions: package declarations, imports, URLs in comments, text blocks

## Braces

- Control statements must use braces
- Use K&R style

## Statements

- Exactly one statement per line

## Whitespace

- Use one space after control keywords
- Use one space around binary operators
- Use one space before `{`

## Naming

- Package: lowercase only
- Class: UpperCamelCase
- Method: lowerCamelCase
- Field: lowerCamelCase
- Constant: UPPER_SNAKE_CASE

## Variable Declarations

- One variable per declaration
- Declare variables near usage

## Modifier Order

Use this order:

- public
- protected
- private
- abstract
- default
- static
- final
- transient
- volatile
- synchronized
- native
- strictfp

## Annotations

- Class annotations must be on separate lines
- Method annotations may appear on the same line

## Comments

- Only add comments for complex logic, business rules, temporary workarounds, or TODO items
- Do not comment obvious code
- Inline comments should explain non-obvious logic and should not appear at line end
