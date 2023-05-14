package com.example.solobeast.Extras;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;

import com.example.solobeast.R;

/**
 A custom dialog for picking a name or number from a list of items.
 @author Ofek Almog
 */
public class PickerDialog extends Dialog {
    private static final String[] DIFFICULTIES = {"EASY", "MEDIUM", "HARD"};
    private final String action;
    private onPickerSelectedListener mListener;
    private NumberPicker mPicker;

    /**
     Constructor for creating a new instance of the PickerDialog.
     @param context The Context in which the dialog should be created.
     @param action The action to perform when an item is selected. Can be "diff" for selecting difficulty level or "number" for selecting a number.
     */
    public PickerDialog(Context context, String action) {
        super(context);
        this.action = action;
        init();
    }

    /**
     Initializes the dialog by setting its layout and listeners based on the selected action.
     */
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
     Set a listener to be notified when an item is selected.
     @param listener The listener to set.
     */
    public void setOnNameSelectedListener(onPickerSelectedListener listener) {
        mListener = listener;
    }

    /**
     Sets a listener to be invoked when the dialog is canceled.
     @param listener The {@link OnCancelListener} to set, or null to remove the currently set listener.
     */
    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }


}