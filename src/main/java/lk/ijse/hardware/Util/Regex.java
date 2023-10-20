package lk.ijse.hardware.Util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFieldValid(TextFilds textField, String text){
        String field="";

        switch (textField){
            case ID:
                field="^([A-Z0-9])$";
                break;
            case LANKA_ID:
                field="^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
                break;
            case NAME:
                field="^[A-z|\\s]{3,}$";
                break;
            case EMAIL:
                field="^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case ADDRESS:
                field="^([A-z0-9]|[-/,.@+]|\\s){4,}$";
                break;
            case PHONE:
                field="^([+]94{1,3}|[0])([1-9]{2})([0-9]){7}$";
                break;
            case DOUBLE:
                field="^([0-9]){1,}[.]([0-9]){1,}$";
                break;
            case INVOICE:
                field="^([0-9]){1,}$";
                break;
            case NONE_CHARACTER:
                field="^[\\W]{1,}$";
                break;
        }

        Pattern pattern= Pattern.compile(field);
        if(text !=null){
            if(text.trim().isEmpty()){
                return false;
            }
        }else{
            return false;
        }
        Matcher matcher= pattern.matcher(text);

        if(matcher.matches()){
            return true;
        }
        return false;
    }
    public static boolean setTextColor(TextFilds location, JFXTextField fields) {

        if (Regex.isTextFieldValid(location, fields.getText())) {
            fields.setFocusColor(Paint.valueOf("Green"));
            fields.setUnFocusColor(Paint.valueOf("Green"));
            return true;
        } else {
            fields.setFocusColor(Paint.valueOf("Red"));
            fields.setUnFocusColor(Paint.valueOf("Red"));
            return false;
        }
    }
    public static boolean isValidItemCode(String staffId){
        Pattern pattern=Pattern.compile("I[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidEmpId(String staffId){
        Pattern pattern=Pattern.compile("E[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidCustId(String staffId){
        Pattern pattern=Pattern.compile("C[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidOrderId(String staffId){
        Pattern pattern=Pattern.compile("O[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidWhId(String staffId){
        Pattern pattern=Pattern.compile("W[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidSuplId(String staffId){
        Pattern pattern=Pattern.compile("S[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidDriverId(String staffId){
        Pattern pattern=Pattern.compile("D[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidDeliveryId(String staffId){
        Pattern pattern=Pattern.compile("DE[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidAtteId(String staffId){
        Pattern pattern=Pattern.compile("A[0-9]{3}$");
        Matcher matcher=pattern.matcher(staffId);
        return matcher.matches();
    }

    public static boolean isValidNic(String nic){
        Pattern pattern=Pattern.compile("^[0-9]{9}[vVxX]||[0-9]{12}$");
        Matcher matcher=pattern.matcher(nic);
        return matcher.matches();
    }
}
