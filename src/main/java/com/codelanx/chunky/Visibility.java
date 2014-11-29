/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codelanx.chunky;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class description for {@link Visibility}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class Visibility {

    private final Set<Tuple<Integer, Integer>> visible = new HashSet<>();

    public void clearVisibleChunks() {
        this.visible.clear();
    }

    public Set<Tuple<Integer, Integer>> getVisibleChunks() {
        return Collections.unmodifiableSet(this.visible);
    }

    public boolean isChunkVisible(int chunkX, int chunkZ) {
        return this.visible.contains(new Tuple<>(chunkX, chunkZ));
    }

    public void setVisible(int chunkX, int chunkZ, boolean visible) {
        Tuple<Integer, Integer> loc = new Tuple<>(chunkX, chunkZ);
        if (visible) {
            this.visible.add(loc);
        } else {
            this.visible.remove(loc);
        }
    }

    public void setMultipleChunksVisible(int[] chunkX, int[] chunkZ) {
        if (chunkX.length != chunkZ.length) {
            throw new IllegalArgumentException("Chunk X and Z coordinate count is not the same");
        }
        synchronized(this.visible) {
            for (int i = 0; i < chunkX.length; i++) {
                this.visible.add(new Tuple<>(chunkX[i], chunkZ[i]));
            }
        }
    }

    public boolean isVisible(int chunkX, int chunkZ) {
        return this.visible.contains(new Tuple<>(chunkX, chunkZ));
    }

}
