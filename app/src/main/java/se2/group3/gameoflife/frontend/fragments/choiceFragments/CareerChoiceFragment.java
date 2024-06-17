package se2.group3.gameoflife.frontend.fragments.choiceFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.activities.GameActivity;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se2.group3.gameoflife.frontend.fragments.OverlayFragment;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class CareerChoiceFragment extends Fragment {
    public final String TAG = "Networking";
    private View rootView;
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable;

    public CareerChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_career_choice, container, false);
        compositeDisposable = new CompositeDisposable();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
                if (isBound) {
                    connectionService = activity.getService();
                    if (connectionService != null) {
                    LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
                    if (lobbyDTO == null || lobbyDTO.getCareerCardDTOS() == null || lobbyDTO.getCareerCardDTOS().isEmpty()) {
                        Log.e(TAG, "LobbyDTO is null or empty in CareerChoiceFragment.");
                        return;
                    }

                    List<CareerCardDTO> cardDTOList = lobbyDTO.getCareerCardDTOS();

                    CareerCardDTO careerCard1 = cardDTOList.get(0);
                    CareerCardDTO careerCard2 = cardDTOList.get(1);

                    Button career1BTN = rootView.findViewById(R.id.chooseCareer1BTN);
                    Button career2BTN = rootView.findViewById(R.id.chooseCareer2BTN);


                    updateUI(careerCard1, careerCard2);

                    career1BTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));

                    career2BTN.setOnClickListener(v -> compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error))));
                    }
                }
            });
        }
    }



    @SuppressLint("SetTextI18n")
    private void updateUI(CareerCardDTO careerCard1, CareerCardDTO careerCard2) {
        TextView career1name = rootView.findViewById(R.id.career1Name);
        TextView career2name = rootView.findViewById(R.id.career2Name);
        TextView career1salary = rootView.findViewById(R.id.career1Salary);
        TextView career2salary = rootView.findViewById(R.id.career2salary);
        TextView career1bonus = rootView.findViewById(R.id.career1bonus);
        TextView career2bonus = rootView.findViewById(R.id.career2bonus);

        career1name.setText(careerCard1.getName());
        career2name.setText(careerCard2.getName());
        career1salary.setText("Salary: " + careerCard1.getSalary());
        career2salary.setText("Salary: " + careerCard2.getSalary());
        career1bonus.setText("Bonus: " + careerCard1.getBonus());
        career2bonus.setText("Bonus: " + careerCard2.getBonus());
    }

    private void navigateToOverlayFragment() {
        if (isAdded()) {
            FragmentTransaction transactionOverLay = requireActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}
