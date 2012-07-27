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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AsynchronousProcessing implements Runnable {

	public boolean running;
	
	private static final int maxYDistance = 8;
	private static final int minXZDistance = 18;
	private static final int maxXZDistance = 23;
	private static final int maxMobsPerPlayer = 20;
	
	private int difference;
	private int id_air;
	private int id_lava;
	private int id_water;
	private int id_stationary_lava;
	private int id_stationary_water;
	

	public AsynchronousProcessing(){
		id_air = Material.AIR.getId();
		id_lava = Material.LAVA.getId();
		id_water = Material.WATER.getId();
		id_stationary_lava = Material.STATIONARY_LAVA.getId();
		id_stationary_water = Material.STATIONARY_WATER.getId();
		difference = maxXZDistance - minXZDistance;
		running = true;
	}
	
	@Override
	public void run() {
		while (running) {
			try {Thread.sleep(1000);} catch (InterruptedException e) {continue;}
			
			for (Player player : OakMobs.server.getOnlinePlayers()) {
				
				World playerWorld = player.getWorld();
				SpawnTable spawnTable = OakMobs.spawnTables.get(playerWorld);
				Location playerLocation = player.getLocation();
				
				// Let's see if we should even consider spawning a mob.
				if (spawnTable == null) {continue;}
				if (!OakMobs.nearbyMobCounts.containsKey(player)) continue;
				if (OakMobs.nearbyMobCounts.get(player) >= maxMobsPerPlayer) continue;
				
				// Okay fine, let's spawn a mob. First let's choose random nearby location.
				int randomXExtra = (int) ((OakMobs.randomGen.nextDouble() * difference) + minXZDistance); 
				int randomZExtra = (int) ((OakMobs.randomGen.nextDouble() * difference) + minXZDistance);
				if (OakMobs.randomGen.nextBoolean()) {randomXExtra = randomXExtra * -1;}
				if (OakMobs.randomGen.nextBoolean()) {randomZExtra = randomZExtra * -1;}
				int calculatedX = playerLocation.getBlockX() + randomXExtra;
				int calculatedZ = playerLocation.getBlockZ() + randomZExtra;
				int calculatedY = playerLocation.getBlockY();
				
				// From the random location we picked let's find a valid spawn point.
				boolean blockAIsValid = false; boolean blockBIsValid = false; boolean blockCIsValid = true;
				int potentialBlockTypeA; int potentialBlockTypeB; int potentialBlockTypeC;
				int totalLoops = 0;
				while (! (blockAIsValid  || blockBIsValid) && totalLoops < maxXZDistance) {
					calculatedX++; calculatedY++; calculatedZ++; totalLoops++;
					potentialBlockTypeA = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY, calculatedZ);
					potentialBlockTypeB = playerWorld.getBlockTypeIdAt(calculatedX * -1, calculatedY * -1, calculatedZ * -1);
					blockAIsValid = (potentialBlockTypeA == id_air);
					blockBIsValid = (potentialBlockTypeB == id_air);
				}
				if (!blockAIsValid && blockBIsValid) {
					calculatedX = calculatedX * -1;
					calculatedZ = calculatedZ * -1;
					calculatedY = calculatedY * -1;
				} else if (!blockAIsValid && !blockBIsValid) {
					continue;
				}
				int distanceXFromPlayer = Math.abs(calculatedX - playerLocation.getBlockX());
				int distanceZFromPlayer = Math.abs(calculatedZ - playerLocation.getBlockZ());
				if (distanceXFromPlayer > maxXZDistance || distanceXFromPlayer < minXZDistance || distanceZFromPlayer > maxXZDistance || distanceZFromPlayer < minXZDistance) {
					continue;
				}

				// We want to spawn mobs on the ground, not way up in the air.
				totalLoops = 0;
				while (blockCIsValid && totalLoops < 200) {
					calculatedY--; totalLoops++;
					potentialBlockTypeC = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY, calculatedZ);
					blockCIsValid = (potentialBlockTypeC == id_air);
				}
				calculatedY++;
				Location spawnLocation = new Location(playerWorld, calculatedX, calculatedY, calculatedZ);
				
				// Let's not waste time. If it shouldn't be spawned here just forget this spawn location.
				int distanceYFromPlayer = Math.abs(calculatedY - playerLocation.getBlockY());
				if (distanceYFromPlayer > maxYDistance) continue;

				// Okay, we found a block that is valid but let's make sure the entity will have room to spawn.
				int type1 = playerWorld.getBlockTypeIdAt(calculatedX - 1, calculatedY, calculatedZ);
				int type2 = playerWorld.getBlockTypeIdAt(calculatedX + 1, calculatedY, calculatedZ);
				int type3 = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY, calculatedZ - 1);
				int type4 = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY, calculatedZ + 1);
				int type5 = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY + 1, calculatedZ);
				int type6 = playerWorld.getBlockTypeIdAt(calculatedX, calculatedY - 1, calculatedZ);
				if (type1 == id_air && 
					type2 == id_air &&
					type3 == id_air && 
					type4 == id_air &&
					type5 == id_air &&
					type6 != id_lava &&
					type6 != id_water &&
					type6 != id_stationary_lava &&
					type6 != id_stationary_water)
				{
					// Finally, let's que this mob to get spawned.
					OakMobs.spawnsQueued.add(spawnLocation);
				}
			}
		}
	}

}
