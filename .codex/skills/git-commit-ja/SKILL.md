---
name: git-commit-ja
description: Use when the user wants to prepare or perform a Git commit with a Japanese commit message. Inspect staged changes, draft a concise Japanese commit message, ask for confirmation, and only then run git commit.
---

# Japanese Git Commit

## Workflow

1. Stage the intended files.
2. Inspect staged diffs.
3. Draft a Japanese commit message based on the staged changes.
4. Show the message to the user and ask for confirmation.
5. Only after approval, run `git commit`.

## Commit Message Rules

- First line should be concise Japanese within 50 characters
- Start with a verb such as `追加`, `修正`, `削除`, `改善`, or `整備`
- Add a body only when it helps explain why the change was made
- In the body, prefer short bullet points starting with `- `

## No Changes Case

If there is no staged diff, report that there is nothing to commit.
