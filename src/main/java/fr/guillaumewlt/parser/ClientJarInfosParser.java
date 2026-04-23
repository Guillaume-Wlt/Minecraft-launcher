package fr.guillaumewlt.parser;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.models.ClientJarInfos;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;

public class ClientJarInfosParser {

    private JSONObject clientObj;

    public ClientJarInfosParser(LauncherContext context) {
        if (context.getVersionRawData() == null || context.getVersionRawData().clientData() == null) {
            ConsoleMessage.VERSIONRAWDATA_RECORD_CLIENT_JSON_OBJECT_NULL_ERR.throwException();
        }
        clientObj = context.getVersionRawData().clientData();
    }

    public ClientJarInfos jsonParser() {
        String downloadURL = clientObj.getString("url");
        String versionHash = clientObj.getString("sha1");
        long clientSize = clientObj.getLong("size");

        ConsoleMessage.CLIENT_JAR_INFOS_PARSER_URL_MESSAGE.outPrintln(downloadURL);
        ConsoleMessage.CLIENT_JAR_INFOS_PARSER_HASH_MESSAGE.outPrintln(versionHash);
        ConsoleMessage.CLIENT_JAR_INFOS_PARSER_SIZE_MESSAGE.outPrintln(clientSize / 1024 / 1024);

        return new ClientJarInfos(downloadURL, versionHash, clientSize);
    }
}
