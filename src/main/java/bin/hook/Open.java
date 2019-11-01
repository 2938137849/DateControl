package bin.hook;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostPlayerUpdateSubscriber;
import basemod.interfaces.StartGameSubscriber;
import bin.frame.PlayerManager;
import bin.relics.PlayerDateControlRelic;
import bin.utils.MyUtil;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;

/**
 * @author bin
 * @version 1.0.0
 */

@SpireInitializer
public final class Open implements PostInitializeSubscriber, StartGameSubscriber, EditRelicsSubscriber, EditStringsSubscriber {
  private PlayerManager manager;
  private boolean isClose = false;

  private Open() {
    manager = new PlayerManager();
    BaseMod.subscribe(this);

    MyUtil.subscribe(this::flash, PostPlayerUpdateSubscriber.class);
    MyUtil.subscribe(this::flash, PostDungeonUpdateSubscriber.class);
  }


  public static void initialize() {
    new Open();
  }

  public void receivePostInitialize() {
    ModPanel settingPanel = new ModPanel();
    ModLabel text = new ModLabel(
       "Click The Button Below to toggle the on/off state",
       350.0F,
       700.0F,
       Color.SALMON,
       settingPanel,
       (click) -> {
       }
    );
    settingPanel.addUIElement(text);
    ModLabeledToggleButton
       btn =
       new ModLabeledToggleButton(
          "Close It ?",
          400.0F,
          500.0F,
          Settings.GREEN_TEXT_COLOR,
          FontHelper.charDescFont,
          isClose,
          settingPanel,
          (l) -> {
          },
          (button) -> {
            this.isClose = button.enabled;
            System.out.println("isClose = " + isClose);
          }

       );
    settingPanel.addUIElement(btn);


    BaseMod.registerModBadge(
       ImageMaster.loadImage("bin/img/outline/MyRelicUI.png"),
       "Game Data Edit Mod",
       "bin",
       "TODO",
       settingPanel
    );
  }

  public void receiveStartGame() {
    if (isClose) return;
    manager.setVisible(true);
    manager.init();
  }

  private void flash() {
    manager.setPlayer(AbstractDungeon.player);
  }

  public void receiveEditRelics() {
    BaseMod.addRelic(new PlayerDateControlRelic(), RelicType.SHARED);
  }

  public void receiveEditStrings() {
    BaseMod.loadCustomStringsFile(RelicStrings.class, "bin/localization/RelicStrings.json");
  }

}
