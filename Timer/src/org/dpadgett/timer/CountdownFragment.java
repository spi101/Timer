package org.dpadgett.timer;

import android.R.attr;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CountdownFragment extends Fragment {

	private boolean inputMode;
	private LinearLayout inputLayout;
	private LinearLayout timerLayout;
	private View rootView;
	private final Handler handler;
	
	public CountdownFragment() {
		this.inputMode = true;
		this.handler = new Handler();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.countdown, container, false);
        LinearLayout inputs = (LinearLayout) rootView.findViewById(R.id.inputsLayout);
        this.inputLayout = (LinearLayout) rootView.findViewById(R.id.inputsInnerLayout);
        EditText countdownHours = (EditText) rootView.findViewById(R.id.countdownHours);
		countdownHours.addTextChangedListener(new IntLimiter(24));
        this.timerLayout = createTimerLayout(inputs);
        Button startButton = (Button) rootView.findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.post(new ToggleInputMode());
			}
        });
        return rootView;
    }

    private class ToggleInputMode implements Runnable {

		@Override
		public void run() {
			inputMode = !inputMode;
			LinearLayout inputs = (LinearLayout) rootView.findViewById(R.id.inputsLayout);
			Button startButton = (Button) rootView.findViewById(R.id.startButton);
			if (inputMode) {
				inputs.removeAllViews();
				inputs.addView(inputLayout);
				startButton.setText("Start");
			} else {
				inputs.removeAllViews();
				inputs.addView(timerLayout);
				startButton.setText("Stop");
			}
		}
    	
    }
    
    private static class IntLimiter implements TextWatcher {
    	private final int limit;
    	private String oldNumber;
    	
    	private IntLimiter(int limit) {
    		this.limit = limit;
    	}
    	
		@Override
		public void afterTextChanged(Editable arg0) {
			if (arg0.length() > 0) {
				int newNumber = Integer.parseInt(arg0.toString());
				if (newNumber > limit) {
					arg0.replace(0, arg0.length(), oldNumber);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			oldNumber = arg0.toString();
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
    	
    }

    private static LinearLayout createTimerLayout(LinearLayout inputs) {
		LinearLayout runningLayout = new LinearLayout(inputs.getContext());
		runningLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0f));
		runningLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		runningLayout.setId(R.id.inputsInnerLayout);
		
		TextView timerText = new TextView(inputs.getContext());
		timerText.setText("00:00:00");
		timerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);
		timerText.setTextAppearance(inputs.getContext(), attr.textAppearanceLarge);
		
		runningLayout.addView(timerText);
		return runningLayout;
    }
}
