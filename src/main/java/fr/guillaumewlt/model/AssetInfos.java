package fr.guillaumewlt.model;

/**
 * Holds the metadata for a single Minecraft game asset (texture, sound, language file, etc.).
 * Set by {@link fr.guillaumewlt.parser.AssetsInfosParser#jsonParser()},
 * which reads the asset objects from the asset index JSON file.
 *
 * @param hash The SHA-1 hash of the asset file, used both as a unique identifier
 *             and for integrity verification. The first two characters of the hash
 *             also serve as the subdirectory name in the assets objects folder.
 * @param size The size of the asset file in bytes.
 */
public record AssetInfos(
    String hash,
    long size
) {}
