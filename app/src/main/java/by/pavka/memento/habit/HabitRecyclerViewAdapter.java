package by.pavka.memento.habit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.pavka.memento.R;
import by.pavka.memento.util.CalendarConverter;
import by.pavka.memento.util.Validator;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.HabitViewHolder> {
    private List<Habit> habits;
    List<HabitProgress> progress;
    private LayoutInflater inflater;
    private Context context;

    public HabitRecyclerViewAdapter(Context context, UserHabitTracker tracker) {
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
//        if (status != HabitStatus.ACTIVE) {
////            holder.status.setText(status.toString());
//            holder.start.setText("");
//            holder.end.setText("");
//            holder.progressBar.setProgress(0);
//        } else {
////            holder.status.setText(CalendarConverter.showDate(prog.getStartDate()) + " - "
////                    + CalendarConverter.showDate(prog.getEndDate()));
//            holder.start.setText(CalendarConverter.showDate(prog.getStartDate()));
//            holder.end.setText(CalendarConverter.showDate(prog.getEndDate()));
//            holder.progressBar.setProgress(CalendarConverter.showProgress(prog.getStartDate(), prog.getEndDate()));
//        }
        holder.imageView.setImageResource(habit.getImageId());
        switch (status) {
            case ENABLED:
//                holder.itemView.setBackgroundColor(0xFF03DAC5);
//                holder.start.setText("");
//                holder.end.setText("");
                holder.period.setText("");
                holder.progressBar.setProgress(0);
                break;
            case ACTIVE:
//                holder.itemView.setBackgroundColor(0xFFFF693B);
//                holder.start.setText(CalendarConverter.showDate(prog.getStartDate()));
//                holder.end.setText(CalendarConverter.showDate(prog.getEndDate()));
                String timePeriod = CalendarConverter.showDate(prog.getStartDate()) + " - " + CalendarConverter.showDate(prog.getEndDate());
                holder.period.setText(timePeriod);
                Drawable background = new ColorDrawable(Color.YELLOW);
                Drawable progress = new ScaleDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, 1, -1);
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {background, progress});
                layerDrawable.setId(0, android.R.id.background);
                layerDrawable.setId(1, android.R.id.progress);
                holder.progressBar.setProgressDrawable(layerDrawable);
                //holder.progressBar.getProgressDrawable().setColorFilter(0xFF00FF00, PorterDuff.Mode.SRC_IN);
                holder.progressBar.setProgress(CalendarConverter.showProgress(prog.getStartDate(), prog.getEndDate()));
                break;
            default:
//                holder.start.setText("");
//                holder.end.setText("");
//                holder.progressBar.setProgress(100);
                holder.period.setText("");
                holder.progressBar.setVisibility(View.GONE);
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
//        private TextView status;
//        private TextView start;
//        private TextView end;
        private TextView period;
        private ProgressBar progressBar;

        public HabitViewHolder(@NonNull View itemView, HabitRecyclerViewAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            imageView = itemView.findViewById(R.id.card_image);
            habitName = itemView.findViewById(R.id.card_text);
//            status = itemView.findViewById(R.id.card_status);
//            start = itemView.findViewById(R.id.start);
//            end = itemView.findViewById(R.id.end);
            period = itemView.findViewById(R.id.period);
            progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HabitProgress habitProgress = progress.get(getLayoutPosition());
                    if (habitProgress.getHabitStatus() == HabitStatus.DISABLED) {
                        Validator.showSnackbar(R.string.disabled, v);
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
