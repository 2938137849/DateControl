package bin.interfaces;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;


/**
 * @author bin
 * @version 1.0.0
 */
public interface EventInterface {

  @FunctionalInterface
  interface onPlayCard {
    void run(final AbstractCard c, final AbstractMonster m);
  }

  @FunctionalInterface
  interface onUseCard {
    void run(final AbstractCard card, final UseCardAction action);
  }

  @FunctionalInterface
  interface onExhaust {
    void run(final AbstractCard card);
  }

  @FunctionalInterface
  interface onCardDraw {
    void run(final AbstractCard card);
  }

  @FunctionalInterface
  interface onGainGold {
    void run();
  }

  @FunctionalInterface
  interface onLoseGold {
    void run();
  }

  @FunctionalInterface
  interface onEquip {
    void run();
  }

  @FunctionalInterface
  interface onUnequip {
    void run();
  }

  @FunctionalInterface
  interface atPreBattle {
    void run();
  }

  @FunctionalInterface
  interface atBattleStart {
    void run();
  }

  @FunctionalInterface
  interface atBattleStartPreDraw {
    void run();
  }

  @FunctionalInterface
  interface atTurnStart {
    void run();
  }

  @FunctionalInterface
  interface onPlayerEndTurn {
    void run();
  }

  @FunctionalInterface
  interface onManualDiscard {
    void run();
  }

  @FunctionalInterface
  interface onVictory {
    void run();
  }

  @FunctionalInterface
  interface onMonsterDeath {
    void run(final AbstractMonster m);
  }

  @FunctionalInterface
  interface onPlayerGainBlock {
    int run(final int blockAmount);
  }

  @FunctionalInterface
  interface onPlayerHeal {
    int run(final int healAmount);
  }

  @FunctionalInterface
  interface onEnterRestRoom {
    void run();
  }

  @FunctionalInterface
  interface onRest {
    void run();
  }

  @FunctionalInterface
  interface onAttack {
    void run(final DamageInfo info, final int damageAmount, final AbstractCreature target);
  }

  @FunctionalInterface
  interface onAttacked {
    int run(final DamageInfo info, final int damageAmount);
  }

  @FunctionalInterface
  interface onEnterRoom {
    void run(final AbstractRoom room);
  }

  @FunctionalInterface
  interface onChestOpen {
    void run(final boolean isBossChest);
  }

  @FunctionalInterface
  interface onDrawOrDiscard {
    void run();
  }

}
