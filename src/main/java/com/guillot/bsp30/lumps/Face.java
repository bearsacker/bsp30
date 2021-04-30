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

import static java.util.Arrays.copyOfRange;

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

    private ArrayList<LightMap> lightMaps = new ArrayList<>();

    public Face(BinaryFileReader file, ArrayList<Plane> planes, ArrayList<Vector3f> surfEdges, ArrayList<TextureInfo> textureInfos,
            byte[] lightData) {
        plane = planes.get(file.readShort());
        side = file.readShort();

        int firstSurfEdge = file.readInt();
        int numSurfEdges = file.readShort();
        textureInfo = textureInfos.get(file.readShort());
        styles[0] = file.readByte();
        styles[1] = file.readByte();
        styles[2] = file.readByte();
        styles[3] = file.readByte();
        int lightMapOffset = file.readInt();

        // Compute s and t extents
        Vector3f vertex0 = surfEdges.get(firstSurfEdge);
        float s0 = textureInfo.getS().dot(vertex0) + textureInfo.getDistS();
        float t0 = textureInfo.getT().dot(vertex0) + textureInfo.getDistT();

        float[] min = new float[] {s0, t0};
        float[] max = new float[] {s0, t0};

        vertices = new ArrayList<>();
        for (int i = firstSurfEdge; i < firstSurfEdge + numSurfEdges; i++) {
            Vector3f vertex = surfEdges.get(i);
            vertices.add(vertex);

            // Compute s and t extents
            float s = textureInfo.getS().dot(vertex) + textureInfo.getDistS();
            min[0] = Math.min(min[0], s);
            max[0] = Math.max(max[0], s);

            float t = textureInfo.getT().dot(vertex) + textureInfo.getDistT();
            min[1] = Math.min(min[1], t);
            max[1] = Math.max(max[1], t);
        }

        if (textureInfo.getFlags() == 0) {
            // Compute lightmap size
            int width = (int) (Math.ceil(max[0] / 16f) - Math.floor(min[0] / 16f)) + 1;
            int height = (int) (Math.ceil(max[1] / 16f) - Math.floor(min[1] / 16f)) + 1;

            // Generate lightmaps texture
            for (int i = 0; i < MAXLIGHTMAPS; i++) {
                if (styles[i] == -1) {
                    break;
                }

                try {
                    int length = width * height * 3;
                    int index = lightMapOffset + i * length;

                    lightMaps.add(new LightMap(textureInfo, copyOfRange(lightData, index, index + length), width, height, min, max));
                } catch (Exception e) {}
            }
        }
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

    public ArrayList<LightMap> getLightMaps() {
        return lightMaps;
    }

}
