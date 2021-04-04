package live.chanakancloud.serverguard.meta;

import com.google.gson.JsonElement;

import live.chanakancloud.serverguard.player.PlayerData;
import lombok.NonNull;

/**
 * @author Braydon
 */
public interface IMetadata {
    /**
     * Get the name of the metadata element
     *
     * @return the name
     */
    String getName();

    /**
     * Get the json element for the provided player data.
     *
     * @param playerData the player data to get the json element for.
     * @return the json element
     * @see JsonElement
     * @see PlayerData
     */
    JsonElement getJsonElement(@NonNull PlayerData playerData);
}