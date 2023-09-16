package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.Tools.Pair;

public class ClanPartyCommand extends AClanCommand {

	public ClanPartyCommand() {
		super("party", new String[]{"druzyna"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.PARTY));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		if(result.isTrue())
			me.Vark123.EpicParty.PlayerPartySystem.PlayerManager.get().getPartyPlayer(sender)
				.ifPresentOrElse(pp -> {
					pp.getParty().ifPresent(party -> {
						result.setValue(party.getLeader().equals(pp));
					});
				}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		
		PartyPlayer pp = me.Vark123.EpicParty.PlayerPartySystem.PlayerManager.get().getPartyPlayer(sender).get();
		MutableObject<Party> party = new MutableObject<>();
		if(pp.getParty().isPresent())
			party.setValue(pp.getParty().get());
		if(party.getValue() != null && !party.getValue().getLeader().equals(pp)) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie jestes liderem party! Nie mozesz zapraszac graczy!");
			return false;
		}
		
		clan.getMembers().keySet().stream()
			.map(cp -> cp.toBukkitPlayer())
			.filter(p -> p.isOnline() && !p.getUniqueId().equals(sender.getUniqueId()))
			.map(p -> p.getPlayer())
			.forEach(p -> {
				me.Vark123.EpicParty.PlayerPartySystem.PlayerManager.get().getPartyPlayer(p)
					.ifPresent(pTarget -> {
						if(pTarget.getParty().isPresent() 
								&& party.getValue() != null
								&& party.getValue().equals(pTarget.getParty().get())) {
							return;
						}
						pTarget.getPartyInvitations().keySet().stream()
							.filter(task -> !task.isCancelled()
									&& pTarget.getPartyInvitations().get(task).getKey().equals(pp))
							.findFirst()
							.ifPresentOrElse(task -> {
								Pair<PartyPlayer, Party> pair = pTarget.getPartyInvitations().get(task);
								if((party.getValue() == null && pair.getValue() == null)
										|| party.getValue().equals(pair.getValue())) {
									return;
								} else {
									pTarget.cancelInvitation(task);
									pTarget.sendInvitation(pp, party.getValue());
									return;
								}
							}, () -> {
								pTarget.sendInvitation(pp, party.getValue());
							});
					});
			});
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan party §7- Zapros wszystkich czlonkow klanu do party");
	}

}
