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

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.guillot.bsp30.utils.BinaryFileReader;

public class TextureInfo {

    public static final int SIZE = 40;

    private Vector3f s;

    private Vector3f t;

    private float distS;

    private float distT;

    private Texture texture;

    private int flags;

    public TextureInfo(BinaryFileReader file, ArrayList<Texture> textures) {
        this.s = new Vector3f(file.readFloat(), file.readFloat(), file.readFloat());
        this.distS = file.readFloat();
        this.t = new Vector3f(file.readFloat(), file.readFloat(), file.readFloat());
        this.distT = file.readFloat();
        this.texture = textures.get(file.readInt());
        this.flags = file.readInt();
    }

    public Vector2f computeTextureCoords(Vector3f vertex) {
        Vector2f coords = new Vector2f();

        coords.x = s.dot(vertex) + distS;
        coords.x /= texture.getWidth();

        coords.y = t.dot(vertex) + distT;
        coords.y /= texture.getHeight();

        return coords;
    }

    public Vector3f getS() {
        return s;
    }

    public Vector3f getT() {
        return t;
    }

    public float getDistS() {
        return distS;
    }

    public float getDistT() {
        return distT;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getFlags() {
        return flags;
    }
}
