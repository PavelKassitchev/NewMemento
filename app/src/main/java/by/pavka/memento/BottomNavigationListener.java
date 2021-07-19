package by.pavka.memento;

import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.track.HabitActivity;
import by.pavka.memento.profile.MainActivity;
import by.pavka.memento.weight.MeasureActivity;

public class BottomNavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppCompatActivity activity;

    public BottomNavigationListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.profile:
                intent = new Intent(activity, MainActivity.class);
                break;
            case R.id.habit:
                intent = new Intent(activity, HabitActivity.class);
                break;
            case R.id.weights:
                intent = new Intent(activity, MeasureActivity.class);
                break;
            default:
        }
        if (intent == null) {
            return false;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
        return true;
    }
}
