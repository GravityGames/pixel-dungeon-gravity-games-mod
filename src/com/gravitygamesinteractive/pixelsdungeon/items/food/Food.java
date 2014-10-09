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
package com.gravitygamesinteractive.pixelsdungeon.items.food;

import java.util.ArrayList;

import com.gravitygamesinteractive.pixelsdungeon.Assets;
import com.gravitygamesinteractive.pixelsdungeon.Badges;
import com.gravitygamesinteractive.pixelsdungeon.Statistics;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Hunger;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.Hero;
import com.gravitygamesinteractive.pixelsdungeon.effects.Speck;
import com.gravitygamesinteractive.pixelsdungeon.effects.SpellSprite;
import com.gravitygamesinteractive.pixelsdungeon.items.Item;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.ScrollOfRecharging;
import com.gravitygamesinteractive.pixelsdungeon.sprites.ItemSpriteSheet;
import com.gravitygamesinteractive.pixelsdungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Food extends Item {

	private static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Hunger.HUNGRY;
	public String message = "That food tasted delicious!";
	
	{
		stackable = true;
		name = "ration of food";
		image = ItemSpriteSheet.RATION;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );
			
			((Hunger)hero.buff( Hunger.class )).satisfy( energy );
			GLog.i( message );
			
			switch (hero.heroClass) {
			case KYLE:
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + 5, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
			case IRIS:
				hero.belongings.charge( false );
				ScrollOfRecharging.charge( hero );
				break;
			case CHOMP:
			case WHOMP:
			case BLAZE:
			case ASH:
			case SHADE:
			case BYTT:
				break;
			}
			
			hero.sprite.operate( hero.pos );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			Sample.INSTANCE.play( Assets.SND_EAT );
			
			hero.spend( TIME_TO_EAT );
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public String info() {
		return 
			"Nothing fancy here: dried meat, " +
			"some biscuits - things like that.";
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
}
