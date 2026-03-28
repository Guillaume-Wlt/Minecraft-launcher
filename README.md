# Minecraft Launcher

A Minecraft launcher developed in Java (Swing), allowing to download and launch game versions via the official Mojang API.

## Project Status

> **In progress** — The full download pipeline is complete. Game launch (classpath construction, ProcessBuilder) is the remaining step before the CLI version is functional. The Swing GUI and authentication layer come after.

| Feature | Status |
|---|---|
| Download manifest | Done |
| Version selection (user input) | Done |
| Download version JSON | Done |
| Interpret version JSON | Done |
| Interpret & download client `.jar` (SHA-1) | Done |
| Interpret & download libraries (SHA-1, OS filter) | Done |
| Interpret assets index | Done |
| Download assets index JSON | Done |
| Interpret assets list | Done |
| Download game assets (textures, sounds…) | Done |
| Build classpath from libraries + client JAR | To do |
| Launch game client via ProcessBuilder | To do |
| Native libraries extraction (legacy versions) | To do |
| Java Swing GUI | To do |
| Mojang / Microsoft authentication | To do |

## Technical Stack

| Technology | Version | Role |
|---|---|---|
| Java | 21 | Main language |
| Maven | - | Build management |
| Lombok | 1.18.44 | Boilerplate reduction |
| org.json | 20251224 | JSON parsing |
| Maven Shade Plugin | 3.6.0 | Fat JAR generation |

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
                                                             └─> STARTING_CLIENT       <- to do
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
└── bin/              # Game binaries
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
