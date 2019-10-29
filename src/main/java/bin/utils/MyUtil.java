package bin.utils;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;

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

  public static void TableModelListener(TableModelEvent e, ArrayList<AbstractCard> group, CardType type) {
    if (e.getType() == TableModelEvent.UPDATE) {
      int row = e.getFirstRow();
      DefaultTableModel table = (DefaultTableModel)e.getSource();
      Integer i = (Integer)table.getValueAt(row, 0);
      AbstractCard card = group.get(i);
      Integer value = (Integer)table.getValueAt(row, e.getColumn());
      switch (type) {
        case COST:
          if (value != null) {
            card.cost = value;
            card.costForTurn = value;
          }
          break;
        case DAMAGE:
          break;
      }
    }
  }

  public static <T extends ISubscriber> void subscribe(T cub, Class<T> c) {
    BaseMod.subscribe(cub, c);
  }

  public static <T extends ISubscriber> void unsubscribe(T cub, Class<T> c) {
    BaseMod.unsubscribe(cub, c);
  }

  public enum CardType {
    DAMAGE,
    COST
  }
}
