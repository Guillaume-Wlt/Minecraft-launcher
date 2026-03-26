# Minecraft Launcher

Un launcher Minecraft développé en Java (Swing), permettant de télécharger et lancer des versions du jeu via l'API officielle de Mojang.

## Fonctionnalités

- Récupération du manifeste officiel Mojang listant toutes les versions disponibles
- Sélection interactive de la version à télécharger
- Téléchargement du fichier JSON de la version sélectionnée
- Téléchargement du client Minecraft (`.jar`) avec vérification d'intégrité (SHA-1)
- Affichage de la progression du téléchargement
- Gestion des dossiers du launcher (création automatique si absents)

## Stack technique

| Technologie | Version | Rôle |
|---|---|---|
| Java | 21 | Langage principal |
| Maven | - | Gestion de build |
| Lombok | 1.18.44 | Réduction du boilerplate |
| org.json | 20251224 | Parsing JSON |
| Maven Shade Plugin | 3.6.0 | Génération du fat JAR |

## Architecture

Le projet est organisé en couches distinctes :

```
fr.guillaumewlt/
├── workflow/         # Orchestration du workflow (machine à états)
├── processing/
│   └── steps/        # Étapes de traitement (init, download, interpret...)
├── downloads/        # Logique de téléchargement des fichiers
├── parser/           # Parsing des réponses JSON Mojang
├── utils/            # Utilitaires (chemins, URLs, messages console...)
└── exceptionhandler/ # Gestion des erreurs
```

### Workflow

Le launcher suit un enchaînement d'étapes piloté par `WorkflowRunner` :

```
INIT
 └─> DOWNLOAD_MANIFEST
      └─> INTERPRET_MANIFEST        (sélection de la version)
           └─> DOWNLOAD_VERSION_JSON
                └─> INTERPRET_VERSION_JSON
                     └─> INTERPRET_CLIENT_JAR_INFOS
                          └─> DOWNLOAD_CLIENT_JAR
                               └─> DOWNLOAD_VERSION_LIBRARIES
                                    └─> END
```

## Structure des dossiers générés

```
target/launcher/
├── bin/          # Binaires du jeu
├── temp/         # Fichiers temporaires
└── versions/
    └── <version>/
        ├── <version>.json
        └── <version>.jar
```

## Lancer le projet

### Prérequis

- JDK 21+
- Maven

### Build

```bash
mvn package
```

### Exécution

```bash
java -jar target/minecraft-launcher.jar
```

## État du projet

| Fonctionnalité | Statut |
|---|---|
| Téléchargement du manifest | Terminé |
| Sélection de version | Terminé |
| Téléchargement du client `.jar` | Terminé |
| Vérification intégrité SHA-1 | Terminé |
| Téléchargement des librairies | En cours |
| Lancement du jeu | À faire |
| Interface graphique (Swing) | À faire |
| Authentification Mojang | À faire |