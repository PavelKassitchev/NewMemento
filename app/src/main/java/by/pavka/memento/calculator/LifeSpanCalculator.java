package by.pavka.memento.calculator;

import java.util.Calendar;
import java.util.Locale;

public interface LifeSpanCalculator {
    Calendar tuneLifeDaySpan(int gender, Calendar birthDate, double weight, double height, Locale locale, PreCalculator preCalculator,
                             Questionnaire questionnaire, int[] answers, int obtainedHabits);
}
