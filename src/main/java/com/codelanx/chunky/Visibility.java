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
