package by.pavka.memento.habit;

public class HabitProgress {
    private HabitStatus habitStatus;

    public HabitProgress(HabitStatus habitStatus) {
        this.habitStatus = habitStatus;
    }

    public HabitProgress() {
        habitStatus = HabitStatus.ENABLED;
    }

    public HabitStatus getHabitStatus() {
        return habitStatus;
    }

    public void setHabitStatus(HabitStatus habitStatus) {
        this.habitStatus = habitStatus;
    }
}
