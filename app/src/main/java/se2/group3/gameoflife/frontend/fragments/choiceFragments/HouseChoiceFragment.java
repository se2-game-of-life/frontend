package se2.group3.gameoflife.frontend.fragments.choiceFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.cards.CardDTO;
import se2.group3.gameoflife.frontend.dto.cards.HouseCardDTO;
import se2.group3.gameoflife.frontend.fragments.OverlayFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseChoiceFragment extends Fragment {
    private View rootView;
    private GameViewModel gameViewModel;


    public HouseChoiceFragment() {
        // Required empty public constructor
    }

    public static HouseChoiceFragment newInstance() {
        HouseChoiceFragment fragment = new HouseChoiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_house_choice, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        List<CardDTO> cardDTOList = gameViewModel.getLobbyDTO().getCards();
        HouseCardDTO houseCard1 = (HouseCardDTO) cardDTOList.get(0);
        HouseCardDTO houseCard2 = (HouseCardDTO) cardDTOList.get(1);

        updateUI(houseCard1, houseCard2);
        Button house1BTN = rootView.findViewById(R.id.chooseHouse1BTN);
        Button house2BTN = rootView.findViewById(R.id.chooseHouse2BTN);

        house1BTN.setOnClickListener(v -> {
            gameViewModel.makeChoice(true);
            navigateToOverlayFragment();
        });

        house2BTN.setOnClickListener(v -> {
            gameViewModel.makeChoice(false);
            navigateToOverlayFragment();
        });

        return rootView;
    }

    private void updateUI(HouseCardDTO houseCard1, HouseCardDTO houseCard2){
        TextView house1name = rootView.findViewById(R.id.house1Name);
        TextView house2name = rootView.findViewById(R.id.house2Name);
        TextView house1purchasePrice = rootView.findViewById(R.id.house1purchasePrice);
        TextView house2purchasePrice = rootView.findViewById(R.id.house2purchasePrice);
        TextView house1redSellPrice = rootView.findViewById(R.id.house1redSellPrice);
        TextView house2redSellPrice = rootView.findViewById(R.id.house2redSellPrice);
        TextView house1blackSellPrice = rootView.findViewById(R.id.house1blackSellPrice);
        TextView house2blackSellPrice = rootView.findViewById(R.id.house2blackSellPrice);

        house1name.setText(houseCard1.getName());
        house2name.setText(houseCard2.getName());
        house1purchasePrice.setText("Purchase Price: " + houseCard1.getPurchasePrice());
        house2purchasePrice.setText("Purchase Price: " + houseCard2.getPurchasePrice());
        house1redSellPrice.setText("Red Sell Price: " + houseCard1.getRedSellPrice());
        house2redSellPrice.setText("Red Sell Price: " + houseCard2.getRedSellPrice());
        house1blackSellPrice.setText("Black Sell Price: " + houseCard1.getBlackSellPrice());
        house2blackSellPrice.setText("Black Sell Price: " + houseCard2.getBlackSellPrice());

    }

    private void navigateToOverlayFragment(){
        if (getActivity() != null) {
            FragmentTransaction transactionOverLay = getActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}