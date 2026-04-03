package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AssetsInfosParser {

    private final LauncherContext context;
    private String assetsIndexPath;

    public AssetsInfosParser(LauncherContext context) {
        this.context = context;
        if (context.getAssetsIndex() == null || context.getAssetsIndex().id() == null) {
            throw new LauncherException(ConsoleMessage.ASSETSINDEX_RECORD_ID_NULL_ERR.getMessage());
        }
        assetsIndexPath = context.getLauncherDirs().assetsIndexesDir().path() + context.getAssetsIndex().id() + ".json";
    }

    public List<AssetInfos> jsonParser() {
        List<AssetInfos> assetsInfos = new ArrayList<>();
        System.out.println(ConsoleMessage.ASSETSINFOS_PARSER_LOADING_POINT_PATH_MESSAGE.format(assetsIndexPath));
        try {
            String content = Files.readString(Path.of(assetsIndexPath));
            JSONObject jsonObject = new JSONObject(content);

            // Handle virtual assets for legacy versions
            boolean isVirtual = jsonObject.optBoolean("virtual", false)
                                || jsonObject.optBoolean("map_to_resources", false);
            context.setVirtualAssets(isVirtual);

            JSONObject objects = jsonObject.getJSONObject("objects");
            for (String key : objects.keySet()) {
                JSONObject asset = objects.getJSONObject(key);
                String name = key;
                String hash = asset.getString("hash");
                long size = asset.getLong("size");
                assetsInfos.add(new AssetInfos(name, hash, size));
            }
        } catch (IOException e) {
            throw new LauncherException(ConsoleMessage.ASSETSINFOS_PARSER_LOADING_POINT_PARSING_ERR.format(assetsIndexPath), e);
        }
        return assetsInfos;
    }
}
