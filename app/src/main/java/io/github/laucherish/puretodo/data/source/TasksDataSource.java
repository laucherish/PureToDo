package io.github.laucherish.puretodo.data.source;

import java.util.List;

import io.github.laucherish.puretodo.data.Task;

/**
 * @author laucherish
 * @date 16/4/14
 */
public interface TasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void saveTask(Task task);

    void deleteTask(String taskId);

    void deleteAllTasks();

    void getTask(String taskId, GetTaskCallback callback);

    void getAllTasks(LoadTasksCallback callback);

    void completeTask(String taskId);

    void activateTask(String taskId);

    void clearCompletedTasks();

    void refreshTasks();
}
