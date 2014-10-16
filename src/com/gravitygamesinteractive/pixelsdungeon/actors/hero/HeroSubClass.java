/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.gravitygamesinteractive.pixelsdungeon.actors.hero;

import com.gravitygamesinteractive.pixelsdungeon.App;
import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE( null, null ),
	
	GLADIATOR( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[0], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[0] ),
		
	BERSERKER( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[1], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[1] ),
	
	WARLOCK( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[2], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[2] ),

	BATTLEMAGE( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[3], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[3] ),
	
	ASSASSIN( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[4], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[4] ),

	FREERUNNER( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[5], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[5] ),
		
	SNIPER( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[6], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[6] ),

	WARDEN( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_NAMES)[7], 
			App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_SUBCLASS_DESC)[7] );
	
	private String title;
	private String desc;
	
	private HeroSubClass( String title, String desc ) {
		this.title = title;
		this.desc = desc;
	}
	
	public String title() {
		return title;
	}
	
	public String desc() {
		return desc;
	}
	
	private static final String SUBCLASS	= "subClass";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( SUBCLASS, toString() );
	}
	
	public static HeroSubClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( SUBCLASS );
		try {
			return valueOf( value );
		} catch (Exception e) {
			return NONE;
		}
	}
	
}
