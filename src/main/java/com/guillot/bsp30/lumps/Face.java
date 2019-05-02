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

import org.joml.Vector3f;

import com.guillot.bsp30.utils.BinaryFileReader;

public class Face {

    public static final int SIZE = 20;

    private static final int MAXLIGHTMAPS = 4;

    private Plane plane;

    private ArrayList<Vector3f> vertices;

    private TextureInfo textureInfo;

    private int side;

    private byte[] styles = new byte[MAXLIGHTMAPS];

    private int lightMapOffset;

    public Face(BinaryFileReader file, ArrayList<Plane> planes, ArrayList<Vector3f> surfEdges, ArrayList<TextureInfo> textureInfos) {
        plane = planes.get(file.readShort());
        side = file.readShort();

        int firstSurfEdge = file.readInt();
        int numSurfEdges = file.readShort();
        vertices = new ArrayList<>();
        for (int i = firstSurfEdge; i < firstSurfEdge + numSurfEdges; i++) {
            vertices.add(surfEdges.get(i));
        }

        textureInfo = textureInfos.get(file.readShort());
        styles[0] = file.readByte();
        styles[1] = file.readByte();
        styles[2] = file.readByte();
        styles[3] = file.readByte();
        lightMapOffset = file.readInt();
    }

    public Plane getPlane() {
        return plane;
    }

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public TextureInfo getTextureInfo() {
        return textureInfo;
    }

    public int getSide() {
        return side;
    }

    public byte[] getStyles() {
        return styles;
    }

    public int getLightMapOffset() {
        return lightMapOffset;
    }
}
