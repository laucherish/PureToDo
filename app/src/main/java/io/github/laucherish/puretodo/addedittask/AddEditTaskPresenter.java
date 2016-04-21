package io.github.laucherish.puretodo.addedittask;

import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.data.source.TasksDataSource;
import io.github.laucherish.puretodo.data.source.TasksRepository;

/**
 * @author laucherish
 * @date 16/4/15
 */
public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private TasksRepository mTasksRepository;

    private AddEditTaskContract.View mAddTaskView;

    private String mTaskId;

    public AddEditTaskPresenter(TasksRepository tasksRepository, AddEditTaskContract.View addTaskView, String taskId) {
        mTasksRepository = tasksRepository;
        mAddTaskView = addTaskView;
        mTaskId = taskId;

        mAddTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mTaskId != null) {
            populateTask();
        }
    }

    @Override
    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksRepository.saveTask(newTask);
            mAddTaskView.showTasksList();
        }
    }

    @Override
    public void updateTask(Task task) {
        mTasksRepository.updateTask(task);
        mAddTaskView.showTasksList();
    }

    @Override
    public void populateTask() {
        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mAddTaskView.isActive()) {
                    mAddTaskView.setTaskTitle(task.getTitle());
                    mAddTaskView.setTaskDescription(task.getDescription());
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mAddTaskView.isActive()) {
                    mAddTaskView.showEmptyTaskError();
                }
            }
        });
    }
}
