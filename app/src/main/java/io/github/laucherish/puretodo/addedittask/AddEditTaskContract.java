package io.github.laucherish.puretodo.addedittask;

import io.github.laucherish.puretodo.BasePresenter;
import io.github.laucherish.puretodo.BaseView;

/**
 * @author laucherish
 * @date 16/4/15
 */
public interface AddEditTaskContract {

    interface Presenter extends BasePresenter{

        void createTask(String title, String description);

        void updateTask(String title, String description);

        void populateTask();
    }

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTaskTitle(String title);

        void setTaskDescription(String description);

        boolean isActive();
    }
}
