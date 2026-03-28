package fr.guillaumewlt.model;

/**
 * Holds the Minecraft version selected by the user and its associated metadata URL.
 * Set by {@link fr.guillaumewlt.parser.ManifestParser#jsonparser()}.
 *
 * @param selectedVersion The version ID entered by the user (e.g. {@code "1.20.1"}),
 *                        read from the manifest JSON array field {@code "id"}.
 * @param url             The URL pointing to the version JSON file for the selected version,
 *                        read from the manifest JSON array field {@code "url"}.
 */
public record SelectedVersion(
        String selectedVersion,
        String url
) {
}
