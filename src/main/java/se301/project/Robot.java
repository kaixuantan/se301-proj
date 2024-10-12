package se301.project;

import java.util.List;

public class Robot implements Runnable {
  private int id;
  private List<Task> taskQueue;

  public Robot(int id, List<Task> taskQueue) {
    this.id = id;
    this.taskQueue = taskQueue;
  }

  public int getId() {
    return id;
  }

  @Override
  public void run() {
    for (Task task : taskQueue) {
      System.out.println("Robot " + id + ": " +  task.execute());
    }
  }
}
