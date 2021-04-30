package by.pavka.memento.calculator;

import java.util.Calendar;
import java.util.Locale;

public interface PreCalculator {
    int MAX_AGE = 75;
    int findLifeDaySpan(int gender, Calendar birthDate, Locale locale);
}
