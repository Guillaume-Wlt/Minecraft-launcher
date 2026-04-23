package fr.guillaumewlt.workflow;

import fr.guillaumewlt.models.*;
import fr.guillaumewlt.models.assets.AssetInfos;
import fr.guillaumewlt.models.assets.AssetsIndex;
import fr.guillaumewlt.models.directory.LauncherDirs;
import fr.guillaumewlt.ui.panels.BackgroundPanel;
import fr.guillaumewlt.ui.panels.BottomPanel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private BottomPanel bottomPanel;
    private BackgroundPanel backgroundPanel;
    private CountDownLatch latch;
    private JFrame mainWindow;
}
