package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.IOException;

/**
 * Étape de lancement du client Minecraft.
 * <p>
 * Construit et exécute la commande Java permettant de démarrer le jeu via {@link ProcessBuilder}.
 * Le processus hérite des flux I/O du launcher (stdout/stderr redirigés vers la console),
 * et le launcher attend la fin du processus avant de se terminer.
 */
public class StartingClientProcess extends Processes {

    /**
     * Construit un {@code StartingClientProcess} à partir du contexte du launcher.
     *
     * @param context le contexte du launcher contenant toutes les informations nécessaires
     *                au lancement (classpath, RAM, username, chemins, version, assets)
     */
    public StartingClientProcess(LauncherContext context) {
        super(context);
    }

    /**
     * Construit la commande de lancement et démarre le client Minecraft.
     * <p>
     * La commande générée suit la structure :
     * <pre>
     * java
     *   -Xmx{maxRam}G -Xms{minRam}M        (limites mémoire JVM)
     *   -Djava.library.path=bin/{version}/  (dossier des natives isolé par version)
     *   -cp {classpath}                     (toutes les bibliothèques + client.jar)
     *   {mainClass}                         (point d'entrée Minecraft, lu depuis le JSON de version)
     *   --username --version --gameDir ...  (arguments du jeu)
     * </pre>
     *
     * @throws fr.guillaumewlt.exceptionhandler.LauncherException si le processus ne peut pas
     *                                                             démarrer ou est interrompu
     */
    @Override
    public void process() {
        try {
            String version = context.getSelectedVersion().selectedVersion();
            String assetsIndex = context.getAssetsIndex().id();

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    // Limites mémoire de la JVM
                    "-Xmx" + context.getMaxRam() + "G",
                    "-Xms" + context.getMinRam() + "M",
                    // Indique à la JVM où trouver les fichiers natifs (.dll/.so/.dylib)
                    // Isolé par version pour éviter les conflits entre natives de versions différentes
                    "-Djava.library.path=" + context.getLauncherDirs().binDir().path() + context.getSelectedVersion().selectedVersion(),
                    // Classpath : toutes les bibliothèques standard + client.jar
                    "-cp", context.getClassPath(),
                    // Point d'entrée du client Minecraft
                    context.getVersionRawData().mainClass(),
                    // Arguments passés au jeu
                    "--username", context.getUsername(),
                    "--version", version,
                    "--gameDir", context.getLauncherDirs().launcherDir().path(),
                    "--assetsDir", context.getLauncherDirs().assetsDir().path(),
                    "--assetIndex", assetsIndex,
                    "--accessToken", "0",
                    "--userType", "legacy"
            );

            // Définit le répertoire de travail du processus (là où le jeu cherche ses fichiers)
            pb.directory(new File(context.getLauncherDirs().launcherDir().path()));
            // Redirige stdout/stderr du jeu vers la console du launcher
            pb.inheritIO();
            // Bloque le launcher jusqu'à la fermeture du jeu
            pb.start().waitFor();

        } catch (IOException | InterruptedException e) {
            stop(e.getMessage(), 1);
        }
    }
}
