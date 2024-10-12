package se301.project.factory;

import lombok.RequiredArgsConstructor;
import se301.project.robot.Robot;
import se301.project.robot.RobotImpl;
import se301.project.task.Task;

import java.util.List;

@RequiredArgsConstructor
public class RobotFactory implements Factory<Robot, Integer> {
    private final List<Task> taskQueue;

    @Override
    public Robot create(Integer robotId) {
        return new RobotImpl(robotId, taskQueue);
    }
}
