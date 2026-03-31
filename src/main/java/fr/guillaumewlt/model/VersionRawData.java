package fr.guillaumewlt.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Holds the raw JSON data extracted from the Minecraft version JSON file.
 * Set by {@link fr.guillaumewlt.parser.VersionJSONParser#jsonParser()}.
 * This record acts as an intermediate data container passed through
 * {@link fr.guillaumewlt.workflow.LauncherContext} to the downstream parsers.
 *
 * @param clientData    The raw JSON object describing the client JAR download info,
 *                      extracted from the version JSON field {@code downloads.client}.
 *                      Consumed by {@link fr.guillaumewlt.parser.ClientJarInfosParser}.
 * @param librariesData The raw JSON array listing all libraries required by this version,
 *                      extracted from the version JSON field {@code libraries}.
 *                      Consumed by {@link fr.guillaumewlt.parser.LibrariesInfosParser}.
 * @param assets        The raw JSON object describing the asset index metadata,
 *                      extracted from the version JSON field {@code assetIndex}.
 *                      Consumed by {@link fr.guillaumewlt.parser.AssetsIndexParser}.
 * @param mainClass     The fully qualified main class name used as the JVM entry point,
 *                      extracted from the version JSON field {@code mainClass}.
 *                      Defaults to {@code "net.minecraft.client.main.Main"} if absent.
 *                      Consumed by {@link fr.guillaumewlt.processing.steps.StartingClientProcess}.
 * @param javaVersion   The major Java version required to run this Minecraft version,
 *                      extracted from the version JSON field {@code javaVersion.majorVersion}.
 *                      Defaults to {@code 8} if the field is absent (legacy versions).
 *                      Consumed by {@link fr.guillaumewlt.processing.steps.StartingClientProcess}.
 */
public record VersionRawData(
        JSONObject clientData,
        JSONArray librariesData,
        JSONObject assets,
        String mainClass,
        String javaVersion
) {}
