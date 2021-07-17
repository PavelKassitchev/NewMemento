package by.pavka.memento.calculator;

public interface Questionnaire {
    int getLength();
    boolean next();
    String getQuestion();
    double getPositive();
    double getNegative();
    void setCursor(int i);
}
