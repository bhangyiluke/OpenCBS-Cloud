Optimal chunk size for CLAUDE.md creation: ~2000 tokens per chunk.

At the end of every task, update context.md with what you learned about the optimal chunk size for this file, and then immediately start the next chunk based on those updated rules.

/loop Create the CLAUDE.md file in chunks of 2000 tokens.

1. Write the first section (e.g., Project Overview & Architecture).
2. Append it to CLAUDE.md.
3. Ask yourself: "Are there more sections to add?"
4. If yes, immediately write the next section (e.g., Coding Standards, File Structure).
5. If no, stop and show me the final file.
6. Target Size: Keep CLAUDE.md under 1,000 tokens (~400-500 words)
7. Focus: Include only behavior-changing instructions (rules, constraints, architecture). Remove philosophical fluff or lengthy explanations that don't directly affect code generation.
8. Offload Details: Move detailed documentation, API specs, or large examples to separate files (e.g., ARCHITECTURE.md, API_SPECS.md) and reference them in CLAUDE.md instead of pasting the content directly.