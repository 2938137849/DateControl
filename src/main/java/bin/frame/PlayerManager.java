/*
 * Created by JFormDesigner on Sat Oct 26 17:50:39 CST 2019
 */

package bin.frame;

import basemod.BaseMod;
import basemod.interfaces.MaxHPChangeSubscriber;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostPlayerUpdateSubscriber;
import bin.utils.MyUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * @author bin
 * @version 1.0
 */
public class PlayerManager extends JFrame {
  private static final Logger logger = LogManager.getLogger(PlayerManager.class.getName());

  private final PostPlayerUpdateSubscriber Hp;
  private MaxHPChangeSubscriber MaxHp;
  private final PostPlayerUpdateSubscriber Mp;
  private final PostPlayerUpdateSubscriber MaxMp;
  private final PostPlayerUpdateSubscriber Gold;
  private final OnCardUseSubscriber Damage;
  private final OnCardUseSubscriber Block;
  private final PostDrawSubscriber Upgrade;

  {
    Hp = () -> this.player.currentHealth = ((int)this.spinnerHp.getValue());
    MaxHp = new MaxHPChangeSubscriber() {
      @Override public int receiveMaxHPChange(int i) {
        return 0;
      }
    };
    Mp = () -> EnergyPanel.totalCount = ((int)this.spinnerMp.getValue());
    MaxMp = () -> this.player.energy.energy = ((int)this.spinnerMp.getValue());
    Gold = () -> this.player.gold = ((int)(this.spinnerGold).getValue());
    Damage = card -> {
      AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(
         null,
         DamageInfo.createDamageMatrix((int)this.spinnerDamage.getValue(), true),
         DamageInfo.DamageType.THORNS,
         AbstractGameAction.AttackEffect.NONE,
         true
      ));
      logger.info(card.name + ".baseDamage = " + card.baseDamage);
    };
    Block = card -> {
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(
         AbstractDungeon.player,
         AbstractDungeon.player,
         (int)this.spinnerBlock.getValue(),
         true
      ));
      card.baseBlock += ((int)this.spinnerBlock.getValue());
      logger.info(card.name + ".baseBlock = " + card.baseBlock);
    };
    Upgrade = card -> {
      logger.info(card.name + " upgrade");
      card.upgrade();
    };
    BaseMod.underScoreCardIDs.containsKey("");
  }

  private AbstractPlayer player = null;

  private void flash() {
    flashPlayerMgr();
  }

  private void flashPlayerMgr() {
    if (spinnerHp.isEnabled()) spinnerHp.setValue(player.currentHealth);
    if (spinnerMaxHp.isEnabled()) spinnerMaxHp.setValue(player.maxHealth);
    if (spinnerMp.isEnabled()) spinnerMp.setValue(EnergyPanel.totalCount);
    if (spinnerMaxMp.isEnabled()) spinnerMaxMp.setValue(player.energy.energyMaster);
    if (spinnerGold.isEnabled()) spinnerGold.setValue(player.gold);
  }

  private void flashTable(int currentTable) {
    System.out.println(currentTable);
    DefaultTableModel model;
    switch (currentTable) {
      case 2:
        model = (DefaultTableModel)tablePotions.getModel();
        MyUtil.clearModel(model);
        this.player.potions.forEach(potion -> {
          Vector<Object> vector = MyUtil.createVector(
             model.getRowCount(),
             potion.ID,
             potion.name
          );
          model.addRow(vector);
        });
        break;
      case 0:
        model = (DefaultTableModel)tableCards.getModel();
        MyUtil.clearModel(model);
        this.player.masterDeck.group.forEach(card -> {
          Vector<Object> vector = MyUtil.createVector(
             model.getRowCount(),
             card.cardID,
             card.name,
             card.costForTurn
          );
          model.addRow(vector);
        });
        break;
      case 1:
        model = (DefaultTableModel)tableRelics.getModel();
        MyUtil.clearModel(model);
        this.player.relics.forEach(relic -> {
          Vector<Object> vector =
             MyUtil.createVector(
                model.getRowCount(),
                relic.relicId,
                relic.name,
                relic.description
             );
          model.addRow(vector);
        });
        break;
      case 3:
        model = (DefaultTableModel)tableHandCrads.getModel();
        MyUtil.clearModel(model);
        this.player.hand.group.forEach(card -> {
          Vector<Object> vector = MyUtil.createVector(
             model.getRowCount(),
             card.cardID,
             card.name,
             card.costForTurn
          );
          model.addRow(vector);
        });
        break;
    }
  }

  public void setPlayer(AbstractPlayer player) {
    if (this.player != player) {
      this.player = player;
    }
    flash();
  }

  public PlayerManager() {
    initComponents();
    tableCards.getModel().addTableModelListener(e ->
       MyUtil.TableModelListener(
          e,
          AbstractDungeon.player.masterDeck.group,
          MyUtil.CardType.COST
       )
    );
    tableHandCrads.getModel().addTableModelListener(e ->
       MyUtil.TableModelListener(
          e,
          AbstractDungeon.player.hand.group,
          MyUtil.CardType.COST
       )
    );
    /*tablePotions.getModel().addTableModelListener(e -> {
      if (e.getType() == TableModelEvent.UPDATE) {
        System.out.println("e.getColumn() = " + e.getColumn());
        System.out.println("e.getFirstRow() = " + e.getFirstRow());
      }
    });
    tableRelics.getModel().addTableModelListener(e -> {
      if (e.getType() == TableModelEvent.UPDATE) {
        System.out.println("e.getColumn() = " + e.getColumn());
        System.out.println("e.getFirstRow() = " + e.getFirstRow());
      }
    });*/
    pack();
  }

  public void init() {
    lockHp.setSelected(false);
    lockMaxHp.setSelected(false);
    lockMp.setSelected(false);
    lockMaxMp.setSelected(false);
    lockGold.setSelected(false);
    EXDamage.setSelected(false);
    EXBlock.setSelected(false);
    checkBoxUpgrade.setSelected(false);
  }

  private void spinnerHpStateChanged(ChangeEvent e) {
    if (player != null) {
      player.currentHealth = ((int)((JSpinner)e.getSource()).getValue());
      logger.info("player.currentHealth = " + player.currentHealth);
    }
    flashPlayerMgr();
  }

  private void spinnerMaxHpStateChanged(ChangeEvent e) {
    if (player != null) {
      player.maxHealth = ((int)((JSpinner)e.getSource()).getValue());
      logger.info("player.maxHealth = " + player.maxHealth);
    }
    flashPlayerMgr();
  }

  private void spinnerMpStateChanged(ChangeEvent e) {
    if (player != null) {
      EnergyPanel.totalCount = ((int)((JSpinner)e.getSource()).getValue());
      logger.info("EnergyPanel.totalCount = " + EnergyPanel.totalCount);
      player.hand.glowCheck();
    }
    flashPlayerMgr();
  }

  private void spinnerMaxMpStateChanged(ChangeEvent e) {
    if (player != null) {
      player.energy.energy = ((int)((JSpinner)e.getSource()).getValue());
      player.energy.energyMaster = player.energy.energy;
      logger.info("player.energy.energy = " + player.energy.energy);
    }
    flashPlayerMgr();
  }

  private void spinnerGoldStateChanged(ChangeEvent e) {
    if (player != null) {
      player.gold = ((int)((JSpinner)e.getSource()).getValue());
      logger.info("player.gold = " + player.gold);
    }
    flashPlayerMgr();
  }

  private void LockGoldItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerGold.setEnabled(false);
      MyUtil.subscribe(Gold, PostPlayerUpdateSubscriber.class);
      logger.info("Gold.lock");
    } else {
      spinnerGold.setEnabled(true);
      MyUtil.unsubscribe(Gold, PostPlayerUpdateSubscriber.class);
      logger.info("Gold.unlock");
    }
  }

  private void checkBoxUpgradeItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      MyUtil.subscribe(Upgrade, PostDrawSubscriber.class);
      logger.info("Upgrade.on");
    } else {
      MyUtil.unsubscribe(Upgrade, PostDrawSubscriber.class);
      logger.info("Upgrade.off");
    }
  }

  private void lockMpItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerHp.setEnabled(false);
      MyUtil.subscribe(Mp, PostPlayerUpdateSubscriber.class);
      logger.info("Mp.lock");
    } else {
      spinnerHp.setEnabled(true);
      MyUtil.unsubscribe(Mp, PostPlayerUpdateSubscriber.class);
      logger.info("Mp.unlock");
    }
  }

  private void lockHpItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerHp.setEnabled(false);
      MyUtil.subscribe(Hp, PostPlayerUpdateSubscriber.class);
      logger.info("Hp.lock");
    } else {
      spinnerHp.setEnabled(true);
      MyUtil.unsubscribe(Hp, PostPlayerUpdateSubscriber.class);
      logger.info("Hp.unlock");
    }
  }

  private void lockMaxHpItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerMaxHp.setEnabled(false);
      MyUtil.subscribe(MaxHp, MaxHPChangeSubscriber.class);
      logger.info("MaxHp.lock");
    } else {
      spinnerMaxHp.setEnabled(true);
      MyUtil.unsubscribe(MaxHp, MaxHPChangeSubscriber.class);
      logger.info("MaxHp.unlock");
    }
  }

  private void lockMaxMpItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerMaxMp.setEnabled(false);
      MyUtil.subscribe(MaxMp, PostPlayerUpdateSubscriber.class);
      logger.info("MaxMp.lock");
    } else {
      spinnerMaxMp.setEnabled(true);
      MyUtil.unsubscribe(MaxMp, PostPlayerUpdateSubscriber.class);
      logger.info("MaxMp.unlock");
    }
  }

  private void tabbedPane1StateChanged(ChangeEvent e) {
    if (this.player == null) return;
    int currentTable = ((JTabbedPane)e.getSource()).getSelectedIndex();
    flashTable(currentTable);
  }

  private void EXDamageItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerDamage.setEnabled(true);
      MyUtil.subscribe(Damage, OnCardUseSubscriber.class);
      logger.info("Damage.lock");
    } else {
      spinnerDamage.setEnabled(false);
      MyUtil.unsubscribe(Damage, OnCardUseSubscriber.class);
      logger.info("Damage.unlock");
    }
  }

  private void EXBlockItemStateChanged(ItemEvent e) {
    if (((JCheckBox)e.getSource()).isSelected()) {
      spinnerBlock.setEnabled(true);
      MyUtil.subscribe(Block, OnCardUseSubscriber.class);
      logger.info("Block.lock");
    } else {
      spinnerBlock.setEnabled(false);
      MyUtil.unsubscribe(Block, OnCardUseSubscriber.class);
      logger.info("Block.unlock");
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    ResourceBundle bundle = ResourceBundle.getBundle("language");
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel();
    this.spinnerHp = new JSpinner();
    this.lockHp = new JCheckBox();
    JLabel label2 = new JLabel();
    this.spinnerMaxHp = new JSpinner();
    this.lockMaxHp = new JCheckBox();
    JLabel label3 = new JLabel();
    this.spinnerMp = new JSpinner();
    this.lockMp = new JCheckBox();
    JLabel label4 = new JLabel();
    this.spinnerMaxMp = new JSpinner();
    this.lockMaxMp = new JCheckBox();
    JLabel label5 = new JLabel();
    this.spinnerGold = new JSpinner();
    this.lockGold = new JCheckBox();
    JPanel panel3 = new JPanel();
    this.EXDamage = new JCheckBox();
    this.spinnerDamage = new JSpinner();
    this.EXBlock = new JCheckBox();
    this.spinnerBlock = new JSpinner();
    this.checkBoxUpgrade = new JCheckBox();
    JTabbedPane tabbedPane1 = new JTabbedPane();
    JScrollPane scrollPane1 = new JScrollPane();
    this.tableCards = new JTable();
    JScrollPane scrollPane3 = new JScrollPane();
    this.tableRelics = new JTable();
    JScrollPane scrollPane2 = new JScrollPane();
    this.tablePotions = new JTable();
    JScrollPane scrollPane4 = new JScrollPane();
    this.tableHandCrads = new JTable();

    //======== this ========
    setTitle(bundle.getString("PlayerMgr"));
    setResizable(false);
    setAlwaysOnTop(true);
    setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());
    ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[]{0, 0, 0};
    ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[]{105, 173, 0};
    ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
    ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

    //======== panel1 ========
    {
      panel1.setName(bundle.getString("PlayerMgr"));
      panel1.setBorder(new TitledBorder(bundle.getString("PlayerMgr")));
      panel1.setLayout(new GridBagLayout());
      ((GridBagLayout)panel1.getLayout()).columnWidths = new int[]{0, 50, 0, 0};
      ((GridBagLayout)panel1.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0};
      ((GridBagLayout)panel1.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};
      ((GridBagLayout)panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

      //---- label1 ----
      label1.setText(bundle.getString("HP"));
      label1.setHorizontalAlignment(SwingConstants.CENTER);
      panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerHp ----
      this.spinnerHp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerHp.setModel(new SpinnerNumberModel(0, 0, null, 1));
      this.spinnerHp.setBorder(null);
      this.spinnerHp.addChangeListener(e -> spinnerHpStateChanged(e));
      panel1.add(this.spinnerHp, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- lockHp ----
      this.lockHp.setText(bundle.getString("Locking"));
      this.lockHp.setHorizontalAlignment(SwingConstants.CENTER);
      this.lockHp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.lockHp.setBorderPainted(true);
      this.lockHp.setMargin(new Insets(5, 5, 5, 5));
      this.lockHp.addItemListener(e -> lockHpItemStateChanged(e));
      panel1.add(this.lockHp, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- label2 ----
      label2.setText(bundle.getString("MaxHP"));
      label2.setHorizontalAlignment(SwingConstants.CENTER);
      panel1.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerMaxHp ----
      this.spinnerMaxHp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerMaxHp.setModel(new SpinnerNumberModel(0, 0, null, 1));
      this.spinnerMaxHp.setBorder(null);
      this.spinnerMaxHp.addChangeListener(e -> spinnerMaxHpStateChanged(e));
      panel1.add(this.spinnerMaxHp, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- lockMaxHp ----
      this.lockMaxHp.setText(bundle.getString("Locking"));
      this.lockMaxHp.setHorizontalAlignment(SwingConstants.CENTER);
      this.lockMaxHp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.lockMaxHp.setBorderPainted(true);
      this.lockMaxHp.setMargin(new Insets(5, 5, 5, 5));
      this.lockMaxHp.addItemListener(e -> lockMaxHpItemStateChanged(e));
      panel1.add(this.lockMaxHp, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- label3 ----
      label3.setText(bundle.getString("MP"));
      label3.setHorizontalAlignment(SwingConstants.CENTER);
      panel1.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerMp ----
      this.spinnerMp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerMp.setModel(new SpinnerNumberModel(0, -999, 999, 1));
      this.spinnerMp.setBorder(null);
      this.spinnerMp.addChangeListener(e -> spinnerMpStateChanged(e));
      panel1.add(this.spinnerMp, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- lockMp ----
      this.lockMp.setText(bundle.getString("Locking"));
      this.lockMp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.lockMp.setBorderPainted(true);
      this.lockMp.setMargin(new Insets(5, 5, 5, 5));
      this.lockMp.addItemListener(e -> lockMpItemStateChanged(e));
      panel1.add(this.lockMp, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- label4 ----
      label4.setText(bundle.getString("MaxMP"));
      label4.setHorizontalAlignment(SwingConstants.CENTER);
      panel1.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerMaxMp ----
      this.spinnerMaxMp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerMaxMp.setModel(new SpinnerNumberModel(0, -999, 999, 1));
      this.spinnerMaxMp.setBorder(null);
      this.spinnerMaxMp.addChangeListener(e -> spinnerMaxMpStateChanged(e));
      panel1.add(this.spinnerMaxMp, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- lockMaxMp ----
      this.lockMaxMp.setText(bundle.getString("Locking"));
      this.lockMaxMp.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.lockMaxMp.setBorderPainted(true);
      this.lockMaxMp.setMargin(new Insets(5, 5, 5, 5));
      this.lockMaxMp.addItemListener(e -> lockMaxMpItemStateChanged(e));
      panel1.add(this.lockMaxMp, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- label5 ----
      label5.setText(bundle.getString("Gold"));
      label5.setHorizontalAlignment(SwingConstants.CENTER);
      panel1.add(label5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 5, 10), 0, 0
      ));

      //---- spinnerGold ----
      this.spinnerGold.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerGold.setBorder(null);
      this.spinnerGold.addChangeListener(e -> spinnerGoldStateChanged(e));
      panel1.add(this.spinnerGold, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 5, 10), 0, 0
      ));

      //---- lockGold ----
      this.lockGold.setText(bundle.getString("Locking"));
      this.lockGold.setHorizontalAlignment(SwingConstants.CENTER);
      this.lockGold.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.lockGold.setBorderPainted(true);
      this.lockGold.setMargin(new Insets(5, 5, 5, 5));
      this.lockGold.addItemListener(e -> LockGoldItemStateChanged(e));
      panel1.add(this.lockGold, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.NONE,
         new Insets(5, 5, 5, 5), 0, 0
      ));
    }
    contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
       GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(5, 5, 10, 10), 0, 0
    ));

    //======== panel3 ========
    {
      panel3.setBorder(new TitledBorder(bundle.getString("EXEvent")));
      panel3.setLayout(new GridBagLayout());
      ((GridBagLayout)panel3.getLayout()).columnWidths = new int[]{0, 0, 0};
      ((GridBagLayout)panel3.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
      ((GridBagLayout)panel3.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
      ((GridBagLayout)panel3.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

      //---- EXDamage ----
      this.EXDamage.setText(bundle.getString("EXDamage"));
      this.EXDamage.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.EXDamage.setBorderPainted(true);
      this.EXDamage.setMargin(new Insets(5, 5, 5, 5));
      this.EXDamage.addItemListener(e -> EXDamageItemStateChanged(e));
      panel3.add(this.EXDamage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerDamage ----
      this.spinnerDamage.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerDamage.setBorder(null);
      this.spinnerDamage.setModel(new SpinnerNumberModel(0, -999, 999, 1));
      this.spinnerDamage.setEnabled(false);
      panel3.add(this.spinnerDamage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- EXBlock ----
      this.EXBlock.setText(bundle.getString("EXBlock"));
      this.EXBlock.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.EXBlock.setBorderPainted(true);
      this.EXBlock.setMargin(new Insets(5, 5, 5, 5));
      this.EXBlock.addItemListener(e -> EXBlockItemStateChanged(e));
      panel3.add(this.EXBlock, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));

      //---- spinnerBlock ----
      this.spinnerBlock.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.spinnerBlock.setBorder(null);
      this.spinnerBlock.setModel(new SpinnerNumberModel(0, -999, 999, 1));
      this.spinnerBlock.setEnabled(false);
      panel3.add(this.spinnerBlock, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 5), 0, 0
      ));

      //---- checkBoxUpgrade ----
      this.checkBoxUpgrade.setText(bundle.getString("Upgrade"));
      this.checkBoxUpgrade.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
      this.checkBoxUpgrade.setBorderPainted(true);
      this.checkBoxUpgrade.setMargin(new Insets(5, 5, 5, 5));
      this.checkBoxUpgrade.addItemListener(e -> checkBoxUpgradeItemStateChanged(e));
      panel3.add(this.checkBoxUpgrade, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
         GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(5, 5, 10, 10), 0, 0
      ));
    }
    contentPane.add(panel3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
       GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(5, 5, 10, 5), 0, 0
    ));

    //======== tabbedPane1 ========
    {
      tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      tabbedPane1.setPreferredSize(null);
      tabbedPane1.setMaximumSize(new Dimension(0, 173));
      tabbedPane1.setMinimumSize(null);
      tabbedPane1.addChangeListener(e -> tabbedPane1StateChanged(e));

      //======== scrollPane1 ========
      {
        scrollPane1.setPreferredSize(null);

        //---- tableCards ----
        this.tableCards.setModel(new DefaultTableModel(
           new Object[][]{
              {null, null, null, null},
              {null, null, null, null},
              {null, null, null, null},
              {null, null, null, null},
              {null, null, null, null},
              {null, null, null, null},
              {null, null, null, null},
           },
           new String[]{
              "i", "ID", "Name", "Cost"
           }
        ) {
          Class<?>[] columnTypes = new Class<?>[]{
             Integer.class, String.class, String.class, Integer.class
          };
          boolean[] columnEditable = new boolean[]{
             false, false, false, true
          };

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            return this.columnTypes[columnIndex];
          }

          @Override
          public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.columnEditable[columnIndex];
          }
        });
        {
          TableColumnModel cm = this.tableCards.getColumnModel();
          cm.getColumn(0).setResizable(false);
          cm.getColumn(0).setMinWidth(30);
          cm.getColumn(0).setMaxWidth(30);
          cm.getColumn(0).setPreferredWidth(30);
        }
        this.tableCards.setDropMode(DropMode.INSERT_ROWS);
        this.tableCards.setPreferredScrollableViewportSize(new Dimension(0, 0));
        this.tableCards.setPreferredSize(null);
        scrollPane1.setViewportView(this.tableCards);
      }
      tabbedPane1.addTab(bundle.getString("CardMgr"), scrollPane1);

      //======== scrollPane3 ========
      {
        scrollPane3.setPreferredSize(null);

        //---- tableRelics ----
        this.tableRelics.setModel(new DefaultTableModel(
           new Object[][]{
              {null, null, null, null},
           },
           new String[]{
              "i", "ID", "Name", "Description"
           }
        ) {
          Class<?>[] columnTypes = new Class<?>[]{
             Integer.class, String.class, String.class, String.class
          };
          boolean[] columnEditable = new boolean[]{
             false, false, false, false
          };

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            return this.columnTypes[columnIndex];
          }

          @Override
          public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.columnEditable[columnIndex];
          }
        });
        {
          TableColumnModel cm = this.tableRelics.getColumnModel();
          cm.getColumn(0).setResizable(false);
          cm.getColumn(0).setMinWidth(30);
          cm.getColumn(0).setMaxWidth(30);
          cm.getColumn(0).setPreferredWidth(30);
        }
        this.tableRelics.setPreferredScrollableViewportSize(null);
        scrollPane3.setViewportView(this.tableRelics);
      }
      tabbedPane1.addTab(bundle.getString("RelicMgr"), scrollPane3);

      //======== scrollPane2 ========
      {
        scrollPane2.setPreferredSize(null);

        //---- tablePotions ----
        this.tablePotions.setDropMode(DropMode.INSERT_ROWS);
        this.tablePotions.setMinimumSize(null);
        this.tablePotions.setPreferredScrollableViewportSize(null);
        this.tablePotions.setModel(new DefaultTableModel(
           new Object[][]{
              {null, null, null},
           },
           new String[]{
              "i", "ID", "Name"
           }
        ) {
          Class<?>[] columnTypes = new Class<?>[]{
             Integer.class, String.class, String.class
          };
          boolean[] columnEditable = new boolean[]{
             false, false, false
          };

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            return this.columnTypes[columnIndex];
          }

          @Override
          public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.columnEditable[columnIndex];
          }
        });
        {
          TableColumnModel cm = this.tablePotions.getColumnModel();
          cm.getColumn(0).setResizable(false);
          cm.getColumn(0).setMinWidth(30);
          cm.getColumn(0).setMaxWidth(30);
          cm.getColumn(0).setPreferredWidth(30);
        }
        scrollPane2.setViewportView(this.tablePotions);
      }
      tabbedPane1.addTab(bundle.getString("PotionMgr"), scrollPane2);

      //======== scrollPane4 ========
      {

        //---- tableHandCrads ----
        this.tableHandCrads.setModel(new DefaultTableModel(
           new Object[][]{
              {null, null, null, null},
              {null, null, null, null},
           },
           new String[]{
              "i", "ID", "Name", "Cost"
           }
        ) {
          Class<?>[] columnTypes = new Class<?>[]{
             Integer.class, String.class, String.class, Integer.class
          };
          boolean[] columnEditable = new boolean[]{
             false, false, false, true
          };

          @Override
          public Class<?> getColumnClass(int columnIndex) {
            return this.columnTypes[columnIndex];
          }

          @Override
          public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.columnEditable[columnIndex];
          }
        });
        {
          TableColumnModel cm = this.tableHandCrads.getColumnModel();
          cm.getColumn(0).setResizable(false);
          cm.getColumn(0).setMinWidth(30);
          cm.getColumn(0).setMaxWidth(30);
          cm.getColumn(0).setPreferredWidth(30);
        }
        this.tableHandCrads.setPreferredScrollableViewportSize(null);
        scrollPane4.setViewportView(this.tableHandCrads);
      }
      tabbedPane1.addTab(bundle.getString("HandCardMgr"), scrollPane4);
    }
    contentPane.add(tabbedPane1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
       GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(5, 5, 5, 5), 0, 0
    ));
    pack();
    setLocationRelativeTo(null);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JSpinner spinnerHp;
  private JCheckBox lockHp;
  private JSpinner spinnerMaxHp;
  private JCheckBox lockMaxHp;
  private JSpinner spinnerMp;
  private JCheckBox lockMp;
  private JSpinner spinnerMaxMp;
  private JCheckBox lockMaxMp;
  private JSpinner spinnerGold;
  private JCheckBox lockGold;
  private JCheckBox EXDamage;
  private JSpinner spinnerDamage;
  private JCheckBox EXBlock;
  private JSpinner spinnerBlock;
  private JCheckBox checkBoxUpgrade;
  private JTable tableCards;
  private JTable tableRelics;
  private JTable tablePotions;
  private JTable tableHandCrads;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
