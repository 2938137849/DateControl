package bin.frame;

import bin.interfaces.EventInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * @author bin
 * @version 1.0.0
 */
public final class EventList {
  public static List<EventInterface.onPlayCard> onPlayCard = new LinkedList<>();
  public static List<EventInterface.onUseCard> onUseCard = new LinkedList<>();
  public static List<EventInterface.onExhaust> onExhaust = new LinkedList<>();
  public static List<EventInterface.onCardDraw> onCardDraw = new LinkedList<>();
  public static List<EventInterface.onGainGold> onGainGold = new LinkedList<>();
  public static List<EventInterface.onLoseGold> onLoseGold = new LinkedList<>();
  public static List<EventInterface.onEquip> onEquip = new LinkedList<>();
  public static List<EventInterface.onUnequip> onUnequip = new LinkedList<>();
  public static List<EventInterface.atPreBattle> atPreBattle = new LinkedList<>();
  public static List<EventInterface.atBattleStart> atBattleStart = new LinkedList<>();
  public static List<EventInterface.atBattleStartPreDraw> atBattleStartPreDraw = new LinkedList<>();
  public static List<EventInterface.atTurnStart> atTurnStart = new LinkedList<>();
  public static List<EventInterface.onPlayerEndTurn> onPlayerEndTurn = new LinkedList<>();
  public static List<EventInterface.onManualDiscard> onManualDiscard = new LinkedList<>();
  public static List<EventInterface.onVictory> onVictory = new LinkedList<>();
  public static List<EventInterface.onMonsterDeath> onMonsterDeath = new LinkedList<>();
  public static List<EventInterface.onPlayerGainBlock> onPlayerGainBlock = new LinkedList<>();
  public static List<EventInterface.onPlayerHeal> onPlayerHeal = new LinkedList<>();
  public static List<EventInterface.onEnterRestRoom> onEnterRestRoom = new LinkedList<>();
  public static List<EventInterface.onRest> onRest = new LinkedList<>();
  public static List<EventInterface.onAttack> onAttack = new LinkedList<>();
  public static List<EventInterface.onAttacked> onAttacked = new LinkedList<>();
  public static List<EventInterface.onEnterRoom> onEnterRoom = new LinkedList<>();
  public static List<EventInterface.onChestOpen> onChestOpen = new LinkedList<>();
  public static List<EventInterface.onDrawOrDiscard> onDrawOrDiscard = new LinkedList<>();
}
