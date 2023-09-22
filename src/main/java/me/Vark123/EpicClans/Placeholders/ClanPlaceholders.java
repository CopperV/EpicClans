package me.Vark123.EpicClans.Placeholders;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ClanPlaceholders extends PlaceholderExpansion {

	@Override
	public @NotNull String getAuthor() {
		return "Vark123";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "epicclans";
	}

	@Override
	public @NotNull String getVersion() {
		return Main.getInst().getDescription().getVersion();
	}

	@Override
	public String onRequest(OfflinePlayer p, @NotNull String identifier) {
		StringBuilder builder = new StringBuilder("");
		PlayerManager.get().getByUID(p.getUniqueId())
			.ifPresent(cPlayer -> {
				cPlayer.getClan()
					.ifPresent(clan -> {
						switch(identifier.toLowerCase()) {
							case "id":
								builder.append(clan.getId());
								break;
							case "rawname":
								builder.append(clan.getName());
								break;
							case "name":
								builder.append(clan.getColor()+clan.getId());
								break;
							case "bettername":
								builder.append("§7[§r"+clan.getColor()+clan.getId()+"§r§7]");
								break;
							case "roleid":
								builder.append(clan.getMembers().get(cPlayer).getId());
								break;
							case "rolename":
								builder.append(clan.getMembers().get(cPlayer).getDisplay());
								break;
							case "rolepriority":
								builder.append(clan.getMembers().get(cPlayer).getPriority());
								break;
						}
					});
			});
		return builder.toString();
	}

}
