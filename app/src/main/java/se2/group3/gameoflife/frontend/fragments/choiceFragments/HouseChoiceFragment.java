package se2.group3.gameoflife.frontend.fragments.choiceFragments;

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
import se2.group3.gameoflife.frontend.dto.cards.HouseCardDTO;
import se2.group3.gameoflife.frontend.fragments.OverlayFragment;
import se2.group3.gameoflife.frontend.networking.ConnectionService;

public class HouseChoiceFragment extends Fragment {
    private View rootView;
    private final String TAG = "Networking";
    private ConnectionService connectionService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_house_choice, container, false);

        Log.d(TAG, "HouseChoiceFragment started.");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GameActivity activity = (GameActivity) getActivity();
        if (activity != null) {
            activity.getConnectionService(cs -> {
                connectionService = cs;
                if (connectionService != null) {
                    LobbyDTO lobbyDTO = connectionService.getLiveData(LobbyDTO.class).getValue();
                    if (lobbyDTO == null || lobbyDTO.getHouseCardDTOS() == null) {
                        Log.e(TAG, "LobbyDTO is null in HouseChoiceFragment.");
                        return;
                    }
                    List<HouseCardDTO> cardDTOList = lobbyDTO.getHouseCardDTOS();
                    HouseCardDTO houseCard1 = cardDTOList.get(0);
                    HouseCardDTO houseCard2 = cardDTOList.get(1);

                    updateUI(houseCard1, houseCard2);

                    Button house1BTN = rootView.findViewById(R.id.chooseHouse1BTN);
                    Button house2BTN = rootView.findViewById(R.id.chooseHouse2BTN);

                    String uuid = connectionService.getUuidLiveData().getValue();
                    if (uuid != null && uuid.equals(lobbyDTO.getCurrentPlayer().getPlayerUUID())) {
                        house1BTN.setVisibility(View.VISIBLE);
                        house2BTN.setVisibility(View.VISIBLE);
                    } else {
                        house1BTN.setVisibility(View.GONE);
                        house2BTN.setVisibility(View.GONE);
                    }

                    house1BTN.setOnClickListener(v -> {
                        compositeDisposable.add(connectionService.send("/app/lobby/choice", true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error)));
                    });

                    house2BTN.setOnClickListener(v -> {
                        compositeDisposable.add(connectionService.send("/app/lobby/choice", false)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::navigateToOverlayFragment, error -> Log.e(TAG, "Error making choice: " + error)));
                    });
                }
            });
        }
    }

    private void updateUI(HouseCardDTO houseCard1, HouseCardDTO houseCard2) {
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

    private void navigateToOverlayFragment() {
        if (isAdded()) {
            FragmentTransaction transactionOverLay = getActivity().getSupportFragmentManager().beginTransaction();
            OverlayFragment overlayFragment = new OverlayFragment();
            transactionOverLay.replace(R.id.fragmentContainerView2, overlayFragment);
            transactionOverLay.addToBackStack(null);
            transactionOverLay.commit();
        }
    }
}
