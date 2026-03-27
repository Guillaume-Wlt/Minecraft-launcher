package fr.guillaumewlt.workflow;

import fr.guillaumewlt.model.ClientJarInfo;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.VersionRawData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LauncherContext {
    private SelectedVersion selectedVersion;
    private ClientJarInfo clientJarInfo;
    private VersionRawData versionRawData;
}
