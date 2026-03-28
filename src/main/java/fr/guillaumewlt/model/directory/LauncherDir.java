package fr.guillaumewlt.model.directory;

/**
 * Represents a single directory used by the launcher, associating a human-readable name
 * with its absolute path on disk.
 * Instances are created in {@link fr.guillaumewlt.processing.steps.InitProcess#process()}
 * and grouped into a {@link LauncherDirs} record stored in
 * {@link fr.guillaumewlt.workflow.LauncherContext}.
 *
 * @param name The human-readable identifier of the directory (e.g. {@code "launcher"}, {@code "assets"}).
 * @param path The absolute path of the directory on disk (e.g. {@code "[...]/launcher/assets/"}).
 */
public record LauncherDir(
    String name,
    String path
) {}
