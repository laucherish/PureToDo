package io.github.laucherish.puretodo.data.source;

import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.data.source.local.TasksLocalDataSource;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksLocalDataSource mTasksLocalDataSource;

    private TasksRepository(TasksLocalDataSource tasksLocalDataSource) {
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(TasksLocalDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void saveTask(Task task) {
        mTasksLocalDataSource.saveTask(task);
    }

    @Override
    public void deleteTask(String taskId) {
        mTasksLocalDataSource.deleteTask(taskId);
    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
    }

    @Override
    public void getTask(String taskId, GetTaskCallback callback) {
        mTasksLocalDataSource.getTask(taskId,callback);
    }

    @Override
    public void getAllTasks(LoadTasksCallback callback) {
        mTasksLocalDataSource.getAllTasks(callback);
    }

    @Override
    public void completeTask(String taskId) {
        mTasksLocalDataSource.completeTask(taskId);
    }

    @Override
    public void activateTask(String taskId) {
        mTasksLocalDataSource.activateTask(taskId);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksLocalDataSource.clearCompletedTasks();
    }

    @Override
    public void refreshTasks() {

    }
}
