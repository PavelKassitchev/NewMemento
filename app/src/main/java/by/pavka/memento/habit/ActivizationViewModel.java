package by.pavka.memento.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import by.pavka.memento.MementoApplication;

public class ActivizationViewModel extends AndroidViewModel {
    private MementoApplication app;
    private int position;

    public ActivizationViewModel(@NonNull Application application) {
        super(application);
        app = (MementoApplication)application;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
