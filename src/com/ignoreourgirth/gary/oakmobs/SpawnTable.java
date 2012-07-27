/*******************************************************************************
 * Copyright (c) 2012 GaryMthrfkinOak (Jesse Caple).
 * 
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ignoreourgirth.gary.oakmobs;

import org.bukkit.entity.EntityType;

public abstract class SpawnTable {

	private int totalWeight;
	private int[] childWeights;
	private EntityType[] childMobs;
	
	abstract protected int[] weights();
	abstract protected EntityType[] mobs();
	
	public void initValues() {
		childWeights = weights();
		childMobs = mobs();
		for (int weight : childWeights)
		{
		    totalWeight += weight;
		}
	}
	
	public EntityType getMob() {
		int randomIndex = -1;
		double randomDbl = OakMobs.randomGen.nextDouble() * totalWeight;
		for (int i = 0; i < childWeights.length; ++i)
		{
		    randomDbl -= childWeights[i];
		    if (randomDbl <= 0.0d)
		    {
		        randomIndex = i;
		        break;
		    }
		}
		return childMobs[randomIndex];
	}

}
