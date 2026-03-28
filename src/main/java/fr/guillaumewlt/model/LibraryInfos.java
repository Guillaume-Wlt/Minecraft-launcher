package fr.guillaumewlt.model;

/**
 * Holds the download and identification information for a single Minecraft library.
 * Set by {@link fr.guillaumewlt.parser.LibrariesInfosParser#jsonParser()},
 * which iterates over the {@code libraries} array from the version JSON file.
 * Libraries with OS-specific rules that do not match the current OS are excluded.
 *
 * @param name The Maven-style artifact identifier of the library (e.g. {@code "com.mojang:authlib:3.3.39"}),
 *             read from the version JSON field {@code libraries[i].name}.
 * @param sha1 The SHA-1 hash of the library JAR file used for integrity verification,
 *             read from {@code libraries[i].downloads.artifact.sha1}.
 * @param path The relative file path where the library should be saved on disk
 *             (e.g. {@code "com/mojang/authlib/3.3.39/authlib-3.3.39.jar"}),
 *             read from {@code libraries[i].downloads.artifact.path}.
 * @param size The size of the library JAR file in bytes,
 *             read from {@code libraries[i].downloads.artifact.size}.
 * @param url  The download URL for the library JAR file,
 *             read from {@code libraries[i].downloads.artifact.url}.
 */
public record LibraryInfos(
        String name,
        String sha1,
        String path,
        long size,
        String url
) {
}
