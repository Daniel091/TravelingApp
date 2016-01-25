package prak.travelerapp;
import android.util.Log;

public class Logger {
    private static Logger logger = null;
    private boolean do_logging = true;

    public static Logger getInstance(){
        if (logger == null){
            logger = new Logger();
        }
        return logger;
    }

    // Logs only when do_logging is true
    public void log(String tag, String msg){
        if (do_logging){
            Log.d(tag,msg);
        }
    }
}
