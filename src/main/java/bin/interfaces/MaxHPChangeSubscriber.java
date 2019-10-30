package bin.interfaces;

/**
 * @author bin
 * @version 1.0.0
 */
@FunctionalInterface
public interface MaxHPChangeSubscriber extends basemod.interfaces.MaxHPChangeSubscriber {
  @Override
  int receiveMaxHPChange(int amount);
}