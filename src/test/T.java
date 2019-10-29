import bin.frame.PlayerManager;
import org.junit.Test;

/**
 * @author bin
 * @version 1.0.0
 */
public class T {
  @Test
  public void test_1() {
    new PlayerManager().setVisible(true);
    try {
      Thread.sleep(1000 * 60);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
