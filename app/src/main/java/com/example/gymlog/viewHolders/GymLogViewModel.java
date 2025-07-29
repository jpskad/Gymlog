package com.example.gymlog.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gymlog.database.GymLogRepository;
import com.example.gymlog.database.entities.GymLog;

import java.util.List;

public class GymLogViewModel extends AndroidViewModel {
    private final GymLogRepository repository;
//    private final LiveData<List<GymLog>> allLogsByID;
    public GymLogViewModel(Application application){
        super(application);
        repository=GymLogRepository.getRepository(application);
//        allLogsByID = repository.getAllLogsByUserIDLiveData(userID);
    }

    public LiveData<List<GymLog>> getAllLogsByID(int userID) {
        return repository.getAllLogsByUserIDLiveData(userID);
    }

    public void insert(GymLog log) {
        repository.insertGymLog(log);
    }
}
