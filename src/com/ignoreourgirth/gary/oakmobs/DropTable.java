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

public enum DropTable {

	EnderDragon (0.0, 000, 000),
	Blaze       (0.2, 200, 500),
	Creeper     (0.5, 040, 100),
	Enderman    (0.1, 100, 200),
	Giant       (0.5, 100, 600),
	IronGolem   (1.0, 050, 100),
	MagmaCube   (1.0, 015, 025),
	PigZombie   (0.8, 075, 120),
	Skeleton    (0.4, 050, 130),
	Slime       (1.0, 010, 020),
	Spider      (0.4, 050, 130),
	Villager    (1.0, 002, 015),
	Zombie      (0.4, 040, 130),
	CaveSpider  (0.0, 000, 000), //mob spawner mob
	Ghast       (0.0, 000, 000), //would just drop into lava anyways
	_unknown    (0.0, 000, 000); //all others
	
	public Double  dropRate;
	public int     moneyMin;
	public int     moneyMax;
	
	private DropTable(double ev1, int ev2, int ev3){
		dropRate = ev1;
		moneyMin = ev2;
		moneyMax = ev3;
	}
	
}
