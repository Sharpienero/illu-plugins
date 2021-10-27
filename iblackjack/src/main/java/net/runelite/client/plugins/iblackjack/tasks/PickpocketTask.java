package net.runelite.client.plugins.iblackjack.tasks;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iutils.BankUtils;

import javax.inject.Inject;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.*;

public class PickpocketTask extends Task {
    NPC masterfarmer;

    @Inject
    private BankUtils bank;

    @Override
    public boolean validate() {
        if (!inventory.isFull() && !bank.isOpen()) {
            System.out.println("Inventory is not full, find MF");
            masterfarmer = npc.findNearestNpcIndex(selectedNPCIndex, config.npcType().npcid);
            System.out.println(masterfarmer);
            return masterfarmer != null;
        }
        System.out.println("Returning false for PickpocketTask - Inventory is NOT EMPTY and BANK IS OPEN");
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Pickpocket master farmer";
    }

    @Override
    public void onGameTick(GameTick event) {
        entry = new MenuEntry("", "", selectedNPCIndex, MenuAction.NPC_THIRD_OPTION.getId(), 0, 0, false);
        utils.doActionMsTime(entry, masterfarmer.getConvexHull().getBounds(), sleepDelay());
    }
}