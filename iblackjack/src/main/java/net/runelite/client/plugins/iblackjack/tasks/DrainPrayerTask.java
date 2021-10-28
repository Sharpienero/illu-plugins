package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.*;



@Slf4j
public class DrainPrayerTask extends Task {

    @Inject
    public Client client;

    @Inject
    private ClientThread clientThread;

    @Override
    public boolean validate() {
        //If the current prayer level is less than the "real" prayer level AND the current hitpoints level is less than full HP
        return isInNardah() &&
                (client.getBoostedSkillLevel(Skill.PRAYER) < client.getRealSkillLevel(Skill.PRAYER) ||
                client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS));
    }

    @Override
    public String getTaskDescription() {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        status = "Activating prayer to be able to use statue";
        activatePrayer(WidgetInfo.PRAYER_PIETY);
        // Update teleport HP so that it seems more random.
        timeout = tickDelay();

        log.debug(status);
    }

    public void activatePrayer(WidgetInfo widgetInfo) {
        Widget prayer_widget = client.getWidget(widgetInfo);

        if (prayer_widget == null) {
            return;
        }

        if (client.getBoostedSkillLevel(Skill.PRAYER) <= 0) {
            return;
        }

        clientThread.invoke(() ->
                client.invokeMenuAction(
                        "Activate",
                        prayer_widget.getName(),
                        1,
                        MenuAction.CC_OP.getId(),
                        prayer_widget.getItemId(),
                        prayer_widget.getId()
                )
        );
    }
}