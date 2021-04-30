package by.pavka.memento.habit;

public class Habit {
    private String name;
    private int imageId;

    private int question;
    private int better;

    public Habit(String name) {
        this.name = name;
    }

    public Habit(String name, int imageId, int question, int better) {
        this.name = name;
        this.imageId = imageId;
        this.question = question;
        this.better = better;
    }

    public Habit(String name, int imageId) {
        this.name = name;
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
}
