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


public class Node {

    public static final int SIZE = 24;

    private Plane plane;

    private int[] children = new int[2];

    private Vector3f mins;

    private Vector3f maxs;

    private ArrayList<Face> faces;

    public Node(BinaryFileReader file, ArrayList<Plane> planes, ArrayList<Face> faces) {
        plane = planes.get(file.readInt());
        children[0] = file.readSignedShort();
        children[1] = file.readSignedShort();
        mins = new Vector3f(file.readShort(), file.readShort(), file.readShort());
        maxs = new Vector3f(file.readShort(), file.readShort(), file.readShort());

        int firstFace = file.readShort();
        int numFaces = file.readShort();
        this.faces = new ArrayList<>();
        for (int i = firstFace; i < firstFace + numFaces; i++) {
            this.faces.add(faces.get(i));
        }
    }

    public Plane getPlane() {
        return plane;
    }

    public int[] getChildren() {
        return children;
    }

    public Vector3f getMins() {
        return mins;
    }

    public Vector3f getMaxs() {
        return maxs;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }
}
