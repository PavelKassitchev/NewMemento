package by.pavka.memento.habit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.MementoActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityHabbitBinding;

public class  HabitActivity extends MementoActivity {

    private MementoApplication application;
    private RecyclerView recycler;
    private HabitRecyclerViewAdapter adapter;
    private UserHabitTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHabbitBinding binding = ActivityHabbitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.habit);
        item.setChecked(true);
        tracker = application.getUser().getTracker();
        Log.d("TRACKER", "In activity tracker = " + tracker);
        recycler = findViewById(R.id.recyclerView);
        adapter = new HabitRecyclerViewAdapter(this, tracker);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration().orientation * 2));
        Intent intent = getIntent();
        Log.d("MYSTERY", "On Create Intent = " + intent);
        clearHabit(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("MYSTERY", "OnNewIntent");
        clearHabit(intent);
    }

    private void clearHabit(Intent intent) {
        Log.d("MYSTERY", "In ClearHabit Intent habit = " + intent.getSerializableExtra("habit"));
        if (intent != null && intent.getSerializableExtra("habit") != null) {
            Habit habit = (Habit)intent.getSerializableExtra("habit");
            Log.d("MYSTERY", "In HabitActivity habit id = " + habit.getId());
            tracker.clearHabitProgress(habit.getId());
            Log.d("MYSTERY", "In HabitActivity status = " + tracker.getHabitProgress(habit.getId()).getHabitStatus());
            adapter.setTracker(tracker);
            adapter.notifyItemChanged(adapter.habits.indexOf(habit));
            application.saveHabits();
            if (intent.getBooleanExtra("expire", false)) {
                Log.d("MYSTERY", "In Habit Activity exprire = true");
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TRACKER", "RESULT...");
        if (resultCode == RESULT_OK) {
            Log.d("TRACKER", "RESULT OK");
            Habit habit = (Habit)data.getSerializableExtra("habit");
            application.launchNotification(habit.getId(), true);
            application.saveHabits();
            adapter.setTracker(application.getUser().getTracker());
            for (Map.Entry<Habit, HabitProgress> entry : application.getUser().getTracker().getHabits().entrySet()) {
                Log.d("MYSTERY", entry.getKey().getName() + " - " + entry.getValue().getHabitStatus());
            }
//            adapter.notifyDataSetChanged();
            adapter.notifyItemChanged(adapter.habits.indexOf(habit));
        }
    }
}