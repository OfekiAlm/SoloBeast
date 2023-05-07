package com.example.solobeast.Extras;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        setContentView(R.layout.custom_guider_dialog);
        TextView explanationTv = findViewById(R.id.explanation_text);
        explanationTv.setText(mExplanation);
        Button okBtn = findViewById(R.id.proceed_guide_btn);

        okBtn.setOnClickListener(view ->
                proceedOk());
    }
    public void startDialog() {
        activityTracker = new ActivityGuideTracker(mContext);
        if (!activityTracker.isVisited(mActivityName)) {
            init();
            activityTracker.setVisited(mActivityName);
        }
    }
    public void proceedOk(){
        dismiss();
    }
}