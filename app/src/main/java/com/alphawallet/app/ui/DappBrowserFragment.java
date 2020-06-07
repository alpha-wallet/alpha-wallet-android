package com.alphawallet.app.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
<<<<<<< HEAD
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
=======
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
>>>>>>> e3074436a... Attempt to upgrade to AndroidX
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alphawallet.app.C;
import com.alphawallet.app.R;
import com.alphawallet.app.entity.CryptoFunctions;
import com.alphawallet.app.entity.CustomViewSettings;
import com.alphawallet.app.entity.DApp;
import com.alphawallet.app.entity.DAppFunction;
import com.alphawallet.app.entity.FragmentMessenger;
import com.alphawallet.app.entity.NetworkInfo;
import com.alphawallet.app.entity.QRResult;
import com.alphawallet.app.entity.SignAuthenticationCallback;
import com.alphawallet.app.entity.SignTransactionInterface;
import com.alphawallet.app.entity.URLLoadInterface;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.entity.WalletPage;
import com.alphawallet.app.repository.EthereumNetworkBase;
import com.alphawallet.app.repository.EthereumNetworkRepository;
import com.alphawallet.app.repository.TokensRealmSource;
import com.alphawallet.app.repository.entity.RealmToken;
import com.alphawallet.app.ui.widget.OnDappClickListener;
import com.alphawallet.app.ui.widget.OnDappHomeNavClickListener;
import com.alphawallet.app.ui.widget.OnHistoryItemRemovedListener;
import com.alphawallet.app.ui.widget.adapter.DappBrowserSuggestionsAdapter;
import com.alphawallet.app.ui.widget.entity.DappBrowserSwipeInterface;
import com.alphawallet.app.ui.widget.entity.DappBrowserSwipeLayout;
import com.alphawallet.app.ui.widget.entity.ItemClickListener;
import com.alphawallet.app.ui.zxing.FullScannerFragment;
import com.alphawallet.app.ui.zxing.QRScanningActivity;
import com.alphawallet.app.util.BalanceUtils;
import com.alphawallet.app.util.DappBrowserUtils;
import com.alphawallet.app.util.Hex;
import com.alphawallet.app.util.KeyboardUtils;
import com.alphawallet.app.util.LocaleUtils;
import com.alphawallet.app.util.QRParser;
import com.alphawallet.app.util.Utils;
import com.alphawallet.app.viewmodel.DappBrowserViewModel;
import com.alphawallet.app.viewmodel.DappBrowserViewModelFactory;
import com.alphawallet.app.web3.OnSignMessageListener;
import com.alphawallet.app.web3.OnSignPersonalMessageListener;
import com.alphawallet.app.web3.OnSignTransactionListener;
import com.alphawallet.app.web3.OnSignTypedMessageListener;
import com.alphawallet.app.web3.Web3View;
import com.alphawallet.app.web3.entity.Address;
import com.alphawallet.app.web3.entity.Web3Transaction;
import com.alphawallet.app.widget.AWalletAlertDialog;
import com.alphawallet.app.widget.SignMessageDialog;
import com.alphawallet.app.widget.SignTransactionDialog;
import com.alphawallet.token.entity.EthereumMessage;
import com.alphawallet.token.entity.EthereumTypedMessage;
import com.alphawallet.token.entity.SalesOrderMalformed;
import com.alphawallet.token.entity.Signable;
import com.alphawallet.token.tools.Numeric;
import com.alphawallet.token.tools.ParseMagicLink;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static com.alphawallet.app.C.ETHER_DECIMALS;
import static com.alphawallet.app.C.RESET_TOOLBAR;
import static com.alphawallet.app.C.RESET_WALLET;
import static com.alphawallet.app.entity.CryptoFunctions.sigFromByteArray;
import static com.alphawallet.app.entity.Operation.SIGN_DATA;
import static com.alphawallet.app.entity.tokens.Token.TOKEN_BALANCE_PRECISION;
import static com.alphawallet.app.ui.MyAddressActivity.KEY_ADDRESS;
import static com.alphawallet.app.widget.AWalletAlertDialog.ERROR;

