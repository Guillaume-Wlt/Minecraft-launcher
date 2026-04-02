# Guide — Adding a Swing GUI

This document summarises the steps to integrate a Java Swing GUI into the launcher without modifying the existing pipeline.

---

## Why the current architecture is already well-suited for a GUI

`LauncherContext` is a pure data object — it has no knowledge of whether its values come from the console or a graphical window. `WorkflowRunner` orchestrates the steps without performing any user I/O itself. **The business logic and the display layer are already separated** — adding a GUI means plugging in a new input layer, not refactoring what already exists.

---

## The only CLI coupling point to replace

`RequestInfosProcess` reads user input (username, RAM) from a `Scanner`.
This is the **only place** where the user currently interacts with the launcher.

For the GUI, this process is bypassed: values are injected directly into the context before the workflow starts.

```java
context.setUsername("Steve");
context.setMinRam("512");
context.setMaxRam("2");
```

---

## Implementation steps

### 1. Create the UI package

Create a dedicated package to keep the GUI separate from the business logic:

```
fr.guillaumewlt.ui/
├── MainWindow.java        # Main window (JFrame)
└── LaunchPanel.java       # Launch panel (version list, fields, button)
```

---

### 2. Build the main window (`MainWindow`)

Create a `JFrame` containing:
- a drop-down list (`JComboBox`) for version selection
- a text field (`JTextField`) for the username
- two fields for minimum and maximum RAM
- a **"Play"** button
- a progress bar (`JProgressBar`) to track downloads
- a log area (`JTextArea`) to display the current step

---

### 3. Populate the version list after `INTERPRET_MANIFEST`

The manifest is parsed during the `INTERPRET_MANIFEST` step. Once that step completes, `LauncherContext` holds the available versions.

Strategy:
- First run only `DOWNLOAD_MANIFEST` and `INTERPRET_MANIFEST` inside a `SwingWorker`.
- Once done, populate the `JComboBox` with the version list.
- Wait for the user to click "Play" before running the rest of the workflow.

---

### 4. Run the workflow on a background thread (mandatory)

In Swing, **anything that blocks the Event Dispatch Thread (EDT) freezes the UI**. Downloads must run on a separate thread.

Use `SwingWorker<Void, String>`:

```java
SwingWorker<Void, String> worker = new SwingWorker<>() {
    @Override
    protected Void doInBackground() {
        // run the WorkflowRunner here
        new WorkflowRunner().run();
        return null;
    }

    @Override
    protected void done() {
        // called on the EDT when the workflow finishes
        statusLabel.setText("Game closed.");
    }
};
worker.execute();
```

---

### 5. Connect the progress bar to `DownloadProgress`

Currently, `DownloadProgress` prints progress to the console via `System.out.print`. For the GUI, it needs a way to notify the interface.

Approach: pass a `Consumer<Integer>` (or a listener) to `DownloadProgress` so it can report the percentage, then update the `JProgressBar` from the EDT using `SwingUtilities.invokeLater`.

```java
SwingUtilities.invokeLater(() -> progressBar.setValue(percent));
```

---

### 6. Adapt `RequestInfosProcess` for the GUI

Two options:

**Option A — Bypass the process**: before starting the workflow, feed the context directly from the Swing fields, then skip the `REQUEST_INFOS` step entirely.

**Option B — Make the process flexible**: extract a `UserInputProvider` interface implemented by both the console version (Scanner) and the GUI version (Swing fields). `RequestInfosProcess` depends on the interface, not the implementation.

Option B is cleaner in the long run.

---

## What you do NOT need to change

| Layer | Status |
|---|---|
| Download pipeline | Unchanged |
| Parsers and models | Unchanged |
| `WorkflowRunner` | Unchanged (or minimal adaptation) |
| `LauncherContext` | Unchanged |
| `StartingClientProcess` | Unchanged |

---

## Suggested starting order

1. Create `MainWindow.java` with an empty window that displays on launch.
2. Run `DOWNLOAD_MANIFEST` + `INTERPRET_MANIFEST` in a `SwingWorker` and populate the `JComboBox` with the versions.
3. On "Play" click, inject the field values into the context and run the rest of the workflow in a second `SwingWorker`.
4. Connect the `JProgressBar` to `DownloadProgress`.
5. Replace `RequestInfosProcess` using Option A or B depending on preference.