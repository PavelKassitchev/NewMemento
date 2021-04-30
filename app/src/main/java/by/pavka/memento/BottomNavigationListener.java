package by.pavka.memento;

import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.habit.HabitActivity;

public class BottomNavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppCompatActivity activity;

    public BottomNavigationListener(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_map:
                intent = new Intent(activity, MainActivity.class);
                break;
            case R.id.action_dial:
                intent = new Intent(activity, HabitActivity.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
        return true;
    }
}
