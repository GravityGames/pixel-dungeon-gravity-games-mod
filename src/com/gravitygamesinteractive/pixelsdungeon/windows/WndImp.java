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
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Imp;
import com.gravitygamesinteractive.pixelsdungeon.items.Item;
import com.gravitygamesinteractive.pixelsdungeon.items.quest.DwarfToken;
import com.gravitygamesinteractive.pixelsdungeon.scenes.PixelScene;
import com.gravitygamesinteractive.pixelsdungeon.sprites.ItemSprite;
import com.gravitygamesinteractive.pixelsdungeon.ui.RedButton;
import com.gravitygamesinteractive.pixelsdungeon.ui.Window;
import com.gravitygamesinteractive.pixelsdungeon.utils.GLog;
import com.gravitygamesinteractive.pixelsdungeon.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;

public class WndImp extends Window {
	
	private static final String TXT_MESSAGE	= 
		"Oh yes! You are my hero!\n" +
		"Regarding your reward, I don't have cash with me right now, but I have something better for you. " +
		"This is my family heirloom ring: my granddad took it off a dead paladin's finger.";
	private static final String TXT_REWARD		= "Take the ring";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndImp( final Imp imp, final DwarfToken tokens ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tokens.image(), null ) );
		titlebar.label( Utils.capitalize( tokens.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline message = PixelScene.createMultiline( TXT_MESSAGE, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnReward = new RedButton( TXT_REWARD ) {
			@Override
			protected void onClick() {
				takeReward( imp, tokens, Imp.Quest.reward );
			}
		};
		btnReward.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnReward );
		
		resize( WIDTH, (int)btnReward.bottom() );
	}
	
	private void takeReward( Imp imp, DwarfToken tokens, Item reward ) {
		
		hide();
		
		tokens.detachAll( Dungeon.hero.belongings.backpack );

		reward.identify();
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
			Dungeon.level.drop( reward, imp.pos ).sprite.drop();
		}
		
		imp.flee();
		
		Imp.Quest.complete();
	}
}
