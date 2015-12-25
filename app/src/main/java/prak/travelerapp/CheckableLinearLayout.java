package prak.travelerapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable{
    private boolean isChecked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        changeColor(isChecked);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        this.isChecked = !this.isChecked;
        changeColor(isChecked);
    }

    private void changeColor(boolean isChecked){
        if(isChecked){
            setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }
}
