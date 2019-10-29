package bin.hook;

import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostEnergyRechargeSubscriber;
import basemod.interfaces.PostPlayerUpdateSubscriber;
import basemod.interfaces.StartGameSubscriber;
import bin.frame.PlayerManager;
import bin.utils.MyUtil;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author bin
 * @version 1.0.0
 */

@SpireInitializer
@SuppressWarnings("unused")
public class Open {
  private static final Logger logger = LogManager.getLogger(Open.class.getName());
  private PlayerManager manager;


  private Open() {
    manager = new PlayerManager();
    MyUtil.subscribe(this::receiveStartGame, StartGameSubscriber.class);

    MyUtil.subscribe(this::flash, PostPlayerUpdateSubscriber.class);
    MyUtil.subscribe(this::flash, PostEnergyRechargeSubscriber.class);
    MyUtil.subscribe(this::flash, PostDungeonUpdateSubscriber.class);
  }

  public static void initialize() {
    new Open();
  }

  private void receiveStartGame() {
    manager.setVisible(true);
    manager.init();
  }

  private void flash() {
    manager.setPlayer(AbstractDungeon.player);
  }


}
