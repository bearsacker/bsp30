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

import com.guillot.bsp30.exception.BSP30ParseException;
import com.guillot.bsp30.utils.BinaryFileReader;

public class Header {

    private static final int BSPVERSION = 30;

    private int version;

    private ArrayList<Lump> lumps;

    public Header(BinaryFileReader file) throws BSP30ParseException {
        version = file.readInt();

        if (version != BSPVERSION) {
            throw new BSP30ParseException("Invalid header!");
        }

        lumps = new ArrayList<>();
        for (int i = 0; i < Lump.NUM_LUMPS; i++) {
            lumps.add(new Lump(file));
        }
    }

    public int getVersion() {
        return version;
    }

    public Lump getLump(LumpType lump) {
        return lumps.get(lump.getValue());
    }
}
