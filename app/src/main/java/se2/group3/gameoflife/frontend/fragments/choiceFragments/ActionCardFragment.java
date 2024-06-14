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

import se2.group3.gameoflife.frontend.R;
import se2.group3.gameoflife.frontend.dto.cards.ActionCardDTODTO;
import se2.group3.gameoflife.frontend.fragments.OverlayFragment;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;

public class ActionCardFragment extends Fragment {

    private View rootView;
    private GameViewModel gameViewModel;


    public ActionCardFragment() {
        // Required empty public constructor
    }

    public static ActionCardFragment newInstance() {
        ActionCardFragment fragment = new ActionCardFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_action_card, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        //todo: lobbyDTO is not available yet, klau den code aus an fragment raus :)

       // updateUI(actionCard);

        Button actionCardBTN = rootView.findViewById(R.id.ok);

        actionCardBTN.setOnClickListener(v -> {
            navigateToOverlayFragment();
        });

        return rootView;

    }

    private void updateUI(ActionCardDTODTO actionCard) {

        TextView title = rootView.findViewById(R.id.actionCardTitle);
        TextView description = rootView.findViewById(R.id.actionCardDescription);
        TextView money = rootView.findViewById(R.id.actionCardMoney);
        TextView affect = rootView.findViewById(R.id.actionCardAffectedPlayer);

        title.setText(actionCard.getName());
        description.setText(actionCard.getDescription());
        if (actionCard.getMoneyAmount() >= 0) {
            money.setText("You get " + actionCard.getMoneyAmount() + " money");
        } else {
            money.setText("You loose " + actionCard.getMoneyAmount() + " money");
        }
        if (actionCard.isAffectAllPlayers()) {
            affect.setText("All players are affected");
        } else if (actionCard.isAffectBank()) {
            affect.setText("The Bank is affected");
        } else {
            affect.setText("You are affected");
        }
    }

    private void navigateToOverlayFragment() {
        if (getActivity() != null) {
            FragmentTransaction transactionOverLay = getActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}