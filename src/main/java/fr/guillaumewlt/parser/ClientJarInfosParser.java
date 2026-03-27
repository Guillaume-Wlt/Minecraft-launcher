package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.ClientJarInfos;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;

public class ClientJarInfosParser {

    private JSONObject clientObj;

    public ClientJarInfosParser(LauncherContext context) {
        if (context.getVersionRawData() == null || context.getVersionRawData().clientData() == null) {
            throw new LauncherException(ConsoleMessage.VERSIONRAWDATA_RECORD_CLIENT_JSON_OBJECT_NULL_ERR.getMessage());
        }
        clientObj = context.getVersionRawData().clientData();
    }

    public ClientJarInfos jsonParser() {
        String downloadURL = clientObj.getString("url");
        String versionHash = clientObj.getString("sha1");
        int clientSize = clientObj.getInt("size");

        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_URL_MESSAGE.format(downloadURL));
        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_HASH_MESSAGE.format(versionHash));
        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_SIZE_MESSAGE.format(clientSize / 1024 / 1024));

        return new ClientJarInfos(downloadURL, versionHash, clientSize);
    }
}
