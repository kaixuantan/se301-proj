package se301.project.robot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import se301.project.task.Task;

import java.util.List;

@RequiredArgsConstructor
public class Robot implements Runnable {
  @Getter
  private final int id;
  private final List<Task> taskQueue;

  @Override
  public void run() {
    for (Task task : taskQueue) {
      task.execute();
    }
  }
}
