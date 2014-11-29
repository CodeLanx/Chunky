/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codelanx.chunky;

import com.comphenix.protocol.ProtocolLibrary;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.ChunkCoordIntPair;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
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
