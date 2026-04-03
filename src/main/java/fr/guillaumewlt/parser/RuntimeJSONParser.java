package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.RuntimeRawData;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Analyse le manifest runtime Mojang ({@code runtime_manifest.json}) afin d'extraire
 * les métadonnées de téléchargement du composant JRE requis par la version Minecraft sélectionnée.
 * <p>
 * Le manifest est organisé par clé OS/architecture (ex. {@code "windows-x64"}), puis
 * par nom de composant (ex. {@code "jre-legacy"}, {@code "java-runtime-gamma"}).
 * Le composant à utiliser est lu depuis {@link fr.guillaumewlt.model.VersionRawData#javaVersion()},
 * lui-même extrait du champ {@code javaVersion.component} du JSON de version Minecraft.
 */
public class RuntimeJSONParser {

    private final LauncherContext context;

    /**
     * Construit un {@code RuntimeJSONParser} à partir du contexte du launcher.
     *
     * @param context le contexte du launcher, utilisé pour accéder aux répertoires,
     *                aux données de la version sélectionnée et au nom du composant JRE requis
     */
    public RuntimeJSONParser(LauncherContext context) {
        this.context = context;
    }

    /**
     * Analyse le manifest runtime local et retourne les métadonnées de téléchargement
     * du composant JRE correspondant à l'OS courant et à la version Minecraft sélectionnée.
     * <p>
     * Chemin de navigation dans le manifest :
     * <pre>
     * {clé-os} → {composant} → [0] → manifest → { sha1, size, url }
     *                                  version  → { name, released }
     * </pre>
     *
     * @return un {@link RuntimeRawData} contenant l'URL du manifest, le SHA-1, la taille,
     *         le nom de version JRE et la date de publication du composant requis
     * @throws LauncherException si le fichier manifest ne peut pas être lu, ou si aucune
     *                           entrée runtime n'est trouvée pour le composant requis sur l'OS courant
     */
    public RuntimeRawData jsonParser() {
        try {
            String content = Files.readString(Path.of(context.getLauncherDirs().runtimeDir().path(), LauncherUtils.getRuntimeName()));
            JSONObject runtimeJSON = new JSONObject(content);

            JSONObject osEntry = runtimeJSON.getJSONObject(LauncherUtils.getRuntimeOSKey());
            String component = context.getVersionRawData().javaVersion();

            JSONArray componentArray = osEntry.getJSONArray(component);
            if (componentArray.isEmpty()) {
                throw new LauncherException(ConsoleMessage.RUNTIMEJSON_PARSER_COMPONENTARRAY_EMPTY_ERR.format(component));
            }
            JSONObject entry = componentArray.getJSONObject(0);
            JSONObject manifest = entry.getJSONObject("manifest");
            JSONObject version = entry.getJSONObject("version");

            String sha1 = manifest.getString("sha1");
            long size = manifest.getLong("size");
            String url = manifest.getString("url");
            String jreName = version.getString("name");
            String jreReleaseDate = version.getString("released");

            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_NAME_MESSAGE.format(component));
            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_HASH_MESSAGE.format(sha1));
            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_SIZE_MESSAGE.format(size));
            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_URL_MESSAGE.format(url));
            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_JRE_NAME_MESSAGE.format(jreName));
            System.out.println(ConsoleMessage.RUNTIMEJSON_PARSER_JRE_RELEASED_DATE_MESSAGE.format(jreReleaseDate));
            return new RuntimeRawData(component, sha1, size, url, jreName, jreReleaseDate);
        } catch (IOException e) {
            throw new LauncherException(ConsoleMessage.RUNTIMEJSON_PARSER_PARSING_ERR.format(e.getMessage()));
        }
    }
}
