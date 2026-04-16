package com.flowingsun.warpattern.cohmode.client;

import com.flowingsun.warpattern.cohmode.CohModeModels;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Standalone role selection UI opened via command.
 */
public class CohModeRoleScreen extends Screen {
    public CohModeRoleScreen() {
        super(Component.literal("Cohmode Role UI"));
    }

    @Override
    protected void init() {
        int left = 12;
        int top = 20;

        addRenderableWidget(Button.builder(Component.literal("Refresh"), b ->
                CohModeClientState.sendAction("refresh", CohModeClientState.json()))
                .bounds(left, top, 80, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
                .bounds(width - 92, top, 80, 20).build());

        int y = top + 34;
        addRoleButton(left, y, "Commander", CohModeModels.Role.COMMANDER);
        addRoleButton(left + 116, y, "Rifleman", CohModeModels.Role.RIFLEMAN);
        addRoleButton(left + 232, y, "Assault", CohModeModels.Role.ASSAULT);
        addRoleButton(left, y + 26, "Support", CohModeModels.Role.SUPPORT);
        addRoleButton(left + 116, y + 26, "Sniper", CohModeModels.Role.SNIPER);
    }

    private void addRoleButton(int x, int y, String label, CohModeModels.Role role) {
        addRenderableWidget(Button.builder(Component.literal(label), b -> {
            JsonObject payload = CohModeClientState.json();
            payload.addProperty("role", role.name());
            CohModeClientState.sendAction("select_role", payload);
        }).bounds(x, y, 110, 20).build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        CohModeModels.LobbyStateView s = CohModeClientState.state();
        graphics.drawCenteredString(font, "Standalone Role Selection", width / 2, 6, 0xFFFFFF);
        graphics.drawString(font, "Selected role: " + (s.selectedRole == null ? "none" : s.selectedRole.label), 12, 88, 0xFFFFFF, false);
        graphics.drawString(font, "Selected camp: " + (s.selectedCamp == null ? "none" : s.selectedCamp.label), 12, 100, 0xC8C8C8, false);
        if (s.statusText != null && !s.statusText.isEmpty()) {
            graphics.drawCenteredString(font, s.statusText, width / 2, height - 14, 0xF0F0A0);
        }
    }
}
