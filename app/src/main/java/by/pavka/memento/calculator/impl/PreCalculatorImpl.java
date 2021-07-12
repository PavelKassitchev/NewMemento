package by.pavka.memento.calculator.impl;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import by.pavka.memento.calculator.PreCalculator;

public class PreCalculatorImpl implements PreCalculator {
    private static final double AVG_MALE = 69.0;
    private static final double AVG_FEMALE = 78.0;

    private final Calendar now;

    public PreCalculatorImpl() {
        now = Calendar.getInstance();
    }

    @Override
    public int findLifeDaySpan(int gender, Calendar birthDate, Locale locale) {
        double ageInYears = (TimeUnit.MILLISECONDS.toDays(now.getTimeInMillis() - birthDate.getTimeInMillis())) / 365.25;
        switch (gender) {
            case -1:
                return (int) femaleLifeDaySpan(ageInYears, locale);
            case 1:
                return (int) maleLifeDaySpan(ageInYears, locale);
            default:
                return (int) ((femaleLifeDaySpan(ageInYears, locale) + maleLifeDaySpan(ageInYears, locale)) / 2);
        }
    }

    private long maleLifeDaySpan(double ageInYears, Locale locale) {
//        return Math.round(365 * (0.00007 * Math.pow(ageInYears, 3) - 0.0037 * Math.pow(ageInYears, 2) + 0.1131 * ageInYears + AVG_MALE));
        double resultYear = 0.0046 * ageInYears * ageInYears - 0.1552 * ageInYears + 70.87;
        if (ageInYears < 4) {
            resultYear -= 1;
        }
        return Math.round(365 * resultYear);
    }

    private long femaleLifeDaySpan(double ageInYears, Locale locale) {
        return Math.round(365 * (0.0000624 * Math.pow(ageInYears, 3) - 0.00532 * Math.pow(ageInYears, 2) + 0.157 * ageInYears + AVG_FEMALE));
    }
}
