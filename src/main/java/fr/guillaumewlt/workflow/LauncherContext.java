package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.ClientJarInfos;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.VersionRawData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LauncherContext {

    private SelectedVersion selectedVersion;
    private ClientJarInfos clientJarInfos;
    private VersionRawData versionRawData;

    private List<LibraryInfos> librariesInfos;
}
