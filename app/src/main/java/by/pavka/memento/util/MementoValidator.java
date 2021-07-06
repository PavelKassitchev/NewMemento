package by.pavka.memento.util;

import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import by.pavka.memento.calculator.PreCalculator;

public class MementoValidator {
    public static final int MIN_WEIGHT = 3;
    public static final int MIN_HEIGHT = 30;
    public static final int MAX_WEIGHT = 150;
    public static final int MAX_HEIGHT = 200;


    public static boolean validateWeight(TextView tv) {
        String sw = tv.getText().toString();
        if (!sw.isEmpty()) {
            double weight = 0;
            try {
                weight = Double.parseDouble(sw);
            } catch (NumberFormatException e) {
                return false;
            }
            if (weight > MIN_WEIGHT && weight < MAX_WEIGHT) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateHeight(TextView tv) {
        String sh = tv.getText().toString();
        if (!sh.isEmpty()) {
            double height = 0;
            try {
                height = Double.parseDouble(sh);
            } catch (NumberFormatException e) {
                return false;
            }
            if (height > MIN_HEIGHT && height < MAX_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateGender(RadioGroup rg) {
        if (rg.getCheckedRadioButtonId () == -1) {
            return false;
        }
        return true;
    }

    public static boolean validateDate(TextView yearOfBirth, Spinner month) {
        String birthYear = yearOfBirth.getText().toString();
        if (birthYear.length() < 4) {
            return false;
        }
        int year = Integer.parseInt(birthYear);
        int birthMonth = month.getSelectedItemPosition();
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        if (year > nowYear || (year == nowYear && birthMonth > nowMonth) || (nowYear - year > PreCalculator.MAX_AGE)) {
            return false;
        }
        return true;
    }
}
