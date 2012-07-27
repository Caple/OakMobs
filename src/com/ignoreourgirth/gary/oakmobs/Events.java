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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.ignoreourgirth.gary.oakcorelib.OakCoreLib;

public class Events implements Listener {
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.SPAWNER) {
			OakMobs.doNotDropList.add(event.getEntity().getEntityId());
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity eventEntity = event.getEntity();
		DropDataCompiler dropData;
		if (eventEntity instanceof Player) {
			Player deadPlayer = (Player) eventEntity;
			Double moneyLost = OakCoreLib.getEconomy().getBalance(deadPlayer.getName());
			OakCoreLib.getEconomy().bankWithdraw(deadPlayer.getName(), moneyLost);
			dropData = new DropDataCompiler(moneyLost);
		} else {
			EntityDamageEvent damageEvent = eventEntity.getLastDamageCause();
			if (!OakMobs.doNotDropList.contains(eventEntity.getEntityId())) {
				if (damageEvent == null) {
					OakMobs.doNotDropList.add(eventEntity.getEntityId());
				} else if (damageEvent.equals(DamageCause.DROWNING) || 
						damageEvent.equals(DamageCause.FIRE) ||
						damageEvent.equals(DamageCause.FIRE_TICK) || 
						damageEvent.equals(DamageCause.LIGHTNING) ||
						damageEvent.equals(DamageCause.CONTACT) ||
						damageEvent.equals(DamageCause.FALL) ||
						damageEvent.equals(DamageCause.LAVA)) {
					OakMobs.doNotDropList.add(eventEntity.getEntityId());
				}
			}
			dropData = new DropDataCompiler(eventEntity);
		}
		for (ItemStack stack : dropData.getStacks()) {
			event.getDrops().add(stack);
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player eventPlayer = event.getPlayer();
		Item eventItem = event.getItem();
		if(eventItem.getItemStack().getDurability() == OakMobs.myPluginDropID) {
			if(! event.isCancelled()) {
				int worth;
				if (eventItem.getItemStack().getAmount() > 1)
				{
					 worth = Short.MAX_VALUE * eventItem.getItemStack().getAmount();
					 worth += eventItem.getItemStack().getEnchantmentLevel(Enchantment.DURABILITY);
				} else {
					worth = eventItem.getItemStack().getEnchantmentLevel(Enchantment.DURABILITY);
				}
				if (worth == 1) {
					eventPlayer.sendMessage("§eYou picked up " + worth + " " + OakCoreLib.getEconomy().currencyNameSingular() + ".");
				} else {
					eventPlayer.sendMessage("§eYou picked up " + worth + " " + OakCoreLib.getEconomy().currencyNamePlural() + ".");
				}
				OakCoreLib.getEconomy().bankDeposit(eventPlayer.getName(), worth);
				eventItem.remove();
			}
			event.setCancelled(true);
		}
	}
}
