package by.pavka.memento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;

import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.user.User;

public class QuestionnaireViewModel extends AndroidViewModel {

    private Questionnaire questionnaire;
    private final int length;
    private final User user;
    private int[] answers;
    private int page;
    private MementoApplication app;

    public QuestionnaireViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication)application;
        questionnaire = ((MementoApplication)application).getQuestionnaire();
        length = questionnaire.getLength();
        user = ((MementoApplication)application).getUser();
        answers = Arrays.copyOf(user.getAnswers(), length);
    }

    public String getText() {
        questionnaire.setCursor(page);
        return questionnaire.getQuestion();
    }

    public int getReply() {
        return answers[page];
    }

    public void setReply(int i) {
        answers[page] = i;
    }

    public void next() {
        page++;
    }

    public void prev() {
        page--;
    }

    public int getLength() {
        return length;
    }

    public int getPage() {
        return page;
    }

    public int[] getAnswers() {
        return answers;
    }

}
