package com.flowingsun.warpattern.cohmode.net;

import com.flowingsun.warpattern.cohmode.client.CohModeClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server-to-client full UI state snapshot packet.
 */
public class CohModeStateS2C {
    public static final int OPEN_NONE = 0;
    public static final int OPEN_MAIN = 1;
    public static final int OPEN_ROLE = 2;

    public final String stateJson;
    public final int openUi;

    public CohModeStateS2C(String stateJson, int openUi) {
        this.stateJson = stateJson;
        this.openUi = openUi;
    }

    public static void encode(CohModeStateS2C pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.stateJson, 262144);
        buf.writeVarInt(pkt.openUi);
    }

    public static CohModeStateS2C decode(FriendlyByteBuf buf) {
        return new CohModeStateS2C(buf.readUtf(262144), buf.readVarInt());
    }

    public static void handle(CohModeStateS2C pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CohModeClientHooks.applyState(pkt.stateJson, pkt.openUi)));
        ctx.get().setPacketHandled(true);
    }
}
