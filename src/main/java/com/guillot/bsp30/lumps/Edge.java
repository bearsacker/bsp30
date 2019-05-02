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


public class Edge {

    public static final int SIZE = 4;

    private Vector3f firstVertex;

    private Vector3f secondVertex;

    public Edge(BinaryFileReader file, ArrayList<Vector3f> vertices) {
        firstVertex = vertices.get(file.readShort());
        secondVertex = vertices.get(file.readShort());
    }

    public Vector3f getFirstVertex() {
        return firstVertex;
    }

    public Vector3f getSecondVertex() {
        return secondVertex;
    }
}
