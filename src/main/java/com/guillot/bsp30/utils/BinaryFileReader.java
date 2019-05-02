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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class BinaryFileReader {

    private byte[] data;

    private int offset = 0;

    public BinaryFileReader(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        data = new byte[dataInputStream.available()];
        dataInputStream.readFully(data);
        dataInputStream.close();
    }

    public byte readByte() {
        return data[offset++];
    }

    public int readByteAsInt() {
        return readByte() & 0xFF;
    }

    public int readShort() {
        int byte1 = readByteAsInt();
        int byte2 = readByteAsInt() << 8;

        return byte1 | byte2;
    }

    public int readSignedShort() {
        int byte1 = readByteAsInt();
        int byte2 = readByte() << 8;

        return byte1 | byte2;
    }

    public int readInt() {
        int byte1 = readByteAsInt();
        int byte2 = readByteAsInt() << 8;
        int byte3 = readByteAsInt() << 16;
        int byte4 = readByteAsInt() << 24;

        return byte1 | byte2 | byte3 | byte4;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public String readString(int length) {
        for (int i = 0; i < length; i++) {
            if (data[offset + i] == (byte) 0) {
                length = i;
                break;
            }
        }

        String result = new String(data, offset, length);
        offset += length;
        return result;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (offset >= 0 && offset <= data.length) {
            this.offset = offset;
        }
    }
}
