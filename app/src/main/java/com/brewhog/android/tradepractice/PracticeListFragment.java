package com.brewhog.android.tradepractice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PracticeListFragment extends Fragment
implements BillingProcessor.IBillingHandler{
    public static final String TAG = "PracticeListFragment";
    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";
    private ImageView lessonKindIllustration;
    private RecyclerView practiceListRecyclerView;
    private FirebaseAuth mAuth;
    private PracticeAdapter mAdapter;
    private BroadcastReceiver notifycationReceiver;
    private List<Practice> mPracticeList;
    private PracticePack mPracticePack;
    private ProgressBar loadingProgress;
    private int orientation;
    private  BillingProcessor bp;

    public static PracticeListFragment newInstance(int imageResID) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID_ARGS,imageResID);

        PracticeListFragment fragment = new PracticeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = this.getResources().getConfiguration().orientation;

        //Инициализируем биллинг процессор - необходим для сервиса подписки
        bp = BillingProcessor.newBillingProcessor(getActivity(), BillingConstants.PUBLIC_KEY, this);
        bp.initialize();

        notifycationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Срабатывание метода означает, что пользователь видет активность.
                //Для отмены оповещения меняем результат, NotificationReceiver не будет уведомлять
                //о новых графиках, при этом текущий список обновится автоматически.
                Log.i(TAG,"cancel notification");
                setResultCode(Activity.RESULT_CANCELED);
                updateUI();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice_list,container,false);
        setHasOptionsMenu(true);

        int imageResId = getArguments().getInt(IMAGE_RES_ID_ARGS);
        lessonKindIllustration = view.findViewById(R.id.practice_lesson_large_illustration);
        lessonKindIllustration.setImageResource(imageResId);

        practiceListRecyclerView = view.findViewById(R.id.practice_recycler_view);
        practiceListRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(),3, RecyclerView.VERTICAL,false));
        loadingProgress = view.findViewById(R.id.load_progress_bar);

        connectToFirebase();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ChartUpdateService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(notifycationReceiver,intentFilter,
                ChartUpdateService.PERM_PRIVATE,null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(notifycationReceiver);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        // произошла покупка
        Log.i(TAG,"onProductPurchased");


    }

    @Override
    public void onPurchaseHistoryRestored() {
        // подгружает историю покупок, например после  переустновки приложения
        Log.i(TAG,"onPurchaseHistoryRestored");

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        //ошибка при покупке,  в том числе, когда пользователь не совершил покупку
        Log.i(TAG,"onBillingError");
        showPreFinishDialog("Не удалось приобрести подписку на практический раздел.");
    }

    @Override
    public void onBillingInitialized() {
        //вызывается , когда билинг проинициализировался
        Log.i(TAG,"onBillingInitialized");


        //проверяем поддерживает ли устройство возможность подписки
        boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
        if(isSubsUpdateSupported) {
            //проверяем была ли сделана покупка подписки, если нет вызываем диалог для покупки
            if (!isSubscribeActive(BillingConstants.SUBSCRIPTION_ID)){
                bp.subscribe(getActivity(),BillingConstants.SUBSCRIPTION_ID);
            }

        }else{
            showPreFinishDialog("Оплата подписки не поддерживается на данном устройстве.");
        }
    }

    private boolean isSubscribeActive(String subscribeId){
        boolean purchaseResult = bp.loadOwnedPurchasesFromGoogle();
        if (purchaseResult){
            TransactionDetails subscriptionTransactionDetail = bp.
                    getSubscriptionTransactionDetails(subscribeId);

            if (subscriptionTransactionDetail != null){
                //подписка активна
                return true;
            }else {
                //подписка не активна
                return false;
            }

        }else{
            return purchaseResult;
        }
    }

    //вызывает диалог с сообщением, который закрывает активность
    private void showPreFinishDialog(String message){
        FragmentManager manager = getFragmentManager();
        DialogFragment dialog = PreFinishDialog.newInstance(message);
        dialog.show(manager,TAG);

    }

    private class PracticeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tickerView;
        private ImageView chartView;
        private TextView dateView;
        private Practice mPracticeItem;

        public PracticeHolder(@NonNull View itemView) {
            super(itemView);

            tickerView = itemView.findViewById(R.id.ticker);
            chartView = itemView.findViewById(R.id.chart);
            dateView = itemView.findViewById(R.id.date_of_chart);

            itemView.setOnClickListener(this);
        }

        public void Bind(Practice practiceItem){
            mPracticeItem = practiceItem;

            Glide.with(getActivity())
                    .load(mPracticeItem.getChartUrl())
                    .placeholder(R.drawable.chart_placeholder)
                    .into(chartView);
            tickerView.setText(mPracticeItem.getTicker());
            dateView.setText(mPracticeItem.getDate());
        }

        @Override
        public void onClick(View view) {
            String chartUrl = mPracticeItem.getChartUrl();
            String chartUrlDone = mPracticeItem.getChartDoneUrl();
            ArrayList<String> signals = (ArrayList<String>) mPracticeItem.getSignals();

            Intent intent = ChartActivity.newIntent(getActivity(),chartUrl,chartUrlDone,signals);
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                getActivity().startActivity(intent);

            }else{
                ChartActivity.startActivityWithTransition(getActivity(),chartView,intent);
            }
        }
    }

    public class PracticeAdapter extends RecyclerView.Adapter<PracticeHolder>{
        private List<Practice> mPracticeList;
        private  RecyclerView mRecyclerView;

        public PracticeAdapter(List<Practice> practiceList) {
            mPracticeList = practiceList;
        }

        @NonNull
        @Override
        public PracticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_practice,parent,false);
            PracticeHolder holder = new PracticeHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PracticeHolder holder, int position) {
            Practice practice = mPracticeList.get(position);
            holder.Bind(practice);
        }

        @Override
        public int getItemCount() {
            return mPracticeList.size();
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            mRecyclerView = recyclerView;
        }

        public void welcomeAnimation(){
            loadingProgress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            Animation animation = AnimationUtils.
                    loadAnimation(getActivity(),R.anim.item_animation_from_bottom);
            mRecyclerView.setAnimation(animation);
        }
    }

    private void updateUI(){
        Log.i(TAG,"updateUI called");

        if (mAdapter == null){
            mPracticeList = new ArrayList<>();
            mAdapter = new PracticeAdapter(mPracticeList);
            practiceListRecyclerView.setAdapter(mAdapter);

            //Загружаем данные, вставляем в созданный лист, обнавляем адаптер
            mPracticePack = new PracticePack(mAdapter,mPracticeList);
            mPracticePack.loadPracticeList();
        } else{
            mPracticePack.loadPracticeList();
            mAdapter.notifyDataSetChanged();
        }

    }

    private void connectToFirebase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            updateUI();
        }else{
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.e(TAG,"signInAnonymously:SUCCESS");
                        updateUI();

                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"signInAnonymously:FAILURE",e);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.fragment_practice_list,menu);
         MenuItem notificationSelector = menu.findItem(R.id.notification_selector);

         boolean isAlarmOn = ChartUpdateService.isChartServiceAlarmOn(getActivity());
         if (isAlarmOn){
             notificationSelector.setTitle(getString(R.string.notify_on));
         }else{
             notificationSelector.setTitle(getString(R.string.notify_off));
         }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification_selector:

                boolean shouldStartService = !ChartUpdateService.isChartServiceAlarmOn(getActivity());
                ChartUpdateService.setChartServiceAlarm(getActivity(),shouldStartService);
                getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
