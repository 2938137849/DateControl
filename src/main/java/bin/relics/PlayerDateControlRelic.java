package bin.relics;

import basemod.abstracts.CustomRelic;
import bin.frame.EventList;
import bin.interfaces.EventInterface;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author bin
 * @version 1.0.0
 */
public class PlayerDateControlRelic extends CustomRelic {
  private static final Logger logger = LogManager.getLogger(PlayerDateControlRelic.class.getName());
  /** 遗物Id，添加遗物、替换遗物时填写该id而不是遗物类名。 */
  public static final String ID = "DataEdit_MyRelic";
  /** 遗物图片路径 */
  private static final Texture IMG;
  /** 遗物外轮廓路径 */
  private static final Texture OUTLINE;

  static {
    IMG = ImageMaster.loadImage("bin/img/MyRelic.png");
    OUTLINE = ImageMaster.loadImage("bin/img/outline/MyRelic.png");
  }

  /*
   * 特殊格式： 1.文本描叙中#r、#y、#b、#g分别能使文本变成红、黄、蓝、绿色。
   * 使用方法：将需要变色的部分无空格放在#r(#y/#b/#g)后面，然后将这一块前后用空格与其他文本隔开。
   * 例：public static final DESCRIPTION = "回合开始时获得 #b3 点力量.";
   * 2.文本描叙中[R]、[G]、[B]分别对应战士、猎手、机器人的能量。
   * 使用方法同上，不再赘叙。
   */
  public PlayerDateControlRelic() {
    /*
     * 参数：ID-遗物Id，
     * new Texture(Gdx.files.internal(IMG))-遗物图片，
     * new Texture(Gdx.files.internal(OUTLINE))-遗物轮廓，
     * RelicTier.BOSS-遗物种类，
     * LandingSound.FLAT-遗物音效。
     */
    super(
       ID,
       IMG,
       OUTLINE,
       RelicTier.STARTER,
       LandingSound.FLAT
    );

  }

  /*
   * 遗物种类：
   * RelicTier.BOSS-boss遗物,
   * RelicTier.COMMON-一般遗物,
   * RelicTier.RARE-罕见遗物,
   * RelicTier.SHOP-商店遗物,
   * RelicTier.SPECIAL-事件遗物,
   * RelicTier.STARTER-初始遗物,
   * RelicTier.UNCOMMON-稀有遗物。
   */
  /*
   * 遗物音效：
   * LandingSound.CLINK,
   * LandingSound.FLAT,
   * LandingSound.HEAVY,
   * LandingSound.MAGICAL,
   * LandingSound.SOLID
   * 具体音效请到游戏内听。
   */

  /**
   * 文本更新方法，当你修改了DESCRIPTION时，调用该方法。
   *
   * @return str
   */
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  /**
   * 复制该遗物信息的方法。
   *
   * @return like this.clone()
   */
  public AbstractRelic makeCopy() {
    return new PlayerDateControlRelic();
  }

  /*
   * 触发时机，以下部分触发时机仅翻译英文，具体效果未知，如有错误，请见谅。
   * <p>
   * 如仍无法满足DIY需求，请详参desktop的各遗物源码或AbstractRelics类的源码。
   * <p>
   * 小tips：在以下触发时机里，需要的闪烁的，可调用flash();让遗物闪一下.
   * <p>
   * 在以下触发时机里，需要遗物计数的可调用this.counter进行运算，用来计数。
   * <p>
   * 也可在上方的ModBaseRelics() super以后调用获得初始计数。
   */

  /**
   * 触发时机：当一张卡被打出且卡牌效果生效前。
   *
   * @param c 使用的卡牌
   * @param m 目标敌人
   */
  public void onPlayCard(final AbstractCard c, final AbstractMonster m) {
    logger.info("onPlayCard(): " + c + " ==> " + m);
    EventList.onPlayCard.forEach(onPlayCard -> onPlayCard.run(c, m));
  }

  /**
   * 触发时机：当一张卡被打出且卡牌效果生效后。(参考死灵之书)
   *
   * @param card 使用的卡牌
   * @param action UseCardAction
   */
  public void onUseCard(final AbstractCard card, final UseCardAction action) {
    logger.info("onUseCard(): " + card + " ==> " + action);
    EventList.onUseCard.forEach(onUseCard -> onUseCard.run(card, action));
  }

