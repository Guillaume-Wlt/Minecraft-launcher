package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.*;
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

    private List<LibraryInfos> librariesInfos;
    private List<AssetInfos> assetsInfos;
}
