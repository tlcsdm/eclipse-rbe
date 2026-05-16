# Changelog

All notable changes to this project will be documented in this file.

## [1.1.1] - 2026

### Added
- Add opt-in preference to sort keys alphabetically on save ([#56](https://github.com/tlcsdm/eclipse-rbe/pull/56)).
- Add Persian (Farsi) language support ([#52](https://github.com/tlcsdm/eclipse-rbe/pull/52)).
- Show language banner image as fallback when country image is unavailable ([#45](https://github.com/tlcsdm/eclipse-rbe/pull/45)).
- Port "Displayed Locales" filter from upstream ([#44](https://github.com/tlcsdm/eclipse-rbe/pull/44)).
- Sort editor languages using Collator locale rules ([#43](https://github.com/tlcsdm/eclipse-rbe/pull/43)).

### Fixed
- Fix unreadable text in DevStyle dark theme ([#55](https://github.com/tlcsdm/eclipse-rbe/pull/55)).
- Wrap ResourceChangeListener UI updates in asyncExec to fix invalid thread access ([#54](https://github.com/tlcsdm/eclipse-rbe/pull/54)).
- Recalculate equalIndex per key when alignment is disabled for grouped keys ([#53](https://github.com/tlcsdm/eclipse-rbe/pull/53)).
- Update Georgian flag (ge.gif) to Five Crosses design adopted in 2004 ([#51](https://github.com/tlcsdm/eclipse-rbe/pull/51)).
- Change KEEP_EMPTY_FIELDS default value to true ([#50](https://github.com/tlcsdm/eclipse-rbe/pull/50)).
- Honor forced newline style for comment output on Windows ([#49](https://github.com/tlcsdm/eclipse-rbe/pull/49)).
- Correct French typo mauscules → majuscules in bundle_fr.properties ([#48](https://github.com/tlcsdm/eclipse-rbe/pull/48)).
- Downgrade adapter NPE log severity from ERROR to WARNING ([#47](https://github.com/tlcsdm/eclipse-rbe/pull/47)).
- Prevent text corruption when wrapping lines with no spaces ([#46](https://github.com/tlcsdm/eclipse-rbe/pull/46)).

## [1.1.0] - 2026

### Changed
- Built with Eclipse 2024-06 and Java 17.

## [1.0.7] - 2024

### Changed
- Improve settings.

## [1.0.6] - 2017-02-24

### Fixed
- Fixed NPE in Eclipse Neon ([#52](https://github.com/essiembre/eclipse-rbe/issues/52)).

## [1.0.5] - 2015-11-30

### Fixed
- Fixed default bundle not showing ([#25](https://github.com/essiembre/eclipse-rbe/issues/25)).
- Keys filter now case-insensitive ([#27](https://github.com/essiembre/eclipse-rbe/issues/27)).
- Fixed German and Spanish translations.

## [1.0.4] - 2014-10-12

### Fixed
- Removed hard-coding of white color for editor text field value ([#11](https://github.com/essiembre/eclipse-rbe/issues/11)).
- Fixed breaking Eclipse features by wrongly disposing of a system color ([#12](https://github.com/essiembre/eclipse-rbe/issues/12)).

## [1.0.3] - 2014-08-07

### Fixed
- Fixed saving of fragment-related plugin preferences.

## [1.0.2] - 2014-08-03

### Fixed
- Fixed the line break ending the Eclipse properties editor line separator.

## [1.0.1] - 2014-07-22

### Changed
- Re-introduce Eclipse Properties editors for locale-specific editors.

## [1.0.0] - 2014-07-20

### Added
- License change from LGPL to Apache License v2.
- Now requires Java 7 or higher.
- Upgraded to work properly with Eclipse 4.x (dropped Eclipse 2.x plugin-style in favor of OSGi).
- Upgraded code to add generics where missing and resolve deprecated code.
- Restored previous editor field navigation (SHIFT-)Tab to switch field, CTRL-Up/CTRL-Down to cycle through tree elements.
- Updated key fonts and colors to be more intuitive.
- Reintroduced vertical scroll in favor of auto-adjust.
- Background color of groups with no key is now gray.
