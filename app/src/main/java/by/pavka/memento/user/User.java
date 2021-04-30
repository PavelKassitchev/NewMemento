package by.pavka.memento.user;

import java.util.Calendar;

import by.pavka.memento.calculator.Questionnaire;
import by.pavka.memento.calculator.impl.QuestionnaireImpl;

public class User {
    private String name;
    private int gender;
    private Calendar birthDate;
    private int weight;
    private int height;
    private int[] answers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public int[] getAnswers() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }

    public int getReply(int i) {
        return answers[i];
    }
}
