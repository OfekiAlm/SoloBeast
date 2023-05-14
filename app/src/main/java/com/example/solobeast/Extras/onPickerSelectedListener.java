package com.example.solobeast.Extras;

/**
 This interface defines a callback to be invoked when a name or a number is selected from a picker.
 @author Ofek Almog
 */
public interface onPickerSelectedListener {

    /**
     Called when a name is selected from the picker.
     @param name The name selected from the picker.
     */
    void onNameSelected(String name);

    /**
     Called when a number is selected from the picker.
     @param num The number selected from the picker.
     */
    void onNumberSelected(int num);
}