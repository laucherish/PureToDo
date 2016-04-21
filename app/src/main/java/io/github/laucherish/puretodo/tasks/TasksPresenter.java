package io.github.laucherish.puretodo.tasks;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.data.source.TasksDataSource;
import io.github.laucherish.puretodo.data.source.TasksRepository;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class TasksPresenter implements TasksContract.Presenter {

    private static final String TAG = "TasksPresenter";

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private TaskFilterType mCurrentFiltering = TaskFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(TasksRepository tasksRepository, TasksContract.View tasksView) {
        mTasksRepository = tasksRepository;
        mTasksView = tasksView;
        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public void loadTasks(boolean forceUpdate, final boolean showLoadingUi) {
        if (showLoadingUi) {
            mTasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }
        mTasksRepository.getAllTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                for (Task task : tasks) {
                    Log.d(TAG, "onTasksLoaded: " + task.toString());
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }

                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUi) {
                    mTasksView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }
                switch (mCurrentFiltering) {
                    case ACTIVE_TASKS:
                        mTasksView.showNoActiveTasks();
                        break;
                    case COMPLETED_TASKS:
                        mTasksView.showNoCompletedTasks();
                        break;
                    default:
                        mTasksView.showNoTasks();
                        break;
                }
            }
        });
    }

    private void processTasks(List<Task> tasksToShow) {
        if (tasksToShow.isEmpty()) {
            processEmptyTasks();
        } else {
            mTasksView.showTasks(tasksToShow);

            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mTasksView.showAllFilterLabel();
                break;
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mTasksView.showNoTasks();
                break;
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetail(Task requestedTask) {
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(Task completeTask) {
        mTasksRepository.completeTask(completeTask.getId());
        mTasksView.showTaskMarkedCompleted();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(Task activateTask) {
        mTasksRepository.activateTask(activateTask.getId());
        mTasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTask() {
        mTasksRepository.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    /**
     * 设置Task过滤器
     *
     * @param requestType
     */
    @Override
    public void setFiltering(TaskFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public TaskFilterType getFiltering() {
        return mCurrentFiltering;
    }
}
