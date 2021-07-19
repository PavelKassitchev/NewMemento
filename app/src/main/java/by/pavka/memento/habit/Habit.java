package by.pavka.memento.habit;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Objects;

import by.pavka.memento.MementoApplication;

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
        if (question > -1) {
            this.description = name;
        } else {
            description = "";
        }
        this.imageId = imageId;
        this.question = question;
        this.better = better;
    }

    public Habit(String name, int imageId) {
        this.name = name;
        this.description = name;
        this.imageId = imageId;
        this.question = -1;
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

    public int getQuestion() {
        return question;
    }

    public int getBetter() {
        return better;
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

    public void updateName(boolean clean) {
        if (question < 0) {
            if (clean) {
                name = MementoApplication.getNewHabit();

            } else {
                name = description;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return imageId == habit.imageId &&
                question == habit.question &&
                better == habit.better;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, question, better);
    }

    @NonNull
    @Override
    public String toString() {
        return "{\"better\":" + better + ",\"imageId\":" + imageId + ",\"name\":\"" + name + "\",\"question\":" + question + " + }" + " descr= " + description;
    }
}
