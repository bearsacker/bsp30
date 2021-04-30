/*
* Copyright (C) 2019 Pierre Guillot
* This file is part of BSP30 library.
*
* BSP30 library is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* BSP30 library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with Way.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.guillot.bsp30.lumps;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class LightMap {

    private TextureInfo textureInfo;

    private int width;

    private int height;

    private byte[] data;

    private float[] min;

    private float[] max;

    public LightMap(TextureInfo textureInfo, byte[] data, int width, int height, float[] min, float[] max) {
        this.textureInfo = textureInfo;
        this.data = data;
        this.width = width;
        this.height = height;
        this.min = min;
        this.max = max;
    }

    public Vector2f computeTextureCoords(Vector3f vertex) {
        Vector2f coords = new Vector2f();

        coords.x = textureInfo.getS().dot(vertex) + textureInfo.getDistS() - min[0];
        coords.x /= (float) width * 16f;

        coords.y = textureInfo.getT().dot(vertex) + textureInfo.getDistT() - min[1];
        coords.y /= (float) height * 16f;

        return coords;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getData() {
        return data;
    }

    public float[] getMin() {
        return min;
    }

    public float[] getMax() {
        return max;
    }

}
