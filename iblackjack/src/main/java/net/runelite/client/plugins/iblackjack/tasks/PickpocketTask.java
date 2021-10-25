package net.runelite.client.plugins.iblackjack.tasks;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iblackjack.Task;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.*;

public class PickpocketTask extends Task {
    NPC bandit;
    NPC masterfarmer;

    @Override
    public boolean validate() {
        if (selectedNPCIndex == 0) {
            System.out.println("Npc ID = 0");
            return false;
        }
        else {
            System.out.println("Else Npc ID = " + selectedNPCIndex);
        }

        masterfarmer = npc.findNearestNpcIndex(selectedNPCIndex, config.npcType().npcid);
        return client.getTickCount() < nextKnockoutTick && masterfarmer != null;
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