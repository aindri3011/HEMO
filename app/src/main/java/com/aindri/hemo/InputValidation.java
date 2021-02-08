package com.aindri.hemo;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {
    private Context context;
    public InputValidation(Context context) {
        this.context = context;
    }
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message){
        String value=textInputEditText.getText().toString().trim();
        if(value.isEmpty()){
            textInputLayout.setError(message);
            hidekeyboardFrom(textInputEditText);
            return false;
        }
        else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout,String message){
        String value=textInputEditText.getText().toString().trim();
        if(value.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            textInputLayout.setError(message);
            hidekeyboardFrom(textInputEditText);
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;

    }
    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message){
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if(!value1.contentEquals(value2)){
            textInputLayout.setError(message);
            hidekeyboardFrom(textInputEditText2);
            return false;
        }
        else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /*
     * method to hide keyboard
     */

    private void hidekeyboardFrom(View view){
        InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    //password should be of length greater than 8
    public boolean isPasswordLengthSatisfied(TextInputEditText textInputEditText, TextInputLayout textInputLayout,String message){
        String value=textInputEditText.getText().toString().trim();
        if(value.length()<8){
            textInputLayout.setError(message);
            hidekeyboardFrom(textInputEditText);
            return false;
        }
        else {
            textInputLayout.setErrorEnabled(false);

        }
        return true;

    }
    //methods for checking empty Edit Text and Email Patterns
    public boolean isEditTextFilled(EditText editText, String message){
        String value = editText.getText().toString().trim();
        if(value.isEmpty()) {
            editText.setError(message);
            hidekeyboardFrom(editText);
            return false;
        }
        else {
            System.out.print("Email Id format is correct");
        }
        return true;
    }

    public boolean isEditTextEmail(EditText EditText,String message){
        String value = EditText.getText().toString().trim();
        if(value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            EditText.setError(message);
            hidekeyboardFrom(EditText);
            return false;
        }
        else {
            System.out.print("No error");
        }
        return true;
    }
}
