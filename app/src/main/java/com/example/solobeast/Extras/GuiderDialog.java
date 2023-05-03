package com.example.solobeast.Extras;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.example.solobeast.R;

public class GuiderDialog extends Dialog {

    private Context mContext;
    private String mActivityName;
    private String mExplanation;
    private  ActivityGuideTracker activityTracker;

    public GuiderDialog(Context context, String activityName, String explanation) {
        super(context);
        mContext = context;
        mActivityName = activityName;
        mExplanation = explanation;
    }

    private void init() {
        show();
        setContentView(R.layout.custom_picker_dialog);
        //TextView explanationTv = findViewById(R.id.text_explanation);
        //explanationTv.setText(mExplanation);
        //Button okBtn = findViewById(R.id.button_ok_guide);

        //okBtn.setOnClickListener(view ->
         //       proceedOk(view));
    }

    public void goshow() {
        activityTracker = new ActivityGuideTracker(mContext);
        activityTracker.setUnvisited(mActivityName);
        if (!activityTracker.isVisited(mActivityName)) {
            init();

        }
    }
    public void proceedOk(View view){
        activityTracker.clearActivitesStatus();
        dismiss();

    }

}