package fr.guillaumewlt.models;

/**
 * Holds the metadata extracted from the Mojang runtime manifest for a specific JRE component.
 * Set by {@link fr.guillaumewlt.parser.RuntimeJSONParser#jsonParser()}.
 * This record acts as an intermediate data container passed through
 * {@link fr.guillaumewlt.workflow.LauncherContext} to the downstream download steps.
 *
 * @param name           The component name identifying the JRE (e.g. {@code "jre-legacy"},
 *                       {@code "java-runtime-gamma"}), read from the version JSON field
 *                       {@code javaVersion.component}.
 * @param sha1           The SHA-1 checksum of the detailed JRE manifest file,
 *                       used to verify the integrity of the downloaded manifest.
 * @param size           The size in bytes of the detailed JRE manifest file.
 * @param url            The URL pointing to the detailed JRE manifest listing all files
 *                       to download in order to reconstruct the JRE locally.
 * @param jreName        The version name of the JRE (e.g. {@code "17.0.15"}, {@code "8u202"}),
 *                       read from the runtime manifest field {@code version.name}.
 * @param jreReleaseDate The release date of the JRE version (ISO-8601),
 *                       read from the runtime manifest field {@code version.released}.
 */
public record RuntimeRawData(
        String name,
        String sha1,
        long size,
        String url,
        String jreName,
        String jreReleaseDate
) {}
