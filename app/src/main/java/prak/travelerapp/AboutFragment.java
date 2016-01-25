package prak.travelerapp;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * About the developers and credits
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ImageButton hamburger_button = (ImageButton) view.findViewById(R.id.button_hamburger);
        hamburger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        TextView linkMail = (TextView) view.findViewById(R.id.textView4link);
        TextView linkWeather = (TextView) view.findViewById(R.id.textView7link);
        TextView link500px = (TextView) view.findViewById(R.id.textView9link);
        linkMail.setClickable(true);
        linkMail.setMovementMethod(LinkMovementMethod.getInstance());
        linkWeather.setClickable(true);
        linkWeather.setMovementMethod(LinkMovementMethod.getInstance());
        link500px.setClickable(true);
        link500px.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

    public void onStart() {
        super.onStart();
    }
}
