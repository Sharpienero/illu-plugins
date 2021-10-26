package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iutils.ActionQueue;

import javax.inject.Inject;
import java.util.List;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.*;

@Slf4j
public class DropTask extends Task {
    @Inject
    private ActionQueue action;

    @Override
    public boolean validate() {
        return inventory.getEmptySlots() < 5;
    }

    @Override
    public String getTaskDescription() {
        return "Banking seeds";
    }

    @Override
    public void onGameTick(GameTick event) {
        log.info("Entering bank task");
        selectedNPCIndex = 0;
        nextKnockoutTick = 0;

        List<WidgetItem> jugs = inventory.getItems(List.of(ItemID.JUG));
        long sleep = 0;
        for (WidgetItem jug : jugs) {
            entry = new MenuEntry("", "", jug.getId(), MenuAction.ITEM_FIFTH_OPTION.getId(), jug.getIndex(),
                    WidgetInfo.INVENTORY.getId(), false);
            sleep += sleepDelay();
            log.info("Adding jug: {}, delay time: {}", jug.getIndex(), sleep);
            utils.doActionMsTime(entry, jug.getCanvasBounds(), sleep);
        }
    }
}