package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iblackjack.iBlackjackPlugin;
import net.runelite.client.plugins.iutils.ActionQueue;
import net.runelite.client.plugins.iutils.BankUtils;
import net.runelite.client.plugins.iutils.InventoryUtils;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class OpenBankTask extends Task {

    @Inject
    ActionQueue action;

    @Inject
    InventoryUtils inventory;

    @Inject
    BankUtils bank;

    @Override
    public boolean validate() {
        return !inventory.isEmpty() && !bank.isOpen();
    }

    @Override
    public String getTaskDescription() {
        return "Walking to bank";
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
        openBank();
    }
}