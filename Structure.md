# Minecraft Launcher - Folder Structure

```
launcher/
├── versions/
│   └── {version}/                          (ex: 1.21.5, 1.4.3)
│       ├── {version}.jar                   Client JAR (game executable)
│       └── {version}.json                  Version JSON (libraries, assets, args...)
│
├── libraries/
│   └── {groupId}/{artifactId}/{version}/   Maven path structure
│       └── {artifactId}-{version}.jar      Library JAR
│                                           (ex: com/google/gson/gson/2.11.0/gson-2.11.0.jar)
│
├── assets/
│   ├── indexes/
│   │   └── {assets-id}.json               Asset index JSON (ex: pre-1.6.json, 24.json)
│   │                                       Downloaded from assetIndex.url in version JSON
│   └── objects/
│       └── {first-2-chars}/
│           └── {sha1-hash}                Asset file (no extension, identified by hash)
│                                           (ex: objects/ab/ab123...def)
│
├── bin/                                    Runtime binaries / natives
│   └── {version}/
│       └── {native}.dll / .so / .dylib    Extracted native files (LWJGL, jinput...)
│
└── temp/                                   Temporary files (debug, intermediate data)
```

## Notes

- **versions/** : each version is self-contained (jar + json)
- **libraries/** : shared across versions, uses Maven path from `downloads.artifact.path`
- **assets/indexes/** : shared across versions, one index per asset version (ex: `pre-1.6`, `24`)
- **assets/objects/** : shared across versions, each file named by its SHA1 hash
- **bin/** : extracted natives (.dll/.so/.dylib) from classifier JARs, needed at runtime