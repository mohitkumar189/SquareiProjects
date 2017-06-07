package com.app.justclap.interfaces;

/**
 * Created by ratnadeep on 8/20/15.
 */
public interface QuestionDatailInterface {
    Object getPageDataModel(int position);

    void setPageDataModel(int position, Object obj);

    void showNext(Boolean bool);


}
