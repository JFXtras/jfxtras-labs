/**
 * LocalTileSource.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
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

package jfxtras.labs.map.tile.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import jfxtras.labs.map.tile.DefaultTileSource;

/**
 * This is a tile source for file based tiles
 *
 * @author Mario Schroeder
 * @author jsmith
 *
 */
public class LocalTileSource extends DefaultTileSource {

    private String tilesRootDir;
    
    public LocalTileSource(String name, String base_url) {
        super(name, base_url);
    }

    void setTilesRootDir(String tilesRootDir) {
        this.tilesRootDir = tilesRootDir;
    }

    @Override
    public String getTileUrl(int zoom, int tilex, int tiley) {
        return tilesRootDir + File.separator + getTilePath(zoom, tilex, tiley);
    }
    
    @Override
    public int getMinZoom() {
    	int zoom = super.getMinZoom();
    	LinkedList<String> dirs = listZoomDirs();
    	if(!dirs.isEmpty()){
    		zoom = Integer.parseInt(dirs.getFirst());
    	}
    	return zoom;
    }
    
    @Override
    public int getMaxZoom() {
    	int zoom = super.getMaxZoom();
    	LinkedList<String> dirs = listZoomDirs();
    	if(!dirs.isEmpty()){
    		zoom = Integer.parseInt(dirs.getLast());
    	}
    	return zoom;
    }
    
    private LinkedList<String> listZoomDirs(){
    	LinkedList<String> dirs = new LinkedList<>();
    	Path root = Paths.get(tilesRootDir);
    	try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(root)) {
            for (Path path : directoryStream) {
            	//TODO check if the name is a number
                dirs.add(path.getFileName().toString());
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    	return dirs;
    }
}
