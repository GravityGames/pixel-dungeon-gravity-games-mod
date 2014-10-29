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
package com.gravitygamesinteractive.pixelsdungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import com.gravitygamesinteractive.pixelsdungeon.actors.Actor;
import com.gravitygamesinteractive.pixelsdungeon.actors.Char;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Amok;
import com.gravitygamesinteractive.pixelsdungeon.actors.buffs.Light;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.Hero;
import com.gravitygamesinteractive.pixelsdungeon.actors.hero.HeroClass;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Blacksmith;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Ghost;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Imp;
import com.gravitygamesinteractive.pixelsdungeon.actors.mobs.npcs.Wandmaker;
import com.gravitygamesinteractive.pixelsdungeon.items.Ankh;
import com.gravitygamesinteractive.pixelsdungeon.items.potions.Potion;
import com.gravitygamesinteractive.pixelsdungeon.items.rings.Ring;
import com.gravitygamesinteractive.pixelsdungeon.items.scrolls.Scroll;
import com.gravitygamesinteractive.pixelsdungeon.items.wands.Wand;
import com.gravitygamesinteractive.pixelsdungeon.levels.CavesBossLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.CavesLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.CityBossLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.CityLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.DeadEndLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.HallsBossLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.HallsLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.LastLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.LastShopLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.Level;
import com.gravitygamesinteractive.pixelsdungeon.levels.PrisonBossLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.PrisonLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.Room;
import com.gravitygamesinteractive.pixelsdungeon.levels.SewerBossLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.SewerLevel;
import com.gravitygamesinteractive.pixelsdungeon.levels.pixelstower.EntranceLevel;
import com.gravitygamesinteractive.pixelsdungeon.scenes.GameScene;
import com.gravitygamesinteractive.pixelsdungeon.scenes.StartScene;
import com.gravitygamesinteractive.pixelsdungeon.utils.BArray;
import com.gravitygamesinteractive.pixelsdungeon.utils.Utils;
import com.gravitygamesinteractive.pixelsdungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Dungeon {
	
	private static final String NO_TIPS = "The text  is indecipherable...";
	private static final String[] TIPS = {
		"Don't overestimate your strength, use weapons and armor you can handle.",
		"Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
		"Remember, that raising your strength is not the only way to access better equipment, you can go " +
			"the other way lowering its strength requirement with Scrolls of Upgrade.",
		"You can spend your gold in shops on deeper levels of the dungeon. The first one is on the 6th level.",
			
		"Beware of Goo!",
		
		"Pixel-Mart - all you need for successful adventure!",
		"Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
			"when you actually need them.",
		"Being hungry doesn't hurt, but starving does hurt.",
		"Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
			"a closed door when you know it is approaching.",
		
		"Don't let The Tengu out!",
		
		"Pixel-Mart. Spend money. Live longer.",
		"When you're attacked by several monsters at the same time, try to retreat behind a door.",
		"If you are burning, you can't put out the fire in the water while levitating.",
		"There is no sense in possessing more than one Ankh at the same time, because you will lose them upon resurrecting.",
		
		"DANGER! Heavy machinery can cause injury, loss of limbs or death!",
		
		"Pixel-Mart. A safer life in dungeon.",
		"When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
		"In a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
		"The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",
		
		"No weapons allowed in the presence of His Majesty!",
		
		"Pixel-Mart. Special prices for demon hunters!",
		"The text is written in demonic language.",
		"The text is written in demonic language.",
		"The text is written in demonic language."
	};
	
	private static final String TXT_DEAD_END = 
		"What are you doing here?!";
	
	public static int potionOfStrength;
	public static int scrollsOfUpgrade;
	public static int arcaneStyli;
	public static boolean dewVial;		// true if the dew vial can be spawned
	public static int transmutation;	// depth number for a well of transmutation
	public static int challenges;

	public static Hero hero;
	public static Level level;
	
	// Either Item or Class<? extends Item>
	public static Object quickslot;
	
	public static int depth;
	public static int gold;
	// Reason of death
	public static String resultDescription;
	
	public static HashSet<Integer> chapters;
	
	// Hero's field of view
	public static boolean[] visible = new boolean[Level.LENGTH];
	
	public static boolean nightMode;
	
	public static void init() {
		
		challenges = PixelDungeon.challenges();

		Actor.clear();
		
		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		
		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();
		
		Statistics.reset();
		Journal.reset();
		
		depth = 0;
		gold = 0;
		
		potionOfStrength = 0;
		scrollsOfUpgrade = 0;
		arcaneStyli = 0;
		dewVial = true;
		transmutation = Random.IntRange( 6, 14 );
		
		chapters = new HashSet<Integer>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();
		
		Room.shuffleTypes();
		
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		StartScene.curClass.initHero( hero );
	}
	
	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		depth++;
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		
		Arrays.fill( visible, false );
		
		Level level;
		switch (depth) {
		case 1:
		case 2:
		case 3:
		case 4:
			//level = new SewerLevel();
			level = new EntranceLevel();
			break;
		case 5:
			level = new SewerBossLevel();
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			level = new PrisonLevel();
			break;
		case 10:
			level = new PrisonBossLevel();
			break;
		case 11:
		case 12:
		case 13:
		case 14:
			level = new CavesLevel();
			break;
		case 15:
			level = new CavesBossLevel();
			break;
		case 16:
		case 17:
		case 18:
		case 19:
			level = new CityLevel();
			break;
		case 20:
			level = new CityBossLevel();
			break;
		case 21:
			level = new LastShopLevel();
			break;
		case 22:
		case 23:
		case 24:
			level = new HallsLevel();
			break;
		case 25:
			level = new HallsBossLevel();
			break;
		case 26:
			level = new LastLevel();
			break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		Arrays.fill( visible, false );
		
		level.reset();
		switchLevel( level, level.entrance );
	}
	
	public static String tip() {
		
		if (level instanceof DeadEndLevel) {
			
			return TXT_DEAD_END;
			
		} else {
			
			int index = depth - 1;
			
			if (index < TIPS.length) {
				return TIPS[index];
			} else {
				return NO_TIPS;
			}
		}
	}
	
	public static boolean shopOnLevel() {
		return depth == 6 || depth == 11 || depth == 16;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
	}
	
	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		
		nightMode = new Date().getHours() < 7;
		
		Dungeon.level = level;
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add( level.respawner() );
		}
		
		hero.pos = pos != -1 ? pos : level.exit;
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		observe();
	}
	
	public static boolean posNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance( quota, potionOfStrength );
	}
	
	public static boolean soeNeeded() {
		int[] quota = {5, 3, 10, 6, 15, 9, 20, 12, 25, 13};
		return chance( quota, scrollsOfUpgrade );
	}
	
	private static boolean chance( int[] quota, int number ) {
		
		for (int i=0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float)(qNumber - number) / (qDepth - depth + 1);
			}
		}
		
		return false;
	}
	
	public static boolean asNeeded() {
		return Random.Int( 12 * (1 + arcaneStyli) ) < depth;
	}
	
	private static final String CHOMP_GAME_FILE	= "game.dat";
	private static final String CHOMP_DEPTH_FILE	= "depth%d.dat";
	
	private static final String KYLE_GAME_FILE	= "kyle.dat";
	private static final String KYLE_DEPTH_FILE	= "kyle%d.dat";
	
	private static final String IRIS_GAME_FILE	= "iris.dat";
	private static final String IRIS_DEPTH_FILE	= "iris%d.dat";
	
	private static final String WHOMP_GAME_FILE	= "whomp.dat";
	private static final String WHOMP_DEPTH_FILE	= "whomp%d.dat";
	
	private static final String BLAZE_GAME_FILE	= "blaze.dat";
	private static final String BLAZE_DEPTH_FILE	= "blaze%d.dat";
	
	private static final String ASH_GAME_FILE	= "ash.dat";
	private static final String ASH_DEPTH_FILE	= "ash%d.dat";
	
	private static final String SHADE_GAME_FILE	= "shade.dat";
	private static final String SHADE_DEPTH_FILE	= "shade%d.dat";
	
	private static final String BYTT_GAME_FILE	= "bytt.dat";
	private static final String BYTT_DEPTH_FILE	= "bytt%d.dat";
	
	private static final String VERSION		= "version";
	private static final String CHALLENGES  = "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String QUICKSLOT	= "quickslot";
	private static final String LEVEL		= "level";
	private static final String POS			= "potionsOfStrength";
	private static final String SOU			= "scrollsOfEnhancement";
	private static final String AS			= "arcaneStyli";
	private static final String DV			= "dewVial";
	private static final String WT			= "transmutation";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	
	public static String gameFile( HeroClass cl ) {
		switch (cl) {
		case KYLE:
			return KYLE_GAME_FILE;
		case IRIS:
			return IRIS_GAME_FILE;
		case WHOMP:
			return WHOMP_GAME_FILE;
		case BLAZE:
			return BLAZE_GAME_FILE;
		case ASH:
			return ASH_GAME_FILE;
		case SHADE:
			return SHADE_GAME_FILE;
		case BYTT:
			return BYTT_GAME_FILE;
		default:
			return CHOMP_GAME_FILE;
		}
	}
	
	private static String depthFile( HeroClass cl ) {
		switch (cl) {
		case KYLE:
			return KYLE_DEPTH_FILE;
		case IRIS:
			return IRIS_DEPTH_FILE;
		case WHOMP:
			return WHOMP_DEPTH_FILE;
		case BLAZE:
			return BLAZE_DEPTH_FILE;
		case ASH:
			return ASH_DEPTH_FILE;
		case SHADE:
			return SHADE_DEPTH_FILE;
		case BYTT:
			return BYTT_DEPTH_FILE;
		default:
			return CHOMP_DEPTH_FILE;
		}
	}
	
	public static void saveGame( String fileName ) throws IOException {
		try {
			Bundle bundle = new Bundle();
			
			bundle.put( VERSION, Game.version );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			
			bundle.put( POS, potionOfStrength );
			bundle.put( SOU, scrollsOfUpgrade );
			bundle.put( AS, arcaneStyli );
			bundle.put( DV, dewVial );
			bundle.put( WT, transmutation );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			Room.storeRoomsInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Journal.storeInBundle( bundle );
			
			if (quickslot instanceof Class) {
				bundle.put( QUICKSLOT, ((Class<?>)quickslot).getName() );
			}
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Wand.save( bundle );
			Ring.save( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
			
		} catch (Exception e) {

			GamesInProgress.setUnknown( hero.heroClass );
		}
	}
	
	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		OutputStream output = Game.instance.openFileOutput( Utils.format( depthFile( hero.heroClass ), depth ), Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	
	public static void saveAll() throws IOException {
		if (hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( gameFile( hero.heroClass ) );
			saveLevel();
			
			GamesInProgress.set( hero.heroClass, depth, hero.lvl );
				//hero.belongings.armor != null ? hero.belongings.armor.tier : 0 );
			
		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );
			
		}
	}
	
	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame( gameFile( cl ), true );
	}
	
	public static void loadGame( String fileName ) throws IOException {
		loadGame( fileName, false );
	}
	
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {
		
		Bundle bundle = gameBundle( fileName );
		
		Dungeon.challenges = bundle.getInt( CHALLENGES );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		if (fullLoad) {
			PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		}
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Wand.restore( bundle );
		Ring.restore( bundle );
		
		potionOfStrength = bundle.getInt( POS );
		scrollsOfUpgrade = bundle.getInt( SOU );
		arcaneStyli = bundle.getInt( AS );
		dewVial = bundle.getBoolean( DV );
		transmutation = bundle.getInt( WT );
		
		if (fullLoad) {
			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			Room.restoreRoomsFromBundle( bundle );
		}
		
		Bundle badges = bundle.getBundle( BADGES );
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		String qsClass = bundle.getString( QUICKSLOT );
		if (qsClass != null) {
			try {
				quickslot = Class.forName( qsClass );
			} catch (ClassNotFoundException e) {
			}
		} else {
			quickslot = null;
		}
		
		@SuppressWarnings("unused")
		String version = bundle.getString( VERSION );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );
		
		Statistics.restoreFromBundle( bundle );
		Journal.restoreFromBundle( bundle );
	}
	
	public static Level loadLevel( HeroClass cl ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), depth ) ) ;
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return (Level)bundle.get( "level" );
	}
	
	public static void deleteGame( HeroClass cl, boolean deleteLevels ) {
		
		Game.instance.deleteFile( gameFile( cl ) );
		
		if (deleteLevels) {
			int depth = 1;
			while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}
		
		GamesInProgress.delete( cl );
	}
	
	public static Bundle gameBundle( String fileName ) throws IOException {
		
		InputStream input = Game.instance.openFileInput( fileName );
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return bundle;
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		if (info.depth == -1) {
			info.depth = bundle.getInt( "maxDepth" );	// <-- It has to be refactored!
		}
		Hero.preview( info, bundle.getBundle( HERO ) );
	}
	
	public static void fail( String desc ) {
		resultDescription = desc;
		if (hero.belongings.getItem( Ankh.class ) == null) { 
			Rankings.INSTANCE.submit( false );
		}
	}
	
	public static void win( String desc ) {
		
		if (challenges != 0) {
			Badges.validateChampion();
		}
		
		resultDescription = desc;
		Rankings.INSTANCE.submit( true );
	}
	
	public static void observe() {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView( hero );
		System.arraycopy( Level.fieldOfView, 0, visible, 0, visible.length );
		
		BArray.or( level.visited, visible, level.visited );
		
		GameScene.afterObserve();
	}
	
	private static boolean[] passable = new boolean[Level.LENGTH];
	
	public static int findPath( Char ch, int from, int to, boolean pass[], boolean[] visible ) {
		
		if (Level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Level.avoid[to]) ? to : -1;
		}
		
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		
		return PathFinder.getStep( from, to, passable );
		
	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {
		
		if (ch.flying) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
