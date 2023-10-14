package com.java.carsimulator;


/**
	This class stores global setting variables for the program.
	
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

public class Settings {
	
	/** The amount of change to the x,y,z axis when moving camera in free mode */
    public static float CameraShift = 20.0f;
    /** The amount of change to the steering angle */
    public static float SteeringAngleChange = 10.0f;
        
    /** Show collision boundaries  */
    public static boolean DrawCollisionBounds = false;
    
    /** The maximum steering angle of the car */
    public static float MaxSteeringAngle = 120.0f; //450
    
    /** The maximum steering angle of the car */
    public static float MaxWheelAngle = 30.0f;
    
    /** Wheel axis line length  */
    public static int WheelAxisLength = 20;
    
}
