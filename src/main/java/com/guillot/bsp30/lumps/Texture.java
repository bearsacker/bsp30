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

import com.guillot.bsp30.utils.BinaryFileReader;

public class Texture {

    private static final int MAX_TEXTURE_NAME = 16;

    private static final int MIP_LEVELS = 4;

    private String name;

    private int width;

    private int height;

    private int[] offsets = new int[MIP_LEVELS];

    public Texture(BinaryFileReader file) {
        name = file.readString(MAX_TEXTURE_NAME);
        width = file.readInt();
        height = file.readInt();
        offsets[0] = file.readInt();
        offsets[1] = file.readInt();
        offsets[2] = file.readInt();
        offsets[3] = file.readInt();
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getOffsets() {
        return offsets;
    }
}
