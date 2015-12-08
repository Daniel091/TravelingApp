package prak.travelerapp.PictureAPI;

import android.graphics.Bitmap;


/**
 * Created by Michael on 24.11.15.
 */
public interface AsyncPictureResponse {
    void getURLProcessFinish(String url);
    void getURLProcessFailed();
    void getImageFromURLProcessFinish(Bitmap image);
    void getImageFromURLProcessFailed();
}
