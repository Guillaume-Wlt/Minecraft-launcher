package fr.guillaumewlt.processing.json;

import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.URLUtils;
import org.json.JSONObject;

public class ClientJarInfosProcess {

    private JSONObject clientObj;

    public ClientJarInfosProcess(JSONObject clientObj) {
        this.clientObj = clientObj;
    }

    public void processClientJarInfos() {
        String downloadURL = clientObj.getString("url");
        URLUtils.setSelectedClientJarURL(downloadURL);
        String versionHash = clientObj.getString("sha1");
        LauncherUtils.setSelectedClientHash(versionHash);
        int clientSize = clientObj.getInt("size");
        LauncherUtils.setSelectedClientSize(clientSize);
        System.out.println("Download URL >> " + downloadURL);
        System.out.println("Client Hash (Sha1) >> " + versionHash);
        System.out.println("Client Size >> " + clientSize / 1024 / 1024 + "Mo");
    }
}
