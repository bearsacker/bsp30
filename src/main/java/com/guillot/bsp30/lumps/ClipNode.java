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

import com.guillot.bsp30.utils.BinaryFileReader;

public class ClipNode {

    public static final int SIZE = 8;

    private Plane plane;

    private int[] children = new int[2];

    public ClipNode(BinaryFileReader file, ArrayList<Plane> planes) {
        plane = planes.get(file.readInt());
        children[0] = file.readShort();
        children[1] = file.readShort();
    }

    public Plane getPlane() {
        return plane;
    }

    public int[] getChildren() {
        return children;
    }
}
