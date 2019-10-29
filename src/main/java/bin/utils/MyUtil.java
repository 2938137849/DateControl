package bin.utils;

import basemod.BaseMod;
import basemod.interfaces.ISubscriber;

import javax.swing.table.DefaultTableModel;
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

  public static <T extends ISubscriber> void subscribe(T cub, Class<T> c) {
    BaseMod.subscribe(cub, c);
  }

  public static <T extends ISubscriber> void unsubscribe(T cub, Class<T> c) {
    BaseMod.unsubscribe(cub, c);
  }
}
