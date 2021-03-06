package ru.bulldog.justmap.server.config;

import com.google.gson.JsonObject;

import ru.bulldog.justmap.config.Config;
import ru.bulldog.justmap.config.ConfigWriter;
import ru.bulldog.justmap.config.ConfigKeeper.BooleanEntry;

public class ServerConfig extends Config{
private static ServerConfig instance;
	
	public static ServerConfig get() {
		if (instance == null) {
			instance = new ServerConfig();
		}
		
		return instance;
	}
	
	private ServerConfig() {
		KEEPER.registerEntry("use_game_rules", new BooleanEntry(ServerParams.useGameRules, (b) -> ServerParams.useGameRules = b, () -> ServerParams.useGameRules));
		KEEPER.registerEntry("allow_caves_map", new BooleanEntry(ServerParams.allowCavesMap, (b) -> ServerParams.allowCavesMap = b, () -> ServerParams.allowCavesMap));
		KEEPER.registerEntry("allow_entities_radar", new BooleanEntry(ServerParams.allowEntities, (b) -> ServerParams.allowEntities = b, () -> ServerParams.allowEntities));
		KEEPER.registerEntry("allow_hostile_radar", new BooleanEntry(ServerParams.allowHostile, (b) -> ServerParams.allowHostile = b, () -> ServerParams.allowHostile));
		KEEPER.registerEntry("allow_creatures_radar", new BooleanEntry(ServerParams.allowCreatures, (b) -> ServerParams.allowCreatures = b, () -> ServerParams.allowCreatures));
		KEEPER.registerEntry("allow_players_radar", new BooleanEntry(ServerParams.allowPlayers, (b) -> ServerParams.allowPlayers = b, () -> ServerParams.allowPlayers));
		
		JsonObject config = ConfigWriter.load();
		if (config.size() > 0) {
			KEEPER.fromJson(config);
		} else {
			ConfigWriter.save(KEEPER.toJson());
		}
	}

	@Override
	public void saveChanges()  {
		ConfigWriter.save(KEEPER.toJson());
	}
}
