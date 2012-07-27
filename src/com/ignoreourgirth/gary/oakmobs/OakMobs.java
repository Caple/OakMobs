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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

//import com.ignoreourgirth.gary.oakmobs.spawntables.NetherSpawnTable;
import com.ignoreourgirth.gary.oakmobs.spawntables.NormalSpawnTable;

public class OakMobs extends JavaPlugin  {
	
	static final int dropEntityID = 371;
	static final short myPluginDropID = 7002;
	
	public static Logger log;
	public static Random randomGen;
	public static Server server;

	public static SynchronizedProcessing syncTask;
	public static AsynchronousProcessing asyncTask;
	
    public static HashSet<Integer> doNotDropList;
    public static Hashtable<World, SpawnTable> spawnTables;
    public static Hashtable<Player, Integer> nearbyMobCounts;
    public static ArrayList<Location> spawnsQueued;
	
	public void onEnable() {
		
		log = this.getLogger();
		server = this.getServer();
		randomGen = new java.util.Random();
		spawnsQueued = new ArrayList<Location>();
		doNotDropList = new HashSet<Integer>();
        spawnTables = new Hashtable<World, SpawnTable>();
        nearbyMobCounts = new Hashtable<Player, Integer>();

        SpawnTable sT1 = new NormalSpawnTable(); sT1.initValues(); spawnTables.put(server.getWorld("new_world"), sT1);
        //SpawnTable sT2 = new NetherSpawnTable(); sT2.initValues(); spawnTables.put(server.getWorld("new_world_nether"), sT2);
        syncTask = new SynchronizedProcessing();
        asyncTask = new AsynchronousProcessing();
      
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		server.getScheduler().scheduleSyncRepeatingTask(this, syncTask, 40L, 40L);
		new Thread(asyncTask).start();
		
		log.info("OakMobs enabled."); 
		
	}
	
	public void onDisable() {
		asyncTask.running = false;
		server.getScheduler().cancelTasks(this);
		log.info("OakMobs disabled.");
	}
	
}
