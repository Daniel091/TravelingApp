package prak.travelerapp.Autocompleter;

import android.widget.AutoCompleteTextView;

/**
 * Created by Michael on 05.12.15.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CityAutoCompleteView extends AutoCompleteTextView {


        public CityAutoCompleteView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public CityAutoCompleteView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        public CityAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }

        // this is how to disable AutoCompleteTextView filter
        @Override
        protected void performFiltering(final CharSequence text, final int keyCode) {
            String filterText = "";
            super.performFiltering(filterText, keyCode);
        }

        /*
         * after a selection we have to capture the new value and append to the existing text
         */
        @Override
        protected void replaceText(final CharSequence text) {
            super.replaceText(text);
        }


}
