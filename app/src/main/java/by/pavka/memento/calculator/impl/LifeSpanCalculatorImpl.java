package by.pavka.memento.calculator.impl;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import by.pavka.memento.calculator.LifeSpanCalculator;
import by.pavka.memento.calculator.PreCalculator;
import by.pavka.memento.calculator.Questionnaire;

public class LifeSpanCalculatorImpl implements LifeSpanCalculator {

    private final Calendar now;

    public LifeSpanCalculatorImpl() {
        now = Calendar.getInstance();
    }

    @Override
    public Calendar tuneLifeDaySpan(int gender, Calendar birthDate, double weight, double height, Locale locale, PreCalculator preCalculator,
                                    Questionnaire questionnaire, int[] answers, int obtainedCustomizedHabits) {
        //TODO account weight and height
        birthDate.set(Calendar.DAY_OF_MONTH, 15);
        int totalDaysRaw = preCalculator.findLifeDaySpan(gender, birthDate, locale);
        Calendar end = (Calendar) birthDate.clone();
        end.add(Calendar.DAY_OF_MONTH, totalDaysRaw);
        int leftDays = (int) TimeUnit.MILLISECONDS.toDays(end.getTimeInMillis() - now.getTimeInMillis());
        double factor = 365.2 * leftDays / totalDaysRaw;
        int i = 0;
        do {
            switch (answers[i]) {
                case -1:
                    leftDays += (int) (factor * questionnaire.getNegative());
                    break;
                case 1:
                    leftDays += (int) (factor * questionnaire.getPositive());
                    break;
                default:
                    break;
            }
            i++;
        } while (questionnaire.next());
        Log.d("COR", "BEFORE = "+ leftDays);
        leftDays += (int)(factor * findBMICorrection(birthDate, weight, height));
        Log.d("COR", "AFTER = "+ leftDays);
        now.add(Calendar.DAY_OF_MONTH, leftDays);
        return now;
    }

    private int findBMICorrection(Calendar birthDate, double weight, double height) {
        Calendar now = Calendar.getInstance();
        double ageInYears = (TimeUnit.MILLISECONDS.toDays(now.getTimeInMillis() - birthDate.getTimeInMillis())) / 365.25;
        int correction = 0;
        if (ageInYears < 5) {
            return correction;
        }
        double BMI = weight * 10000 / (height * height);
        Log.d("COR", "BMI = " + BMI);
        if (ageInYears < 12) {
            BMI += 1;
        }
        if (ageInYears > 52) {
            BMI -= 1;
        }
        if (BMI < 16) {
            correction = (int)(-2 + (BMI - 16) * 0.5);
            Log.d("COR", "<16");
        } else if (BMI < 18) {
            correction = (int) (2 * (BMI - 18) / 2);
            Log.d("COR", "<18");
        } else if (BMI < 19) {
            correction = (int)(BMI - 18) * 2;
            Log.d("COR", "<19");
        } else if (BMI < 25) {
            correction = 2;
            Log.d("COR", "<25");
        } else if (BMI < 40) {
            correction = (int)(2 - (BMI - 25) * 0.27);
            Log.d("COR", "<40");
        } else {
            correction = - 2;
            Log.d("COR", ">40");
        }
        Log.d("COR", "correction = " + correction);
        return correction;
    }
}
