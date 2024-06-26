package se.group3.gameoflife.frontend.fragments.choice_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import se2.group3.gameoflife.frontend.R;
import se.group3.gameoflife.frontend.activities.GameActivity;
import se.group3.gameoflife.frontend.dto.LobbyDTO;

import se.group3.gameoflife.frontend.dto.cards.ActionCardDTODTO;
import se.group3.gameoflife.frontend.fragments.OverlayFragment;
import se.group3.gameoflife.frontend.networking.ConnectionService;

public class ActionCardFragment extends Fragment {

    private String playername = "";
    private static final String NAME = "";
    private static final String TAG = "NETWORKING";
    private View rootView;
    private ConnectionService connectionService;


    public ActionCardFragment() {
        // Required empty public constructor
    }

    public static ActionCardFragment newInstance(String playername) {
        ActionCardFragment fragment = new ActionCardFragment();
        Bundle args = new Bundle();

        args.putString(NAME, playername);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playername = getArguments().getString(NAME);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_action_card, container, false);

        Log.d(TAG, "ActionCardFragment started.");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.getIsBound().observe(getViewLifecycleOwner(), isBound -> {
                if (Boolean.TRUE.equals(isBound)) {
                    connectionService = activity.getService();
                    if (connectionService != null) {
                        LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
                        if (lobbyDTO == null || lobbyDTO.getHouseCardDTOS() == null) {
                            Log.e(TAG, "LobbyDTO is null in ActionCardFragment.");
                            return;
                        }

                        ActionCardDTODTO actionCard = lobbyDTO.getActionCardDTOs().get(0);

                        updateUI(actionCard);

                        Button actionCardBTN = rootView.findViewById(R.id.ok);

                        actionCardBTN.setOnClickListener(v -> navigateToOverlayFragment());

                    }
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(ActionCardDTODTO actionCard) {

        TextView title = rootView.findViewById(R.id.actionCardTitle);
        TextView description = rootView.findViewById(R.id.actionCardDescription);
        TextView money = rootView.findViewById(R.id.actionCardMoney);
        TextView affect = rootView.findViewById(R.id.actionCardAffectedPlayer);

        title.setText(actionCard.getName());
        description.setText(actionCard.getDescription());
        if (actionCard.getMoneyAmount() >= 0) {
            money.setText(playername + " got " + actionCard.getMoneyAmount() + " money");
        } else {
            money.setText(playername + " looses " + actionCard.getMoneyAmount() + " money");
        }
        if (actionCard.isAffectAllPlayers()) {
            affect.setText("All players are affected");
        } else if (actionCard.isAffectBank()) {
            affect.setText("The Bank is affected");
        } else {
            affect.setText(playername + " is affected");
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