package io.github.laucherish.puretodo.tasks;

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
    }
}
