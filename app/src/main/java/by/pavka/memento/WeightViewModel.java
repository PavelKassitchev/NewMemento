package by.pavka.memento;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;

import by.pavka.memento.user.User;

public class WeightViewModel extends AndroidViewModel {
    private MementoApplication app;
    private double weight;
    private double height;
    private double bmi;

    public WeightViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication) application;
        weight = app.getUser().getWeight();
        height = app.getUser().getHeight();
        if (weight != 0 && height != 0) {
            bmi = weight * 10000 / height / height;
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public void recalculateBMI(double w, double h) {
        weight = w;
        height = h;
        bmi = weight * 10000 / height / height;
    }

    public void resetBMIData(double w, double h) {
        User user = app.getUser();
        user.setWeight(w);
        user.setHeight(h);
        app.saveBMIData(w, h);
        user.getChronicler().addRecord(Calendar.getInstance(), w);
        app.saveChronicler();
    }
}