public class DappBrowserFragment extends Fragment implements OnSignTransactionListener, OnSignPersonalMessageListener, OnSignTypedMessageListener, OnSignMessageListener,
        URLLoadInterface, ItemClickListener, SignTransactionInterface, OnDappClickListener, OnDappHomeNavClickListener, OnHistoryItemRemovedListener, DappBrowserSwipeInterface, SignAuthenticationCallback
{
    private static final String TAG = DappBrowserFragment.class.getSimpleName();
    private static final String DAPP_BROWSER = "DAPP_BROWSER";
    private static final String MY_DAPPS = "MY_DAPPS";
    private static final String DISCOVER_DAPPS = "DISCOVER_DAPPS";
    private static final String HISTORY = "HISTORY";
    public static final String SEARCH = "SEARCH";
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    public static final String CURRENT_FRAGMENT = "currentFragment";
    private static final String CURRENT_URL = "urlInBar";
    private ValueCallback<Uri[]> uploadMessage;
    private WebChromeClient.FileChooserParams fileChooserParams;
    private Intent picker;
    private RealmResults<RealmToken> realmUpdate;

    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    private static final int UPLOAD_FILE = 1;
    public static final int REQUEST_FILE_ACCESS = 31;
    public static final int REQUEST_FINE_LOCATION = 110;

    /**
     Below object is used to set Animation duration for expand/collapse and rotate
     */
    private final int ANIMATION_DURATION = 100;

    static byte[] getEthereumMessagePrefix(int messageLength) {
        return MESSAGE_PREFIX.concat(String.valueOf(messageLength)).getBytes();
    }

    @Inject
    DappBrowserViewModelFactory dappBrowserViewModelFactory;
    private DappBrowserViewModel viewModel;

    private DappBrowserSwipeLayout swipeRefreshLayout;
    private Web3View web3;
    private AutoCompleteTextView urlTv;
    private ProgressBar progressBar;
    private Wallet wallet;
    private NetworkInfo networkInfo;
    private SignMessageDialog dialog;
    private AWalletAlertDialog resultDialog;
    private DappBrowserSuggestionsAdapter adapter;
    private String loadOnInit;
    private boolean homePressed;

    private final Fragment myDappsFragment;
    private final Fragment discoverDappsFragment;
    private final Fragment browserHistoryFragment;

    private Toolbar toolbar;
    private ImageView back;
    private ImageView next;
    private ImageView clear;
    private ImageView refresh;
    private TextView currentNetwork;
    private ImageView currentNetworkCircle;
    private LinearLayout currentNetworkClicker;
    private TextView balance;
    private TextView symbol;
    private View layoutNavigation;
    private GeolocationPermissions.Callback geoCallback = null;
    private String geoOrigin;
    private final Handler handler;

    private String currentWebpageTitle;
    private String currentFragment;

    private Signable messageTBS;  // To-Be-Signed
    private byte[] messageBytes;
    private DAppFunction dAppFunction;
    private SignType signType;
    private volatile boolean canSign = true;

    @Nullable
    private Disposable disposable;

    private enum SignType
    {
        SIGN_PERSONAL_MESSAGE, SIGN_MESSAGE
    }

    public DappBrowserFragment()
    {
        myDappsFragment = new MyDappsFragment();
        discoverDappsFragment = new DiscoverDappsFragment();
        browserHistoryFragment = new BrowserHistoryFragment();
        handler = new Handler();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LocaleUtils.setActiveLocale(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        homePressed = false;
        if (currentFragment == null) currentFragment = DAPP_BROWSER;
        attachFragment(currentFragment);
        if ((web3 == null || viewModel == null) && getActivity() != null) //trigger reload
        {
            ((HomeActivity)getActivity()).resetFragment(WalletPage.DAPP_BROWSER);
        }
        else
        {
            web3.setWebLoadCallback(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        LocaleUtils.setActiveLocale(getContext());
        int webViewID = CustomViewSettings.minimiseBrowserURLBar() ? R.layout.fragment_webview_compact : R.layout.fragment_webview;
        View view = inflater.inflate(webViewID, container, false);
        initViewModel();
        initView(view);
        setupAddressBar();
        viewModel.prepare(getContext());
        loadOnInit = null;

        // Load url from a link within the app
        if (getArguments() != null && getArguments().getString("url") != null) {
            String url = getArguments().getString("url");
            loadOnInit = url;
        } else {
            String lastUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(CURRENT_URL, "");
            if (savedInstanceState != null)
            {
                lastUrl = savedInstanceState.getString(CURRENT_URL, "");
            }

            attachFragment(DAPP_BROWSER);
            loadOnInit = TextUtils.isEmpty(lastUrl) ? EthereumNetworkRepository.defaultDapp() : lastUrl;
        }

        return view;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (getContext() != null && fragment.getTag() != null)
        {
            switch (fragment.getTag())
            {
                case DISCOVER_DAPPS:
                    ((DiscoverDappsFragment) fragment).setCallbacks(this);
                    break;
                case MY_DAPPS:
                    ((MyDappsFragment) fragment).setCallbacks(this);
                    break;
                case HISTORY:
                    ((BrowserHistoryFragment) fragment).setCallbacks(this, this);
                    break;
                case DAPP_BROWSER:
                    break;
                default:
                    //no init
                    break;
            }
        }
    }

    private void attachFragment(Fragment fragment, String tag)
    {
        Fragment testFrag = getChildFragmentManager().findFragmentByTag(tag);
        if (testFrag != null && testFrag.isVisible() && !testFrag.isDetached())
        {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commitAllowingStateLoss();
        }
        else if (tag != null && getHost() != null && getChildFragmentManager().findFragmentByTag(tag) == null)
        {
            showFragment(fragment, tag);
        }
    }

    private void attachFragment(String tag) {
        if (tag != null && getHost() != null && getChildFragmentManager().findFragmentByTag(tag) == null)
        {
            Fragment f = null;
            switch (tag)
            {
                case DISCOVER_DAPPS:
                    f = discoverDappsFragment;
                    break;
                case MY_DAPPS:
                    f = myDappsFragment;
                    break;
                case HISTORY:
                    f = browserHistoryFragment;
                    break;
                case DAPP_BROWSER: //special case - dapp browser is no fragments loaded
                    addToBackStack(DAPP_BROWSER);
                    break;
            }

            if (f != null && !f.isAdded()) showFragment(f, tag);
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        addToBackStack(tag);
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame, fragment, tag)
                .commit();

        setBackForwardButtons();
    }

    private void detachFragments()
    {
        detachFragment(MY_DAPPS);
        detachFragment(DISCOVER_DAPPS);
        detachFragment(HISTORY);
        detachFragment(SEARCH);
    }

    public void homePressed()
    {
        homePressed = true;
        detachFragments();
        currentFragment = DAPP_BROWSER;
        if (urlTv != null)
            urlTv.getText().clear();
        if (web3 != null)
        {
            web3.clearHistory();
            web3.stopLoading();

            web3.loadUrl(EthereumNetworkRepository.defaultDapp(), getWeb3Headers());
            urlTv.setText(EthereumNetworkRepository.defaultDapp());
        }

        //blank forward / backward arrows
        setBackForwardButtons();
    }

    @Override
    public void onDappHomeNavClick(int position) {
        detachFragments();
        switch (position) {
            case 0: {
                addToBackStack(MY_DAPPS);
                attachFragment(myDappsFragment, MY_DAPPS);
                break;
            }
            case 1: {
                addToBackStack(DISCOVER_DAPPS);
                attachFragment(discoverDappsFragment, DISCOVER_DAPPS);
                break;
            }
            case 2: {
                addToBackStack(HISTORY);
                attachFragment(browserHistoryFragment, HISTORY);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onDappClick(DApp dapp) {
        addToBackStack(DAPP_BROWSER);
        loadUrl(dapp.getUrl());
    }

    @Override
    public void onHistoryItemRemoved(DApp dApp) {
        adapter.removeSuggestion(dApp);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        viewModel.onDestroy();
        if (realmUpdate != null) realmUpdate.removeAllChangeListeners();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    private void setupMenu(View baseView)
    {
        refresh = baseView.findViewById(R.id.refresh);
        final MenuItem reload = toolbar.getMenu().findItem(R.id.action_reload);
        final MenuItem share = toolbar.getMenu().findItem(R.id.action_share);
        final MenuItem scan = toolbar.getMenu().findItem(R.id.action_scan);
        final MenuItem add = toolbar.getMenu().findItem(R.id.action_add_to_my_dapps);
        final MenuItem history = toolbar.getMenu().findItem(R.id.action_history);
        final MenuItem bookmarks = toolbar.getMenu().findItem(R.id.action_my_dapps);
        final MenuItem clearCache = toolbar.getMenu().findItem(R.id.action_clear_cache);

        if (reload != null) reload.setOnMenuItemClickListener(menuItem -> {
            reloadPage();
            return true;
        });
        if (share != null) share.setOnMenuItemClickListener(menuItem -> {
            if (web3.getUrl() != null && currentFragment != null && currentFragment.equals(DAPP_BROWSER)) {
                if (getContext() != null) viewModel.share(getContext(), web3.getUrl());
            }
            else
            {
                displayNothingToShare();
            }
            return true;
        });
        if (scan != null) scan.setOnMenuItemClickListener(menuItem -> {
            viewModel.startScan(getActivity());
            return true;
        });
        if (add != null) add.setOnMenuItemClickListener(menuItem -> {
            viewModel.addToMyDapps(getContext(), currentWebpageTitle, urlTv.getText().toString());
            return true;
        });
        if (history != null) history.setOnMenuItemClickListener(menuItem -> {
            addToBackStack(HISTORY);
            attachFragment(browserHistoryFragment, HISTORY);
            return true;
        });
        if (bookmarks != null) bookmarks.setOnMenuItemClickListener(menuItem -> {
            addToBackStack(MY_DAPPS);
            attachFragment(myDappsFragment, MY_DAPPS);
            return true;
        });
        if (clearCache != null) clearCache.setOnMenuItemClickListener(menuItem -> {
            viewModel.onClearBrowserCacheClicked(getContext());
            return true;
        });
    }

    private void initView(View view) {
        web3 = view.findViewById(R.id.web3view);
        progressBar = view.findViewById(R.id.progressBar);
        urlTv = view.findViewById(R.id.url_tv);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setRefreshInterface(this);

        toolbar = view.findViewById(R.id.address_bar);
        layoutNavigation = view.findViewById(R.id.layout_navigator);

        //If you are wondering about the strange way the menus are inflated - this is required to ensure
        //that the menu text gets created with the correct localisation under every circumstance
        MenuInflater inflater = new MenuInflater(LocaleUtils.getActiveLocaleContext(getContext()));
        if (CustomViewSettings.minimiseBrowserURLBar())
        {
            inflater.inflate(R.menu.menu_scan, toolbar.getMenu());
        }
        else if (EthereumNetworkRepository.defaultDapp() != null)
        {
            inflater.inflate(R.menu.menu_bookmarks, toolbar.getMenu());
        }
        refresh = view.findViewById(R.id.refresh);
        setupMenu(view);

        RelativeLayout layout = view.findViewById(R.id.address_bar_layout);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        refresh.setOnClickListener(v -> reloadPage());

        back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> goToPreviousPage());

        next = view.findViewById(R.id.next);
        next.setOnClickListener(v -> goToNextPage());

        clear = view.findViewById(R.id.clear_url);
        clear.setOnClickListener(v -> {
            clearAddressBar();
        });

        currentNetworkClicker = view.findViewById(R.id.network_holder);
        currentNetworkClicker.setOnClickListener(v -> selectNetwork());
        currentNetwork = view.findViewById(R.id.network_text);
        currentNetworkCircle = view.findViewById(R.id.network_colour);
        balance = view.findViewById(R.id.balance);
        symbol = view.findViewById(R.id.symbol);
        web3.setWebLoadCallback(this);

        if (viewModel.getActiveFilterCount() == 1 && EthereumNetworkRepository.defaultDapp() != null) currentNetworkClicker.setVisibility(View.GONE);
    }

    private void displayNothingToShare()
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setTitle(getString(R.string.nothing_to_share));
        resultDialog.setMessage(getString(R.string.nothing_to_share_message));
        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.setCancelable(true);
        resultDialog.show();
    }

    private void selectNetwork() {
        Intent intent = new Intent(getContext(), SelectNetworkActivity.class);
        intent.putExtra(C.EXTRA_SINGLE_ITEM, true);
        intent.putExtra(C.EXTRA_CHAIN_ID, String.valueOf(networkInfo.chainId));
        if (getActivity() != null) getActivity().startActivityForResult(intent, C.REQUEST_SELECT_NETWORK);
    }

    private void clearAddressBar() {
        if (urlTv.getText().toString().isEmpty()) {
            cancelSearchSession();
        } else {
            urlTv.getText().clear();
            beginSearchSession();
        }
    }

    private void setupAddressBar() {
        adapter = new DappBrowserSuggestionsAdapter(
                getContext(),
                viewModel.getDappsMasterList(getContext()),
                this::onItemClick
        );
        urlTv.setAdapter(null);

        urlTv.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_GO)
            {
                String urlText = urlTv.getText().toString();
                handled = loadUrl(urlText);
                detachFragments();
                cancelSearchSession();
            }
            return handled;
        });

        // Both these are required, the onFocus listener is required to respond to the first click.
        urlTv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && getActivity() != null) beginSearchSession();
        });

        urlTv.setOnClickListener(v -> {
            beginSearchSession();
        });

        urlTv.setShowSoftInputOnFocus(true);

        urlTv.setOnLongClickListener(v -> {
            urlTv.dismissDropDown();
            return false;
        });

        urlTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.setHighlighted(editable.toString());
            }
        });
    }

    private void beginSearchSession() {
        urlTv.setAdapter(null);
        expandCollapseView(currentNetwork, false);
        expandCollapseView(layoutNavigation, false);

        disposable = Observable.zip(
                Observable.interval(600, TimeUnit.MILLISECONDS).take(1),
                Observable.fromArray(clear), (interval, item) -> item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::postBeginSearchSession);
    }

    private void postBeginSearchSession(ImageView item)
    {
        urlTv.setAdapter(adapter);
        urlTv.showDropDown();
        if (item.getVisibility() == View.GONE)
        {
            expandCollapseView(item, true);
            KeyboardUtils.showKeyboard(urlTv);
        }
    }

    /**
     * Used to expand or collapse the view
     */
    private synchronized void expandCollapseView(View view, boolean expandView)
    {
        //detect if view is expanded or collapsed
        boolean isViewExpanded = view.getVisibility() == View.VISIBLE;

        //Collapse view
        if (isViewExpanded && !expandView)
        {
            int finalWidth = view.getWidth();
            ValueAnimator valueAnimator = slideAnimator(finalWidth, 0, view);
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            valueAnimator.start();
        }
        //Expand view
        else if (!isViewExpanded && expandView)
        {
            view.setVisibility(View.VISIBLE);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

            view.measure(widthSpec, heightSpec);
            int width = view.getMeasuredWidth();
            ValueAnimator valueAnimator = slideAnimator(0, width, view);
            valueAnimator.start();
        }
    }

    private ValueAnimator slideAnimator(int start, int end, final View view) {

        final ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(valueAnimator -> {
            // Update Height
            int value = (Integer) valueAnimator.getAnimatedValue();

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = value;
            view.setLayoutParams(layoutParams);
        });
        animator.setDuration(ANIMATION_DURATION);
        return animator;
    }

    private void addToBackStack(String nextFragment)
    {
        currentFragment = nextFragment;
    }

    private void addToForwardStack(String prevFragment)
    {
        currentFragment = prevFragment;
    }

    private void cancelSearchSession() {
        detachFragment(SEARCH);
        if (toolbar != null)
        {
            toolbar.getMenu().setGroupVisible(R.id.dapp_browser_menu, true);
            expandCollapseView(currentNetwork, true);
            expandCollapseView(layoutNavigation, true);
            clear.setVisibility(View.GONE);
            urlTv.dismissDropDown();
        }
        KeyboardUtils.hideKeyboard(urlTv);
        setBackForwardButtons();
    }

    private void detachFragment(String tag) {
        if (!isAdded()) return; //the dappBrowserFragment itself may not yet be attached.
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment != null && fragment.isVisible() && !fragment.isDetached()) {
            getChildFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, dappBrowserViewModelFactory)
                .get(DappBrowserViewModel.class);
        viewModel.defaultNetwork().observe(this, this::onDefaultNetwork);
        viewModel.defaultWallet().observe(this, this::onDefaultWallet);
    }

    private void startBalanceListener()
    {
        if (wallet == null || networkInfo == null) return;

        if (realmUpdate != null) realmUpdate.removeAllChangeListeners();
        realmUpdate = viewModel.getRealmInstance(wallet).where(RealmToken.class)
                .equalTo("address", TokensRealmSource.databaseKey(networkInfo.chainId, "eth"))
                .equalTo("chainId", networkInfo.chainId).findAllAsync();
        realmUpdate.addChangeListener(realmTokens -> {
            //update balance
            if (realmTokens.size() == 0) return;
            RealmToken realmToken = realmTokens.first();
            balance.setVisibility(View.VISIBLE);
            symbol.setVisibility(View.VISIBLE);
            String newBalanceStr = BalanceUtils.getScaledValueFixed(new BigDecimal(realmToken.getBalance()), ETHER_DECIMALS, TOKEN_BALANCE_PRECISION);
            balance.setText(newBalanceStr);
            symbol.setText(networkInfo.getShortName());
        });
    }

    private void onDefaultWallet(Wallet wallet) {
        this.wallet = wallet;
        setupWeb3();
        startBalanceListener();
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        int oldChain = this.networkInfo != null ? this.networkInfo.chainId : -1;
        this.networkInfo = networkInfo;
        currentNetwork.setText(networkInfo.getShortName());
        startBalanceListener();
        //select resource
        Utils.setChainCircle(currentNetworkCircle, networkInfo.chainId);
        //reset the pane if required
        if (oldChain > 0 && oldChain != this.networkInfo.chainId)
        {
            web3.reload();
        }
    }

    private void setupWeb3() {
        web3.setActivity(getActivity());
        web3.setChainId(networkInfo.chainId);
        web3.setRpcUrl(EthereumNetworkBase.getDefaultNodeURL(networkInfo.chainId));
        web3.setWalletAddress(new Address(wallet.address));

        web3.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webview, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    refresh.setEnabled(true);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                currentWebpageTitle = title;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback)
            {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                requestGeoPermission(origin, callback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fCParams)
            {
                if (filePathCallback == null) return true;
                uploadMessage = filePathCallback;
                fileChooserParams = fCParams;
                picker = fileChooserParams.createIntent();

                if (checkReadPermission()) return requestUpload();
                else return true;
            }
        });

        web3.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String[] prefixCheck = url.split(":");
                if (prefixCheck.length > 1)
                {
                    Intent intent;
                    switch (prefixCheck[0])
                    {
                        case C.DAPP_PREFIX_TELEPHONE:
                            intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(url));
                            startActivity(Intent.createChooser(intent, "Call " + prefixCheck[1]));
                            return true;
                        case C.DAPP_PREFIX_MAILTO:
                            intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse(url));
                            startActivity(Intent.createChooser(intent, "Email: " + prefixCheck[1]));
                            return true;
                        case C.DAPP_PREFIX_ALPHAWALLET:
                            if(prefixCheck[1].equals(C.DAPP_SUFFIX_RECEIVE)) {
                                viewModel.showMyAddress(getContext());
                                return true;
                            }
                            break;
                        case C.DAPP_PREFIX_WALLETCONNECT:
                            //start walletconnect
                            if (getContext() != null) viewModel.handleWalletConnect(getContext(), url);
                            return true;
                        default:
                            break;
                    }
                }

                urlTv.setText(url);
                return false;
            }
        });

        web3.setOnSignMessageListener(this);
        web3.setOnSignPersonalMessageListener(this);
        web3.setOnSignTransactionListener(this);
        web3.setOnSignTypedMessageListener(this);

        if (loadOnInit != null)
        {
            addToBackStack(DAPP_BROWSER);
            web3.loadUrl(Utils.formatUrl(loadOnInit), getWeb3Headers());
            urlTv.setText(Utils.formatUrl(loadOnInit));
        }
    }

    protected boolean requestUpload()
    {
        try
        {
            startActivityForResult(picker, UPLOAD_FILE);
        }
        catch (ActivityNotFoundException e)
        {
            uploadMessage = null;
            Toast.makeText(getActivity().getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onSignMessage(EthereumMessage message) {
        messageTBS = message;
        dAppFunction = new DAppFunction() {
            @Override
            public void DAppError(Throwable error, Signable message) {
                web3.onSignCancel(message);
                dialog.dismiss();
            }

            @Override
            public void DAppReturn(byte[] data, Signable message) {
                String signHex = Numeric.toHexString(data);
                Log.d(TAG, "Initial Msg: " + message.getMessage());
                web3.onSignMessageSuccessful(message, signHex);
                dialog.dismiss();
            }
        };

        try
        {
            dialog = new SignMessageDialog(getActivity(), message);
            dialog.setAddress(wallet.address);
            dialog.setOnApproveListener(v -> {
                // TODO: Weiwu: this segment should be encapsulated in EthereumMessage
                // ensure we generate the signature correctly:
                if (messageTBS.getMessage() != null)
                {
                    viewModel.getAuthorisation(wallet, getActivity(), this);
                }
                else
                {
                    onSignError();
                }
            });
            dialog.setOnRejectListener(v -> {
                if (web3 != null) web3.onSignCancel(message);
                dialog.dismiss();
            });
            dialog.show();
        }
        catch (Exception e)
        {
            onSignError(e.getMessage());
        }
    }

    @Override
    public void onSignPersonalMessage(EthereumMessage message) {
        messageTBS = message;
        dAppFunction = new DAppFunction() {
            @Override
            public void DAppError(Throwable error, Signable message) {
                web3.onSignCancel(message);
                dialog.dismiss();
            }

            // TODO: Weiwu issue #1556 move this code to a class.
            @Override
            public void DAppReturn(byte[] data, Signable message) {
                String signHex = Numeric.toHexString(data);
                Log.d(TAG, "Initial Msg: " + message.getMessage());
                web3.onSignPersonalMessageSuccessful(message, signHex);
                //Test Sig in debug build
                if (BuildConfig.DEBUG) testRecoverAddressFromSignature(Hex.hexToUtf8(message.getMessage()), signHex);
                dialog.dismiss();
            }
        };

        try
        {
            // opens a dialogue to ask the user to sign
            dialog = new SignMessageDialog(getActivity(), message);
            dialog.setAddress(wallet.address);
            dialog.setMessage(message.getUserMessage());
            dialog.setOnApproveListener(v -> {
                messageBytes = getEthereumMessage(Numeric.hexStringToByteArray(message.getMessage()));
                viewModel.getAuthorisation(wallet, getActivity(), this);
            });
            dialog.setOnRejectListener(v -> {
                web3.onSignCancel(message);
                dialog.dismiss();
            });
            dialog.show();
        }
        catch (Exception e)
        {
            // this will be mainly for developers, so no need to tidy the exception
            // if a user comes across this message they can report to the dapp writer
            onSignError(e.getMessage());
        }
    }

    static byte[] getEthereumMessage(byte[] message) {
        byte[] prefix = getEthereumMessagePrefix(message.length);

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return result;
    }

    @Override
    public void onSignTypedMessage(EthereumTypedMessage message)
    {
        if (message.getPrehash() == null)
        {
            web3.onSignCancel(message);
        }
        else
        {
            messageTBS = message;
            dAppFunction = new DAppFunction() {
                @Override
                public void DAppError(Throwable error, Signable message) {
                    web3.onSignCancel(message);
                    dialog.dismiss();
                }

                @Override
                public void DAppReturn(byte[] data, Signable message) {
                    String signHex = Numeric.toHexString(data);
                    Log.d(TAG, "Initial Msg: " + message.getMessage());
                    web3.onSignMessageSuccessful(message, signHex);
                    dialog.dismiss();
                }
            };

            try
            {
                dialog = new SignMessageDialog(getActivity(), message);
                dialog.setAddress(wallet.address);
                dialog.setOnApproveListener(v -> {
                    // TODO: Weiwu: this segment should be encapsulated in EthereumMessage
                    // ensure we generate the signature correctly:
                    if (messageTBS.getOrigin() != null)
                    {
                        viewModel.getAuthorisation(wallet, getActivity(), this);
                    }
                    else
                    {
                        onSignError();
                    }
                });
                dialog.setOnRejectListener(v -> {
                    if (web3 != null) web3.onSignCancel(message);
                    dialog.dismiss();
                });
                dialog.show();
            }
            catch (Exception e)
            {
                onSignError(e.getMessage());
            }
        }
    }


    @Override
    public void onSignTransaction(Web3Transaction transaction, String url)
    {
        try
        {
            viewModel.updateGasPrice(networkInfo.chainId); //start updating gas price right before we open
            //minimum for transaction to be valid: recipient and value or payload
            if ((transaction.recipient.equals(Address.EMPTY) && transaction.payload != null) // Constructor
                    || (!transaction.recipient.equals(Address.EMPTY) && (transaction.payload != null || transaction.value != null))) // Raw or Function TX
            {
                if (canSign)
                {
                    viewModel.openConfirmation(getActivity(), transaction, url, networkInfo);
                    canSign = false;
                    handler.postDelayed(() -> canSign = true, 3000); //debounce 3 seconds to avoid multiple signing issues
                }
            }
            else
            {
                //display transaction error
                onInvalidTransaction(transaction);
                web3.onSignCancel(transaction);
            }
        }
        catch (android.os.TransactionTooLargeException e)
        {
            transactionTooLarge();
            web3.onSignCancel(transaction);
        }
        catch (Exception e)
        {
            onInvalidTransaction(transaction);
            web3.onSignCancel(transaction);
        }
    }

    //return from the openConfirmation above
    public void handleTransactionCallback(int resultCode, Intent data)
    {
        if (data == null || web3 == null) return;
        Web3Transaction web3Tx = data.getParcelableExtra(C.EXTRA_WEB3TRANSACTION);
        if (resultCode == RESULT_OK && web3Tx != null)
        {
            String hashData = data.getStringExtra(C.EXTRA_TRANSACTION_DATA);
            web3.onSignTransactionSuccessful(web3Tx, hashData);
        }
        else if (web3Tx != null)
        {
            web3.onSignCancel(web3Tx);
        }
    }

    private void onSignError()
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setIcon(AWalletAlertDialog.ERROR);
        resultDialog.setTitle(getString(R.string.dialog_title_sign_message));
        resultDialog.setMessage(getString(R.string.contains_no_data));
        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.setCancelable(true);
        resultDialog.show();
    }

    private void onSignError(String message)
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setIcon(AWalletAlertDialog.ERROR);
        resultDialog.setTitle(getString(R.string.dialog_title_sign_message));
        resultDialog.setMessage(message);
        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.setCancelable(true);
        resultDialog.show();
    }

    private void transactionTooLarge()
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setIcon(AWalletAlertDialog.ERROR);
        resultDialog.setTitle(getString(R.string.transaction_too_large));
        resultDialog.setMessage(getString(R.string.unable_to_handle_tx));

        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.setCancelable(true);
        resultDialog.show();
    }

    private void onInvalidTransaction(Web3Transaction transaction)
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setIcon(AWalletAlertDialog.ERROR);
        resultDialog.setTitle(getString(R.string.invalid_transaction));

        if (transaction.recipient.equals(Address.EMPTY) && (transaction.payload == null || transaction.value != null))
        {
            resultDialog.setMessage(getString(R.string.contains_no_recipient));
        }
        else if (transaction.payload == null && transaction.value == null)
        {
            resultDialog.setMessage(getString(R.string.contains_no_value));
        }
        else
        {
            resultDialog.setMessage(getString(R.string.contains_no_data));
        }
        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.setCancelable(true);
        resultDialog.show();
    }

    private void goToPreviousPage()
    {
        if (back.getAlpha() == 0.3f) return;
        if (!currentFragment.equals(DAPP_BROWSER))
        {
            //switch to dapp browser
            detachFragments();
            addToBackStack(DAPP_BROWSER);
            setBackForwardButtons();
        }
        else if (web3.canGoBack())
        {
            checkBackClickArrowVisibility(); //to make arrows function correctly - don't want to wait for web page to load to check back/forwards - this looks clunky
            web3.goBack();
            detachFragments();
            loadSessionUrl(-1);
        }
        else
        {
            //load homepage
            homePressed = true;
            web3.loadUrl(EthereumNetworkBase.defaultDapp(), getWeb3Headers());
            urlTv.setText(EthereumNetworkBase.defaultDapp());
            web3.clearHistory();
        }
    }

    private void goToNextPage()
    {
        if (next.getAlpha() == 0.3f) return;
        if (currentFragment.equals(DAPP_BROWSER) && web3.canGoForward())
        {
            checkForwardClickArrowVisibility();
            web3.goForward();
            loadSessionUrl(1);
        }
    }

    /**
     * Check if this is the last web item and the last fragment item.
     */
    private void checkBackClickArrowVisibility()
    {
        //will this be last item?
        WebBackForwardList sessionHistory = web3.copyBackForwardList();
        int nextIndex = sessionHistory.getCurrentIndex() - 1;
        if (nextIndex <= 0) back.setAlpha(0.3f);
        else back.setAlpha(1.0f);
    }

    /**
     * After a forward click while web browser active, check if forward and back arrows should be updated.
     * Note that the web item only becomes history after the next page is loaded, so if the next item is new, then
     */
    private void checkForwardClickArrowVisibility()
    {
        WebBackForwardList sessionHistory = web3.copyBackForwardList();
        int nextIndex = sessionHistory.getCurrentIndex() + 1;
        if (nextIndex >= sessionHistory.getSize() - 1) next.setAlpha(0.3f);
        else next.setAlpha(1.0f);
    }

    /**
     * Browse to relative entry with sanity check on value
     * @param relative relative addition or subtraction of browsing index
     */
    private void loadSessionUrl(int relative)
    {
        WebBackForwardList sessionHistory = web3.copyBackForwardList();
        int newIndex = sessionHistory.getCurrentIndex() + relative;
        if (newIndex < sessionHistory.getSize())
        {
            WebHistoryItem newItem = sessionHistory.getItemAtIndex(newIndex);
            if (newItem != null)
            {
                urlTv.setText(newItem.getUrl());
            }
        }
    }

    @Override
    public void onWebpageLoaded(String url, String title)
    {
        if (getContext() == null) return; //could be a late return from dead fragment
        if (homePressed)
        {
            homePressed = false;
            if (currentFragment.equals(DAPP_BROWSER) && url.equals(EthereumNetworkRepository.defaultDapp()))
            {
                web3.clearHistory();
            }
        }
        DApp dapp = new DApp(title, url);
        DappBrowserUtils.addToHistory(getContext(), dapp);
        adapter.addSuggestion(dapp);
        onWebpageLoadComplete();
    }

    @Override
    public void onWebpageLoadComplete()
    {
        handler.post(this::setBackForwardButtons); //execute on UI thread
    }

    private void setBackForwardButtons()
    {
        WebBackForwardList sessionHistory = null;
        boolean canBrowseBack = false;
        boolean canBrowseForward = false;

        if (web3 != null)
        {
            sessionHistory = web3.copyBackForwardList();
            String url = web3.getUrl();
            canBrowseBack = !currentFragment.equals(DAPP_BROWSER) || web3.canGoBack() || (!TextUtils.isEmpty(url) && !url.equals(EthereumNetworkBase.defaultDapp()));
            canBrowseForward = (currentFragment.equals(DAPP_BROWSER) && sessionHistory != null && sessionHistory.getCurrentIndex() < sessionHistory.getSize() - 1);
        }

        if (back != null)
        {
            if (canBrowseBack)
            {
                back.setAlpha(1.0f);
            }
            else
            {
                back.setAlpha(0.3f);
            }
        }

        if (next != null)
        {
            if (canBrowseForward)
            {
                next.setAlpha(1.0f);
            }
            else
            {
                next.setAlpha(0.3f);
            }
        }
    }

    private boolean loadUrl(String urlText)
    {
        detachFragments();
        addToBackStack(DAPP_BROWSER);
        cancelSearchSession();
        if (checkForMagicLink(urlText)) return true;
        web3.loadUrl(Utils.formatUrl(urlText), getWeb3Headers());
        urlTv.setText(Utils.formatUrl(urlText));
        web3.requestFocus();
        viewModel.setLastUrl(getContext(), urlText);
        Activity current = getActivity();
        if (current != null)
        {
            current.sendBroadcast(new Intent(RESET_TOOLBAR));
        }
        return true;
    }

    /* Required for CORS requests */
    private Map<String, String> getWeb3Headers()
    {
        //headers
        return new HashMap<String, String>() {{
            put("Connection", "close");
            put("Content-Type", "text/plain");
            put("Access-Control-Allow-Origin", "*");
            put("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
            put("Access-Control-Max-Age", "600");
            put("Access-Control-Allow-Credentials", "true");
            put("Access-Control-Allow-Headers", "accept, authorization, Content-Type");
        }};
    }

    public void reloadPage() {
        if (currentFragment.equals(DAPP_BROWSER))
        {
            refresh.setEnabled(false);
            web3.reload();
        }
    }

    @Override
    public void onItemClick(String url)
    {
        addToBackStack(DAPP_BROWSER);
        loadUrl(url);
    }

    public void testRecoverAddressFromSignature(String message, String sig)
    {
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = (prefix + message).getBytes();

        byte[] signatureBytes = Numeric.hexStringToByteArray(sig);
        Sign.SignatureData sd = sigFromByteArray(signatureBytes);
        String addressRecovered;

        try
        {
            BigInteger recoveredKey = Sign.signedMessageToKey(msgHash, sd);
            addressRecovered = "0x" + Keys.getAddress(recoveredKey);
            System.out.println("Recovered: " + addressRecovered);
        }
        catch (SignatureException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void signTransaction(Web3Transaction transaction, String txHex, boolean success)
    {
        if (success)
        {
            web3.onSignTransactionSuccessful(transaction, txHex);
        }
        else
        {
            web3.onSignCancel(transaction);
        }
    }

    public void handleSelectNetwork(int resultCode, Intent data) {
        if (getActivity() == null) return;
        if (resultCode == RESULT_OK) {
            int networkId = data.getIntExtra(C.EXTRA_CHAIN_ID, 1); //default to mainnet in case of trouble
            if (networkInfo.chainId != networkId) {
                viewModel.setNetwork(networkId);
                if (getActivity() != null) getActivity().sendBroadcast(new Intent(RESET_WALLET));
                balance.setVisibility(View.GONE);
                symbol.setVisibility(View.GONE);
            }
        }
    }

    public void handleQRCode(int resultCode, Intent data, FragmentMessenger messenger)
    {
        //result
        String qrCode = null;
        try
        {
            switch (resultCode)
            {
                case FullScannerFragment.SUCCESS:
                    if (data != null)
                    {
                        qrCode = data.getStringExtra(FullScannerFragment.BarcodeObject);
                        if (qrCode == null || checkForMagicLink(qrCode)) return;
                        QRParser parser = QRParser.getInstance(EthereumNetworkBase.extraChains());
                        QRResult result = parser.parse(qrCode);
                        switch (result.type)
                        {
                            case ADDRESS:
                                //ethereum address was scanned. In dapp browser what do we do? maybe populate an input field with address?
                                copyToClipboard(result.getAddress());
                                break;
                            case PAYMENT:
                                //EIP681 payment request scanned, should go to send
                                viewModel.showSend(getContext(), result);
                                break;
                            case TRANSFER:
                                //EIP681 transfer, go to send
                                viewModel.showSend(getContext(), result);
                                break;
                            case FUNCTION_CALL:
                                //EIP681 function call. TODO: create function call confirmation. For now treat same way as tokenscript function call
                                break;
                            case URL:
                                loadUrlRemote(qrCode);
                                break;
                            case OTHER:
                                qrCode = null;
                                break;
                        }
                    }
                    break;
                case QRScanningActivity.DENY_PERMISSION:
                    showCameraDenied();
                    break;
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            qrCode = null;
        }

        if (qrCode == null && getActivity() != null)
        {
            Toast.makeText(getActivity(), R.string.toast_invalid_code, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads URL from remote process; this converts a request to load URL which isn't on the app's thread
     * @param qrCode
     */
    private void loadUrlRemote(final String qrCode)
    {
        handler.post(() -> loadUrl(qrCode));
    }

    private void showCameraDenied()
    {
        if (getActivity() == null) return;
        resultDialog = new AWalletAlertDialog(getActivity());
        resultDialog.setTitle(R.string.title_dialog_error);
        resultDialog.setMessage(R.string.error_camera_permission_denied);
        resultDialog.setIcon(ERROR);
        resultDialog.setButtonText(R.string.button_ok);
        resultDialog.setButtonListener(v -> {
            resultDialog.dismiss();
        });
        resultDialog.show();
    }

    private void copyToClipboard(String address)
    {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(KEY_ADDRESS, address);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    private boolean checkForMagicLink(String data)
    {
        try
        {
            ParseMagicLink parser = new ParseMagicLink(new CryptoFunctions(), EthereumNetworkRepository.extraChains());
            if (parser.parseUniversalLink(data).chainId > 0) //see if it's a valid link
            {
                //handle magic link import
                viewModel.showImportLink(getActivity(), data);
                return true;
            }
        }
        catch (SalesOrderMalformed e)
        {
            //
        }

        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    private boolean checkReadPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, REQUEST_FILE_ACCESS);
            return false;
        }
    }

    // Handles the requesting of the fine location permission.
    // Note: If you intend allowing geo-location in your app you need to ask the permission.
    private void requestGeoPermission(String origin, GeolocationPermissions.Callback callback)
    {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            geoCallback = callback;
            geoOrigin = origin;
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            getActivity().requestPermissions(permissions, REQUEST_FINE_LOCATION);
        }
        else
        {
            callback.invoke(origin, true, false);
        }
    }

    public void gotGeoAccess(String[] permissions, int[] grantResults)
    {
        boolean geoAccess = false;
        for (int i = 0; i < permissions.length; i++)
        {
            if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] != -1) geoAccess = true;
        }
        if (!geoAccess) Toast.makeText(getContext(), "Permission not given", Toast.LENGTH_SHORT).show();
        if (geoCallback != null && geoOrigin != null) geoCallback.invoke(geoOrigin, geoAccess, false);
    }

    public void gotFileAccess(String[] permissions, int[] grantResults)
    {
        boolean fileAccess = false;
        for (int i = 0; i < permissions.length; i++)
        {
            if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] != -1) fileAccess = true;
        }

        if (fileAccess && picker != null) requestUpload();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAGMENT, currentFragment);
        outState.putString(CURRENT_URL, urlTv.getText().toString());
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putString(CURRENT_FRAGMENT, currentFragment)
                .putString(CURRENT_URL, urlTv.getText().toString())
                .apply();
    }

    @Override
    public void RefreshEvent()
    {
        //determine scroll position
        Log.i("Touch", "SCROLL: " + web3.getScrollY());
        if (web3.getScrollY() == 0)
        {
            loadUrl(web3.getUrl());
        }
    }

    @Override
    public int getCurrentScrollPosition()
    {
        return web3.getScrollY();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode >= SignTransactionDialog.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS && requestCode <= SignTransactionDialog.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS + 10)
        {
            gotAuthorisation(resultCode == RESULT_OK);
        }
        else if (requestCode == UPLOAD_FILE && uploadMessage != null)
        {
            if (resultCode == RESULT_OK)
            {
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            }
            uploadMessage = null;
        }
        else if (requestCode == REQUEST_FILE_ACCESS)
        {
            if (resultCode == RESULT_OK)
            {
                requestUpload();
            }
        }
    }

    // this is called when the signing is approved by the user (e.g. fingerprint / PIN)
    @Override
    public void gotAuthorisation(boolean gotAuth)
    {
        if (gotAuth)
        {
            viewModel.completeAuthentication(SIGN_DATA);
            viewModel.signMessage(messageTBS, dAppFunction);
        }
        else if (dialog != null && dialog.isShowing())
        {
            web3.onSignCancel(messageTBS);
            dialog.dismiss();
        }
    }

    @Override
    public void cancelAuthentication()
    {

    }
}
