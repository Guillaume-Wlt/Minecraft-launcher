package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.ClientJarInfosUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import org.json.JSONObject;

public class ClientJarInfosParser extends Parsers{

    private JSONObject clientObj;

    public ClientJarInfosParser() {
        if (ClientJarInfosUtils.getClientJarInfos() == null) {
            throw new LauncherException(ConsoleMessage.CLIENTJARINFOS_UTILS_CLIENT_JAR_INFOS_OBJECT_NULL_ERR.getMessage());
        }
        clientObj = ClientJarInfosUtils.getClientJarInfos();
    }

    @Override
    public void jsonParser() {
        String downloadURL = clientObj.getString("url");
        ClientJarInfosUtils.setSelectedClientJarURL(downloadURL); // set URL for selected client

        String versionHash = clientObj.getString("sha1");
        ClientJarInfosUtils.setSelectedClientHash(versionHash); // set Hash for selected client (should correspond once download is finish)

        int clientSize = clientObj.getInt("size");
        ClientJarInfosUtils.setSelectedClientSize(clientSize); // set Total Size for selected client (should correspond once download is finish)

        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_URL_MESSAGE.format(downloadURL));
        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_HASH_MESSAGE.format(versionHash));
        System.out.println(ConsoleMessage.CLIENT_JAR_INFOS_PARSER_SIZE_MESSAGE.format(clientSize / 1024 / 1024));
    }
}
