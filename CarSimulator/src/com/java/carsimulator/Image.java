package com.java.carsimulator;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;

/**
	This class loads a bitmap image with a transparent colour and creates
	an image with an alpha channel.
	
	@version 1.0
	@modified 9/11/2011
	@author Lawrence Schmid<BR><BR>
	
	This file is part of Space War.<BR><BR>
	
	Space War is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.<BR><BR>
	
	Space War  is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.<BR><BR>
	
	You should have received a copy of the GNU General Public License
	along with Space War.  If not, see http://www.gnu.org/licenses/.<BR><BR>
	
	Copyright 2011 Lawrence Schmid
*/

public class Image {

	/** The buffered image with alpha channel */
    private BufferedImage mImage;

    /**
     * Constructs the Image
     * @param path The file input path
     */
    public Image(String path) {
        try {
            File file = new File(path);
            mImage = ImageIO.read(file);
            //create a BufferedImage with alpha channel
            BufferedImage img = new BufferedImage(mImage.getWidth(), mImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.drawImage(mImage, 0, 0, null);
            //set rgb (168,230,29) transparent
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    if (img.getRGB(i, j) == new Color(181, 230, 29).getRGB()) {
                        img.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
                    }
                }
            }

            mImage = img; 
        } catch (Exception e) {
            mImage = null;
            e.printStackTrace();
        }
    }

    /**
     * Returns the image containing an alpha channel
     * @return The image with an alpha channel
     */
    public BufferedImage getImage() {
        return mImage;
    }
    
    /**
     * Returns a sub image defined by a specified rectangular region. 
     * The returned BufferedImage shares the same data array as the original image. 
	 * @param x - the X coordinate of the upper-left corner of the specified rectangular region
     * @param y - the Y coordinate of the upper-left corner of the specified rectangular region
     * @param w - the width of the specified rectangular region
     * @param h - the height of the specified rectangular region 
     * @return A BufferedImage that is the sub image of this BufferedImage. 
     */
    public BufferedImage getSubImage(int x, int y, int w, int h)
    {
        return mImage.getSubimage(x, y, w, h);
    }   
}
