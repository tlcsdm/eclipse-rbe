# Copilot instructions for eclipse-rbe

This repository is an Eclipse PDE plug-in built with Maven Tycho using the pomless layout from `tlcsdm/eclipse-translation`.

## Project layout

- Plug-in bundle: `plugins/com.essiembre.eclipse.rbe/`
- Feature project: `features/com.essiembre.eclipse.rbe.feature/`
- Update site project: `sites/com.essiembre.eclipse.rbe.site/`
- Target platform descriptors: `target-platform.target` and `targets/*.target`

## Development guidance

- Keep changes surgical and avoid unrelated source cleanups.
- Prefer Tycho/PDE metadata changes over hand-written Maven module POMs in plug-in or feature directories.
- Keep bundle and feature versions aligned. Current release line is `1.1.0.qualifier`.
- Use Java 17 APIs only; the bundle declares `Bundle-RequiredExecutionEnvironment: JavaSE-17`.
- Update both English and Chinese README files when install or build instructions change.
- If an update-site URL changes, check `feature.xml`, README files, and workflow files for stale references.

## Validation

Run targeted validation before committing:

```sh
mvn -B validate -ntp -DskipTests
```

Run the full Tycho build when build metadata, target platforms, workflows, or release packaging changes:

```sh
mvn -B clean verify -ntp -DskipTests
```
