package io.github.laucherish.puretodo.tasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.data.source.TasksRepository;
import io.github.laucherish.puretodo.data.source.local.TasksLocalDataSource;
import io.github.laucherish.puretodo.util.ActivityUtils;

public class TasksActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private static final String PREFS_NAME = "Prefs";

    private TasksPresenter mTasksPresenter;

    public ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // 设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        TasksFragment tasksFragment = (TasksFragment) getFragmentManager().findFragmentById(R.id.fl_content);
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getFragmentManager(), tasksFragment, R.id.fl_content);
        }

        mTasksPresenter = new TasksPresenter(TasksRepository.getInstance(TasksLocalDataSource.getInstance(getApplicationContext())), tasksFragment);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int filterInt = settings.getInt(CURRENT_FILTERING_KEY, 0);
        if (mTasksPresenter != null) {
            mTasksPresenter.setFilterInt(filterInt);
        }

        mActionBar.setTitle(getTitle(filterInt));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        int filter = 0;
        if (mTasksPresenter != null) {
            filter = mTasksPresenter.getFilterInt();
        }
        editor.putInt(CURRENT_FILTERING_KEY, filter);

        editor.commit();
    }

    private int getTitle(int filter) {
        switch (filter) {
            case 1:
                return R.string.label_active;
            case 2:
                return R.string.label_completed;
            default:
                return R.string.label_all;
        }
    }
}
