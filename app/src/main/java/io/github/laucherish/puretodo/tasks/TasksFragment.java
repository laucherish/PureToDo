package io.github.laucherish.puretodo.tasks;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.addedittask.AddEditTaskActivity;
import io.github.laucherish.puretodo.addedittask.AddEditTaskFragment;
import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.other.AboutActivity;
import io.github.laucherish.puretodo.util.DividerItemDecoration;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;

    private TasksAdapter mTasksAdapter;

    private RecyclerView mRcvTasks;

    private View mViewNoTasks;

    private ImageView mIvNoTasks;

    private TextView mTvNoTasks;

    private TextView mTvNoTasksAdd;

    private LinearLayout mLlTasksView;

    private TextView mTvFilteringLabel;

    private ActionBar mActionBar;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        mRcvTasks = (RecyclerView) root.findViewById(R.id.rcv_tasks);
        mRcvTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvTasks.setHasFixedSize(true);
        mRcvTasks.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        mRcvTasks.setAdapter(mTasksAdapter);

        mTvFilteringLabel = (TextView) root.findViewById(R.id.tv_filtering_label);
        mLlTasksView = (LinearLayout) root.findViewById(R.id.ll_tasks);

        mViewNoTasks = root.findViewById(R.id.ll_notasks);
        mIvNoTasks = (ImageView) root.findViewById(R.id.iv_notasks);
        mTvNoTasks = (TextView) root.findViewById(R.id.tv_notasks);
        mTvNoTasksAdd = (TextView) root.findViewById(R.id.tv_notasks_add);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tasks, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTask();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showTasks(List<Task> tasks) {
        Log.d(TAG, "showTasks: " + tasks.size());
        mTasksAdapter.replaceData(tasks);

        mLlTasksView.setVisibility(View.VISIBLE);
        mViewNoTasks.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID,taskId);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskToDelete(final Task task) {
        Snackbar.make(getView(),getString(R.string.task_to_delete),Snackbar.LENGTH_LONG).setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteTask(task);
            }
        }).show();
    }

    @Override
    public void showTaskMarkedCompleted() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        if (mActionBar != null) {
                            mActionBar.setTitle(R.string.label_active);
                        }
                        mPresenter.setFiltering(TaskFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        if (mActionBar != null) {
                            mActionBar.setTitle(R.string.label_completed);
                        }
                        mPresenter.setFiltering(TaskFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        if (mActionBar != null) {
                            mActionBar.setTitle(R.string.label_all);
                        }
                        mPresenter.setFiltering(TaskFilterType.ALL_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void showAllFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_all);
    }

    @Override
    public void showCompletedFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_completed);
    }

    @Override
    public void showActiveFilterLabel() {
        mTvFilteringLabel.setText(R.string.label_active);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    TasksAdapter.TaskItemListener mItemListener = new TasksAdapter.TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetail(clickedTask);
        }

        @Override
        public void onTaskLongClick(Task longClickedTask) {
            showTaskToDelete(longClickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activateTask(activatedTask);
        }
    };

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mLlTasksView.setVisibility(View.GONE);
        mViewNoTasks.setVisibility(View.VISIBLE);

        mTvNoTasks.setText(mainText);
        mIvNoTasks.setImageDrawable(getResources().getDrawable(iconRes));
        mTvNoTasksAdd.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }
}
