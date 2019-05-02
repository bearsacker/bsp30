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


public class Leaf {

    public static final int SIZE = 28;

    private static final int NUM_AMBIENTS = 4;

    private ContentType contents;

    private int visOffset;

    private Vector3f mins;

    private Vector3f maxs;

    private ArrayList<Face> faces;

    private byte[] ambientLevel = new byte[NUM_AMBIENTS];

    public Leaf(BinaryFileReader file, ArrayList<Face> markSurfaces) {
        contents = ContentType.fromValue(file.readInt());
        visOffset = file.readInt();
        mins = new Vector3f(file.readShort(), file.readShort(), file.readShort());
        maxs = new Vector3f(file.readShort(), file.readShort(), file.readShort());

        int firstMarkSurface = file.readShort();
        int numMarkSurfaces = file.readShort();
        faces = new ArrayList<>();
        for (int i = firstMarkSurface; i < firstMarkSurface + numMarkSurfaces; i++) {
            faces.add(markSurfaces.get(i));
        }

        ambientLevel[0] = file.readByte();
        ambientLevel[1] = file.readByte();
        ambientLevel[2] = file.readByte();
        ambientLevel[3] = file.readByte();
    }

    public ContentType getContents() {
        return contents;
    }

    public void setContents(ContentType contents) {
        this.contents = contents;
    }

    public int getVisOffset() {
        return visOffset;
    }

    public void setVisOffset(int visOffset) {
        this.visOffset = visOffset;
    }

    public Vector3f getMins() {
        return mins;
    }

    public void setMins(Vector3f mins) {
        this.mins = mins;
    }

    public Vector3f getMaxs() {
        return maxs;
    }

    public void setMaxs(Vector3f maxs) {
        this.maxs = maxs;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }

    public byte[] getAmbientLevel() {
        return ambientLevel;
    }

    public void setAmbientLevel(byte[] ambientLevel) {
        this.ambientLevel = ambientLevel;
    }
}
