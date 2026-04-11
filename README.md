# Minecraft Launcher

A Minecraft launcher developed in Java (Swing), allowing to download and launch game versions via the official Mojang API.

## Latest Release

**[v1.2 — Current Release](https://github.com/Guillaume-Wlt/Minecraft-launcher/releases/tag/v1.2)** *(CLI only)*

The CLI version is fully functional. See the release page for installation instructions.

## Project Status

> **In progress** — The core pipeline is complete and stable. Versions **1.6.4**, **1.7**, **1.8.9** and **1.14.4** are functional. Legacy versions (1.6.x, 1.7.x) correctly map assets to a virtual directory for sound support. The **Java Swing GUI** is now integrated into the workflow — version selection and username input are handled through the UI. A **ConsoleWindow** displays launcher and game logs in real time with colour-coded output. A **workflow progress bar** with shimmer animation tracks each step after Play is clicked. The console sidebar now includes **clear**, **go to bottom**, and **copy to clipboard** buttons. The **SettingsWindow** is fully functional: min/max RAM fields with visual feedback, a **Remember Settings** checkbox, persistence to `config/settings.json`, settings loaded on startup, and a **Check Local files** button opening the launcher directory in the file explorer. The **BottomPanel** now includes a **Remember** checkbox next to the version combo to persist username and version on launch. The `settings.json` write logic now **merges** new keys into existing data instead of overwriting.

| Feature | Status      |
|---|-------------|
| Download manifest | Done        |
| Version selection (user input) | Done        |
| Download version JSON | Done        |
| Interpret version JSON | Done        |
| Interpret & download client `.jar` (SHA-1) | Done        |
| Interpret & download libraries (SHA-1, OS filter) | Done        |
| Native libraries extraction into `bin/<version>/` | Done        |
| `${arch}` placeholder resolution in native classifiers | Done        |
| Classpath filtering (natives excluded) | Done        |
| Interpret assets index | Done        |
| Download assets index JSON | Done        |
| Interpret assets list | Done        |
| Download game assets (textures, sounds…) | Done        |
| Build classpath from libraries + client JAR | Done        |
| Collect user info (username, RAM) via CLI | Done        |
| Launch game client via ProcessBuilder | Done        |
| Dynamic `mainClass` resolution from version JSON | Done        |
| Parse `javaVersion.component` field from version JSON | Done        |
| Download Mojang runtime manifest (`all.json`) | Done        |
| Parse runtime manifest to extract JRE component URL | Done        |
| Download detailed JRE manifest (file list) | Done        |
| Parse detailed JRE manifest | Done        |
| Download JRE files into `runtime/<component>/` | Done        |
| Resolve Java executable path from downloaded JRE | Done        |
| Legacy assets virtual directory mapping (sound on 1.6.x / 1.7.x) | Done        |
| 1.13+ library parsing (OS-filtered natives, rules evaluation) | Done        |
| Compatibility with pre-1.6 versions (full) | Done        |
| Java Swing GUI — main window, menu bar, content & bottom panels | Done        |
| Swing GUI — version combobox populated from manifest | Done        |
| Swing GUI — username input with validation | Done        |
| Swing GUI — Play button triggering workflow via CountDownLatch | Done        |
| Swing GUI — workflow integrated as `SHOW_UI` step | Done        |
| Swing GUI — ConsoleWindow (singleton) displaying launcher & game logs in real time | Done        |
| Swing GUI — ConsoleUtils queue-based batching (100ms flush, no EDT flooding) | Done        |
| Swing GUI — Console colour coding (timestamps green, errors red + stack trace, steps blue) | Done        |
| Swing GUI — Minecraft stdout/stderr redirected to ConsoleWindow | Done        |
| Swing GUI — Workflow progress bar with shimmer animation (visible after Play) | Done        |
| Swing GUI — Progress bar tracks workflow steps with percentage after SHOW_UI | Done        |
| Swing GUI — Play button grayed out and labeled "Launched" while game is running, restored on window close | In Progress |
| Swing GUI — Console sidebar — clear console button | Done        |
| Swing GUI — Console sidebar — go to bottom button | Done        |
| Swing GUI — Console sidebar — copy console to clipboard button | Done        |
| Swing GUI — ButtonHandler centralised (replaces PlayBtnHandler) with setter-injected dependencies | Done        |
| Swing GUI — SettingsWindow singleton with min/max RAM fields (defaults 512 / 2048 MB) | Done        |
| Swing GUI — Settings window — visual feedback (✔ indicator) after save | Done        |
| Swing GUI — Settings window — "Remember Settings" checkbox controlling persistence | Done        |
| Swing GUI — Settings window — "Save" button applying RAM to context and persisting to `config/settings.json` | Done        |
| Swing GUI — Settings window — "Check Local files" button opening launcher directory in file explorer | Done        |
| Swing GUI — Settings — `config/settings.json` written on first save, directory created automatically | Done        |
| Swing GUI — Settings — `settings.json` loaded on startup and pre-populated in fields | Done        |
| Swing GUI — BottomPanel — "Remember" checkbox next to version combo persisting username and version | Done        |
| Swing GUI — `settings.json` write merges new keys into existing data (no overwrite) | Done        |
| RAM input validation against system available memory | To do       |
| Error recovery — retry failed steps instead of stopping | To do       |
| Mojang / Microsoft authentication | To do       |
| Auto-update system | Long term   |
| LZMA compression for JRE file downloads (bandwidth optimisation) | Long term   |
| Log filtering system (INFO, DEBUG, ERROR-ONLY, …) | Long term   |

### Known issues

- **Pre-1.6 versions — no sound** (e.g. 1.4.6) : these versions download their sounds at runtime from `s3.amazonaws.com/MinecraftResources/`, a server that has been offline for years. The game launches and is playable but has no audio. This cannot be fixed at the launcher level — it is a limitation of the client itself.

## Technical Stack

| Technology | Version | Role |
|---|---|---|
| Java | 21 | Main language |
| Maven | - | Build management |
| Lombok | 1.18.44 | Boilerplate reduction |
| org.json | 20251224 | JSON parsing |
| FlatLaf | 3.5.4 | Swing dark/light theme |
| Maven Shade Plugin | 3.6.0 | Fat JAR generation |
| JUnit Jupiter | 5.11.0 | Unit & integration testing |
| Maven Surefire Plugin | 3.2.5 | Test execution via Maven |

## Architecture

The project is organised in distinct layers:

```
fr.guillaumewlt/
├── workflow/             # Workflow orchestration (state machine, context)
├── processing/
│   └── steps/            # Processing steps (init, download, interpret…)
├── downloads/            # File download logic
├── parser/               # Mojang JSON response parsing
├── model/                # Record classes (data holders)
├── utils/                # Utilities (paths, URLs, console messages…)
├── extractor/            # Native libraries extraction
├── ui/
│   ├── MainWindow.java   # Root JFrame
│   ├── builders/         # WindowBuilder, PanelBuilder (factory helpers)
│   ├── components/       # Reusable components (MenuBar…)
│   ├── panels/           # Content panels (ContentPanel, BottomPanel, ImagePanel…)
│   ├── eventhandler/     # UI event handlers (ButtonHandler…)
│   └── windows/          # Secondary windows (ConsoleWindow, SettingsWindow…)
└── exceptionhandler/     # Error handling
```

### Workflow

The launcher follows a sequential step chain driven by `WorkflowRunner`. Steps are grouped below by phase:

**1. Initialisation**
`INIT` → `DOWNLOAD_MANIFEST` → `INTERPRET_MANIFEST` → `SHOW_UI`

**2. Version**
`DOWNLOAD_VERSION_JSON` → `INTERPRET_VERSION_JSON`

**3. Client JAR**
`INTERPRET_CLIENT_JAR_INFOS` → `DOWNLOAD_CLIENT_JAR`

**4. Libraries**
`INTERPRET_VERSION_LIBRARIES_INFOS` → `DOWNLOAD_VERSION_LIBRARIES` → `EXTRACT_NATIVES_LIBRARIES`

**5. Assets**
`INTERPRET_CLIENT_ASSETS_INDEX` → `DOWNLOAD_CLIENT_ASSETS_INDEX` → `INTERPRET_CLIENT_ASSETS_INFOS` → `DOWNLOAD_CLIENT_ASSETS`

**6. Java Runtime (JRE)**
`DOWNLOAD_RUNTIME_JSON` → `INTERPRET_RUNTIME_JSON` → `DOWNLOAD_JRE_MANIFEST` → `INTERPRET_JRE_MANIFEST` → `DOWNLOAD_JRE_FILES`

**7. Launch**
`CLASSPATH_BUILDING` → `REQUEST_INFOS` → `STARTING_CLIENT` → `END`

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
│   ├── objects/      # Asset files (hashed subdirectories)
│   └── virtual/
│       └── legacy/   # Mapped assets for legacy versions (1.6.x, 1.7.x) — original filenames
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
java -jar target/minecraft-launcher-<version>.jar
```