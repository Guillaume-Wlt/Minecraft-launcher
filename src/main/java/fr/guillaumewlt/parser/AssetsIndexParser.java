package fr.guillaumewlt.parser;

import fr.guillaumewlt.model.assets.AssetsIndex;
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

        ConsoleMessage.ASSETSINDEX_PARSER_ID_MESSAGE.outPrintln(id);
        ConsoleMessage.ASSETSINDEX_PARSER_HASH_MESSAGE.outPrintln(sha1);
        ConsoleMessage.ASSETSINDEX_PARSER_SIZE_MESSAGE.outPrintln(size);
        ConsoleMessage.ASSETSINDEX_PARSER_TOTALSIZE_MESSAGE.outPrintln(totalSize);
        ConsoleMessage.ASSETSINDEX_PARSER_URL_MESSAGE.outPrintln(url);

        return new AssetsIndex(id, sha1, size, totalSize, url);
    }
}
