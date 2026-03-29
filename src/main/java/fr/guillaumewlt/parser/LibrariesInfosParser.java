package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parseur des informations des bibliothèques Minecraft à partir du JSON de version.
 * <p>
 * Cette classe analyse le tableau {@code libraries} du fichier JSON de version Minecraft
 * afin d'extraire les métadonnées (nom, sha1, chemin, taille, URL) de chaque bibliothèque
 * nécessaire au lancement du jeu. Elle gère deux types de bibliothèques :
 * <ul>
 *   <li>Les bibliothèques standard (entrée {@code artifact}) — {@code extractExcludes} vaut {@code null}</li>
 *   <li>Les bibliothèques natives spécifiques à l'OS (entrée {@code natives} / {@code classifiers}) —
 *       le classifier est sélectionné selon l'OS courant. Si sa clé contient le placeholder
 *       {@code ${arch}}, celui-ci est remplacé par l'architecture JVM ({@code "32"} ou {@code "64"}).
 *       {@code extractExcludes} contient les chemins à ignorer lors de l'extraction du JAR natif,
 *       ou une liste vide si le champ {@code extract} est absent.</li>
 * </ul>
 */
public class LibrariesInfosParser {

    private JSONArray libraries;

    /**
     * Construit un {@code LibrariesInfosParser} à partir du contexte du launcher.
     *
     * @param context le contexte du launcher contenant les données brutes de la version,
     *                dont le tableau JSON {@code libraries}
     */
    public LibrariesInfosParser(LauncherContext context) {
        libraries = context.getVersionRawData().librariesData();
    }

