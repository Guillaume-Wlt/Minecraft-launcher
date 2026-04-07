package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.utils.ConsoleUtils;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Étape de lancement du client Minecraft.
 * <p>
 * Construit et exécute la commande Java permettant de démarrer le jeu via {@link ProcessBuilder}.
 * Les flux stdout et stderr du processus sont lus dans deux threads dédiés et redirigés
 * simultanément vers la console système et vers la {@link fr.guillaumewlt.ui.windows.ConsoleWindow} du launcher.
 * Le thread du workflow est bloqué via {@code process.waitFor()} jusqu'à la fermeture du jeu.
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
     * L'exécutable Java est résolu dynamiquement depuis le JRE téléchargé via Mojang
     * ({@code runtime/<component>/bin/java[.exe]}), en fonction du composant indiqué dans
     * le JSON de version et de l'OS courant (Windows : {@code java.exe}, autre : {@code java}).
     * <p>
     * La commande générée suit la structure :
     * <pre>
     * runtime/&lt;component&gt;/bin/java[.exe]
     *   -Xmx{maxRam}G -Xms{minRam}M        (limites mémoire JVM)
     *   -Djava.library.path=bin/{version}/  (dossier des natives isolé par version)
     *   -cp {classpath}                     (toutes les bibliothèques + client.jar)
     *   {mainClass}                         (point d'entrée Minecraft, lu depuis le JSON de version)
     *   --username --version --gameDir ...  (arguments du jeu, dont --userProperties {} requis par 1.7/1.8)
     * </pre>
     *
     * @throws fr.guillaumewlt.exceptionhandler.LauncherException si le processus ne peut pas
     *                                                             démarrer ou est interrompu
     */
    @Override
    public void process() {
        try {
            String javaExecutable = System.getProperty("os.name").toLowerCase().contains("win") ? "java.exe" : "java";
            String jrePath = context.getLauncherDirs().runtimeDir().path() + context.getVersionRawData().javaVersion() + "/bin/" + javaExecutable;
            String version = context.getSelectedVersion().selectedVersion();
            String assetsIndex = context.getAssetsIndex().id();

            ProcessBuilder pb = new ProcessBuilder(
                    jrePath,
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
                    "--assetsDir", context.isVirtualAssets() ? context.getLauncherDirs().assetsDir().path() + "virtual/legacy/" : context.getLauncherDirs().assetsDir().path(),
                    "--assetIndex", assetsIndex,
                    "--accessToken", "0",
                    "--userType", "legacy",
                    "--userProperties", "{}"
            );

            // Définit le répertoire de travail du processus (là où le jeu cherche ses fichiers)
            pb.directory(new File(context.getLauncherDirs().launcherDir().path()));
            // Redirige stdout/stderr du jeu vers la console du launcher
            pb.redirectErrorStream(false);
            // Process créer à partir du démarrage du processBuilder
            Process process = pb.start();

            ProgressBarUtils.update(100, "Client Started - Good Game!");

            // Récupère les outputs pour les display dans la console System et dans la console du launcher
            new Thread(() -> {
               new BufferedReader(new InputStreamReader(process.getInputStream()))
                   .lines()
                   .forEach(line -> {
                       System.out.println(line);
                       ConsoleUtils.out.println(line);
                   });
            }).start();
            // Récupère les erreurs pour les display dans la console System et dans la console du launcher
            new Thread(() -> {
                new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines()
                    .forEach(line -> {
                        System.err.println(line);
                        ConsoleUtils.err.println(line);
                    });
            }).start();

            process.waitFor(); // Attend que le process se ferme.
        } catch (IOException | InterruptedException e) {
            stop(e.getMessage(), 1);
        }
    }
}