  /**
   * 触发时机：当你消耗一张 卡牌时。(参考卡戍之灰)
   *
   * @param card 卡牌
   */
  public void onExhaust(final AbstractCard card) {
    logger.info("onExhaust(): " + card);
    EventList.onExhaust.forEach(onExhaust -> onExhaust.run(card));
  }

  /**
   * 触发时机：当你抽一张牌时。
   *
   * @param card card
   */
  public void onCardDraw(final AbstractCard card) {
    logger.info("onCardDraw(): " + card);
    EventList.onCardDraw.forEach(onCardDraw -> onCardDraw.run(card));
  }

  /*
   * 卡牌信息:
   * c.cardID-卡牌Id,
   * c.cost-卡牌费用,
   * c.costForTurn-卡牌一回合内消耗的费用(如旋风斩的费用),
   * c.color-卡牌颜色,
   * c.damage-含力量、钢笔尖等加成的伤害,
   * c.block-含敏捷等加成的格挡,
   * c.baseDamage-不含力量、钢笔尖等加成的伤害,
   * c.baseBlock-不含敏捷等加成的格挡
   * c.magicNumber-卡牌特殊值,
   * c.type-卡牌种类,
   * c.rarity-卡牌稀有度.
   */

  /**
   * 触发时机：当玩家获得金币时。(参考金神像)
   */
  public void onGainGold() {
    logger.info("onGainGold()");
    EventList.onGainGold.forEach(onGainGold -> onGainGold.run());
  }

  /**
   * 触发时机：当玩家失去金币时。(参考鲜血神像)
   */
  public void onLoseGold() {
    logger.info("onLoseGold()");
    EventList.onLoseGold.forEach(onLoseGold -> onLoseGold.run());
  }

  /**
   * 触发时机：当玩家获得该遗物时。(参考灵体外质、诅咒钥匙、天鹅绒项圈等)
   */
  public void onEquip() {
    logger.info("onEquip()");
    EventList.onEquip.forEach(onEquip -> onEquip.run());
  }

  /**
   * 触发时机：当玩家失去该遗物时。(参考灵体外质、诅咒钥匙、天鹅绒项圈等)
   */
  public void onUnequip() {
    logger.info("onUnequip()");
    EventList.onUnequip.forEach(onUnequip -> onUnequip.run());
  }

  /**
   * 触发时机：每一场战斗（具体作用时机未知）
   */
  public void atPreBattle() {
    logger.info("atPreBattle()");
    EventList.atPreBattle.forEach(atPreBattle -> atPreBattle.run());
  }

  /**
   * 触发时机：当玩家战斗开始时，在第一轮抽牌之后。(参考金刚杵、缩放仪)
   */
  public void atBattleStart() {
    logger.info("atBattleStart()");
    EventList.atBattleStart.forEach(atBattleStart -> atBattleStart.run());
  }

  /**
   * 触发时机：当玩家战斗开始时，在第一轮抽牌的时候，每抽一次牌触发一次。（猜测效果，具体触发时机请实测）
   */
  public void atBattleStartPreDraw() {
    logger.info("atBattleStartPreDraw()");
    EventList.atBattleStartPreDraw.forEach(atBattleStartPreDraw -> atBattleStartPreDraw.run());
  }

  /**
   * 触发时机：在玩家回合开始时。
   */
  public void atTurnStart() {
    logger.info("atTurnStart()");
    EventList.atTurnStart.forEach(atTurnStart -> atTurnStart.run());
  }

  /***
   * 触发时机：在玩家回合结束时。
   */
  public void onPlayerEndTurn() {
    logger.info("onPlayerEndTurn()");
    EventList.onPlayerEndTurn.forEach(onPlayerEndTurn -> onPlayerEndTurn.run());
  }

  /**
   * 触发时机：当你手动弃牌时。
   */
  public void onManualDiscard() {
    logger.info("onManualDiscard()");
    EventList.onManualDiscard.forEach(onManualDiscard -> onManualDiscard.run());
  }

  /**
   * 触发时机：当玩家战斗胜利时。(参考精致折扇)
   */
  public void onVictory() {
    logger.info("onVictory()");
    EventList.onVictory.forEach(onVictory -> onVictory.run());
  }

  /**
   * 触发时机：当一名敌人死亡时。
   *
   * @param m 死亡的敌人
   */
  public void onMonsterDeath(final AbstractMonster m) {
    logger.info("onMonsterDeath()");
    EventList.onMonsterDeath.forEach(onMonsterDeath -> onMonsterDeath.run(m));
  }

