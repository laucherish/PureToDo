package io.github.laucherish.puretodo.addedittask;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.github.laucherish.puretodo.R;

/**
 * @author laucherish
 * @date 16/4/15
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View{

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    private EditText mEtTitle;

    private EditText mEtDescription;

    private String mEditedTaskId;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    public AddEditTaskFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addedittask, container, false);

        mEtTitle = (EditText) root.findViewById(R.id.et_task_title);
        mEtDescription = (EditText) root.findViewById(R.id.et_task_description);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTaskIdIfAny();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewTask()) {
                    mPresenter.createTask(mEtTitle.getText().toString(), mEtDescription.getText().toString());
                } else {
                    mPresenter.updateTask(mEtTitle.getText().toString(), mEtDescription.getText().toString());
                }
            }
        });
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mEtTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTaskTitle(String title) {
        mEtTitle.setText(title);
    }

    @Override
    public void setTaskDescription(String description) {
        mEtDescription.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setTaskIdIfAny() {
        if (getArguments() != null && getArguments().containsKey(ARGUMENT_EDIT_TASK_ID)) {
            mEditedTaskId = getArguments().getString(ARGUMENT_EDIT_TASK_ID);
        }
    }

    private boolean isNewTask() {
        return mEditedTaskId == null;
    }
}
