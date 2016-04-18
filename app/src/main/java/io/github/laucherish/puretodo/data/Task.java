package io.github.laucherish.puretodo.data;

import android.text.TextUtils;

import java.util.UUID;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class Task {

    private String mId;

    private String mTitle;

    private String mDescription;

    private boolean mCompleted;

    public Task(String title, String description) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    public Task(String id, String title, String description, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty(){
        return (TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mDescription));
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public String getTitleForList(){
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCompleted=" + mCompleted +
                '}';
    }
}
