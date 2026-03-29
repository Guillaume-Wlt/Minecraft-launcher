package fr.guillaumewlt.model;

import java.util.List;

/**
 * Holds the download and identification information for a single Minecraft library.
 * Set by {@link fr.guillaumewlt.parser.LibrariesInfosParser#jsonParser()},
 * which iterates over the {@code libraries} array from the version JSON file.
 * Libraries with OS-specific rules that do not match the current OS are excluded.
 *
 * @param libType         The type of library : {@code "library"} for standard libraries,
 *                        {@code "native"} for OS-specific native libraries.
 *                        Used to distinguish which entries must be extracted into the {@code bin/} directory.
 * @param name            The Maven-style artifact identifier of the library (e.g. {@code "com.mojang:authlib:3.3.39"}),
 *                        read from the version JSON field {@code libraries[i].name}.
 * @param sha1            The SHA-1 hash of the library JAR file used for integrity verification,
 *                        read from {@code libraries[i].downloads.artifact.sha1}.
 * @param path            The relative file path where the library should be saved on disk
 *                        (e.g. {@code "com/mojang/authlib/3.3.39/authlib-3.3.39.jar"}),
 *                        read from {@code libraries[i].downloads.artifact.path}.
 * @param size            The size of the library JAR file in bytes,
 *                        read from {@code libraries[i].downloads.artifact.size}.
 * @param url             The download URL for the library JAR file,
 *                        read from {@code libraries[i].downloads.artifact.url}.
 * @param extractExcludes For native libraries only: the list of entry path prefixes to skip when
 *                        extracting the JAR into the {@code bin/} directory
 *                        (e.g. {@code ["META-INF/"]}), read from {@code libraries[i].extract.exclude}.
 *                        {@code null} for standard (non-native) libraries.
 *                        An empty list means the native library has no exclusion rules.
 */
public record LibraryInfos(
        String libType,
        String name,
        String sha1,
        String path,
        long size,
        String url,
        List<String> extractExcludes
) {
}
