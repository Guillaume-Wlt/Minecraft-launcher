package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.*;
import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.model.assets.AssetsIndex;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.ui.panels.BackgroundPanel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LauncherContext {

    // ----------------------------------------------------------UI Data
    private List<String> versions;
    private String username;
    private String minRam;
    private String maxRam;
    // ----------------------------------------------------------Workflow Data
    private SelectedVersion selectedVersion;
    private ClientJarInfos clientJarInfos;
    private VersionRawData versionRawData;
    private AssetsIndex assetsIndex;
    private LauncherDirs launcherDirs;
    private RuntimeRawData runtimeRawData;

    private List<LibraryInfos> librariesInfos;
    private List<AssetInfos> assetsInfos;
    private List<JREFileInfos> jreFilesInfos;

    // ----------------------------------------------------------Process Builder Data
    private String mainClassPath;
    private String classPath;
    private boolean virtualAssets;
    // ----------------------------------------------------------UI
    private BackgroundPanel backgroundPanel;
}
