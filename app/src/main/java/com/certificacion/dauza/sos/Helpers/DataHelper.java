package com.certificacion.dauza.sos.Helpers;

import android.text.TextUtils;
import android.widget.EditText;

import com.certificacion.dauza.sos.Models.Validation;
import com.certificacion.dauza.sos.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zarpick on 18/12/17.
 * 2017 SOS. All rights reserverd.
 */

public class DataHelper {

    //region Validations

    public final static Validation isEmail(EditText component) {

        Validation emptyValidation = isNotEmpty("Correo electrónico", component);

        if (emptyValidation.valid) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(component.getText().toString()).matches()) {
                return new Validation(true, "");
            }
            else {
                String message = "Correo electrónico invalido.";
                if (component != null) {
                    component.requestFocus();
                    component.setError(message);
                }
                return new Validation(false, message);
            }
        }
        else {
            return emptyValidation;
        }
    }

    public final static Validation isNotEmpty(String componentName, EditText component) {
        if (!TextUtils.isEmpty(component.getText().toString())) {
            return new Validation(true, "");
        }
        else {
            String message = componentName + " no puede estar vacío.";
            component.requestFocus();
            component.setError(message);
            return new Validation(false, message);
        }
        //TODO preguntar andres como acceder a recursos string desde helper
    }

    public final static Validation onlyHasLetters(String componentName, EditText component) {

        String text = component.getText().toString();

        Validation emptyValidation = isNotEmpty(componentName, component);

        if (emptyValidation.valid) {
            Pattern pattern = Pattern.compile("^[\\p{L} .'-]+$");
            Matcher matcher = pattern.matcher(text);
            if(matcher.matches()) {
                return new Validation(true, "");
            }
            else  {
                String message = componentName + " invalido.";
                component.requestFocus();
                component.setError(message);
                return new Validation(false, message);
            }
        }
        else {
            return emptyValidation;
        }
    }

    public final static Validation textMatches(String componentName1, EditText component1, String componentName2, EditText component2) {

        String text1 = component1.getText().toString();
        String text2 = component2.getText().toString();

        Validation emptyValidation1 = isNotEmpty(componentName1, component1);
        Validation emptyValidation2 = isNotEmpty(componentName2, component2);

        if (!emptyValidation1.valid) {
            return emptyValidation1;
        }
        else if (!emptyValidation2.valid) {
            return emptyValidation2;
        }
        else {
            if (text1.equals(text2)) {
                return new Validation(true, "");
            }
            else {
                String message = componentName1 + " y " + componentName2 + " no coinciden.";
                component1.requestFocus();
                component1.setText("");
                component1.setError(message);
                component2.setText("");
                component2.setError(message);
                return new Validation(false, message);
            }
        }
    }

    public final static Validation passwordMinLength (EditText component) {
        if (component.getText().toString().length() > 6) {
            return new Validation(true, "");
        }
        else {
            String message = "La contraseña debe tener al menos 7 caractéres.";
            component.requestFocus();
            component.setError(message);
            return new Validation(false, message);
        }
    }

    //endregion

}
