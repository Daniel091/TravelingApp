package prak.travelerapp.PictureAPI;

import android.graphics.Bitmap;


public interface AsyncPictureResponse {
    void getURLProcessFinish(String url);
    void getURLProcessFailed();
    void getImageFromURLProcessFinish(Bitmap image);
    void getImageFromURLProcessFailed();
    void getAuthorProcessFinish(String author);
    void getAuthorProcessFailed();
}
