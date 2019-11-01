import bin.frame.PlayerManager;
import org.junit.Test;

import java.net.URL;

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

  @Test
  public void test_2() {
    URL url = T.class.getClassLoader().getResource("bin/img/MyRelic.png");
    assert url != null;
    System.out.println("url.getPath() = " + url.getPath());
    System.out.println("url = " + url.getFile());
  }
}
