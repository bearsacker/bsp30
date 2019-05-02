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

public enum LumpType {
    LUMP_ENTITIES(0),
    LUMP_PLANES(1),
    LUMP_TEXTURES(2),
    LUMP_VERTICES(3),
    LUMP_VISIBILITY(4),
    LUMP_NODES(5),
    LUMP_TEXINFO(6),
    LUMP_FACES(7),
    LUMP_LIGHTING(8),
    LUMP_CLIPNODES(9),
    LUMP_LEAVES(10),
    LUMP_MARKSURFACES(11),
    LUMP_EDGES(12),
    LUMP_SURFEDGES(13),
    LUMP_MODELS(14);

    private int value;

    private LumpType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
