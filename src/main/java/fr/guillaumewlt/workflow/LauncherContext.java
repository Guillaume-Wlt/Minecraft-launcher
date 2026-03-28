package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.ClientJarInfos;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.VersionRawData;
import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.model.assets.AssetsIndex;
import fr.guillaumewlt.model.directory.LauncherDirs;
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
