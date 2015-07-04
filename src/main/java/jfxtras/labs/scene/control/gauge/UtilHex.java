/**
 * UtilHex.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.MissingResourceException;


/**
 * @author Jose Pereda Llamas &lt;jperedadnr&gt;
 *         Created on : 23-jun-2012, 14:49:58
 */
public class UtilHex {

    private byte[] rawData = null;

    public UtilHex() {
    }

    public static int bytes2int(byte[] bytes){
        int ret=0;
        for (int i=0; i<bytes.length; i++) {
            ret <<= 8;
            ret |= (int)bytes[i] & 0xFF;
        }
        return ret;
    }
    public static byte[] toBytes(String hexString) {
        return toBytes(hexString.split("\\s"));
    }

    public static byte[] toBytes(String[] bytes) {
        byte[] result = new byte[bytes.length];
        int i=0;
        for (String bStr : bytes) {
            int n = 0;
            if (!(bStr.equals("0") || bStr.equals("00"))) {
                n = Byte.parseByte(bStr.substring(0, 1), 16);
                n = 16 * n + Byte.parseByte(bStr.substring(1), 16);
            }
            result[i++]=(byte) n;
        }
        return result;
    }
    public static String long2Dword(long l,boolean bLastSB){
        String s1=Long.toHexString(l);
        String ss1=("00000000".substring(0,8-s1.length())).concat(s1);
        //orden lsb:
        if(bLastSB){
            return ss1.substring(6,8).concat(" ").concat(ss1.substring(4,6)).
                    concat(" ").concat(ss1.substring(2,4)).concat(" ").concat(ss1.substring(0,2));
        }
        return ss1.substring(0,2).concat(" ").concat(ss1.substring(2,4)).
                    concat(" ").concat(ss1.substring(4,6)).concat(" ").concat(ss1.substring(6,8));
        
    }
    public static String long2Word(long l, boolean bLastSB) {
        String s1 = Long.toHexString(l);
        String ss1 = ("0000".substring(0, 4 - s1.length())).concat(s1);
        //orden lsb:
        if (bLastSB) {
            return ss1.substring(2, 4).concat(" ").concat(ss1.substring(0, 2));
        }
        return ss1.substring(0, 2).concat(" ").concat(ss1.substring(2, 4));
    }

    public static String dec2hexStr(int b) {
        return Integer.toString((b & 0xff) + 0x100, 16).substring(1).toUpperCase();
    }

    public static String bin2Hex(String s) {
        return Integer.toHexString(Integer.parseInt(s, 2));
    }

    /*
        Looks in a jar in the classhpath, extracts the bmp file to a temporary folder
        and returns its absolute path
    */

    public static String getBmpFromJar(String jarPathBmpFile){
        /*
        Extract file from Jar to System Temporal folder
        */
        String temp = System.getProperty("java.io.tmpdir");
        File dir = new File(temp+File.separator+"tempJarFolder");  
        if(!dir.isDirectory()){
            dir.mkdirs();
        }
        
        InputStream is=UtilHex.class.getResourceAsStream(jarPathBmpFile);
        String path="";
        try {
            File dstfile=new File(dir,"\\"+jarPathBmpFile);
//            dstfile.deleteOnExit();
            if(!dstfile.getParentFile().isDirectory()){
                dstfile.getParentFile().mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(dstfile)) {
                int b;
                while((b = is.read()) != -1){
                    fos.write(b);
                }
                path=dstfile.getAbsolutePath();
            } 
        } 
        catch (IOException e) { 
             System.out.println("Error with image "+jarPathBmpFile+": "+e.getMessage());
        }
        return path;
    }
    
