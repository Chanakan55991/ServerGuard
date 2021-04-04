package live.chanakancloud.serverguard.meta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import live.chanakancloud.serverguard.meta.impl.PlayerMetadata;
import live.chanakancloud.serverguard.meta.impl.ServerMetadata;
import live.chanakancloud.serverguard.meta.impl.ViolationMetadata;
import live.chanakancloud.serverguard.player.PlayerData;
import lombok.NonNull;

/**
 * @author Braydon
 */
public class MetadataManager {
    /**
     * The array of metadata elements to use when creating metadata for player data
     *
     * @see IMetadata
     * @see PlayerData
     */
    private static final IMetadata[] METADATA_ELEMENTS = new IMetadata[] {
            new PlayerMetadata(),
            new ViolationMetadata(),
            new ServerMetadata()
    };
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    /**
     * Create the metadata json for the provided player data.
     *
     * @param playerData the player data to create the metadata json for
     * @return the metadata json
     * @see IMetadata
     * @see PlayerData
     */
    public static String getMetadataJson(@NonNull PlayerData playerData) {
        JsonObject jsonElement = new JsonObject();
        for (IMetadata metadataElement : METADATA_ELEMENTS)
            jsonElement.add(metadataElement.getName(), metadataElement.getJsonElement(playerData));
        return GSON.toJson(jsonElement);
    }
}