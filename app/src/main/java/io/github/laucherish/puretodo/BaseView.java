package io.github.laucherish.puretodo;

/**
 * @author laucherish
 * @date 16/4/14
 */
public interface BaseView<T> {

    String TAG = "BaseView";

    void setPresenter(T presenter );

}
