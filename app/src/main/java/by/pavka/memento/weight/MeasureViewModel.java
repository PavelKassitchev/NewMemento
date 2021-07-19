package by.pavka.memento.weight;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;

import by.pavka.memento.MementoApplication;
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

    public void updateChronicler() {
        if (app.getUser().getChronicler().addRecord(measureDate, weight)) {
            app.getUser().setWeight(weight);
        }
        app.saveChronicler();
    }

    public void removeRecord(Calendar calendar) {
        weight = app.getUser().getChronicler().removeRecord(calendar);
        app.getUser().setWeight(weight);
        app.saveChronicler();
    }
}
