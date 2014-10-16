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
import com.gravitygamesinteractive.pixelsdungeon.Assets;
import com.gravitygamesinteractive.pixelsdungeon.Badges;
import com.gravitygamesinteractive.pixelsdungeon.Dungeon;
import com.gravitygamesinteractive.pixelsdungeon.items.TomeOfMastery;
import com.gravitygamesinteractive.pixelsdungeon.items.armor.ClothArmor;
import com.gravitygamesinteractive.pixelsdungeon.items.bags.PotionBag;
import com.gravitygamesinteractive.pixelsdungeon.items.food.Food;
import com.gravitygamesinteractive.pixelsdungeon.items.potions.PotionOfStrength;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfShadows;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfIdentify;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfMagicMapping;
import com.gravitygamesinteractive.pixelsdungeon.items.wands.WandOfMagicMissile;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.melee.Dagger;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.melee.Knuckles;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.melee.PlasticHammerKyle;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.melee.ShortSword;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.missiles.Boomerang;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.missiles.Dart;
import com.watabou.utils.Bundle;

public enum HeroClass {

	KYLE( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[0] ), 
	IRIS( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[1] ), 
	CHOMP( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[2] ), 
	WHOMP( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[3] ), 
	BLAZE( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[4] ), 
	ASH( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[5] ), 
	SHADE( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[6] ), 
	BYTT( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.HERO_NAMES)[7] );
	
	private String title;
	
	private HeroClass( String title ) {
		this.title = title;
	}
	
	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		
		switch (this) {
		case KYLE:
			initKyle( hero );
			break;
			
		case IRIS:
			initIris( hero );
			break;
			
		case CHOMP:
			initChomp( hero );
			break;
			
		case WHOMP:
			initWhomp( hero );
			break;
			
		case BLAZE:
			initBlaze( hero );
			break;
			
		case ASH:
			initAsh( hero );
			break;
			
		case SHADE:
			initShade( hero );
			break;
			
		case BYTT:
			initBytt( hero );
			break;
		}
		
		hero.updateAwareness();
	}
	
	private static void initKyle( Hero hero ) {
		hero.STR = hero.STR + 1;
		
		(hero.belongings.weapon = new PlasticHammerKyle()).identify();
		//(hero.belongings.armor = new ClothArmor()).identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		new PotionBag().collect();
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_KYLE )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new PotionOfStrength().setKnown();
	}
	
	private static void initIris( Hero hero ) {	
		(hero.belongings.weapon = new Knuckles()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		new Food().identify().collect();
		
		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_IRIS )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = wand;
		
		new ScrollOfIdentify().setKnown();
	}
	
	private static void initChomp( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_CHOMP )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initWhomp( Hero hero ) {
		
		hero.HP = (hero.HT -= 5);
		
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();
		new Food().identify().collect();
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_WHOMP )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = boomerang;
	}
	
	private static void initBlaze( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_BLAZE )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initAsh( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_ASH )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initShade( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_SHADE )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initBytt( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.armor = new ClothArmor()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		new Food().identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		if (Badges.isUnlocked( Badges.Badge.MASTERY_BYTT )) {
			new TomeOfMastery().collect();
		}
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case KYLE:
			return Assets.KYLE;
		case IRIS:
			return Assets.IRIS;
		case CHOMP:
			return Assets.CHOMP;
		case WHOMP:
			return Assets.WHOMP;
		case BLAZE:
			return Assets.BLAZE;
		case ASH:
			return Assets.ASH;
		case SHADE:
			return Assets.SHADE;
		case BYTT:
			return Assets.BYTT;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case KYLE:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.KYLE_PERKS);
		case IRIS:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.IRIS_PERKS);
		case CHOMP:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.CHOMP_PERKS);
		case WHOMP:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.WHOMP_PERKS);
		case BLAZE:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.BLAZE_PERKS);
		case ASH:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.ASH_PERKS);
		case SHADE:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.SHADE_PERKS);
		case BYTT:
			return App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.BYTT_PERKS);
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : CHOMP;
	}
}
