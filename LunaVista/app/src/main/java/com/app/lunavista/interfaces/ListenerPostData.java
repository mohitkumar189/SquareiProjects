package com.app.lunavista.interfaces;

/**
 * Created by ratnadeep on 8/20/15.
 */
public interface ListenerPostData {
    void onPostRequestSucess(int position, String response);
    void onPostRequestFailed(int position, String response);

}
