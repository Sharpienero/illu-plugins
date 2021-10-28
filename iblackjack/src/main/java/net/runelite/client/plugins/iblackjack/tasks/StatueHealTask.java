package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.*;


@Slf4j
public class StatueHealTask extends Task {

    @Inject
    private ClientThread clientThread;

    @Override
    public boolean validate() {
        return isInNardah() && client.getBoostedSkillLevel(Skill.PRAYER) < client.getRealSkillLevel(Skill.PRAYER) && client.isPrayerActive(Prayer.PIETY);
    }

    @Override
    public String getTaskDescription() {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        status = "In Nardah, heal";

        GameObject statue = object.findNearestGameObject(10389);

        if (statue != null) {
            status = "Restoring stats";
            entry = new MenuEntry("Pray-at", "Elidinis Statuette", statue.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), statue.getSceneMinLocation().getX(), statue.getSceneMinLocation().getY(), false);
            utils.doActionMsTime(entry, new Point(0, 0), sleepDelay());
            if (client.isPrayerActive(Prayer.PIETY)) {
                deactivatePrayer(WidgetInfo.PRAYER_PIETY);
            }
        }

        // Update teleport HP so that it seems more random.
        timeout = tickDelay();

        log.debug(status);
    }

    public void deactivatePrayer(WidgetInfo widgetInfo) {
        Widget prayer_widget = client.getWidget(widgetInfo);

        System.out.println("Trying to disable prayer");

        if (prayer_widget == null) {
            System.out.println("prayer_widget == null");
            return;

        }

        if (client.getBoostedSkillLevel(Skill.PRAYER) <= 0) {
            System.out.println("client.getBoostedSkillLevel(Skill.PRAYER) <= 0");
            return;
        }

        clientThread.invoke(() ->
                client.invokeMenuAction(
                        "Deactivate",
                        prayer_widget.getName(),
                        1,
                        MenuAction.CC_OP.getId(),
                        prayer_widget.getItemId(),
                        prayer_widget.getId()
                )
        );
    }
}