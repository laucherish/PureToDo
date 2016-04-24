package io.github.laucherish.puretodo.tasks;

import java.util.List;

import io.github.laucherish.puretodo.BasePresenter;
import io.github.laucherish.puretodo.BaseView;
import io.github.laucherish.puretodo.data.Task;

/**
 * @author laucherish
 * @date 16/4/14
 */
public interface TasksContract {

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetail(Task requestedTask);

        void deleteTask(Task deleteTask);

        void completeTask(Task completeTask);

        void activateTask(Task activateTask);

        void clearCompletedTask();

        void setFiltering(TaskFilterType requestType);

        TaskFilterType getFiltering();

        int getFilterInt();

        void setFilterInt(int filterInt);
    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskToDelete(Task task);

        void showTaskMarkedCompleted();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showNoCompletedTasks();

        void showNoActiveTasks();

        void showFilteringPopUpMenu();

        void showAllFilterLabel();

        void showCompletedFilterLabel();

        void showActiveFilterLabel();

        void showSuccessfullySavedMessage();

        boolean isActive();
    }
}
