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
import java.util.HashMap;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import com.guillot.bsp30.exception.BSP30ParseException;
import com.guillot.bsp30.utils.StringParser;

public class Entity {

    private static final int MAX_MAP_ENTITIES = 1024;

    private static final int MAX_KEY = 32;

    private static final int MAX_VALUE = 1024;

    private HashMap<String, String> ePairs;

    public Entity() {
        ePairs = new HashMap<>();
    }

    public static HashMap<String, List<Entity>> parseEntities(String buffer) throws BSP30ParseException {
        StringParser stringParser = new StringParser(buffer);
        HashMap<String, List<Entity>> map = new HashMap<>();
        Entity entity;
        int size = 0;

        while ((entity = parseEntity(stringParser)) != null) {
            String key = entity.getEPair("classname");
            if (key != null && !key.isEmpty()) {
                if (map.containsKey(key)) {
                    map.get(key).add(entity);
                } else {
                    ArrayList<Entity> entities = new ArrayList<>();
                    entities.add(entity);
                    map.put(key, entities);
                }

                size++;

                if (size == MAX_MAP_ENTITIES) {
                    throw new BSP30ParseException("numEntities == MAX_MAP_ENTITIES (= " + MAX_MAP_ENTITIES + ")");
                }
            }
        }

        return map;
    }

    private static Entity parseEntity(StringParser stringParser) throws BSP30ParseException {
        String line = stringParser.getLine();
        if (line == null) {
            return null;
        }

        if (line.compareTo("{") != 0) {
            throw new BSP30ParseException("ParseEntity: { not found");
        }

        Entity entity = new Entity();

        do {
            line = stringParser.getLine();
            if (line.compareTo("}") == 0) {
                break;
            }

            entity.parseEPair(stringParser, line);
        } while (true);

        return entity;
    }

    public void parseEPair(StringParser stringParser, String line) {
        EPair ePair = stringParser.getEPair(line);
        if (ePair != null && ePair.getKey().length() < MAX_KEY && ePair.getValue().length() < MAX_VALUE) {
            if (!ePairs.containsKey(ePair.getKey())) {
                ePairs.put(ePair.getKey(), ePair.getValue());
            }
        }
    }

    public HashMap<String, String> getEPairs() {
        return ePairs;
    }

    public String getEPair(String key) {
        return ePairs.get(key);
    }

    public Float getEPairFloat(String key) {
        if (ePairs.containsKey(key)) {
            return Float.parseFloat(ePairs.get(key));
        }

        return null;
    }

    public Integer getEPairInt(String key) {
        if (ePairs.containsKey(key)) {
            return Integer.parseInt(ePairs.get(key));
        }

        return null;
    }

    public Vector3f getEPairVector3f(String key) {
        if (ePairs.containsKey(key)) {
            Vector3f vector = new Vector3f();
            String[] values = ePairs.get(key).split(" ");
            if (values != null && values.length == 3) {
                vector.x = Float.parseFloat(values[0]);
                vector.y = Float.parseFloat(values[1]);
                vector.z = Float.parseFloat(values[2]);

                return vector;
            }
        }

        return null;
    }

    public Vector4f getEPairVector4f(String key) {
        if (ePairs.containsKey(key)) {
            Vector4f vector = new Vector4f();
            String[] values = ePairs.get(key).split(" ");
            if (values != null && values.length == 4) {
                vector.w = Float.parseFloat(values[0]);
                vector.x = Float.parseFloat(values[1]);
                vector.y = Float.parseFloat(values[2]);
                vector.z = Float.parseFloat(values[3]);

                return vector;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String result = "";
        for (var entry : ePairs.entrySet()) {
            result += String.format(EPair.FORMAT, entry.getKey(), entry.getValue());
        }

        return "{\n" + result + "}";
    }
}
