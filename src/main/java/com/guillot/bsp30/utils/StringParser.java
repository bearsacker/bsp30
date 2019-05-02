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
package com.guillot.bsp30.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.guillot.bsp30.lumps.EPair;

public class StringParser {

    private Pattern pattern;

    private String[] lines;

    private int index = 0;

    public StringParser(String buffer) {
        pattern = Pattern.compile(EPair.REGEX);
        if (buffer != null) {
            lines = buffer.split("\n");
        }
    }

    public String getLine() {
        if (index < lines.length) {
            return lines[index++];
        }

        return null;
    }

    public EPair getEPair(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new EPair(matcher.group(1), matcher.group(2));
        }

        return null;
    }
}
