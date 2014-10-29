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
package com.gravitygamesinteractive.pixelsdungeon.items.armor;

import com.gravitygamesinteractive.pixelsdungeon.Dungeon;
import com.gravitygamesinteractive.pixelsdungeon.actors.Actor;
import com.gravitygamesinteractive.pixelsdungeon.actors.Char;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Buff;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Fury;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Invisibility;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Paralysis;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.Hero;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.HeroClass;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.HeroSubClass;
import com.gravitygamesinteractive.pixelsdungeon.effects.CellEmitter;
import com.gravitygamesinteractive.pixelsdungeon.effects.Speck;
import com.gravitygamesinteractive.pixelsdungeon.levels.Level;
import com.gravitygamesinteractive.pixelsdungeon.mechanics.Ballistica;
import com.gravitygamesinteractive.pixelsdungeon.scenes.CellSelector;
import com.gravitygamesinteractive.pixelsdungeon.scenes.GameScene;
import com.gravitygamesinteractive.pixelsdungeon.sprites.HeroSprite;
import com.gravitygamesinteractive.pixelsdungeon.sprites.ItemSpriteSheet;
import com.gravitygamesinteractive.pixelsdungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class KyleArmor extends ClassArmor {
	
	private static int LEAP_TIME	= 1;
	private static int SHOCK_TIME	= 3;
	
	private static final String AC_SPECIAL = "HEROIC LEAP"; 
	
	private static final String TXT_NOT_WARRIOR	= "Only Kyle can use this armor!";
	
	{
		name = "kyle suit of armor";
		image = ItemSpriteSheet.ARMOR_KYLE;
	}
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( leaper );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.KYLE) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_WARRIOR );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return
			"While this armor looks heavy, it allows Kyle to perform heroic leap towards " +
			"a targeted location, slamming down to stun all neighbouring enemies.";
	}
	
	protected static CellSelector.Listener leaper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null && target != curUser.pos) {
				
				int cell = Ballistica.cast( curUser.pos, target, false, true );
				if (Actor.findChar( cell ) != null && cell != curUser.pos) {
					cell = Ballistica.trace[Ballistica.distance - 2];
				}
				
				curUser.HP -= (curUser.HP / 3);
				if (curUser.subClass == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
					Buff.affect( curUser, Fury.class );
				}
				
				Invisibility.dispel();
				
				final int dest = cell;
				curUser.busy();
				((HeroSprite)curUser.sprite).jump( curUser.pos, cell, new Callback() {
				@Override
				public void call() {
				curUser.move( dest );
				Dungeon.level.press( dest, curUser );
				Dungeon.observe();
				
				for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				Char mob = Actor.findChar( curUser.pos + Level.NEIGHBOURS8[i] );
				if (mob != null && mob != curUser) {
				Buff.prolong( mob, Paralysis.class, SHOCK_TIME );
				}
				}
				
				CellEmitter.center( dest ).burst( Speck.factory( Speck.DUST ), 10 );
				Camera.main.shake( 2, 0.5f );
				
				curUser.spendAndNext( LEAP_TIME );
					}
				});
				
				PointF pos = curUser.sprite.point();
				Camera.main.target = null;
				curUser.sprite.y -= 16;
				curUser.sprite.parent.add( new PosTweener( curUser.sprite, pos, 0.1f ) );
				
				CellEmitter.center( cell ).burst( Speck.factory( Speck.DUST ), 10 );
				
				curUser.spendAndNext( LEAP_TIME );
			}
		}
		
		@Override
		public String prompt() {
			return "Choose direction to leap";
		}
	};
}