    /**
     * Parcourt le tableau JSON des bibliothèques et retourne la liste des {@link LibraryInfos}
     * applicables à la plateforme courante.
     * <p>
     * Pour chaque entrée du tableau :
     * <ol>
     *   <li>Si l'entrée contient un champ {@code artifact}, les métadonnées de la bibliothèque
     *       standard sont extraites. {@code extractExcludes} est {@code null} car ces bibliothèques
     *       ne sont pas extraites.</li>
     *   <li>Sinon, si l'entrée contient un champ {@code natives}, la bibliothèque native
     *       correspondant à l'OS courant est recherchée dans les {@code classifiers}.
     *       Si un champ {@code extract.exclude} est présent, les chemins à ignorer lors
     *       de l'extraction du JAR natif sont collectés dans {@code extractExcludes}.</li>
     * </ol>
     * Dans les deux cas, si la bibliothèque possède des {@code rules}, celles-ci sont évaluées
     * via {@link #shouldAddByRules(JSONArray)} pour déterminer si elle doit être incluse.
     *
     * @return la liste des {@link LibraryInfos} à télécharger pour la version courante
     * @throws LauncherException si une bibliothèque native déclare un champ {@code natives}
     *                           mais qu'aucune clé ne correspond à l'OS courant
     */
    public List<LibraryInfos> jsonParser() {
        List<LibraryInfos> librariesInfos = new ArrayList<>();

        for (int i = 0; i < libraries.length(); i++) {
            JSONObject library = libraries.getJSONObject(i);

            // --- CAS 1 : bibliothèque standard ---
            // Les versions récentes exposent un champ "artifact" contenant directement
            // les métadonnées du JAR (sha1, path, size, url). Les versions plus anciennes
            // peuvent ne pas avoir ce champ, d'où la vérification préalable.
            if (library.getJSONObject("downloads").has("artifact")) {
                boolean shouldAdd = true;

                JSONObject artifact = library.getJSONObject("downloads").getJSONObject("artifact");

                String sha1 = artifact.getString("sha1");
                String path = artifact.getString("path");
                long size = artifact.getLong("size");
                String url = artifact.getString("url");

                String name = library.getString("name");

                // Certaines bibliothèques standard ont des règles d'inclusion selon l'OS.
                // Si aucune règle n'est présente, la bibliothèque est incluse par défaut.
                if (library.has("rules")) {
                    shouldAdd = shouldAddByRules(library.getJSONArray("rules"));
                }
                if (shouldAdd) {
                    // extractExcludes est null : les bibliothèques standard ne sont pas extraites
                    librariesInfos.add(new LibraryInfos(name, sha1, path, size, url, null));
                }

            // --- CAS 2 : bibliothèque native ---
            // --- CAS 2 : bibliothèque native ---
            // Les bibliothèques natives sont des JARs contenant du code natif (.dll, .so, .dylib)
            // spécifiques à l'OS. Elles sont référencées via "natives" + "classifiers" plutôt
            // qu'un simple "artifact", car chaque OS a son propre fichier.
            } else if (library.has("natives")) {
                boolean shouldAdd = true;

                // Récupère l'architecture JVM (32 ou 64 bits). Certaines natives utilisent
                // un placeholder "${arch}" dans leur clé classifier qu'il faut résoudre.
                String arch = System.getProperty("sun.arch.data.model", "64");

                // On cherche la clé classifier correspondant à l'OS courant
                // ex : "windows" → "natives-windows" ou "natives-windows-${arch}"
                String usableNativeLib = null;

                JSONObject natives = library.getJSONObject("natives");
                for (String key : natives.keySet()) {
                    if (key.contains(LauncherUtils.currentOs())) {
                        usableNativeLib = natives.getString(key);
                        // String est immuable en Java : replace() retourne une nouvelle chaîne,
                        // il faut donc réassigner le résultat.
                        if (usableNativeLib.contains("${arch}")) {
                            usableNativeLib = usableNativeLib.replace("${arch}", arch);
                        }
                    }
                }

                if (library.getJSONObject("downloads").has("classifiers")) {
                    JSONObject classifiers = library.getJSONObject("downloads").getJSONObject("classifiers");

                    // Si aucune clé native ne correspond à l'OS courant dans "natives", c'est une erreur
                    // inattendue (la lib déclare des natives mais pas pour notre OS).
                    if (usableNativeLib == null) {
                        throw new LauncherException("");
                    }

                    // optJSONObject retourne null au lieu de lever une exception si la clé est absente.
                    // Cela couvre le cas où la lib n'a pas de classifier pour l'OS courant
                    // (ex : une lib uniquement macOS alors qu'on est sur Windows).
                    JSONObject nativeInfos = classifiers.optJSONObject(usableNativeLib);
                    if (nativeInfos == null) {
                        continue; // pas de classifier pour l'OS courant → on ignore cette lib
                    }

                    String path = nativeInfos.getString("path");
                    String sha1 = nativeInfos.getString("sha1");
                    long size = nativeInfos.getLong("size");
                    String url = nativeInfos.getString("url");

                    String name = library.getString("name");

                    // Le champ "extract" indique quels fichiers ignorer lors de l'extraction du JAR natif
                    // dans le dossier "natives/". Typiquement on exclut "META-INF/" qui ne contient
                    // que des métadonnées inutiles au jeu.
                    List<String> excludes = new ArrayList<>();
                    if (library.has("extract")) {
                        JSONArray excludeArray = library.getJSONObject("extract").optJSONArray("exclude");
                        if (excludeArray != null) {
                            for (int j = 0; j < excludeArray.length(); j++) {
                                excludes.add(excludeArray.getString(j));
                            }
                        }
                    }

                    // Même logique que pour les libs standard : on vérifie les règles OS si elles existent.
                    if (library.has("rules")) {
                        shouldAdd = shouldAddByRules(library.getJSONArray("rules"));
                    }
                    if (shouldAdd) {
                        System.out.println(name + " >> " + sha1 + ", " +  path + ", " + size + ", " + url + ", " + excludes);
                        librariesInfos.add(new LibraryInfos(name, sha1, path, size, url, excludes));
                    }
                }
            }
        }
        return librariesInfos;
    }

    /**
     * Évalue un tableau de règles JSON pour déterminer si une bibliothèque doit être incluse.
     * <p>
     * Chaque règle possède une {@code action} ({@code "allow"} ou {@code "disallow"}) et
     * optionnellement un champ {@code os} indiquant le système d'exploitation ciblé.
     * <ul>
     *   <li>Si la règle cible un OS spécifique, elle n'est appliquée que si l'OS courant correspond.</li>
     *   <li>Si la règle ne cible pas d'OS, elle s'applique à toutes les plateformes.</li>
     * </ul>
     * Les règles sont évaluées dans l'ordre ; la dernière règle applicable détermine le résultat.
     *
     * @param rules le tableau JSON des règles associées à une bibliothèque
     * @return {@code true} si la bibliothèque doit être ajoutée selon les règles,
     *         {@code false} sinon
     */
    private boolean shouldAddByRules(JSONArray rules) {
        boolean shouldAdd = false;
        for (int j = 0; j < rules.length(); j++) {
            JSONObject rule = rules.getJSONObject(j);
            String action = rule.getString("action");
            if (rule.has("os")) {
                String osName = rule.getJSONObject("os").getString("name");
                if (LauncherUtils.currentOs().equals(osName)) {
                    shouldAdd = action.equals("allow");
                }
            } else {
                shouldAdd = action.equals("allow");
            }
        }
        return shouldAdd;
    }
}
