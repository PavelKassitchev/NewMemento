package by.pavka.memento.habit;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Habit implements Serializable {
    private int id;
    private String name;
    private String description;
    private int imageId;
    private int question;
    private int better;

    public Habit(String name) {
        this.name = name;
    }

    public Habit(String name, int imageId, int question, int better) {
        this.name = name;
        this.description = name;
        this.imageId = imageId;
        this.question = question;
        this.better = better;
    }

    public Habit(String name, int imageId) {
        this.name = name;
        this.description = name;
        this.imageId = imageId;
        this.question = - 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getBetter() {
        return better;
    }

    public void setBetter(int better) {
        this.better = better;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return imageId == habit.imageId &&
                question == habit.question &&
                better == habit.better &&
                name.equals(habit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imageId, question, better);
    }

    @NonNull
    @Override
    public String toString() {
        return "{\"better\":" + better + ",\"imageId\":" + imageId + ",\"name\":\"" + name + "\",\"question\":" + question + "}";
    }
}