  /**
   * 触发时机：当玩家获得格挡时，返回格挡值，可用来修改获得的格挡值。
   *
   * @param blockAmount 格挡
   * @return 格挡
   */
  public int onPlayerGainBlock(int blockAmount) {
    logger.info("onPlayerGainBlock()");
    for (EventInterface.onPlayerGainBlock onPlayerGainBlock : EventList.onPlayerGainBlock) {
      blockAmount = onPlayerGainBlock.run(blockAmount);
    }
    return blockAmount;
  }

  /**
   * 触发时机：当玩家回复生命值时，返回生命值回复数量，可用来修改生命值回复数量。
   *
   * @param healAmount 生命值
   * @return 生命值
   */
  public int onPlayerHeal(int healAmount) {
    logger.info("onPlayerHeal()");
    for (EventInterface.onPlayerHeal onPlayerHeal : EventList.onPlayerHeal) {
      healAmount = onPlayerHeal.run(healAmount);
    }
    return healAmount;
  }

  /**
   * 触发时机：当玩家进入篝火时。
   */
  public void onEnterRestRoom() {
    logger.info("onEnterRestRoom()");
    EventList.onEnterRestRoom.forEach(onEnterRestRoom -> onEnterRestRoom.run());
  }

  /**
   * 触发时机：当玩家在篝火内休息时。
   */
  public void onRest() {
    logger.info("onRest()");
    EventList.onRest.forEach(onRest -> onRest.run());
  }

  /**
   * 触发时机：当玩家攻击时。info.可调用伤害信息。
   *
   * @param info 伤害信息
   * @param damageAmount 伤害数值
   * @param target 伤害目标
   */
  public void onAttack(
     final DamageInfo info,
     final int damageAmount,
     final AbstractCreature target
  ) {
    logger.info("onAttack(): DamageInfo => " + info +
                "; damageAmount => " + damageAmount +
                "; target => " + target);
    EventList.onAttack.forEach(onAttack -> onAttack.run(info, damageAmount, target));
  }

  /**
   * 触发时机：当玩家被攻击时，返回伤害数值，可用来修改伤害数值。info.可调用伤害信息。
   *
   * @param info 伤害信息
   * @param damageAmount 伤害数值
   * @return 未被格挡的伤害（参考鸟居）
   */
  public int onAttacked(final DamageInfo info, int damageAmount) {
    logger.info("onAttacked(): DamageInfo => " + info +
                "; damageAmount => " + damageAmount);
    for (EventInterface.onAttacked onAttacked : EventList.onAttacked) {
      damageAmount = onAttacked.run(info, damageAmount);
    }
    return damageAmount;
  }

  /*
   * 伤害信息：
   * info.owner (该次伤害的攻击者)
   * info.type(该次伤害的种类，可利用info.type.调用伤害种类)
   */
  /*
   * 伤害种类:
   * DamageInfo.DamageType.HP_LOSS (失去生命，无法被格挡，无法触发原版的【受到伤害时】的条件)
   * DamageInfo.DamageType.NORMAL (一般伤害，可以被格挡，能触发原版的【受到伤害时】的条件)
   * DamageInfo.DamageType.THORNS (荆棘伤害，可以被格挡，无法触发原版的【受到伤害时】的条件)
   */

  /**
   * 触发时机：当玩家进入房间时。(参考永恒羽毛)
   *
   * @param room 进入的房间
   */
  public void onEnterRoom(final AbstractRoom room) {
    logger.info("onEnterRoom(): " + room);
    EventList.onEnterRoom.forEach(onEnterRoom -> onEnterRoom.run(room));
  }

  /**
   * 触发时机：当玩家打开一个箱子时。(详参遗物黑星、套娃、诅咒钥匙)
   *
   * @param isBossChest 是否为boss宝箱，true是boss宝箱，false不是boss宝箱。
   */
  public void onChestOpen(final boolean isBossChest) {
    logger.info("onChestOpen(): isBossChest => " + isBossChest);
    EventList.onChestOpen.forEach(onChestOpen -> onChestOpen.run(isBossChest));
  }

  /**
   * 触发时机：当玩家抽卡或者弃卡时。
   */
  public void onDrawOrDiscard() {
    logger.info("onDrawOrDiscard()");
    EventList.onDrawOrDiscard.forEach(onDrawOrDiscard -> onDrawOrDiscard.run());
  }

}
