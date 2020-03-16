package mx.com.gauta.usedoc.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.gauta.usedoc.R;
import mx.com.gauta.usedoc.user_active;

public class LogOff extends Fragment {

    private LogOffViewModel mViewModel;

    public static LogOff newInstance() {
        return new LogOff();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.log_off_fragment, container, false);
    }

    private void cerrar() {
        user_active.logOff(this.getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LogOffViewModel.class);
        // TODO: Use the ViewModel
    }

}
