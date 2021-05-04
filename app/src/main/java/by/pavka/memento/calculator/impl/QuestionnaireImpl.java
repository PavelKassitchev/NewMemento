package by.pavka.memento.calculator.impl;

import android.content.Context;

import by.pavka.memento.R;
import by.pavka.memento.calculator.Questionnaire;

public class QuestionnaireImpl implements Questionnaire {
    private final String[] questions;
    private final double[] positives;
    private final double[] negatives;
    private final int size;
    private int cursor;

    public QuestionnaireImpl(Context context) {
        questions = context.getResources().getStringArray(R.array.questions);
        size = questions.length;
        String[] positiveStrings = context.getResources().getStringArray(R.array.positives);
        positives = new double[size];
        for (int i = 0; i < size; i++) {
            positives[i] = Double.parseDouble(positiveStrings[i]);
        }
        String[] negativeStrings = context.getResources().getStringArray(R.array.negatives);
        negatives = new double[size];
        for (int i = 0; i < size; i++) {
            negatives[i] = Double.parseDouble(negativeStrings[i]);
        }
    }

    @Override
    public int getLength() {
        return size;
    }

    @Override
    public boolean next() {
        System.out.println("Cursor = " + cursor + " size = " + size);
        if (cursor < size - 1) {
            cursor++;
            return true;
        }
        return false;
    }

    @Override
    public String getQuestion() {
        return questions[cursor];
    }

    @Override
    public double getPositive() {
        return positives[cursor];
    }

    @Override
    public double getNegative() {
        return negatives[cursor];
    }

    @Override
    public void setCursor(int i) {
        cursor = i;
    }

    @Override
    public int getCursor() {
        return cursor;
    }
}
