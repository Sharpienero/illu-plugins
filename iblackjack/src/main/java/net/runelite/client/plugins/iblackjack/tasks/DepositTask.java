package net.runelite.client.plugins.iblackjack.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iblackjack.iBlackjackPlugin;
import net.runelite.client.plugins.iutils.BankUtils;
import net.runelite.client.plugins.iutils.scripts.ReflectBreakHandler;

import javax.inject.Inject;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.timeout;

public class DepositTask extends Task {
    @Inject
    private iBlackjackPlugin plugin;

    @Inject
    public ReflectBreakHandler chinBreakHandler;

    @Inject
    private BankUtils bank;

    @Override
    public boolean validate() {
        return !inventory.isEmpty() && bank.isOpen();
    }

    @Override
    public String getTaskDescription() {
        return "Banking all items";
    }


    private void openBank() {
        GameObject bankTarget = object.findNearestBank();
        if (bankTarget != null) {
            MenuEntry targetMenu = new MenuEntry("", "", bankTarget.getId(),
                    bank.getBankMenuOpcode(bankTarget.getId()), bankTarget.getSceneMinLocation().getX(),
                    bankTarget.getSceneMinLocation().getY(), false);
            utils.doActionMsTime(targetMenu, bankTarget.getConvexHull().getBounds(), sleepDelay());
        } else {
            utils.sendGameMessage("Bank not found, stopping");
        }
    }

    @Override
    public void onGameTick(GameTick event) {
        if (!inventory.isEmpty()) {
            status = "Depositing items";
            bank.depositAll();
        }
    }
}