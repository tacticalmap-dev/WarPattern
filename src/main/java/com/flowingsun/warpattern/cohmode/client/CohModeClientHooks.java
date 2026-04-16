package com.flowingsun.warpattern.cohmode.client;

import com.flowingsun.warpattern.cohmode.net.CohModeInviteS2C;
import com.flowingsun.warpattern.cohmode.net.CohModeStateS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * Client entrypoints called by network packet handlers.
 */
public final class CohModeClientHooks {
    private CohModeClientHooks() {
    }

    public static void applyState(String stateJson, int openUi) {
        CohModeClientState.applyStateJson(stateJson);
        Minecraft mc = Minecraft.getInstance();
        if (openUi == CohModeStateS2C.OPEN_NONE) {
            return;
        }
        if (openUi == CohModeStateS2C.OPEN_MAIN) {
            if (mc.screen instanceof CohModeScreen) {
                return;
            }
            mc.setScreen(new CohModeScreen());
            return;
        }
        if (openUi == CohModeStateS2C.OPEN_ROLE) {
            if (mc.screen instanceof CohModeRoleScreen) {
                return;
            }
            mc.setScreen(new CohModeRoleScreen());
        }
    }

    public static void showInvite(CohModeInviteS2C invite) {
        Minecraft mc = Minecraft.getInstance();
        Screen parent = mc.screen;
        mc.setScreen(new CohModeInviteScreen(parent, invite));
    }
}
