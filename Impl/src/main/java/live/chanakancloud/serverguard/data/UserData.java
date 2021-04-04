package live.chanakancloud.serverguard.data;

import lombok.RequiredArgsConstructor;
import live.chanakancloud.serverguard.check.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserData {
	public static Map<String, UserData> dataMap = Collections.synchronizedMap(new HashMap<>());
	
	public final UUID uuid;
	private Player player;
	public CheckManager checkManager;
	
	public UserData(UUID uuid) {
		this.uuid = uuid;
	}
	
	public static UserData getData(UUID uuid) {
		return dataMap.computeIfAbsent(uuid.toString(), key -> {
			UserData data = new UserData(uuid);
			dataMap.put(key,  data);
			
			return data;
		});
	}
	
	public Player getPlayer() {
		if(player == null) this.player = Bukkit.getPlayer(uuid);
		if(player == null) dataMap.remove(uuid.toString());
		
		return player;
	}
}
