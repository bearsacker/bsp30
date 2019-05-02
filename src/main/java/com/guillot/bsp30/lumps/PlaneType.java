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

public enum PlaneType {
    PLANE_X(0),
    PLANE_Y(1),
    PLANE_Z(2),
    PLANE_ANYX(3),
    PLANE_ANYY(4),
    PLANE_ANYZ(5);

    private int value;

    private PlaneType(int value) {
        this.value = value;
    }

    public static PlaneType fromValue(int value) {
        for (PlaneType planeType : PlaneType.values()) {
            if (value == planeType.getValue()) {
                return planeType;
            }
        }

        return PLANE_X;
    }

    public int getValue() {
        return this.value;
    }
}
