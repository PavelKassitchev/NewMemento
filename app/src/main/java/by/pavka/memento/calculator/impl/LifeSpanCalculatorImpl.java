package by.pavka.memento.calculator.impl;

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
    public Calendar tuneLifeDaySpan(int gender, Calendar birthDate, int weight, int height, Locale locale, PreCalculator preCalculator, Questionnaire questionnaire, int[] answers) {
        //TODO account weight and height
        birthDate.set(Calendar.DAY_OF_MONTH, 15);
        int totalDaysRaw = preCalculator.findLifeDaySpan(gender, birthDate, locale);
        Calendar end = (Calendar) birthDate.clone();
        end.add(Calendar.DAY_OF_MONTH, totalDaysRaw);
        System.out.println("END: " + end.get(Calendar.YEAR));
        int leftDays = (int) TimeUnit.MILLISECONDS.toDays(end.getTimeInMillis() - now.getTimeInMillis());
        double factor = 365.2 * leftDays / totalDaysRaw;
        int i = 0;
        do {
            System.out.println("I = " + i);
            switch (answers[i]) {
                case -1:
                    leftDays += (int) (factor * questionnaire.getNegative());
                    System.out.println("ANSWER negative" + i + leftDays);
                    break;
                case 1:
                    leftDays += (int) (factor * questionnaire.getPositive());
                    System.out.println("ANSWER positive " + i + leftDays);
                    break;
                default:
                    System.out.println("ANSWER neutral " + i + leftDays);
                    break;
            }
            i++;
        } while (questionnaire.next());
        now.add(Calendar.DAY_OF_MONTH, leftDays);
        return now;
    }
}
