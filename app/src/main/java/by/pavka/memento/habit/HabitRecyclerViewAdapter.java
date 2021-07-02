package by.pavka.memento.habit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import by.pavka.memento.R;
import by.pavka.memento.util.CalendarConverter;
import by.pavka.memento.util.Displayer;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.HabitViewHolder> {
    List<Habit> habits;
    List<HabitProgress> progress;
    private LayoutInflater inflater;
    private Context context;

    public HabitRecyclerViewAdapter(Context context, UserHabitTracker tracker) {
        Log.d("Tracker", " " + tracker);
        Log.d("Tracker1", " " + tracker.getHabits().keySet());
        Log.d("Tracker2", " " + tracker.getHabits());
        habits = new ArrayList<>(tracker.getHabits().keySet());
        progress = new ArrayList<>(tracker.getHabits().values());
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_item, parent, false);
        return new HabitViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitName.setText(habit.getName());
        HabitProgress prog = progress.get(position);
        HabitStatus status = prog.getHabitStatus();
        holder.imageView.setImageResource(habit.getImageId());
        switch (status) {
            case ENABLED:
                holder.period.setText("");
                holder.progressBar.setProgress(0);
                Log.d("STATUS", "ENABLED");
                break;
            case ACTIVE:
                String timePeriod = CalendarConverter.showDate(prog.getStartDate()) + "-" + CalendarConverter.showDate(prog.getEndDate());
                holder.period.setText(timePeriod);
                holder.progressBar.setProgress(CalendarConverter.showProgress(prog.getStartDate(), prog.getEndDate(), 8));
                Log.d("STATUS", "ACTIVE");
                break;
            case DISABLED:
                holder.period.setText("");
                holder.progressBar.setVisibility(View.GONE);
                Log.d("STATUS", "DISABLED");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class HabitViewHolder extends RecyclerView.ViewHolder {
        public int REQUEST_CODE = 1;
        private HabitRecyclerViewAdapter adapter;
        private ImageView imageView;
        private TextView habitName;
        private TextView period;
        private ProgressBar progressBar;

        public HabitViewHolder(@NonNull View itemView, HabitRecyclerViewAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            imageView = itemView.findViewById(R.id.card_image);
            habitName = itemView.findViewById(R.id.card_text);
            period = itemView.findViewById(R.id.period);
            progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HabitProgress habitProgress = progress.get(getLayoutPosition());
                    if (habitProgress.getHabitStatus() == HabitStatus.DISABLED) {
                        Displayer.showSnackbar(R.string.disabled, v);
                    } else {
                        Habit habit = habits.get(getLayoutPosition());
                        HabitActivity habitActivity = (HabitActivity) (adapter.context);
                        Intent intent = new Intent(habitActivity, ActivizationActivity.class);
                        intent.putExtra("habit", habit);
                        habitActivity.startActivityForResult(intent, REQUEST_CODE);

                    }
                }
            });
        }


    }

    public void setTracker(UserHabitTracker tracker) {
        habits = new ArrayList<>(tracker.getHabits().keySet());
        progress = new ArrayList<>(tracker.getHabits().values());
    }
}
