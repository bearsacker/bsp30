package com.guillot.bsp30.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Read binary files (by Java Monkey Engine)
 * 
 * @author Tim Biedert
 */
public class BinaryFileReader {

    private byte[] data;

    private int index = 0;

    /*********************************************************************************************************
     * BinaryFileReader() - Constructor
     * 
     * @param is The InputStream of the file to load
     ********************************************************************************************************/
    public BinaryFileReader(InputStream is) {
        try {
            DataInputStream dis = new DataInputStream(is); // Open file as DataInputStream
            this.data = new byte[dis.available()]; // Allocate memory for file
            dis.readFully(this.data); // Read the file completely into memory
            dis.close(); // Close the inut stream
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********************************************************************************************************
     * readByteAsInt() - Read one byte from the data array and return it as Integer. Increase file index by one.
     * 
     * @return The read byte as an int
     ********************************************************************************************************/
    public int readByteAsInt() {
        int b = (this.data[this.index] & 0xFF); // Get byte
        this.index++; // Increase index

        return b;
    }

    /*********************************************************************************************************
     * readByte() - Read one byte from the data array and return it
     * 
     * @return
     ********************************************************************************************************/
    public byte readByte() {
        this.index++;
        return this.data[this.index - 1];
    }

    /*********************************************************************************************************
     * readShort() - Read two bytes from the data array and generate a short. Return it as Integer and increase file index by one.
     * 
     * @return The read short as an int
     ********************************************************************************************************/
    public int readShort() {
        int s1 = (this.data[this.index] & 0xFF); // Get byte one
        int s2 = (this.data[this.index + 1] & 0xFF) << 8; // Get byte two
        this.index += 2; // Increase index

        return (s1 | s2);
    }

    /*********************************************************************************************************
     * readSignedShort() - Read two bytes from the data array and generate a short. Return it as Integer and increase file index by one.
     * 
     * @return The read short as an int
     ********************************************************************************************************/
    public int readSignedShort() {
        int s1 = (this.data[this.index] & 0xFF); // Get byte one
        int s2 = (this.data[this.index + 1]) << 8; // Get byte two
        this.index += 2; // Increase index

        return (s1 | s2);
    }

    /*********************************************************************************************************
     * readInt() - Read 4 bytes from the data array and generate an Integer. Increase file index by four.
     * 
     * @return The read int
     ********************************************************************************************************/
    public int readInt() {
        int i1 = (this.data[this.index] & 0xFF); // Get byte one
        int i2 = (this.data[this.index + 1] & 0xFF) << 8; // Get byte two
        int i3 = (this.data[this.index + 2] & 0xFF) << 16; // Get byte three
        int i4 = (this.data[this.index + 3] & 0xFF) << 24; // Get byte four
        this.index += 4; // Increase index

        return (i1 | i2 | i3 | i4);
    }

    /*********************************************************************************************************
     * readFloat() - Read 4 bytes from the data array and generate a float. Increase file index by four.
     * 
     * @return The read float
     ********************************************************************************************************/
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt()); // Get float from int
    }

    /*********************************************************************************************************
     * readString() - Read some number of bytes from the data array and return a String. Increase file index by number of characters read.
     * 
     * @return The read string
     ********************************************************************************************************/
    public String readString(int length) {
        // Look for zero terminated string from byte array
        for (int i = this.index; i < this.index + length; i++) {
            if (this.data[i] == (byte) 0) {
                String s = new String(this.data, this.index, i - this.index);
                this.index += length;
                return s;
            }
        }

        String s = new String(this.data, this.index, length);
        this.index += length;
        return s;
    }

    /*********************************************************************************************************
     * setOffset() - Sets the file index to the new offset-
     * 
     * @param newOffset The new byte offset in the file
     ********************************************************************************************************/
    public void setOffset(int newOffset) {
        if (newOffset < 0 || newOffset > this.data.length) {
            return;
        }

        this.index = newOffset;
    }
}
