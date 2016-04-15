package io.github.laucherish.puretodo.tasks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.data.Task;

/**
 * @author laucherish
 * @date 16/4/15
 */
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<Task> mTasks;

    private TaskItemListener mListener;

    public TasksAdapter(List<Task> tasks, TaskItemListener listener) {
        setList(tasks);
        mListener = listener;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        if (mTasks == null) {
            return;
        }
        final Task task = mTasks.get(position);

        holder.mCbComplete.setChecked(task.isCompleted());
        holder.mCbComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListener.onCompleteTaskClick(task);
                } else {
                    mListener.onActivateTaskClick(task);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    public void setList(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCbComplete;

        TextView mTvTitle;

        TasksViewHolder(View itemView) {
            super(itemView);
            mCbComplete = (CheckBox) itemView.findViewById(R.id.cb_complete);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public interface TaskItemListener{

        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }
}
