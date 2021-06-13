package by.pavka.memento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;

import by.pavka.memento.habit.Habit;

public class MeasureViewModel extends AndroidViewModel {
    private MementoApplication app;
    private Calendar measureDate;
    private double weight;

    public MeasureViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication)application;
        if (app.getUser().getWeight() != 0) {
            weight = app.getUser().getWeight();
        }
    }

    public Calendar getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(Calendar measureDate) {
        this.measureDate = measureDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
