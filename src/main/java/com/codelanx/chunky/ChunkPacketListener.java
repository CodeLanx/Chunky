/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codelanx.chunky;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 * Class description for {@link ChunkPacketListener}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class ChunkPacketListener implements PacketListener {

    private final Plugin plugin;
    private final ListeningWhitelist receiving;
    private final ListeningWhitelist sending;

    public ChunkPacketListener(Plugin plugin) {
    	this.plugin = plugin;
    	this.receiving = ListeningWhitelist.EMPTY_WHITELIST;
    	List<PacketType> comTypes = new ArrayList<>();
        comTypes.addAll(Arrays.asList(PacketType.Play.Server.RESPAWN,
                PacketType.Play.Server.MAP_CHUNK,
                PacketType.Play.Server.MAP_CHUNK_BULK));
    	this.sending = ListeningWhitelist.newBuilder().priority(ListenerPriority.MONITOR).types(comTypes)
    	    .gamePhase(GamePhase.PLAYING).options(new ListenerOptions[0]).build();
    }
	
    @Override
    public Plugin getPlugin() {
    	return plugin;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
    	return receiving;
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
    	return sending;
    }

    @Override
    public void onPacketSending(PacketEvent pe) {
        //https://github.com/aadnk/PacketWrapper/blob/master/PacketWrapper/src/main/java/com/comphenix/packetwrapper/WrapperPlayServerMapChunk.java
        if (pe.getPacketType() == PacketType.Play.Server.MAP_CHUNK) {
            boolean visible = pe.getPacket().getIntegers().read(2) != 0; // read field "c" aka "chunkDataBitMap"
            int chunkX = pe.getPacket().getIntegers().read(0); // read field "a" aka "x"
            int chunkZ = pe.getPacket().getIntegers().read(1); // read field "b" aka "z"
            Chunky.getInfoFor(pe.getPlayer()).setVisible(chunkX, chunkZ, visible);
        //https://github.com/aadnk/PacketWrapper/blob/master/PacketWrapper/src/main/java/com/comphenix/packetwrapper/WrapperPlayServerMapChunkBulk.java
        } else if (pe.getPacketType() == PacketType.Play.Server.MAP_CHUNK_BULK) {
            // packet never sent unless it is making chunks visible
            int[] chunkX = pe.getPacket().getIntegerArrays().read(0); // read field "a"
            int[] chunkZ = pe.getPacket().getIntegerArrays().read(1); // read field "b"
            Chunky.getInfoFor(pe.getPlayer()).setMultipleChunksVisible(chunkX, chunkZ);
        //https://github.com/aadnk/PacketWrapper/blob/master/PacketWrapper/src/main/java/com/comphenix/packetwrapper/WrapperPlayServerRespawn.java
        } else if (pe.getPacketType() == PacketType.Play.Server.RESPAWN) {
            Chunky.getInfoFor(pe.getPlayer()).clearVisibleChunks();
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent pe) {}

}