# Minecraft Launcher

A Minecraft launcher developed in Java (Swing), allowing to download and launch game versions via the official Mojang API.

## Latest Release

**[v1.0 — First Stable Release](https://github.com/Guillaume-Wlt/Minecraft-launcher/releases/tag/v1.0)**

The CLI version is fully functional. See the release page for installation instructions.

## Project Status

> **In progress** — Native libraries extraction is now implemented. Versions **1.6.4** and **1.8.9** are functional. Some compatibility issues remain with older versions (pre-1.6) and more recent ones (1.13+), along with minor known bugs.

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
| Compatibility with pre-1.6 versions (mainClass) | In progress |
| Compatibility with 1.13+ versions (JSON structure changes) | In progress |
| RAM input validation against system available memory | To do |
| Error recovery — retry failed steps instead of stopping | To do |
| Java Swing GUI | To do |
| Mojang / Microsoft authentication | To do |
| Auto-update system | Long term |

### Known issues

- **Pre-1.6 versions** (e.g. 1.4.6) : the main class `net.minecraft.client.main.Main` does not exist in these versions — the `mainClass` field from the version JSON must be read dynamically instead of being hardcoded.
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
      └─> INTERPRET_MANIFEST              (version selection)
           └─> DOWNLOAD_VERSION_JSON
                └─> INTERPRET_VERSION_JSON
                     └─> INTERPRET_CLIENT_JAR_INFOS
                          └─> DOWNLOAD_CLIENT_JAR
                               └─> INTERPRET_VERSION_LIBRARIES_INFOS
                                    └─> DOWNLOAD_VERSION_LIBRARIES
                                         └─> INTERPRET_CLIENT_ASSETS_INDEX
                                              └─> DOWNLOAD_CLIENT_ASSETS_INDEX  <- done
                                                   └─> INTERPRET_CLIENT_ASSETS_INFOS  <- done
                                                        └─> DOWNLOAD_CLIENT_ASSETS    <- done
                                                             └─> CLASSPATH_BUILDING    <- done
                                                                  └─> REQUEST_INFOS        <- done
                                                                       └─> STARTING_CLIENT <- done
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
└── bin/
    └── <version>/    # Extracted native libraries (.dll / .so / .dylib)
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
