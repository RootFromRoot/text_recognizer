package vadimtk5.realm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import vadimtk5.realm.utils.RequestCodes;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private List<Task> dataSet = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupRealm();
        setupRecyclerView();

    }

    private void setupRealm() {//конфігурація
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build()
        );
        realm = Realm.getDefaultInstance();
    }

    private void setupViews() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    }

    private List<Task> fetchTasksFromDb() {
        return realm.where(Task.class).findAll();
    }

    private void addTaskToDb(Task task) {
        realm.beginTransaction();
        realm.insert(task);
        realm.commitTransaction();
    }

    private void setupRecyclerView() {
        dataSet.addAll(fetchTasksFromDb());
        adapter = new MyAdapter(this, dataSet);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void addNewTask(View view) {
        Intent intent = new Intent(MainActivity.this, TaskSetting.class);
        startActivityForResult(intent, RequestCodes.REQUEST_ADD_TASK);

        recyclerView.scrollToPosition(adapter.getDataSet().size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RequestCodes.REQUEST_ADD_TASK:
                if (resultCode == RESULT_OK) {
                    try {
                        String name = data.getExtras().getString("name");
                        String description = data.getExtras().getString("description");
                        Date date = ((Date) data.getExtras().getSerializable("date"));

                        Task task = new Task(
                                name,
                                description,
                                date
                        );

                        adapter.addTask(task);
                        addTaskToDb(task);
                    } catch (Exception ex) {
                        Log.e(TAG, "onActivityResult: ", ex);
                        Toast.makeText(MainActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            default:
                break;
        }
    }
}