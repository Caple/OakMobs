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
package com.ignoreourgirth.gary.oakmobs.spawntables;

import org.bukkit.entity.EntityType;
import com.ignoreourgirth.gary.oakmobs.SpawnTable;

public class NetherSpawnTable extends SpawnTable {

	int[]     weights = {15, 5, 1};
	EntityType[] mobs = {EntityType.PIG_ZOMBIE, EntityType.BLAZE, EntityType.GHAST};
	 
	@Override protected int[] weights()  {return weights;}
	@Override protected EntityType[] mobs() {return mobs;}
	
}
