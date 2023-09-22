package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanRespCommand extends AClanCommand {

	public ClanRespCommand() {
		super("resp", new String[]{"odrodzenie"});
	}

	@Override
	public boolean canUse(Player sender) {
		if(!GameManager.get().getState().equals(GameState.RUN))
			return false;
		IGame game = GameManager.get().getGame();
		if(game == null || !game.canResp(sender))
			return false;
		if(GameManager.get().getRespTasks().containsKey(sender))
			return false;
		
		BossFightGame bossFight = (BossFightGame) game;
		
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(bossFight.getClans().contains(clan));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		GameManager.get().startRespTask(sender);
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan odrodzenie §7- Odrodz sie ponownie na arenie turnieju klanowego!");
	}

}
