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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

//entity type, damage value, drop rate, money range minimum, money range max
public class DropDataCompiler {
	
	private int numberOfFullStacks = 0;
	private int numberOfItemsInFinalStack = 0;
	private int moneyRemaining = 0;
	private ArrayList<ItemStack> stacks;

	DropDataCompiler(org.bukkit.entity.Entity entity) {
		DropTable table;
		if(entity instanceof org.bukkit.entity.Blaze) {
			table = DropTable.Blaze;
		} else if(entity instanceof org.bukkit.entity.CaveSpider) {
			table = DropTable.CaveSpider;
		} else if(entity instanceof org.bukkit.entity.Creeper) {
			table = DropTable.Creeper;
		} else if(entity instanceof org.bukkit.entity.EnderDragon) {
			table = DropTable.EnderDragon;
		} else if(entity instanceof org.bukkit.entity.Enderman) {
			table = DropTable.Enderman;
		} else if(entity instanceof org.bukkit.entity.Ghast) {
			table = DropTable.Ghast;
		} else if(entity instanceof org.bukkit.entity.Giant) {
			table = DropTable.Giant;
		} else if(entity instanceof org.bukkit.entity.IronGolem) {
			table = DropTable.IronGolem;
		} else if(entity instanceof org.bukkit.entity.PigZombie) {
			table = DropTable.PigZombie;
		} else if(entity instanceof org.bukkit.entity.Skeleton) {
			table = DropTable.Skeleton;
		} else if(entity instanceof org.bukkit.entity.Slime) {
			table = DropTable.Slime;
		} else if(entity instanceof org.bukkit.entity.Spider) {
			table = DropTable.Spider;
		} else if(entity instanceof org.bukkit.entity.Villager) {
			table = DropTable.Villager;
		} else if(entity instanceof org.bukkit.entity.Zombie) {
			table = DropTable.Zombie;
		} else {
			table = DropTable._unknown;
		}
		
		if (table != DropTable._unknown) {
			if (OakMobs.doNotDropList.contains(entity.getEntityId())) {
				OakMobs.doNotDropList.remove(entity.getEntityId());
			} else {
				java.util.Random randomGen = new java.util.Random();
				double chance = randomGen.nextDouble();
				if (table.dropRate >= chance) {
					int totalMoney = randomGen.nextInt(table.moneyMax - table.moneyMin + 1) + table.moneyMin;
					compileDropInformation(totalMoney);
				}
			}
		}
		compileStacks();
    }
	
	DropDataCompiler(double ammount) {
		compileDropInformation(ammount);
		compileStacks();
	}
	
	public ArrayList<ItemStack> getStacks() {
		return stacks;
	}
	
	private void compileDropInformation(double totalMoneyToDrop) {
		int itemsRemaining;
		while (totalMoneyToDrop > 0) {
			itemsRemaining = (int) Math.floor(totalMoneyToDrop / Short.MAX_VALUE);
			if (itemsRemaining > 64) {
				numberOfFullStacks ++;
				totalMoneyToDrop -= (Short.MAX_VALUE * 64);
			} else {
				if (itemsRemaining > 0) {
					numberOfItemsInFinalStack = itemsRemaining;
				}
				moneyRemaining = (short) (totalMoneyToDrop % Short.MAX_VALUE);	
				totalMoneyToDrop = 0;	
			}
        }
	}
	
	private void compileStacks() {
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
		int loopCount = numberOfFullStacks;
	    while (loopCount > 0) {
	    	ItemStack stack = new ItemStack(OakMobs.dropEntityID, 64, OakMobs.myPluginDropID);
	    	stack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
	    	stackList.add(stack);
	    	loopCount --;
	    }
		if (numberOfItemsInFinalStack > 0) {
	    	ItemStack stack = new ItemStack(OakMobs.dropEntityID, numberOfItemsInFinalStack, OakMobs.myPluginDropID);
	    	stack.addUnsafeEnchantment(Enchantment.DURABILITY, moneyRemaining);
	    	stackList.add(stack);
		} else if (moneyRemaining > 0) {
	    	ItemStack stack = new ItemStack(OakMobs.dropEntityID, 1, OakMobs.myPluginDropID);
	    	stack.addUnsafeEnchantment(Enchantment.DURABILITY, moneyRemaining);
	    	stackList.add(stack);
		}
		stacks = stackList;
	}
	
}
