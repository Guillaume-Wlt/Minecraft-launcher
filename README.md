# Minecraft Launcher

A Minecraft launcher developed in Java (Swing), allowing to download and launch game versions via the official Mojang API.

## Latest Release

**[v1.0 — First Stable Release](https://github.com/Guillaume-Wlt/Minecraft-launcher/releases/tag/v1.0)**

The CLI version is fully functional. See the release page for installation instructions.

## Project Status

> **In progress** — The JRE auto-download pipeline is fully implemented and wired into the workflow. The launcher downloads, parses, and installs the Mojang-provided JRE and resolves the Java executable path automatically. Versions **1.6.4** and **1.8.9** are functional. Some compatibility issues remain with older versions (pre-1.6) and more recent ones (1.13+), along with minor known bugs.

| Feature | Status |
|---|---|
| Download manifest | Done |
| Version selection (user input) | Done |
| Download version JSON | Done |
| Interpret version JSON | Done |
| Interpret & download client `.jar` (SHA-1) | Done |
| Interpret & download libraries (SHA-1, OS filter) | Done |
| Native libraries extraction into `bin/<version>/` | Done |
| `${arch}` placeholder resolution in native classifiers | Done |
| Classpath filtering (natives excluded) | Done |
| Interpret assets index | Done |
| Download assets index JSON | Done |
| Interpret assets list | Done |
| Download game assets (textures, sounds…) | Done |
| Build classpath from libraries + client JAR | Done |
| Collect user info (username, RAM) via CLI | Done |
| Launch game client via ProcessBuilder | Done |
| Dynamic `mainClass` resolution from version JSON | Done |
| Parse `javaVersion.component` field from version JSON | Done |
| Download Mojang runtime manifest (`all.json`) | Done |
| Parse runtime manifest to extract JRE component URL | Done |
| Download detailed JRE manifest (file list) | Done |
| Parse detailed JRE manifest | Done |
| Download JRE files into `runtime/<component>/` | Done |
| Resolve Java executable path from downloaded JRE | Done |
| Compatibility with pre-1.6 versions (full) | In progress |
| Compatibility with 1.13+ versions (JSON structure changes) | In progress |
| RAM input validation against system available memory | To do |
| Error recovery — retry failed steps instead of stopping | To do |
| Java Swing GUI — [Implementation guide](GUI_SWING_GUIDE.md) | To do |
| Mojang / Microsoft authentication | To do |
| Auto-update system | Long term |
| LZMA compression for JRE file downloads (bandwidth optimisation) | Long term |
| Log filtering system (INFO, DEBUG, ERROR-ONLY, …) | Long term |

### Known issues

- **Pre-1.6 versions — no sound** (e.g. 1.4.6) : these versions download their sounds at runtime from `s3.amazonaws.com/MinecraftResources/`, a server that has been offline for years. The game launches and is playable but has no audio. This cannot be fixed at the launcher level — it is a limitation of the client itself.
- **Pre-1.6 versions** (e.g. 1.4.6) : `mainClass` is now read dynamically from the version JSON (defaulting to `net.minecraft.client.main.Main`). However, these versions use `LaunchWrapper`, which requires **Java 8** — running them on Java 9+ causes a `ClassCastException` (`AppClassLoader` cannot be cast to `URLClassLoader`). Fix: parse the `javaVersion.majorVersion` field from the version JSON and resolve the correct Java executable path at launch time.
- **1.13+ versions** (e.g. 1.14.4) : some library entries in the JSON do not have a `downloads` field, causing a parsing error. The parser needs to handle this case gracefully.
- **Debug `System.out.println`** present in `LibrariesInfosParser` and `DownloadLibrariesProcess` — to be removed before release.

## Technical Stack

| Technology | Version | Role |
|---|---|---|
| Java | 21 | Main language |
| Maven | - | Build management |
| Lombok | 1.18.44 | Boilerplate reduction |
| org.json | 20251224 | JSON parsing |
| Maven Shade Plugin | 3.6.0 | Fat JAR generation |
| JUnit Jupiter | 5.11.0 | Unit & integration testing |
| Maven Surefire Plugin | 3.2.5 | Test execution via Maven |

## Architecture

The project is organised in distinct layers:

```
fr.guillaumewlt/
├── workflow/         # Workflow orchestration (state machine)
├── processing/
│   └── steps/        # Processing steps (init, download, interpret…)
├── downloads/        # File download logic
├── parser/           # Mojang JSON response parsing
├── model/            # Record classes (data holders)
├── utils/            # Utilities (paths, URLs, console messages…)
└── exceptionhandler/ # Error handling
```

### Workflow

The launcher follows a step chain driven by `WorkflowRunner`:

```
INIT
 └─> DOWNLOAD_MANIFEST
      └─> INTERPRET_MANIFEST                  (version selection)
           └─> DOWNLOAD_VERSION_JSON
                └─> INTERPRET_VERSION_JSON
                     └─> INTERPRET_CLIENT_JAR_INFOS
                          └─> DOWNLOAD_CLIENT_JAR
                               └─> INTERPRET_VERSION_LIBRARIES_INFOS
                                    └─> DOWNLOAD_VERSION_LIBRARIES
                                         └─> EXTRACT_NATIVES_LIBRARIES
                                              └─> INTERPRET_CLIENT_ASSETS_INDEX
                                                   └─> DOWNLOAD_CLIENT_ASSETS_INDEX
                                                        └─> INTERPRET_CLIENT_ASSETS_INFOS
                                                             └─> DOWNLOAD_CLIENT_ASSETS
                                                                  └─> DOWNLOAD_RUNTIME_JSON      <- done
                                                                       └─> INTERPRET_RUNTIME_JSON <- done
                                                                            └─> DOWNLOAD_JRE_MANIFEST  <- done
                                                                                 └─> INTERPRET_JRE_MANIFEST  <- done
                                                                                      └─> DOWNLOAD_JRE_FILES      <- done
                                                                                           └─> CLASSPATH_BUILDING
                                                                                                └─> REQUEST_INFOS
                                                                                                     └─> STARTING_CLIENT
                                                                                                          └─> END
```

## Generated Folder Structure

```
target/launcher/
├── versions/
│   └── <version>/
│       ├── <version>.json
│       └── <version>.jar
├── libraries/        # Game libraries (.jar)
├── assets/
│   ├── indexes/      # Asset index JSON files
│   └── objects/      # Asset files (hashed subdirectories)
├── bin/
│   └── <version>/    # Extracted native libraries (.dll / .so / .dylib)
└── runtime/
    └── <component>/  # Downloaded JRE (e.g. java-runtime-gamma/, jre-legacy/)
        ├── bin/
        │   └── java.exe
        ├── lib/
        └── ...
```

## Running the Project

### Prerequisites

- JDK 21+
- Maven

### Build

```bash
mvn package
```

### Run

```bash
java -jar target/minecraft-launcher.jar
```
