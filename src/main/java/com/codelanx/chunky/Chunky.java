/*
 * Copyright (C) 2015 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2015 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * long with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.chunky;

import com.comphenix.protocol.ProtocolLibrary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class description for {@link Chunky}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class Chunky extends JavaPlugin implements Listener {

    private static final Map<UUID, Visibility> visibilities = new HashMap<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new ChunkPacketListener(this));
    }

    public static Visibility getInfoFor(Player p) {
        Visibility back = Chunky.visibilities.get(p.getUniqueId());
        if (back == null) {
            back = new Visibility();
            Chunky.visibilities.put(p.getUniqueId(), back);
        }
        return back;
    }

    public static void queueChunkSend(Player p, int x, int z) {
        if (!(p instanceof CraftEntity)) {
            return;
        }
        EntityPlayer e = (EntityPlayer) ((CraftEntity) p).getHandle();
        e.chunkCoordIntPairQueue.add(new ChunkCoordIntPair(x, z));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Chunky.visibilities.remove(event.getPlayer().getUniqueId());
    }

}
