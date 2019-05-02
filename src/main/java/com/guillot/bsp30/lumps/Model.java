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

public class Model {

    public static final int SIZE = 64;

    private static final int MAX_MAP_HULLS = 4;

    private Vector3f mins;

    private Vector3f maxs;

    private Vector3f origin;

    private int[] headNode = new int[MAX_MAP_HULLS];

    private int visLeafs;

    private ArrayList<Face> faces;

    public Model(BinaryFileReader file, ArrayList<Face> faces) {
        mins = new Vector3f(file.readFloat(), file.readFloat(), file.readFloat());
        maxs = new Vector3f(file.readFloat(), file.readFloat(), file.readFloat());
        origin = new Vector3f(file.readFloat(), file.readFloat(), file.readFloat());

        headNode[0] = file.readInt();
        headNode[1] = file.readInt();
        headNode[2] = file.readInt();
        headNode[3] = file.readInt();

        visLeafs = file.readInt();

        int firstFace = file.readInt();
        int numFaces = file.readInt();
        this.faces = new ArrayList<>();
        for (int i = firstFace; i < firstFace + numFaces; i++) {
            this.faces.add(faces.get(i));
        }
    }

    public Vector3f getMins() {
        return mins;
    }

    public Vector3f getMaxs() {
        return maxs;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public int[] getHeadNode() {
        return headNode;
    }

    public int getVisLeafs() {
        return visLeafs;
    }

    public void setVisLeafs(int visLeafs) {
        this.visLeafs = visLeafs;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }
}
