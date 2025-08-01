package com.example.gymlog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.GymLog;
import com.example.gymlog.database.entities.User;
import com.example.gymlog.databinding.ActivityMainBinding;
import com.example.gymlog.viewHolders.GymLogAdapter;
import com.example.gymlog.viewHolders.GymLogViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.gymlog.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.gymlog.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;
    private GymLogRepository repository;
    public static final String TAG = "GYMLOG";
    String mExercise = "";
    double mWeight = 0.0;
    int mReps = 0;
    private int loggedInUserID = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.gymlog.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GymLogViewModel gymLogViewModel = new ViewModelProvider(this).get(GymLogViewModel.class);


        RecyclerView recyclerView = binding.logDisplayRecyclerView;
        final GymLogAdapter adapter = new GymLogAdapter(new GymLogAdapter.GymLogDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = GymLogRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        gymLogViewModel.getAllLogsByID(loggedInUserID).observe(this, adapter::submitList);

        //user is not logged in at this point, go to login screen
        if(loggedInUserID == -1) {
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        updatedSharedPreference();

        binding.logButton.setOnClickListener(v -> {
            getInformationFromDisplay();
            insertGymlogRecord();
        });
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
            loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_userID_key),LOGGED_OUT);

        if (loggedInUserID == LOGGED_OUT & savedInstanceState != null && savedInstanceState
                .containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);
        }
        if(loggedInUserID==LOGGED_OUT){
            loggedInUserID=getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);
        }
        if (loggedInUserID==LOGGED_OUT){
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserID(loggedInUserID);
        userObserver.observe(this, user-> {
            this.user=user;
            if(this.user!=null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY,loggedInUserID);
        updatedSharedPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if(user==null){
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(item1 -> {
            showLogoutDialog();
            return false;
        });
        return true;
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", (dialog, which) -> logout());

        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> alertDialog.dismiss());
        alertBuilder.create().show();
    }

    private void logout() {
        loggedInUserID = LOGGED_OUT;
        updatedSharedPreference();

        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);

        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updatedSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userID_key),loggedInUserID);
        sharedPrefEditor.apply();
    }

    static Intent mainActivityIntentFactory(Context context, int userID) {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID,userID);
        return intent;
    }

    private void insertGymlogRecord() {
        if(mExercise.isEmpty()) {
            return;
        }
        GymLog log = new GymLog(mExercise,mWeight,mReps,loggedInUserID);
        repository.insertGymLog(log);
    }

    private void getInformationFromDisplay() {
        mExercise = binding.exerciseInputEditText.getText().toString();
        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG,"Error reading value from Weight edit text");
        }

        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG,"Error reading value from Rep edit text");
        }
    }
}