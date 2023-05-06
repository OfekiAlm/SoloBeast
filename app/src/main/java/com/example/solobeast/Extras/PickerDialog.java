package com.example.solobeast.Extras;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;

import com.example.solobeast.R;

/**
 * A custom dialog for picking a name from a list of strings.
 */
public class PickerDialog extends Dialog {

    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD"};
    private final String action;
    private OnPickerSelectedListener mListener;
    private NumberPicker mPicker;

    public PickerDialog(Context context, String action) {
        super(context);
        this.action = action;
        init();
    }

    private void init() {
        setContentView(R.layout.custom_picker_dialog);
        Button okButton = findViewById(R.id.ok_btn);

        mPicker = findViewById(R.id.number_picker_1);
        if(action.equals("diff")){
            mPicker.setMinValue(0);
            mPicker.setMaxValue(DIFFICULTIES.length - 1);
            mPicker.setDisplayedValues(DIFFICULTIES);

            okButton.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onNameSelected(DIFFICULTIES[mPicker.getValue()]);
                }
                dismiss();
            });
        }
        else{
            mPicker.setMinValue(0);
            mPicker.setMaxValue(200);

            okButton.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onNumberSelected(mPicker.getValue());
                }
                dismiss();
            });
        }


    }

    /**
     * Set a listener to be notified when a name is selected.
     *
     * @param listener The listener to set.
     */
    public void setOnNameSelectedListener(OnPickerSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    /**
     * Interface definition for a callback to be invoked when a name is selected.
     */
    public interface OnPickerSelectedListener {
        void onNameSelected(String name);
        void onNumberSelected(int num);
    }
}