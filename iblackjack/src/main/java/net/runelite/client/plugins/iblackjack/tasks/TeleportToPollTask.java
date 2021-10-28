package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iutils.KeyboardUtils;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.teleportHP;
import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.timeout;


@Slf4j
public class TeleportToPollTask extends Task {

    @Inject
    KeyboardUtils keyboardUtils;

    @Override
    public boolean validate() {
        // If we're in nardah and HP is good
        return client.getBoostedSkillLevel(Skill.HITPOINTS) >= client.getRealSkillLevel(Skill.HITPOINTS)
                && (inventory.containsItem(ItemID.CONSTRUCT_CAPE) || inventory.containsItem(ItemID.CONSTRUCT_CAPET))
                && isInNardah();
    }

    @Override
    public String getTaskDescription() {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        if (!isInNardah()) { return; }

        //BotUtils.pressKey(KeyEvent.VK_ENTER);
        status = "Teleporting To Poll";

        //MenuOption=Teleport MenuTarget=<col=ff9040>Construct. cape(t) Id=9790 Opcode=ITEM_THIRD_OPTION/35 Param0=27 Param1=9764864 CanvasX=679 CanvasY=418
        MenuEntry capeTele = new MenuEntry("Teleport", "Construct. cape(t)", ItemID.CONSTRUCT_CAPET, MenuAction.ITEM_THIRD_OPTION.getId(), 27, 9764864,  false);
        Widget widget =  inventory.getWidgetItem(ItemID.CONSTRUCT_CAPET).getWidget();

        System.out.println(widget);
        if (widget != null) {
            menu.setEntry(capeTele);
            mouse.delayMouseClick(widget.getBounds(), 50);

            // Send '4' after opening teleport menu
            tickDelay();
            keyboardUtils.pressKey('4');
        }

        // Update teleport HP so that it seems more random.
        teleportHP = calc.getRandomIntBetweenRange(config.minEatHP(), config.maxEatHP());
        timeout = tickDelay();

        log.debug(status);
    }
}