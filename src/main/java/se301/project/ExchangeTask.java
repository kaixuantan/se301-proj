package se301.project;

public class ExchangeTask implements Task {
  private int shelfId1;
  private int shelfId2;

  public ExchangeTask(int shelfId1, int shelfId2) {
    this.shelfId1 = shelfId1;
    this.shelfId2 = shelfId2;
  }

  @Override
  public int getShelfId() {
    return shelfId1;
  }

  public int getShelfId2() {
    return shelfId2;
  }
}
