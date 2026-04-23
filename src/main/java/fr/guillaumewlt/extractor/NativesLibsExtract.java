package fr.guillaumewlt.extractor;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.LibraryInfos;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracteur des bibliothèques natives Minecraft.
 * <p>
 * Un JAR natif est un fichier ZIP contenant du code natif spécifique à l'OS
 * (ex : {@code .dll} sur Windows, {@code .so} sur Linux, {@code .dylib} sur macOS).
 * Ces fichiers sont extraits dans un sous-dossier isolé par version ({@code bin/<version>/})
 * afin d'éviter les conflits entre versions qui embarquent des natives différentes
 * (ex : deux versions de {@code lwjgl.dll} incompatibles).
 * La JVM localise ces fichiers au démarrage via l'argument {@code -Djava.library.path=bin/<version>/}.
 */
public class NativesLibsExtract {

    private final LauncherContext context;

    /**
     * Construit un {@code NativesLibsExtract} à partir du contexte du launcher.
     *
     * @param context le contexte du launcher, utilisé pour accéder à la liste des
     *                bibliothèques et aux chemins des dossiers {@code libraries/} et {@code bin/}
     */
    public NativesLibsExtract(LauncherContext context) {
        this.context = context;
    }

    /**
     * Parcourt la liste des bibliothèques et extrait le contenu des JARs natifs
     * dans le dossier {@code bin/<version>/}.
     * <p>
     * Seules les entrées de type {@code "native"} sont traitées. Pour chaque JAR natif :
     * <ol>
     *   <li>Le JAR est ouvert comme un ZIP via {@link ZipInputStream}.</li>
     *   <li>Chaque entrée ({@link ZipEntry}) est examinée :
     *     <ul>
     *       <li>Les entrées de type dossier sont ignorées.</li>
     *       <li>Les entrées dont le nom commence par un chemin présent dans
     *           {@code extractExcludes} (ex : {@code "META-INF/"}) sont ignorées.</li>
     *       <li>Les autres entrées sont copiées dans {@code bin/<version>/} via {@link Files#copy}.
     *           Le dossier de destination est créé automatiquement si absent.</li>
     *     </ul>
     *   </li>
     * </ol>
     *
     * @throws LauncherException si une erreur d'I/O survient lors de la lecture ou de l'extraction
     */
    public void jarExtract() {
        List<LibraryInfos> libraries = context.getLibrariesInfos();
        for (LibraryInfos library : libraries) {
            // On ne traite que les bibliothèques natives
            if (library.libType().equals("native")) {
                // try-with-resources : le ZipInputStream est fermé automatiquement à la fin du bloc
                try (ZipInputStream zip = new ZipInputStream(new FileInputStream(context.getLauncherDirs().librariesDir().path() + library.path()))) {
                    ZipEntry entry;
                    // getNextEntry() avance à l'entrée suivante du ZIP, retourne null quand il n'y en a plus
                    while ((entry = zip.getNextEntry()) != null) {
                        // Variable locale nécessaire car entry est réassignée à chaque tour :
                        // Java interdit d'utiliser une variable non-finale dans un lambda
                        String entryName = entry.getName();

                        // Les entrées de type dossier n'ont pas de contenu à écrire
                        if (entry.isDirectory()) {
                            zip.closeEntry();
                            continue;
                        }

                        // Vérifie si l'entrée doit être exclue selon les règles du champ "extract"
                        // (ex : "META-INF/" contient des métadonnées inutiles au jeu)
                        // "entryName::startsWith" est une method reference, équivalent au lambda :
                        //     exclude -> entryName.startsWith(exclude)
                        // anyMatch() parcourt la liste et retourne true dès qu'un élément correspond
                        boolean excluded = library.extractExcludes().stream()
                                .anyMatch(entryName::startsWith);
                        if (excluded) {
                            zip.closeEntry();
                            continue;
                        }

                        // Copie le contenu de l'entrée dans bin/
                        // Files.copy() accepte directement le ZipInputStream car il étend InputStream
                        // REPLACE_EXISTING écrase le fichier s'il existe déjà
                        Path destination = Path.of(context.getLauncherDirs().binDir().path(), context.getSelectedVersion().selectedVersion(), entryName);
                        Files.createDirectories(destination.getParent());
                        Files.copy(zip, destination, StandardCopyOption.REPLACE_EXISTING);

                        zip.closeEntry();
                    }
                } catch (IOException e) {
                    throw new LauncherException("");
                }
            }
        }
    }
}
