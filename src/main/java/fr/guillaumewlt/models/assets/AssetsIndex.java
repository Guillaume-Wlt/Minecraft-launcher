package fr.guillaumewlt.models.assets;

import fr.guillaumewlt.models.VersionRawData;

/**
 * Holds the metadata for the Minecraft asset index, which is a JSON file listing
 * all game assets (textures, sounds, language files, etc.) for a given version.
 * Set by {@link fr.guillaumewlt.parser.AssetsIndexParser#jsonParser()},
 * which reads the {@code assetIndex} object stored in {@link VersionRawData#assets()}.
 *
 * @param id        The asset index identifier (e.g. {@code "1.20"}), shared across
 *                  Minecraft versions that use the same set of assets.
 *                  Read from the version JSON field {@code assetIndex.id}.
 * @param sha1      The SHA-1 hash of the asset index JSON file, used for integrity verification.
 *                  Read from the version JSON field {@code assetIndex.sha1}.
 * @param size      The size of the asset index JSON file itself in bytes.
 *                  Read from the version JSON field {@code assetIndex.size}.
 * @param totalSize The combined size in bytes of all individual asset files referenced
 *                  by this index. Read from the version JSON field {@code assetIndex.totalSize}.
 * @param url       The download URL for the asset index JSON file.
 *                  Read from the version JSON field {@code assetIndex.url}.
 */
public record AssetsIndex(
        String id,
        String sha1,
        long size,
        long totalSize,
        String url
) {}
