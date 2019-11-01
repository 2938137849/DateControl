package bin.utils;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author bin
 * @version 1.0.0
 */
public final class MyUtil {
  @SafeVarargs public static <T> Vector<T> createVector(T... obj) {
    return new Vector<>(Arrays.asList(obj));
  }

  public static void clearModel(DefaultTableModel model) {
    for (int i = model.getRowCount() - 1; i >= 0; i--) {
      model.removeRow(i);
    }
  }

  public static void CardTableListener(TableModelEvent e, ArrayList<AbstractCard> group) {
    if (e.getType() != TableModelEvent.UPDATE) return;
    int row = e.getFirstRow();
    DefaultTableModel table = (DefaultTableModel)e.getSource();
    Integer i = (Integer)table.getValueAt(row, 0);
    AbstractCard card = group.get(i);
    if (card == null) return;
    int column = e.getColumn();
    Object value = table.getValueAt(row, column);
    if (value == null) return;
    if (column == TableDataColumn.ID) {
      if (!table.getValueAt(row, TableDataColumn.NAME).equals(card.name)) return;
    } else {
      if (!table.getValueAt(row, TableDataColumn.ID).equals(card.cardID)) return;
    }

    switch (column) {
      case TableDataColumn.COST:
        card.cost = (int)value;
        card.costForTurn = (int)value;
        break;
      case TableDataColumn.ID:
        if ("".equals(value)) {
          group.remove(row);
        } else if (CardLibrary.cards.containsKey((String)value)) {
          group.set(row, CardLibrary.getCopy((String)value));
        }
        break;
      case TableDataColumn.NAME:
        card.name = ((String)value);
        break;
    }

  }

  public static void RelicTableListener(TableModelEvent e, ArrayList<AbstractRelic> group) {
    if (e.getType() != TableModelEvent.UPDATE) return;
    int row = e.getFirstRow();
    DefaultTableModel table = (DefaultTableModel)e.getSource();
    Integer i = (Integer)table.getValueAt(row, 0);
    AbstractRelic relic = group.get(i);
    if (relic == null) return;
    int column = e.getColumn();
    if (column != TableDataColumn.ID) return;
    Object value = table.getValueAt(row, column);
    if (value == null) return;
    if (!table.getValueAt(row, TableDataColumn.NAME).equals(relic.name)) return;
    if ("".equals(value)) {
      group.remove(row);
      AbstractDungeon.player.reorganizeRelics();
    } else if (RelicLibrary.isARelic((String)value)) {
      group.remove(row);
      AbstractDungeon.player.reorganizeRelics();
      addRelic(((String)value), true);
    }
  }

  public static <T extends ISubscriber> void subscribe(T cub, Class<T> c) {
    BaseMod.subscribe(cub, c);
  }

  public static <T extends ISubscriber> void unsubscribe(T cub, Class<T> c) {
    BaseMod.unsubscribe(cub, c);
  }

  public static void addRelic(String relicId, boolean makeCopy) {
    AbstractRelic relic = RelicLibrary.getRelic(relicId);
    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
       (float)(Settings.WIDTH / 2),
       (float)(Settings.HEIGHT / 2),
       makeCopy ? relic.makeCopy() : relic
    );
  }

  public static void removeRelic(String relicId) {
    AbstractDungeon.player.loseRelic(relicId);
  }

  public static class TableDataColumn {
    public static final byte ID = 1;
    public static final byte NAME = 2;
    public static final byte COST = 3;
  }

}
