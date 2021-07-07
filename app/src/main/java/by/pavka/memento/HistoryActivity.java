package by.pavka.memento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import by.pavka.memento.databinding.ActivityHistoryBinding;
import by.pavka.memento.util.CalendarConverter;

public class HistoryActivity extends MementoActivity implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener, OnDataPointTapListener {
    private Chronicler chronicler;
    private GraphView graphView;
    private DataPoint[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chronicler = ((MementoApplication) getApplication()).getUser().getChronicler();
        ActivityHistoryBinding binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        Spinner period = binding.period;
        period.setOnItemSelectedListener(this);
        graphView = binding.graph;

        data = chronicler.getDataSeries();
        if (data.length > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
            series.setDrawDataPoints(true);
            series.setOnDataPointTapListener(this);
            graphView.addSeries(series);
            DateFormat df = new SimpleDateFormat("d/M/yy", Locale.getDefault());
            graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, df));
            graphView.getGridLabelRenderer().setNumHorizontalLabels(4);
            graphView.getViewport().setMaxX(new Date().getTime() + 1000 * 3600 * 24);
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getGridLabelRenderer().setHumanRounding(false);
            graphView.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.weight));
        }

        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.weights);
        item.setChecked(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (data.length > 0) {
            int choice = parent.getSelectedItemPosition();
            int index = 0;
            Calendar start = Calendar.getInstance();
            switch (choice) {
                case 0:
                    start.setTimeInMillis((long)data[0].getX());
                    Log.d("CHRON", start.getTime().toString());
                    break;
                case 1:
                    start.add(Calendar.MONTH, -6);
                    index = chronicler.getTimeIndex(start);
                    break;
                case 2:
                    start.add(Calendar.MONTH, -3);
                    index = chronicler.getTimeIndex(start);
                    break;
                case 3:
                    start.add(Calendar.MONTH, -1);
                    index = chronicler.getTimeIndex(start);
                    break;
                case 4:
                    if (((MementoApplication) getApplication()).getUser().getBirthDate() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(getString(R.string.history_clearance)).setMessage(getString(R.string.history_confidence_short))
                                .setNegativeButton(getString(R.string.cancel), this)
                                .setPositiveButton(getString(R.string.yes), this);
                        builder.create().show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(getString(R.string.history_clearance)).setMessage(getString(R.string.main_clearance));
                        builder.create().show();
                    }
                    break;
                default:
                    break;
            }
//            if (data.length > index) {
//                graphView.getViewport().setMinX(data[index].getX());
                graphView.getViewport().setMinX(start.getTimeInMillis());
                graphView.getViewport().setMaxX(new Date().getTime() + 1000 * 3600 * 24);
                graphView.getViewport().setXAxisBoundsManual(true);
//            }
            graphView.onDataChanged(true, false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_POSITIVE:
                chronicler = new Chronicler();
                ((MementoApplication) getApplication()).getUser().setChronicler(chronicler);
                ((MementoApplication) getApplication()).saveChronicler();
                graphView.removeAllSeries();
        }
    }

    @Override
    public void onTap(Series series, DataPointInterface dataPoint) {
        Toast.makeText(this, CalendarConverter.showDate(new Date((long)(dataPoint.getX())))
                + ", " + getResources().getString(R.string.weight) + ": " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
    }
}