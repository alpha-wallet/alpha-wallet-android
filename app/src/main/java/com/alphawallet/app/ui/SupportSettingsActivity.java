package com.alphawallet.app.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.LinearLayout;

import com.alphawallet.app.C;
import com.alphawallet.app.R;
import com.alphawallet.app.entity.MediaLinks;
import com.alphawallet.app.router.HelpRouter;
import com.alphawallet.app.widget.SettingsItemView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.s3.sample.auth.AWS4SignerBase;
import com.amazonaws.services.s3.sample.auth.AWS4SignerForAuthorizationHeader;
import com.amazonaws.services.s3.sample.util.BinaryUtils;
import com.amazonaws.services.s3.sample.util.HttpUtils;

import android.os.StrictMode;

public class SupportSettingsActivity extends BaseActivity {

    private LinearLayout supportSettingsLayout;

    private SettingsItemView telegram;
    private SettingsItemView twitter;
    private SettingsItemView reddit;
    private SettingsItemView facebook;
    private SettingsItemView blog;
    private SettingsItemView faq;
    private SettingsItemView eth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_settings);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        toolbar();
        setTitle(getString(R.string.title_support));

        initializeSettings();

        addSettingsToLayout();
    }

    private void initializeSettings() {
        telegram = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_logo_telegram)
                .withTitle(R.string.telegram)
                .withListener(this::onTelegramClicked)
                .build();

        twitter = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_logo_twitter)
                .withTitle(R.string.twitter)
                .withListener(this::onTwitterClicked)
                .build();

        reddit = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_logo_reddit)
                .withTitle(R.string.reddit)
                .withListener(this::onRedditClicked)
                .build();

        facebook = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_logo_facebook)
                .withTitle(R.string.facebook)
                .withListener(this::onFacebookClicked)
                .build();

        blog = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_settings_blog)
                .withTitle(R.string.title_blog)
                .withListener(this::onBlogClicked)
                .build();

        faq = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_settings_faq)
                .withTitle(R.string.title_faq)
                .withListener(this::onFaqClicked)
                .build();
        eth = new SettingsItemView.Builder(this)
                .withIcon(R.drawable.ic_settings_tokenscript)
                .withTitle(R.string.title_eth)
                .withListener(this::onEthClicked)
                .build();
    }

    private void addSettingsToLayout() {
        supportSettingsLayout = findViewById(R.id.layout);
        if (MediaLinks.AWALLET_TELEGRAM_URL != null) {
            supportSettingsLayout.addView(telegram);
        }
        if (MediaLinks.AWALLET_TWITTER_URL != null) {
            supportSettingsLayout.addView(twitter);
        }
        if (MediaLinks.AWALLET_REDDIT_URL != null) {
            supportSettingsLayout.addView(reddit);
        }
        if (MediaLinks.AWALLET_FACEBOOK_URL != null) {
            supportSettingsLayout.addView(facebook);
        }
        if (MediaLinks.AWALLET_BLOG_URL != null) {
            supportSettingsLayout.addView(blog);
        }
        supportSettingsLayout.addView(faq);
        supportSettingsLayout.addView(eth);
    }

    private void onTelegramClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MediaLinks.AWALLET_TELEGRAM_URL));
        if (isAppAvailable(C.TELEGRAM_PACKAGE_NAME)) {
            intent.setPackage(C.TELEGRAM_PACKAGE_NAME);
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void onLinkedInClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MediaLinks.AWALLET_LINKEDIN_URL));
        if (isAppAvailable(C.LINKEDIN_PACKAGE_NAME)) {
            intent.setPackage(C.LINKEDIN_PACKAGE_NAME);
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void onTwitterClicked() {
        Intent intent;
        try {
            getPackageManager().getPackageInfo(C.TWITTER_PACKAGE_NAME, 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MediaLinks.AWALLET_TWITTER_URL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MediaLinks.AWALLET_TWITTER_URL));
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void onRedditClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (isAppAvailable(C.REDDIT_PACKAGE_NAME)) {
            intent.setPackage(C.REDDIT_PACKAGE_NAME);
        }

        intent.setData(Uri.parse(MediaLinks.AWALLET_REDDIT_URL));

        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void onFacebookClicked() {
        Intent intent;
        try {
            getPackageManager().getPackageInfo(C.FACEBOOK_PACKAGE_NAME, 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MediaLinks.AWALLET_FACEBOOK_URL));
            //intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MediaLinks.AWALLET_FACEBOOK_ID));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MediaLinks.AWALLET_FACEBOOK_URL));
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    private void onBlogClicked() {

    }

    private void onFaqClicked() {
        new HelpRouter().open(this);
    }

    private void onEthClicked() {
        String objectContent = "{\"jsonrpc\": \"2.0\", \"method\": \"web3_clientVersion\", \"params\": [], \"id\": 67}";
        URL endpointUrl;
        try {
            endpointUrl = new URL("https://nd-4vmk4h4mczby7hics5pkg6c6xy.ethereum.managedblockchain.us-east-1.amazonaws.com");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to parse service endpoint: " + e.getMessage());
        }
        byte[] contentHash = AWS4SignerBase.hash(objectContent);
        String contentHashString = BinaryUtils.toHex(contentHash);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-amz-content-sha256", contentHashString);
        headers.put("content-length", "" + objectContent.length());
        headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");

        AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(
                endpointUrl, "PUT", "managedblockchain", "us-east-1");
        String authorization = signer.computeSignature(headers,
                null, // no query parameters
                contentHashString,
                "",
                "");

        // express authorization for this as a header
        headers.put("Authorization", authorization);

        // make the call to Amazon S3
        String response = HttpUtils.invokeHttpRequest(endpointUrl, "PUT", headers, objectContent);
        System.out.println("--------- Response content ---------");
        System.out.println(response);
        System.out.println("------------------------------------");
    }

    private boolean isAppAvailable(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
