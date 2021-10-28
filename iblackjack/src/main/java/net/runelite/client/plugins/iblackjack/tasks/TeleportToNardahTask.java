package net.runelite.client.plugins.iblackjack.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.iblackjack.Task;

import javax.inject.Inject;

import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.teleportHP;
import static net.runelite.client.plugins.iblackjack.iBlackjackPlugin.timeout;



@Slf4j
public class TeleportToNardahTask extends Task {

    @Override
    public boolean validate() {
        // Add check if we're in combat, we want to tele
        return client.getBoostedSkillLevel(Skill.HITPOINTS) <= teleportHP
                && (inventory.containsItem(ItemID.CONSTRUCT_CAPE) || inventory.containsItem(ItemID.CONSTRUCT_CAPET))
                && !isInNardah();
    }

    @Override
    public String getTaskDescription() {
        return status;
    }

    @Override
    public void onGameTick(GameTick event) {
        if (isInNardah()) { return; }

        //BotUtils.pressKey(KeyEvent.VK_ENTER);
        status = "Teleporting To Nardah";

        //MenuOption=Nardah MenuTarget=<col=ff9040>Desert amulet 4 Id=13136 Opcode=ITEM_THIRD_OPTION/35 Param0=25 Param1=9764864 CanvasX=602 CanvasY=420
        MenuEntry desertAmmyTele = new MenuEntry("Nardah", "Desert amulet 4", ItemID.DESERT_AMULET_4, MenuAction.ITEM_THIRD_OPTION.getId(), 25, 9764864,  false);
        Widget widget =  inventory.getWidgetItem(ItemID.CONSTRUCT_CAPET).getWidget();

        System.out.println(widget);
        if (widget != null) {
            menu.setEntry(desertAmmyTele);
            mouse.delayMouseClick(widget.getBounds(), 50);
        }

        // Update teleport HP so that it seems more random.
        teleportHP = calc.getRandomIntBetweenRange(config.minEatHP(), config.maxEatHP());
        timeout = tickDelay();

        log.debug(status);
    }
}