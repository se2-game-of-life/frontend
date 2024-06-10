package se2.group3.gameoflife.frontend.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.cards.CardDTO;
import se2.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


public class CareerChoiceFragment extends Fragment {
    private View rootView;
    private GameViewModel gameViewModel;


    public CareerChoiceFragment() {
        // Required empty public constructor
    }
    public static CareerChoiceFragment newInstance() {
        CareerChoiceFragment fragment = new CareerChoiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_career_choice, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        List<CardDTO> cardDTOList = gameViewModel.getLobbyDTO().getCards();
        CareerCardDTO careerCard1 = (CareerCardDTO) cardDTOList.get(0);
        CareerCardDTO careerCard2 = (CareerCardDTO) cardDTOList.get(1);

        updateUI(careerCard1, careerCard2);
        Button career1BTN = rootView.findViewById(R.id.chooseCareer1BTN);
        Button career2BTN = rootView.findViewById(R.id.chooseCareer2BTN);

        career1BTN.setOnClickListener(v -> gameViewModel.makeChoice(true));

        career2BTN.setOnClickListener(v -> gameViewModel.makeChoice(false));

        return rootView;
    }

    private void updateUI(CareerCardDTO careerCard1, CareerCardDTO careerCard2){
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
}