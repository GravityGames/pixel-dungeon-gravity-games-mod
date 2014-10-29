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
package com.gravitygamesinteractive.pixelsdungeon.actors.mobs;

import java.util.HashSet;

import com.gravitygamesinteractive.pixelsdungeon.Assets;
import com.gravitygamesinteractive.pixelsdungeon.Badges;
import com.gravitygamesinteractive.pixelsdungeon.Dungeon;
import com.gravitygamesinteractive.pixelsdungeon.Badges.Badge;
import com.gravitygamesinteractive.pixelsdungeon.actors.Actor;
import com.gravitygamesinteractive.pixelsdungeon.actors.Char;
import com.gravitygamesinteractive.pixelsdungeon.actors.blobs.ToxicGas;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Poison;
import com.gravitygamesinteractive.pixelsdungeon.effects.CellEmitter;
import com.gravitygamesinteractive.pixelsdungeon.effects.Speck;
import com.gravitygamesinteractive.pixelsdungeon.items.TomeOfMastery;
import com.gravitygamesinteractive.pixelsdungeon.items.keys.SkeletonKey;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfMagicMapping;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfPsionicBlast;
import com.gravitygamesinteractive.pixelsdungeon.items.weapon.enchantments.Death;
import com.gravitygamesinteractive.pixelsdungeon.levels.Level;
import com.gravitygamesinteractive.pixelsdungeon.levels.Terrain;
import com.gravitygamesinteractive.pixelsdungeon.mechanics.Ballistica;
import com.gravitygamesinteractive.pixelsdungeon.scenes.GameScene;
import com.gravitygamesinteractive.pixelsdungeon.sprites.TenguSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Tengu extends Mob {

	private static final int JUMP_DELAY = 5;
	
	{
		name = "Tengu";
		spriteClass = TenguSprite.class;
		
		HP = HT = 120;
		EXP = 20;
		defenseSkill = 20;
	}
	
	private int timeToJump = JUMP_DELAY;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 8, 15 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public void die( Object cause ) {
		
		Badges.Badge badgeToCheck = null;
		switch (Dungeon.hero.heroClass) {
		case KYLE:
			badgeToCheck = Badge.MASTERY_KYLE;
			break;
		case IRIS:
			badgeToCheck = Badge.MASTERY_IRIS;
			break;
		case CHOMP:
			badgeToCheck = Badge.MASTERY_CHOMP;
			break;
		case WHOMP:
			badgeToCheck = Badge.MASTERY_WHOMP;
		case BLAZE:
			badgeToCheck = Badge.MASTERY_BLAZE;
		case ASH:
			badgeToCheck = Badge.MASTERY_ASH;
		case SHADE:
			badgeToCheck = Badge.MASTERY_SHADE;
		case BYTT:
			badgeToCheck = Badge.MASTERY_BYTT;
			break;
		}
		if (!Badges.isUnlocked( badgeToCheck )) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		super.die( cause );
		
		Badges.validateBossSlain();
		
		yell( "Free at last..." );
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
		timeToJump--;
		if (timeToJump <= 0 && Level.adjacent( pos, enemy.pos )) {
			jump();
			return true;
		} else {
			return super.doAttack( enemy );
		}
	}
	
	private void jump() {
		timeToJump = JUMP_DELAY;
		
		for (int i=0; i < 4; i++) {
			int trapPos;
			do {
				trapPos = Random.Int( Level.LENGTH );
			} while (!Level.fieldOfView[trapPos] || !Level.passable[trapPos]);
			
			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Level.set( trapPos, Terrain.POISON_TRAP );
				GameScene.updateMap( trapPos );
				ScrollOfMagicMapping.discover( trapPos );
			}
		}
		
		int newPos;
		do {
			newPos = Random.Int( Level.LENGTH );
		} while (
			!Level.fieldOfView[newPos] || 
			!Level.passable[newPos] || 
			Level.adjacent( newPos, enemy.pos ) ||
			Actor.findChar( newPos ) != null);
		
		sprite.move( pos, newPos );
		move( newPos );
		
		if (Dungeon.visible[newPos]) {
			CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
		}
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Gotcha, " + Dungeon.hero.heroClass.title() + "!" );
	}
	
	@Override
	public String description() {
		return
			"Tengu are members of the ancient assassins clan, which is also called Tengu. " +
			"These assassins are noted for extensive use of shuriken and traps.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
