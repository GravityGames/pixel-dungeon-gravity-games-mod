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

import java.util.ArrayList;
import java.util.HashSet;

import com.gravitygamesinteractive.pixelsdungeon.App;
import com.gravitygamesinteractive.pixelsdungeon.Assets;
import com.gravitygamesinteractive.pixelsdungeon.Badges;
import com.gravitygamesinteractive.pixelsdungeon.Bones;
import com.gravitygamesinteractive.pixelsdungeon.Dungeon;
import com.gravitygamesinteractive.pixelsdungeon.GamesInProgress;
import com.gravitygamesinteractive.pixelsdungeon.actors.Actor;
import com.gravitygamesinteractive.pixelsdungeon.actors.Char;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Barkskin;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Bleeding;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Blindness;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Buff;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Burning;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Charm;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Combo;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Cripple;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Fury;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.GasesImmunity;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Hunger;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Invisibility;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Light;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Ooze;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Paralysis;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Poison;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Regeneration;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Roots;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.SnipersMark;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Weakness;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.Mob;
import com.gravitygamesinteractive.pixelsdungeon.effects.CheckedCell;
import com.gravitygamesinteractive.pixelsdungeon.effects.Flare;
import com.gravitygamesinteractive.pixelsdungeon.effects.Speck;
import com.gravitygamesinteractive.pixelsdungeon.items.Amulet;
import com.gravitygamesinteractive.pixelsdungeon.items.Ankh;
import com.gravitygamesinteractive.pixelsdungeon.items.DewVial;
import com.gravitygamesinteractive.pixelsdungeon.items.Dewdrop;
import com.gravitygamesinteractive.pixelsdungeon.items.Heap;
import com.gravitygamesinteractive.pixelsdungeon.items.Item;
import com.gravitygamesinteractive.pixelsdungeon.items.KindOfWeapon;
import com.gravitygamesinteractive.pixelsdungeon.items.Heap.Type;
import com.gravitygamesinteractive.pixelsdungeon.items.armor.Armor;
import com.gravitygamesinteractive.pixelsdungeon.items.keys.GoldenKey;
import com.gravitygamesinteractive.pixelsdungeon.items.keys.IronKey;
import com.gravitygamesinteractive.pixelsdungeon.items.keys.Key;
import com.gravitygamesinteractive.pixelsdungeon.items.keys.SkeletonKey;
import com.gravitygamesinteractive.pixelsdungeon.items.potions.PotionOfStrength;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfAccuracy;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfDetection;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfElements;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfEvasion;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfHaste;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfShadows;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.RingOfThorns;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfMagicMapping;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfRecharging;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfUpgrade;
import com.gravitygamesinteractive.pixelsdungeon.items.wands.Wand;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.Weapon;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.melee.MeleeWeapon;
import com.gravitygamesinteractive.pixelsdungeon.levels.Level;
import com.gravitygamesinteractive.pixelsdungeon.levels.Terrain;
import com.gravitygamesinteractive.pixelsdungeon.levels.features.AlchemyPot;
import com.gravitygamesinteractive.pixelsdungeon.levels.features.Chasm;
import com.gravitygamesinteractive.pixelsdungeon.plants.Earthroot;
import com.gravitygamesinteractive.pixelsdungeon.scenes.GameScene;
import com.gravitygamesinteractive.pixelsdungeon.scenes.InterlevelScene;
import com.gravitygamesinteractive.pixelsdungeon.scenes.SurfaceScene;
import com.gravitygamesinteractive.pixelsdungeon.sprites.CharSprite;
import com.gravitygamesinteractive.pixelsdungeon.sprites.HeroSprite;
import com.gravitygamesinteractive.pixelsdungeon.ui.AttackIndicator;
import com.gravitygamesinteractive.pixelsdungeon.ui.BuffIndicator;
import com.gravitygamesinteractive.pixelsdungeon.ui.QuickSlot;
import com.gravitygamesinteractive.pixelsdungeon.utils.GLog;
import com.gravitygamesinteractive.pixelsdungeon.windows.WndMessage;
import com.gravitygamesinteractive.pixelsdungeon.windows.WndResurrect;
import com.gravitygamesinteractive.pixelsdungeon.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hero extends Char {
	
	public static final int STARTING_STR = 10;
	
	private static final float TIME_TO_REST		= 1f;
	private static final float TIME_TO_SEARCH	= 2f;
	
	public HeroClass heroClass = HeroClass.CHOMP;
	public HeroSubClass subClass = HeroSubClass.NONE;
	
	private int attackSkill = 10;
	private int defenseSkill = 5;
	
	public boolean ready = false;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;
	
	private Char enemy;
	
	public Armor.Glyph killerGlyph = null;
	
	private Item theKey;
	
	public boolean restoreHealth = false;
	
	public boolean usingRanged = false;
	public Belongings belongings;
	
	public int STR;
	public boolean weakened = false;
	
	public float awareness;
	
	public int lvl = 1;
	public int exp = 0;
	
	private ArrayList<Mob> visibleEnemies; 
	
	public Hero() {
		super();
		name = "you";
		
		HP = HT = 20;
		STR = STARTING_STR;
		awareness = 0.1f;
		
		belongings = new Belongings( this );
		
		visibleEnemies = new ArrayList<Mob>();
	}

	public int STR() {
		return weakened ? STR - 2 : STR;
	}

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "defenseSkill";
	private static final String STRENGTH	= "STR";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		heroClass.storeInBundle( bundle );
		subClass.storeInBundle( bundle );
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		
		bundle.put( STRENGTH, STR );
		
		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		
		belongings.storeInBundle( bundle );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		heroClass = HeroClass.restoreInBundle( bundle );
		subClass = HeroSubClass.restoreInBundle( bundle );
		
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		
		STR = bundle.getInt( STRENGTH );
		updateAwareness();
		
		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );
		
		belongings.restoreFromBundle( bundle );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		// Refactoring needed!
		Armor armor = (Armor)bundle.get( "armor" );
		info.armor = armor == null ? 0 : armor.tier;
		info.level = bundle.getInt( LEVEL );
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}
	
	public void live() {
		Buff.affect( this, Regeneration.class );	
		Buff.affect( this, Hunger.class );
	}
	
	public int tier() {
		return belongings.armor == null ? 0 : belongings.armor.tier;
	}
	
	public boolean shoot( Char enemy, Weapon wep ) {
		
		// Ugly...
		usingRanged = true;
		
		KindOfWeapon curWep = belongings.weapon;
		belongings.weapon = wep;
		
		boolean result = attack( enemy );
		
		belongings.weapon = curWep;
		usingRanged = false;
		
		return result;
	}
	
	@Override
	public int attackSkill( Char target ) {
		
		int bonus = 0;
		for (Buff buff : buffs( RingOfAccuracy.Accuracy.class )) {
			bonus += ((RingOfAccuracy.Accuracy)buff).level;
		}
		float accuracy = (bonus == 0) ? 1 : (float)Math.pow( 1.4, bonus );
		if (usingRanged && Level.distance( pos, target.pos ) == 1) {
			accuracy *= 0.5f;
		}
		
		if (belongings.weapon != null) {
			return (int)(attackSkill * accuracy * belongings.weapon.acuracyFactor( this ));
		} else {
			return (int)(attackSkill * accuracy);
		}
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		
		int bonus = 0;
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			bonus += ((RingOfEvasion.Evasion)buff).level;
		}
		float evasion = bonus == 0 ? 1 : (float)Math.pow( 1.2, bonus );
		if (paralysed) {
			evasion /= 2;
		}
		
		int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
		
		if (aEnc > 0) {
			return (int)(defenseSkill * evasion / Math.pow( 1.5, aEnc ));
		} else {
			
			if (heroClass == HeroClass.CHOMP) {
				
				if (curAction != null && subClass == HeroSubClass.FREERUNNER && !isStarving()) {
					evasion *= 2;
				}
				
				return (int)((defenseSkill - aEnc) * evasion);
			} else {
				return (int)(defenseSkill * evasion);
			}
		}
	}
	
	@Override
	public int dr() {
		int dr = belongings.armor != null ? Math.max( belongings.armor.DR, 0 ) : 0;
		Barkskin barkskin = buff( Barkskin.class );
		if (barkskin != null) {
			dr += barkskin.level();
		}
		return dr;
	}
	
	@Override
	public int damageRoll() {
		int dmg;
		if (belongings.weapon != null) {	
			dmg = belongings.weapon.damageRoll( this );
		} else {
			dmg = STR() > 10 ? Random.IntRange( 1, STR() - 9 ) : 1;
		}
		return buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
	}
	
	@Override
	public float speed() {
		
		int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
		if (aEnc > 0) {
			
			return (float)(super.speed() * Math.pow( 1.3, -aEnc ));
			
		} else {
			
			float speed = super.speed();
			return ((HeroSprite)sprite).sprint( subClass == HeroSubClass.FREERUNNER && !isStarving() ) ? 1.6f * speed : speed;
			
		}
	}
	
	public float attackDelay() {
		if (belongings.weapon != null) {
			
			return belongings.weapon.speedFactor( this );
						
		} else {
			return 1f;
		}
	}
	
	@Override
	public void spend( float time ) {
		int hasteLevel = 0;
		for (Buff buff : buffs( RingOfHaste.Haste.class )) {
			hasteLevel += ((RingOfHaste.Haste)buff).level;
		}
		super.spend( hasteLevel == 0 ? time : (float)(time * Math.pow( 1.1, -hasteLevel )) );
	};
	
	public void spendAndNext( float time ) {
		busy();
		spend( time );
		next();
	}
	
	@Override
	public boolean act() {
		
		super.act();
		
		if (paralysed) {
			
			curAction = null;
			
			spendAndNext( TICK );
			return false;
		}
		
		checkVisibleMobs();
		AttackIndicator.updateState();
		
		if (curAction == null) {
			
			if (restoreHealth) {
				if (isStarving() || HP >= HT) {
					restoreHealth = false;
				} else {
					spend( TIME_TO_REST ); next();
					return false;
				}
			}
			
			ready();
			
		} else {
			
			restoreHealth = false;
			
			ready = false;
			
			if (curAction instanceof HeroAction.Move) {
				
				actMove( (HeroAction.Move)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Interact) {
				
				actInteract( (HeroAction.Interact)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Buy) {
				
				actBuy( (HeroAction.Buy)curAction );
				
			}else 
			if (curAction instanceof HeroAction.PickUp) {
				
				actPickUp( (HeroAction.PickUp)curAction );
				
			} else 
			if (curAction instanceof HeroAction.OpenChest) {
				
				actOpenChest( (HeroAction.OpenChest)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Unlock) {
				
				actUnlock( (HeroAction.Unlock)curAction );
				
			} else 
			if (curAction instanceof HeroAction.Descend) {
				
				actDescend( (HeroAction.Descend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Ascend) {
				
				actAscend( (HeroAction.Ascend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Attack) {

				actAttack( (HeroAction.Attack)curAction );
				
			} else
			if (curAction instanceof HeroAction.Cook) {

				actCook( (HeroAction.Cook)curAction );
				
			}
		}
		
		return false;
	}
	
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		sprite.idle();
		curAction = null;
		ready = true;
		
		GameScene.ready();
	}
	
	public void interrupt() {
		if (curAction != null && curAction.dst != pos) {
			lastAction = curAction;
		}
		curAction = null;
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		act();
	}
	
	private void actMove( HeroAction.Move action ) {

		if (getCloser( action.dst )) {
			
		} else {
			if (Dungeon.level.map[pos] == Terrain.SIGN) {
				GameScene.show( new WndMessage( Dungeon.tip() ) );
			}
			ready();
		}
	}
	
	private void actInteract( HeroAction.Interact action ) {
		
		Mob.NPC npc = action.npc;

		if (Level.adjacent( pos, npc.pos )) {
			
			ready();
			sprite.turnTo( pos, npc.pos );
			npc.interact();
			
		} else {
			
			if (Level.fieldOfView[npc.pos] && getCloser( npc.pos )) {
				
			} else {
				ready();
			}
			
		}
	}
	
	private void actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst || Level.adjacent( pos, dst )) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show( new WndTradeItem( heap, true ) );
			}
			
		} else if (getCloser( dst )) {
			
		} else {
			ready();
		}
	}
	
	private void actCook( HeroAction.Cook action ) {
		int dst = action.dst;
		if (Dungeon.visible[dst]) {

			ready();
			AlchemyPot.operate( this, dst );
			
		} else if (getCloser( dst )) {
			
		} else {
			ready();
		}
	}
	
	private void actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {				
				Item item = heap.pickUp();
				if (item.doPickUp( this )) {
					
					if (item instanceof Dewdrop) {
						
					} else {
						
						if ((item instanceof ScrollOfUpgrade && ((ScrollOfUpgrade)item).isKnown()) ||
							(item instanceof PotionOfStrength && ((PotionOfStrength)item).isKnown())) {
							switch(Dungeon.hero.heroClass){
							case KYLE:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[0], item.name() );
								break;
							case IRIS:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[1], item.name() );
								break;
							case CHOMP:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[2], item.name() );
								break;
							case WHOMP:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[3], item.name() );
								break;
							case BLAZE:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[4], item.name() );
								break;
							case ASH:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[5], item.name() );
								break;
							case SHADE:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[6], item.name() );
								break;
							case BYTT:
								GLog.p( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[7], item.name() );
								break;
							}
						} else {
							switch(Dungeon.hero.heroClass){
							case KYLE:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[0], item.name() );
								break;
							case IRIS:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[1], item.name() );
								break;
							case CHOMP:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[2], item.name() );
								break;
							case WHOMP:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[3], item.name() );
								break;
							case BLAZE:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[4], item.name() );
								break;
							case ASH:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[5], item.name() );
								break;
							case SHADE:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[6], item.name() );
								break;
							case BYTT:
								GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[7], item.name() );
								break;
							}
						}
					}
					
					if (!heap.isEmpty()) {
						switch(Dungeon.hero.heroClass){
					case KYLE:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[0], item.name() );
						break;
					case IRIS:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[1], item.name() );
						break;
					case CHOMP:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[2], item.name() );
						break;
					case WHOMP:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[3], item.name() );
						break;
					case BLAZE:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[4], item.name() );
						break;
					case ASH:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[5], item.name() );
						break;
					case SHADE:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[6], item.name() );
						break;
					case BYTT:
						GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_SOMETHING_ELSE)[7], item.name() );
						break;
						}
					}
					curAction = null;
				} else {
					Dungeon.level.drop( item, pos ).sprite.drop();
					ready();
				}
			} else {
				ready();
			}
			
		} else if (getCloser( dst )) {
			
		} else {
			ready();
		}
	}
	
	private void actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Level.adjacent( pos, dst ) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && 
				(heap.type == Type.CHEST || heap.type == Type.TOMB || heap.type == Type.SKELETON ||
				heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST)) {
				
				theKey = null;
				
				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey( GoldenKey.class, Dungeon.depth );
					
					if (theKey == null) {
						switch(Dungeon.hero.heroClass){
						case KYLE:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[0]);
							break;
						case IRIS:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[1]);
							break;
						case CHOMP:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[2]);
							break;
						case WHOMP:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[3]);
							break;
						case BLAZE:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[4]);
							break;
						case ASH:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[5]);
							break;
						case SHADE:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[6]);
							break;
						case BYTT:
							GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_CHEST)[7]);
							break;
							}
						ready();
						return;
					}
				}
				
				switch (heap.type) {
				case TOMB:
					Sample.INSTANCE.play( Assets.SND_TOMB );
					Camera.main.shake( 1, 0.5f );
					break;
				case SKELETON:
					break;
				default:
					Sample.INSTANCE.play( Assets.SND_UNLOCK );
				}
				
				spend( Key.TIME_TO_UNLOCK );
				sprite.operate( dst );
				
			} else {
				ready();
			}
			
		} else if (getCloser( dst )) {
			
		} else {
			ready();
		}
	}
	
	private void actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (Level.adjacent( pos, doorCell )) {
			
			theKey = null;
			int door = Dungeon.level.map[doorCell];
			
			if (door == Terrain.LOCKED_DOOR) {
				
				theKey = belongings.getKey( IronKey.class, Dungeon.depth );
				
			} else if (door == Terrain.LOCKED_EXIT) {
				
				theKey = belongings.getKey( SkeletonKey.class, Dungeon.depth );
				
			}
			
			if (theKey != null) {
				
				spend( Key.TIME_TO_UNLOCK );
				sprite.operate( doorCell );
				
				Sample.INSTANCE.play( Assets.SND_UNLOCK );
				
			} else {
				switch(Dungeon.hero.heroClass){
				case KYLE:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[0]);
					break;
				case IRIS:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[1]);
					break;
				case CHOMP:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[2]);
					break;
				case WHOMP:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[3]);
					break;
				case BLAZE:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[4]);
					break;
				case ASH:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[5]);
					break;
				case SHADE:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[6]);
					break;
				case BYTT:
					GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LOCKED_DOOR)[7]);
					break;
					}
				ready();
			}
			
		} else if (getCloser( doorCell )) {
			
		} else {
			ready();
		}
	}
	
	private void actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.exit) {
			
			curAction = null;
			
			Hunger hunger = buff( Hunger.class );
			if (hunger != null && !hunger.isStarving()) {
				hunger.satisfy( -Hunger.STARVING / 10 );
			}
			
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene( InterlevelScene.class );
			
		} else if (getCloser( stairs )) {
			
		} else {
			ready();
		}
	}
	
	private void actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.entrance) {
			
			if (Dungeon.depth == 1) {
				
				if (belongings.getItem( Amulet.class ) == null) {
					switch(Dungeon.hero.heroClass){
					case KYLE:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[0] ) );
						break;
					case IRIS:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[1] ) );
						break;
					case CHOMP:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[2] ) );
						break;
					case WHOMP:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[3] ) );
						break;
					case BLAZE:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[4] ) );
						break;
					case ASH:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[5] ) );
						break;
					case SHADE:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[6] ) );
						break;
					case BYTT:
						GameScene.show( new WndMessage( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEAVE)[7] ) );
						break;
					}
					ready();
				} else {
					Dungeon.deleteGame( Dungeon.hero.heroClass, true );
					Game.switchScene( SurfaceScene.class );
				}
				
			} else {
				
				curAction = null;
				
				Hunger hunger = buff( Hunger.class );
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy( -Hunger.STARVING / 10 );
				}

				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene( InterlevelScene.class );
			}
			
		} else if (getCloser( stairs )) {

		} else {
			ready();
		}
	}
	
	private void actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (Level.adjacent( pos, enemy.pos ) && enemy.isAlive() && !pacified) {
			
			spend( attackDelay() );
			sprite.attack( enemy.pos );
			
		} else {
			
			if (Level.fieldOfView[enemy.pos] && getCloser( enemy.pos )) {
				
			} else {
				ready();
			}
			
		}
	}
	
	public void rest( boolean tillHealthy ) {
		spendAndNext( TIME_TO_REST );
		if (!tillHealthy) {
			sprite.showStatus( CharSprite.DEFAULT, App.getContext().getResources().getString(com.gravitygamesinteractive.pixelsdungeon.R.string.TEXT_WAIT) );
		}
		restoreHealth = tillHealthy;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (belongings.weapon != null) {
			
			KindOfWeapon weapon = belongings.weapon;
			weapon.proc( this, enemy, damage );
			
			switch (subClass) {
			case GLADIATOR:
				if (weapon instanceof MeleeWeapon) {
					damage += Buff.affect( this, Combo.class ).hit( enemy, damage );
				}
				break;
			case BATTLEMAGE:
				if (weapon instanceof Wand) {
					Wand wand = (Wand)weapon;
					if (wand.curCharges < wand.maxCharges && damage > 0) {
						
						wand.curCharges++;
						if (Dungeon.quickslot == wand) {
							QuickSlot.refresh();
						}
						
						ScrollOfRecharging.charge( this );
					}
					damage += wand.curCharges;
				}
			case SNIPER:
				if (usingRanged) {
					Buff.prolong( enemy, SnipersMark.class, attackDelay() * 1.1f );
				}
				break;
			default:
			}
		}
		
		return damage;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		RingOfThorns.Thorns thorns = buff( RingOfThorns.Thorns.class ); 
		if (thorns != null) {
			int dmg = Random.IntRange( 0, damage );
			if (dmg > 0) {
				enemy.damage( dmg, thorns );
			}
		}
		
		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}
		
		if (belongings.armor != null) {
			damage = belongings.armor.proc( enemy, this, damage );
		}
		
		return damage;
	}
	
	@Override
	public void damage( int dmg, Object src ) {		
		restoreHealth = false;
		super.damage( dmg, src );
		
		if (subClass == HeroSubClass.BERSERKER && 0 < HP && HP <= HT * Fury.LEVEL) {
			Buff.affect( this, Fury.class );
		}
	}
	
	private void checkVisibleMobs() {

		ArrayList<Mob> visible = new ArrayList<Mob>();

		boolean newMob = false;
		
		for (Mob m : Dungeon.level.mobs) {
			if (Level.fieldOfView[ m.pos ] && m.hostile) {
				visible.add( m );
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}
			}
		}
		
		if (newMob) {
			interrupt();
			restoreHealth = false;
		}
		
		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get( index % visibleEnemies.size() );
	}
	
	private boolean getCloser( final int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = -1;
		
		if (Level.adjacent( pos, target )) {
			
			if (Actor.findChar( target ) == null) {
				if (Level.pit[target] && !flying && !Chasm.jumpConfirmed) {
					Chasm.heroJump( this );
					interrupt();
					return false;
				}
				if (Level.passable[target] || Level.avoid[target]) {
					step = target;
				}
			}
			
		} else {
		
			int len = Level.LENGTH;
			boolean[] p = Level.passable;
			boolean[] v = Dungeon.level.visited;
			boolean[] m = Dungeon.level.mapped;
			boolean[] passable = new boolean[len];
			for (int i=0; i < len; i++) {
				passable[i] = p[i] && (v[i] || m[i]);
			}
			
			step = Dungeon.findPath( this, pos, target, passable, Level.fieldOfView );
		}
		
		if (step != -1) {
			
			sprite.move( pos, step );
			move( step );
			spend( 1 / speed() );
			
			return true;

		} else {

			return false;
			
		}

	}
	
	public void handle( int cell ) {
		
		if (cell == -1) {
			return;
		}
		
		Char ch;
		Heap heap;
		
		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Cook( cell );
			
		} else
		if (Level.fieldOfView[cell] && (ch = Actor.findChar( cell )) instanceof Mob) {
			
			if (ch instanceof Mob.NPC) {
				curAction = new HeroAction.Interact( (Mob.NPC)ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}
			
		} else if ((heap = Dungeon.level.heaps.get( cell )) != null) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().price() > 0 ? 
					new HeroAction.Buy( cell ) : 
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {
			
			curAction = new HeroAction.Unlock( cell );
			
		} else if (cell == Dungeon.level.exit) {
			
			curAction = new HeroAction.Descend( cell );
			
		} else if (cell == Dungeon.level.entrance) {
			
			curAction = new HeroAction.Ascend( cell );
			
		} else  {
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
			
		}

		act();
	}
	
	public void earnExp( int exp ) {
		
		this.exp += exp;
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			lvl++;
			
			HT += 5;
			HP += 5;			
			attackSkill++;
			defenseSkill++;
			
			if (lvl < 10) {
				updateAwareness();
			}
			
			levelUp = true;
		}
		
		if (levelUp) {
			
			GLog.p( App.getContext().getResources().getString(com.gravitygamesinteractive.pixelsdungeon.R.string.TEXT_NEW_LEVEL), lvl );
			switch(Dungeon.hero.heroClass){
			case KYLE:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[0] );
				break;
			case IRIS:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[1] );
				break;
			case CHOMP:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[2] );
				break;
			case WHOMP:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[3] );
				break;
			case BLAZE:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[4] );
				break;
			case ASH:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[5] );
				break;
			case SHADE:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[6] );
				break;
			case BYTT:
				sprite.showStatus( CharSprite.POSITIVE, App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_LEVEL_UP)[7] );
				break;
			}
			Sample.INSTANCE.play( Assets.SND_LEVELUP );
			
			Badges.validateLevelReached();
		}
		
		if (subClass == HeroSubClass.WARLOCK) {
			
			int value = Math.min( HT - HP, 1 + (Dungeon.depth - 1) / 5 );
			if (value > 0) {
				HP += value;
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			}
			
			((Hunger)buff( Hunger.class )).satisfy( 10 );
		}
	}
	
	public int maxExp() {
		return 5 + lvl * 5;
	}
	
	void updateAwareness() {
		awareness = (float)(1 - Math.pow( 
			(heroClass == HeroClass.CHOMP ? 0.85 : 0.90), 
			(1 + Math.min( lvl,  9 )) * 0.5 
		));
	}
	
	public boolean isStarving() {
		return ((Hunger)buff( Hunger.class )).isStarving();
	}
	
	@Override
	public void add( Buff buff ) {
		super.add( buff );
		
		if (sprite != null) {
			if (buff instanceof Burning) {
				GLog.w( "You catch fire!" );
				interrupt();
			} else if (buff instanceof Paralysis) {
				GLog.w( "You are paralysed!" );
				interrupt();
			} else if (buff instanceof Poison) {
				GLog.w( "You are poisoned!" );
				interrupt();
			} else if (buff instanceof Ooze) {
				GLog.w( "Caustic ooze eats your flesh. Wash away it!" );
			} else if (buff instanceof Roots) {
				GLog.w( "You can't move!" );
			} else if (buff instanceof Weakness) {
				GLog.w( "You feel weakened!" );
			} else if (buff instanceof Blindness) {
				GLog.w( "You are blinded!" );
			} else if (buff instanceof Fury) {
				GLog.w( "You become furious!" );
				sprite.showStatus( CharSprite.POSITIVE, "furious" );
			} else if (buff instanceof Charm) {
				GLog.w( "You are charmed!" );
			}  else if (buff instanceof Cripple) {
				GLog.w( "You are crippled!" );
			} else if (buff instanceof Bleeding) {
				GLog.w( "You are bleeding!" );
			}
			
			else if (buff instanceof Light) {
				sprite.add( CharSprite.State.ILLUMINATED );
			}
		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );
		
		if (buff instanceof Light) {
			sprite.remove( CharSprite.State.ILLUMINATED );
		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public int stealth() {
		int stealth = super.stealth();
		for (Buff buff : buffs( RingOfShadows.Shadows.class )) {
			stealth += ((RingOfShadows.Shadows)buff).level;
		}
		return stealth;
	}
	
	@Override
	public void die( Object cause  ) {
		
		curAction = null;
		
		DewVial.autoDrink( this );
		if (isAlive()) {
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( sprite, 2f ) ;
			return;
		}
		
		Actor.fixTime();
		super.die( cause );
		
		Ankh ankh = (Ankh)belongings.getItem( Ankh.class );
		if (ankh == null) {
			
			reallyDie( cause );
			
		} else {
			
			Dungeon.deleteGame( Dungeon.hero.heroClass, false );
			GameScene.show( new WndResurrect( ankh, cause ) );
			
		}
	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Level.LENGTH;
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Level.set( i, Terrain.discover( terr ) );						
					GameScene.updateMap( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
				
		Dungeon.hero.belongings.identify();
		
		GameScene.gameOver();
		
		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}
		
		Dungeon.deleteGame( Dungeon.hero.heroClass, true );
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (!flying) {
			
			if (Level.water[step]) {
				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
			} else {
				Sample.INSTANCE.play( Assets.SND_STEP );
			}
			Dungeon.level.press( step, this );
		}
	}
	
	@Override
	public void onMotionComplete() {
		
		Dungeon.observe();
		search( false );
			
		super.onMotionComplete();
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target( enemy );
		
		attack( enemy );
		curAction = null;
		
		Invisibility.dispel();

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {
		
		if (curAction instanceof HeroAction.Unlock) {
			
			if (theKey != null) {
				theKey.detach( belongings.backpack );
				theKey = null;
			}
			
			int doorCell = ((HeroAction.Unlock)curAction).dst;
			int door = Dungeon.level.map[doorCell];
			
			Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT );
			GameScene.updateMap( doorCell );
			
		} else if (curAction instanceof HeroAction.OpenChest) {
			
			if (theKey != null) {
				theKey.detach( belongings.backpack );
				theKey = null;
			}
			
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst ); 
			if (heap.type == Type.SKELETON) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
			heap.open( this );
		}
		curAction = null;

		super.onOperateComplete();
	}
	
	public boolean search( boolean intentional ) {
		
		boolean smthFound = false;
		
		int positive = 0;
		int negative = 0;
		for (Buff buff : buffs( RingOfDetection.Detection.class )) {
			int bonus = ((RingOfDetection.Detection)buff).level;
			if (bonus > positive) {
				positive = bonus;
			} else if (bonus < 0) {
				negative += bonus;
			}
		}
		int distance = 1 + positive + negative;

		float level = intentional ? (2 * awareness - awareness * awareness) : awareness;
		if (distance <= 0) {

			level /= 2 - distance;
			distance = 1;
		}
		
		int cx = pos % Level.WIDTH;
		int cy = pos / Level.WIDTH;
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Level.WIDTH) {
			bx = Level.WIDTH - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Level.HEIGHT) {
			by = Level.HEIGHT - 1;
		}
		
		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {
				
				if (Dungeon.visible[p]) {
					
					if (intentional) {
						sprite.parent.addToBack( new CheckedCell( p ) );
					}
					
					if (Level.secret[p] && (intentional || Random.Float() < level)) {
						
						int oldValue = Dungeon.level.map[p];
						
						GameScene.discoverTile( p, oldValue );
						
						Level.set( p, Terrain.discover( oldValue ) );	
						
						GameScene.updateMap( p );
						
						ScrollOfMagicMapping.discover( p );
						
						smthFound = true;
					}
				}
			}
		}

		
		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, App.getContext().getResources().getString(com.gravitygamesinteractive.pixelsdungeon.R.string.TEXT_SEARCH) );
			sprite.operate( pos );
			if (smthFound) {
				spendAndNext( Random.Float() < level ? TIME_TO_SEARCH : TIME_TO_SEARCH * 2 );
			} else {
				spendAndNext( TIME_TO_SEARCH );
			}
			
		}
		
		if (smthFound) {
			switch(Dungeon.hero.heroClass){
			case KYLE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[0]);
				break;
			case IRIS:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[1]);
				break;
			case CHOMP:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[2]);
				break;
			case WHOMP:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[3]);
				break;
			case BLAZE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[4]);
				break;
			case ASH:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[5]);
				break;
			case SHADE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[6]);
				break;
			case BYTT:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_NOTICED_SOMETHING)[7]);
				break;
				}
			Sample.INSTANCE.play( Assets.SND_SECRET );
			interrupt();
		}
		
		return smthFound;
	}
	
	public void resurrect( int resetLevel ) {
		
		HP = HT;
		Dungeon.gold = 0;
		exp = 0;
		
		belongings.resurrect( resetLevel );

		live();
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		RingOfElements.Resistance r = buff( RingOfElements.Resistance.class );
		return r == null ? super.resistances() : r.resistances();
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		GasesImmunity buff = buff( GasesImmunity.class );
		return buff == null ? super.immunities() : GasesImmunity.IMMUNITIES;
	}
	
	public static interface Doom {
		public void onDeath();
	}
}
