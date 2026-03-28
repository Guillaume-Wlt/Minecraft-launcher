package fr.guillaumewlt.parser;

import fr.guillaumewlt.model.AssetsIndex;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;

public class AssetsIndexParser {

    private JSONObject assetsIndex;

    public AssetsIndexParser(LauncherContext context) {
        assetsIndex = context.getVersionRawData().assets();

    }

    public AssetsIndex jsonParser() {
        String id = assetsIndex.getString("id");
        String sha1 = assetsIndex.getString("sha1");
        long size = assetsIndex.getLong("size");
        long totalSize = assetsIndex.getLong("totalSize");
        String url = assetsIndex.getString("url");

        System.out.println(ConsoleMessage.ASSETSINDEX_PARSER_ID_MESSAGE.format(id));
        System.out.println(ConsoleMessage.ASSETSINDEX_PARSER_HASH_MESSAGE.format(sha1));
        System.out.println(ConsoleMessage.ASSETSINDEX_PARSER_SIZE_MESSAGE.format(size));
        System.out.println(ConsoleMessage.ASSETSINDEX_PARSER_TOTALSIZE_MESSAGE.format(totalSize));
        System.out.println(ConsoleMessage.ASSETSINDEX_PARSER_URL_MESSAGE.format(url));
        return new AssetsIndex(id, sha1, size, totalSize, url);
    }
}
