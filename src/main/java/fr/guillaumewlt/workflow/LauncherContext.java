package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.*;
import fr.guillaumewlt.model.directories.LauncherDirs;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LauncherContext {

    private SelectedVersion selectedVersion;
    private ClientJarInfos clientJarInfos;
    private VersionRawData versionRawData;
    private AssetsIndex assetsIndex;
    private LauncherDirs launcherDirs;

    private List<LibraryInfos> librariesInfos;
    private List<AssetInfos> assetsInfos;
}
