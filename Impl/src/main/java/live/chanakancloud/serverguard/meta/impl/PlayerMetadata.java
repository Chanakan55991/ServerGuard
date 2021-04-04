package live.chanakancloud.serverguard.meta.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import live.chanakancloud.serverguard.meta.IMetadata;
import live.chanakancloud.serverguard.player.PlayerData;
import lombok.NonNull;

/**
 * @author Braydon
 */
public class PlayerMetadata implements IMetadata {
    /**
     * Get the name of the metadata element
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "player";
    }

    /**
     * Get the json element for the provided player data.
     *
     * @param playerData the player data to get the json element for.
     * @return the json element
     * @see JsonElement
     * @see PlayerData
     */
    @Override
    public JsonElement getJsonElement(@NonNull PlayerData playerData) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", playerData.getUuid().toString());
        jsonObject.addProperty("name", playerData.getBukkitPlayer().getName());
        jsonObject.addProperty("playerDataCreated", playerData.getTimeCreated());
        return jsonObject;
    }
}