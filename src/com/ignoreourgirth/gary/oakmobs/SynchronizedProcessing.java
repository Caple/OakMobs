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

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.ignoreourgirth.gary.oakcorelib.OakCoreLib;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class SynchronizedProcessing implements Runnable{
	
	private static final int maxLightLevel = 5;
	private static final int maxSpawnCheckHorizontal = 50;
	private static final int maxSpawnCheckVerticle = 20;
	
	@Override
	public void run() {
		synchronized(OakMobs.nearbyMobCounts) {
			for (Player player : OakMobs.server.getOnlinePlayers()) {
				int mobCount = 0;
				for(Entity entity : player.getNearbyEntities(maxSpawnCheckHorizontal, maxSpawnCheckVerticle, maxSpawnCheckHorizontal)) {
				    if(entity instanceof Monster) mobCount ++;
				}
				if (OakMobs.nearbyMobCounts.containsKey(player)) OakMobs.nearbyMobCounts.remove(player);
				OakMobs.nearbyMobCounts.put(player, mobCount);
			}
		}
		@SuppressWarnings("unchecked")
		ArrayList<Location> locations = (ArrayList<Location>) OakMobs.spawnsQueued.clone();
		for (Location location : locations) {
			if (location.getBlock().getLightLevel() > maxLightLevel) {continue;}
			if (!OakCoreLib.getWorldGuard().getRegionManager(location.getWorld()).getApplicableRegions(location).allows(DefaultFlag.MOB_SPAWNING)) {continue;}
			if (TownyUniverse.getTownBlock(location) != null) {continue;}
			World locationWorld = location.getWorld();
			SpawnTable spawnTable = OakMobs.spawnTables.get(locationWorld);
			locationWorld.spawnCreature(location, spawnTable.getMob());
		}
		OakMobs.spawnsQueued.clear();
	}
}
