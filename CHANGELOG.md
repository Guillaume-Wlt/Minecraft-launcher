# Changelog

## [v1.1] — 2026-04-02

### Native libraries extraction

- Added `NativesLibsExtract` — reads each native JAR as a ZIP via `ZipInputStream` and extracts its entries into `bin/<version>/`, isolated per version to prevent conflicts between incompatible native files (e.g. two different `lwjgl.dll` builds).
- Added `${arch}` placeholder resolution in native classifiers (e.g. `natives-windows-${arch}` → `natives-windows-64`).
- Fixed classpath filtering: native JARs are now excluded from the `-cp` argument. Only standard libraries and the client JAR are included in the classpath; natives are passed via `-Djava.library.path=bin/<version>/`.
- Fixed library parser (`LibrariesInfosParser`) to correctly handle entries without a `downloads` field (partial fix — 1.13+ versions still affected).

### JRE auto-download pipeline

Full pipeline added to download and install the Mojang-provided JRE automatically:

| Step | Class |
|---|---|
| Download runtime manifest (`all.json`) | `DownloadRuntimeJSONProcess` + `RuntimeJSONDownload` |
| Parse manifest to resolve JRE component URL | `InterpretRuntimeProcess` + `RuntimeJSONParser` |
| Download detailed JRE file manifest | `DownloadJREManifestProcess` + `JREManifestDownload` |
| Parse detailed JRE manifest into `List<JREFileInfos>` | `InterpretJREManifestProcess` + `JREManifestParser` |
| Download all JRE files into `runtime/<component>/` | `DownloadJREFilesProcess` + `JREFilesDownload` |
| Resolve Java executable path at launch | `StartingClientProcess` |

- The Java executable path is resolved dynamically at launch from the downloaded JRE: `runtime/<component>/bin/java[.exe]`, with OS detection (Windows vs. other).
- The JRE component name is read from the `javaVersion.component` field in the version JSON, allowing each Minecraft version to use its correct runtime (e.g. `java-runtime-gamma`, `jre-legacy`).
- All JRE steps are fully wired into `WorkflowRunner` between `DOWNLOAD_CLIENT_ASSETS` and `CLASSPATH_BUILDING`.

### Workflow

- Added `ProgramStep` entries: `DOWNLOAD_RUNTIME_JSON`, `INTERPRET_RUNTIME_JSON`, `DOWNLOAD_JRE_MANIFEST`, `INTERPRET_JRE_MANIFEST`, `DOWNLOAD_JRE_FILES`.
- `WorkflowRunner` updated to execute the full JRE pipeline before building the classpath.

### Models

- Added `RuntimeRawData` — holds the resolved JRE component name and manifest URL.
- Added `JREFileInfos` — holds the path, download URL, SHA-1 hash, and type (`file` / `directory` / `link`) for each JRE entry.

### Known issues (carried over from v1.0)

- **Pre-1.6 versions**: these versions use `LaunchWrapper`, which requires Java 8. Running them on Java 9+ causes a `ClassCastException` (`AppClassLoader` cannot be cast to `URLClassLoader`).
- **1.13+ versions**: some library entries lack a `downloads` field, causing a parsing error.
- **Pre-1.6 sounds**: sound assets were hosted on `s3.amazonaws.com/MinecraftResources/`, a server that has been offline for years. The game is playable but silent — this cannot be fixed at the launcher level.

---

## [v1.0] — First stable release

Initial CLI release. Downloads and launches Minecraft versions via the official Mojang API.
See the [v1.0 release page](https://github.com/Guillaume-Wlt/Minecraft-launcher/releases/tag/v1.0) for details.
