package fr.guillaumewlt.model.directory;

/**
 * Groups all directories used by the launcher into a single immutable container.
 * Set by {@link fr.guillaumewlt.processing.steps.InitProcess#process()},
 * which builds each {@link LauncherDir} and stores this record in
 * {@link fr.guillaumewlt.workflow.LauncherContext}.
 *
 * @param launcherDir  The root launcher directory (e.g. {@code "[...]/launcher/"}).
 * @param binDir       The directory used to store the client JAR file (e.g. {@code "[...]/launcher/bin/"}).
 * @param versionsDir  The directory used to store version manifests and JSON files (e.g. {@code "[...]/launcher/versions/"}).
 * @param librariesDir The directory used to store library JAR files (e.g. {@code "[...]/launcher/libraries/"}).
 * @param assetsDir         The directory used to store game assets (e.g. {@code "[...]/launcher/assets/"}).
 * @param assetsIndexesDir  The directory used to store asset index JSON files (e.g. {@code "[...]/launcher/assets/indexes/"}).
 * @param assetsObjectsDir  The directory used to store individual asset object files (e.g. {@code "[...]/launcher/assets/objects/"}).
 */
public record LauncherDirs(
   LauncherDir launcherDir,
   LauncherDir binDir,
   LauncherDir versionsDir,
   LauncherDir librariesDir,
   LauncherDir assetsDir,
   LauncherDir assetsIndexesDir,
   LauncherDir assetsObjectsDir
) {}
