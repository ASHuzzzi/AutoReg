package ru.lizzzi.autoreg;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class ViewModelMain extends AndroidViewModel {
    private SQLStorage sqlStorage;
    private String defaultCodeOfRegion = "";

    public ViewModelMain(@NonNull Application application) {
        super(application);
        sqlStorage = new SQLStorage(getApplication().getApplicationContext());
    }

    void checkStorage() {
        sqlStorage.checkDataBase();
    }

    String getDefaultCodeOfRegion() {
        return defaultCodeOfRegion;
    }

    String getRegion(String codeOfRegion) {
        return defaultCodeOfRegion = sqlStorage.getRegion(codeOfRegion);
    }

    String getOtherCodeOfRegion(String region) {
        return sqlStorage.getCode(region);
    }
}
