package com.aliware.tianchi;

/**
 * @author zrj CreateDate: 2019/5/29
 */
public enum ProviderStateEnum {

  $$$$IDLE(0, 32),
  $$$IDLE(1, 16),
  $$IDLE(2, 8),
  $IDLE(3, 4),
  IDLE(4, 2),
  NORMAL(5, 0),
  BUSY(6, -1),
  $1BUSY(7, -2),
  $2BUSY(8, -3),
  $3BUSY(9, -4),
  $4BUSY(10, -5),
  $5BUSY(11, -6),
  $6BUSY(12, -7),
  ;

  private int id;

  private double value;

  ProviderStateEnum(int id, double value) {
    this.id = id;
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public int getId() {
    return id;
  }

  public boolean isBusy() {
    return value < 0;
  }

  public static ProviderStateEnum getFromValue(int i) {
    for (ProviderStateEnum stateEnum : ProviderStateEnum.values()) {
      if (stateEnum.getId() == i) {
        return stateEnum;
      }
    }
    return NORMAL;
  }

}