    /*
    * Converts BMP format into a BMT file
    * Levels: [0-255]
    */
    public boolean convertsBmp(String pathBmp, int minLevel, int maxLevel,
                               boolean colorR, boolean colorG, boolean colorB) {
        if (maxLevel > 255) {
            maxLevel = 255;
        }
        if (minLevel > maxLevel) {
            minLevel = maxLevel;
        }
        if (minLevel < 0) {
            minLevel = 0;
        }

        int[] tonos = {
            minLevel,
            (int) ((maxLevel + minLevel) / 2),
            maxLevel
        };

        int[] colores = {
            (colorR) ? 1 : 0,
            (colorG) ? 1 : 0,
            (colorB) ? 1 : 0
        }; // R-G-B

        String fullpathBmp=(pathBmp.endsWith(".bmp")?pathBmp:pathBmp.concat(".bmp"));
        
        InputStream bmpStream=null;
        
        try {	
            // Try load bmp from this jar
            bmpStream = getClass().getResourceAsStream( fullpathBmp );  
            if(bmpStream!=null){ 
                // Try load bmp from another jar if the path is provided
                fullpathBmp = getBmpFromJar(pathBmp);
            }
        }
        catch(MissingResourceException mre){	            
        }
        
        jBMP2Panel bmp = new jBMP2Panel(fullpathBmp, colores, tonos);

        try {
            
            // 1. Open bmp and read rawData
            boolean bFound=false;
            
            if(bmpStream!=null){ 
                bFound=bmp.getBMPImageFromStream(bmpStream);
            }
            else{
                bFound=bmp.getBMPImage();
            }

            if (bFound) {
                // 2. Get hex string with rawdata
                rawData = bmp.BMP2MemoriaGrafica();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error with "+fullpathBmp+": "+e.getMessage());
        } finally {
            if(bmpStream!=null){
                try{
                    bmpStream.close();
                } catch (IOException ioe){}
            }
            bmp.reset();
        }
        
        return false;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void resetRawData(){
        rawData=null;
    }
    
    private class jBMP2Panel {

        /*
        * BMPLoader.
        *
        * JavaZOOM : jlgui@javazoom.net
        *            http://www.javazoom.net
        *
        *-----------------------------------------------------------------------
        *   This program is free software; you can redistribute it and/or modify
        *   it under the terms of the GNU Library General Public License as published
        *   by the Free Software Foundation; either version 2 of the License, or
        *   (at your option) any later version.
        *
        *   This program is distributed in the hope that it will be useful,
        *   but WITHOUT ANY WARRANTY; without even the implied warranty of
        *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        *   GNU Library General Public License for more details.
        *
        *   You should have received a copy of the GNU Library General Public
        *   License along with this program; if not, write to the Free Software
        *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
        *----------------------------------------------------------------------
        */


        private InputStream is;
        private int curPos = 0;
        private int bitmapOffset; // starting position of image data
        private int width; // image width in pixels
        private int height; // image height in pixels
        private int levels = 3; // entre 1 y 4 tonos
        private int[] tonos; // segun levels, entre 0-255 decimal
        private int colorMask = 1; // 7(=Blue 4, Green 2, Red 1 para los tres colores) o 1 (para el Red=1)
        private int numColors = 1;
        private short bitsPerPixel; // 1, 4, 8, or 24 (no color map)
        private int compression; // 0 (none), 1 (8-bit RLE), or 2 (4-bit RLE)
        private int actualSizeOfBitmap;
        private int scanLineSize;
        private int actualColorsUsed;
        private byte r[], g[], b[]; // color palette
        private int noOfEntries;
        private byte[] byteData; // Unpacked data
        private int[] intData; // Unpacked data

        private byte[] m_RawData = null; // the raw bmp data
        private String m_sFullPath = null;

        public jBMP2Panel(String fullPath, int[] colores, int[] tonos) {
            m_sFullPath = fullPath;
            this.tonos = tonos;
            levels = tonos.length;
            colorMask = colores[0] + 2 * colores[1] + 4 * colores[2];
            numColors = 3; //colores[0] + colores[1] + colores[2];
        }

        public boolean getBMPImage() throws Exception {
            File file = new File(m_sFullPath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                System.out.println("File " + m_sFullPath + " not found");
                return false;
            }
            try {
                read(fis);
            } catch (IOException ex) {
                System.out.append("Error reading bmp file");
                return false;
            } finally {
                if(fis!=null){
                    try{
                        fis.close();
                    } catch (IOException ioe){}
                }
            }

            return true;
        }

        public boolean getBMPImageFromStream(InputStream stream) throws Exception 
        {

            try {
                read(stream);
            } catch (IOException ex) {
                System.out.append("Error reading bmp file");
                return false;
            }

            return true;
        }
        
        protected void getFileHeader() throws IOException, Exception {
            // Actual contents (14 bytes):
            short fileType = 0x4d42;// always "BM"
            int fileSize; // size of file in bytes
            short reserved1 = 0; // always 0
            short reserved2 = 0; // always 0
            fileType = readShort();
            if (fileType != 0x4d42) {
                throw new Exception("Not a BMP file"); // wrong file type
            }
            fileSize = readInt();
            reserved1 = readShort();
            reserved2 = readShort();
            bitmapOffset = readInt();
        }

        protected void getBitmapHeader() throws IOException {
            // Actual contents (40 bytes):
            int size; // size of this header in bytes
            short planes; // no. of color planes: always 1
            int sizeOfBitmap; // size of bitmap in bytes (may be 0: if so, calculate)
            int horzResolution; // horizontal resolution, pixels/meter (may be 0)
            int vertResolution; // vertical resolution, pixels/meter (may be 0)
            int colorsUsed; // no. of colors in palette (if 0, calculate)
            int colorsImportant; // no. of important colors (appear first in palette) (0 means all are important)
            boolean topDown;
            int noOfPixels;
            size = readInt();
            width = readInt();
            height = readInt();
            planes = readShort();
            bitsPerPixel = readShort();
            compression = readInt();
            sizeOfBitmap = readInt();
            horzResolution = readInt();
            vertResolution = readInt();
            colorsUsed = readInt();
            colorsImportant = readInt();
            topDown = (height < 0);
            noOfPixels = width * height;
            // Scan line is padded with zeroes to be a multiple of four bytes
            scanLineSize = ((width * bitsPerPixel + 31) / 32) * 4;
            if (sizeOfBitmap != 0) {
                actualSizeOfBitmap = sizeOfBitmap;
            } else
            // a value of 0 doesn't mean zero - it means we have to calculate it
            {
                actualSizeOfBitmap = scanLineSize * height;
            }
            if (colorsUsed != 0) {
                actualColorsUsed = colorsUsed;
            } else
                // a value of 0 means we determine this based on the bits per pixel
                if (bitsPerPixel < 16) {
                    actualColorsUsed = 1 << bitsPerPixel;
                } else {
                    actualColorsUsed = 0; // no palette
                }
        }

        protected void getPalette() throws IOException {
            noOfEntries = actualColorsUsed;
            //IJ.write("noOfEntries: " + noOfEntries);
            if (noOfEntries > 0) {
                r = new byte[noOfEntries];
                g = new byte[noOfEntries];
                b = new byte[noOfEntries];
                int reserved;
                for (int i = 0; i < noOfEntries; i++) {
                    b[i] = (byte) is.read();
                    g[i] = (byte) is.read();
                    r[i] = (byte) is.read();
                    reserved = is.read();
                    curPos += 4;
                }
            }
        }

        protected void unpack(byte[] rawData, int rawOffset, int[] intData, int intOffset, int w) {
            int j = intOffset;
            int k = rawOffset;
            int mask = 0xff;
            for (int i = 0; i < w; i++) {
                int b0 = (((int) (rawData[k++])) & mask);
                int b1 = (((int) (rawData[k++])) & mask) << 8;
                int b2 = (((int) (rawData[k++])) & mask) << 16;
                intData[j] = 0xff000000 | b0 | b1 | b2;
                j++;
            }
        }

        protected void unpack(byte[] rawData, int rawOffset, int bpp, byte[] byteData, int byteOffset, int w) throws Exception {
            int j = byteOffset;
            int k = rawOffset;
            byte mask;
            int pixPerByte;
            switch (bpp) {
                case 1:
                    mask = (byte) 0x01;
                    pixPerByte = 8;
                    break;
                case 4:
                    mask = (byte) 0x0f;
                    pixPerByte = 2;
                    break;
                case 8:
                    mask = (byte) 0xff;
                    pixPerByte = 1;
                    break;
                default:
                    throw new Exception("Unsupported bits-per-pixel value");
            }
            for (int i = 0; ; ) {
                int shift = 8 - bpp;
                for (int ii = 0; ii < pixPerByte; ii++) {
                    byte br = rawData[k];
                    br >>= shift;
                    byteData[j] = (byte) (br & mask);
                    //System.out.println("Setting byteData[" + j + "]=" + Test.byteToHex(byteData[j]));
                    j++;
                    i++;
                    if (i == w) {
                        return;
                    }
                    shift -= bpp;
                }
                k++;
            }
        }

        protected int readScanLine(byte[] b, int off, int len) throws IOException {
            int bytesRead = 0;
            int l = len;
            int r = 0;
            while (len > 0) {
                bytesRead = is.read(b, off, len);
                if (bytesRead == -1) {
                    return r == 0 ? -1 : r;
                }
                if (bytesRead == len) {
                    return l;
                }
                len -= bytesRead;
                off += bytesRead;
                r += bytesRead;
            }
            return l;
        }

        protected void getPixelData() throws IOException, Exception {

            // Skip to the start of the bitmap data (if we are not already there)
            long skip = bitmapOffset - curPos;
            if (skip > 0) {
                is.skip(skip);
                curPos += skip;
            }
            int len = scanLineSize;
            if (bitsPerPixel > 8) {
                intData = new int[width * height];
            } else {
                byteData = new byte[width * height];
            }
            m_RawData = new byte[actualSizeOfBitmap];
            int rawOffset = 0;
            int offset = (height - 1) * width;
            for (int i = height - 1; i >= 0; i--) {
                int n = readScanLine(m_RawData, rawOffset, len);
                if (n < len) {
                    throw new Exception("Scan line ended prematurely after " + n + " bytes");
                }
                if (bitsPerPixel > 8) {
                    // Unpack and create one int per pixel
                    unpack(m_RawData, rawOffset, intData, offset, width);
                } else {
                    // Unpack and create one byte per pixel
                    unpack(m_RawData, rawOffset, bitsPerPixel, byteData, offset, width);
                }
                rawOffset += len;
                offset -= width;

            }
        }

        public byte[] BMP2MemoriaGrafica() {
            // color number of lines padded with zeroes to be a multiple of one bytes (x 8)
            int colSize = ((int) (width / 8) + (width % 8 > 0 ? 1 : 0)) * 8;
            // Scan line is padded with zeroes to be a multiple of two bytes (x 16)
            int lineSize = ((int) (width / 16) + (width % 16 > 0 ? 1 : 0)) * 16;

            long tam = 32l + levels * lineSize * height * numColors / 8;

            // Header of BMT file
            // 04 DF + 01/FF zipped/unzipped + FF FF FF
            // + colSize (word) + filas (word) + colorMask + levels + 00 00 00 00 00 00 00 01 00 00
            // +tam total colors (word)+ 00 00 + tam total (word) + 00 00 00 00
            String mem = "04 DF FF FF FF FF ";
            mem = mem.concat(long2Word(colSize, false)).concat(" ").concat(long2Word(height, false)).concat(" ");
            mem = mem.concat(dec2hexStr(colorMask)).concat(" ").concat(dec2hexStr(levels)).concat(" ");
            mem = mem.concat("00 00 00 00 00 00 01 00 ").concat(long2Dword(tam - 32,false)).concat(" ");
            mem = mem.concat(long2Dword(tam,false)).concat(" 00 00 00 00");

            byte[] head=toBytes(mem);
            byte[] raw = new byte[(int)tam];
            int cont=0;
            for(byte h:head){
                raw[cont++]=h;
            }
            for (int k = 0; k < levels; k++) {
                byte[][] planos = getPanelRawData(tonos[k]);
                for (int i = 0; i < numColors; i++) {
                    for (byte h : planos[i]) {
                       raw[cont++]=h;
                    }
                }
            }
            return raw;
        }

        // transforms bmp to rawData, without header, three splitted planes, according tonoMax
        private byte[][] getPanelRawData(int tonoMax) {

            int tam = scanLineSize; // (bmp row size, bytes, eg. 74) x 3
            int colSize = ((int) (width / 8) + (width % 8 > 0 ? 1 : 0)) * 8; // bytes multiple of 8, eg. 80
            int lineSize = ((int) (width / 16) + (width % 16 > 0 ? 1 : 0)) * 16; // bytes multiple of 16, eg. 80

            byte[][] panelData = new byte[numColors][height * lineSize / 8];

            int[] swapBMP = {2,1,0}; //  bmp: BLUE-GREEN-RED, swap->R,G,B

            for (int k = 0; k < numColors; k++) {
                int iPlano = swapBMP[k];
                // plane k
                int pos = 0;
                for (int j = height - 1; j >= 0; j--) {      // bmp row, from bottom to top
                    for (int i = k; i < lineSize * numColors + k; i += numColors * 8) { // columna del bmp
                        BitSet bs=new BitSet(8);
                        for (int m = 0; m < 8; m++) {
                            if (i + m * numColors < tam) {
                                if ((m_RawData[i + m * numColors + j * tam] & 0xff) >= tonoMax) {
                                    bs.set(7-m);
                                }
                            }
                        }
                        if (pos < lineSize * height / 8) {
                            panelData[iPlano][pos++] = (bs.toByteArray().length>0)? bs.toByteArray()[0]: 0;
                        }
                    }
                }
            }
            return panelData;
        }

        public void read(InputStream is) throws IOException, Exception {
            this.is = is;
            getFileHeader();
            getBitmapHeader();
            if (compression != 0) {
                throw new Exception("BMP Compression not supported");
            }
            getPalette();
            getPixelData();
        }

        protected int readInt() throws IOException {
            int b1 = is.read();
            int b2 = is.read();
            int b3 = is.read();
            int b4 = is.read();
            curPos += 4;
            return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));
        }

        protected short readShort() throws IOException {
            int b1 = is.read();
            int b2 = is.read();
            curPos += 4;
            return (short) ((b2 << 8) + b1);
        }

        public void reset(){
            r=null;
            g=null;
            b=null;
            byteData=null;
            intData=null;
            if(is!=null){
                try{
                    is.close();
                    is=null;
                } catch (IOException ioe){}
            }
            m_RawData=null;
            System.gc();
        }
    }

}
