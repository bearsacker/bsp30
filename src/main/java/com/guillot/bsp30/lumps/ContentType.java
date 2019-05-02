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

public enum ContentType {
    CONTENTS_EMPTY(-1),
    CONTENTS_SOLID(-2),
    CONTENTS_WATER(-3),
    CONTENTS_SLIME(-4),
    CONTENTS_LAVA(-5),
    CONTENTS_SKY(-6),
    CONTENTS_ORIGIN(-7),
    CONTENTS_CLIP(-8),
    CONTENTS_CURRENT_0(-9),
    CONTENTS_CURRENT_90(-10),
    CONTENTS_CURRENT_180(-11),
    CONTENTS_CURRENT_270(-12),
    CONTENTS_CURRENT_UP(-13),
    CONTENTS_CURRENT_DOWN(-14),
    CONTENTS_TRANSLUCENT(-15);

    private int value;

    private ContentType(int value) {
        this.value = value;
    }

    public static ContentType fromValue(int value) {
        for (ContentType contentType : ContentType.values()) {
            if (value == contentType.getValue()) {
                return contentType;
            }
        }

        return CONTENTS_EMPTY;
    }

    public int getValue() {
        return this.value;
    }
}
