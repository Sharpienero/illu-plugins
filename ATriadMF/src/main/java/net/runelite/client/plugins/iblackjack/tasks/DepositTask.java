package net.runelite.client.plugins.iblackjack.tasks;

import net.runelite.api.GameObject;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iblackjack.Task;
import net.runelite.client.plugins.iblackjack.iBlackjackPlugin;
import net.runelite.client.plugins.iutils.BankUtils;
import net.runelite.client.plugins.iutils.scripts.ReflectBreakHandler;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.Point;

import javax.inject.Inject;

import java.util.concurrent.ScheduledExecutorService;

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

    @Inject
    private ScheduledExecutorService executorService;

    @Override
    public String getTaskDescription() {
        return "Banking all items";
    }

    @Override
    public void onGameTick(GameTick event) {
        if (!inventory.isEmpty()) {
            status = "Depositing items";
            bank.depositAll();
        }
    }
}