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
package com.gravitygamesinteractive.pixelsdungeon.windows;

import com.gravitygamesinteractive.pixelsdungeon.App;
import com.gravitygamesinteractive.pixelsdungeon.Dungeon;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.Hero;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Ghost;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.pixelstower.MotivationalSpeakerMan;
import com.gravitygamesinteractive.pixelsdungeon.items.Item;
import com.gravitygamesinteractive.pixelsdungeon.items.quest.DriedRose;
import com.gravitygamesinteractive.pixelsdungeon.scenes.PixelScene;
import com.gravitygamesinteractive.pixelsdungeon.sprites.ItemSprite;
import com.gravitygamesinteractive.pixelsdungeon.ui.RedButton;
import com.gravitygamesinteractive.pixelsdungeon.ui.Window;
import com.gravitygamesinteractive.pixelsdungeon.utils.GLog;
import com.gravitygamesinteractive.pixelsdungeon.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;

public class WndMotivationalSpeakerMan extends Window {
	
	private static final String TXT_FIRE	= 
			"You found the liquid nitrogen? YOU DID!!!" +
			"Motivational Speaker Man loves new gear, HE THINKS YOU SHOULD TOO!" + 
			"With one of these items, you'll go places kid!";
		private static final String TXT_ABOM	= 
			"Yes! The ugly creature is slain and I can finally rest... " +
			"Please take one of these items, maybe they " +
			"will be useful to you in your journey...";
		private static final String TXT_WEAPON	= "MSM's weapon";
		private static final String TXT_ARMOR	= "MSM's armor";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndMotivationalSpeakerMan( final MotivationalSpeakerMan ghost, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), null ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline message = PixelScene.createMultiline( item instanceof DriedRose ? TXT_FIRE : TXT_ABOM, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnWeapon = new RedButton( TXT_WEAPON ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.weapon );
			}
		};
		btnWeapon.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWeapon );
		
		RedButton btnArmor = new RedButton( TXT_ARMOR ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.armor );
			}
		};
		btnArmor.setRect( 0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnArmor );
		
		resize( WIDTH, (int)btnArmor.bottom() );
	}
	
	private void selectReward( MotivationalSpeakerMan ghost, Item item, Item reward ) {
		
		hide();
		
		item.detach( Dungeon.hero.belongings.backpack );
		
		if (reward.doPickUp( Dungeon.hero )) {
			switch(Dungeon.hero.heroClass){
			case KYLE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[0], reward.name() );
				break;
			case IRIS:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[1], reward.name() );
				break;
			case CHOMP:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[2], reward.name() );
				break;
			case WHOMP:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[3], reward.name() );
				break;
			case BLAZE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[4], reward.name() );
				break;
			case ASH:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[5], reward.name() );
				break;
			case SHADE:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[6], reward.name() );
				break;
			case BYTT:
				GLog.i( App.getContext().getResources().getStringArray(com.gravitygamesinteractive.pixelsdungeon.R.array.TEXT_YOU_NOW_HAVE)[7], reward.name() );
				break;
			}
		} else {
			Dungeon.level.drop( reward, ghost.pos ).sprite.drop();
		}
		
		ghost.yell( "Up up and away! You have potential kid, now go places!" );
		ghost.die( null );
		
		MotivationalSpeakerMan.Quest.complete();
	}
}
