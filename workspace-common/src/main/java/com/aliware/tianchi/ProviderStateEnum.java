package com.aliware.tianchi;


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

  private int stateID;

  // value表示不同的state，所对应的调整幅度
  private double value;

  ProviderStateEnum(int stateID, double value) {
    this.stateID = stateID;
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public int getStateID() {
    return stateID;
  }

  public boolean isBusy() {
    return value < 0;
  }

  public static ProviderStateEnum getStateFromID(int stateID) {
    for (ProviderStateEnum stateEnum : ProviderStateEnum.values()) {
      if (stateEnum.getStateID() == stateID) {
        return stateEnum;
      }
    }
    return NORMAL;
  }

}
