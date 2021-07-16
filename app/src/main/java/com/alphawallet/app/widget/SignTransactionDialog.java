package com.alphawallet.app.widget;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.alphawallet.app.C;
import com.alphawallet.app.R;
import com.alphawallet.app.entity.AuthenticationCallback;
import com.alphawallet.app.entity.AuthenticationFailType;
import com.alphawallet.app.entity.Operation;

import java.util.concurrent.Executor;

import static android.content.Context.KEYGUARD_SERVICE;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

/**
 * Created by James on 7/06/2019.
 * Stormbird in Sydney
 */
public class SignTransactionDialog
{
    public static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 123;

    private final AuthenticationStrength authenticationStrength;
    private boolean isShowing;
    private BiometricPrompt biometricPrompt;

    public SignTransactionDialog(Context context)
    {
        isShowing = false;
        BiometricManager biometricManager = BiometricManager.from(context);
        if (biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS ||
            biometricManager.canAuthenticate(DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS)
        {
            authenticationStrength = AuthenticationStrength.STRONG_AUTHENTICATION;
        }
        else if (biometricManager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS)
        {
            authenticationStrength = AuthenticationStrength.WEAK_AUTHENTICATION;
        }
        else
        {
            authenticationStrength = AuthenticationStrength.NO_AUTHENTICATION;
        }
    }

    public void getAuthentication(AuthenticationCallback authCallback, @NonNull Activity activity, Operation callbackId)
    {
        Executor executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt((FragmentActivity) activity,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                isShowing = false;
                switch (errorCode)
                {
                    case BiometricPrompt.ERROR_CANCELED:
                        authCallback.authenticateFail("Cancelled", AuthenticationFailType.FINGERPRINT_ERROR_CANCELED, callbackId);
                        break;
                    case BiometricPrompt.ERROR_LOCKOUT:
                    case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                        authCallback.authenticateFail(activity.getString(R.string.too_many_fails), AuthenticationFailType.FINGERPRINT_NOT_VALIDATED, callbackId);
                        break;
                    case BiometricPrompt.ERROR_USER_CANCELED:
                        authCallback.authenticateFail(activity.getString(R.string.fingerprint_error_user_canceled), AuthenticationFailType.FINGERPRINT_ERROR_CANCELED, callbackId);
                        break;
                    case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                    case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                    case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                    case BiometricPrompt.ERROR_NO_BIOMETRICS:
                    case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                    case BiometricPrompt.ERROR_NO_SPACE:
                    case BiometricPrompt.ERROR_TIMEOUT:
                    case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                    case BiometricPrompt.ERROR_VENDOR:
                        authCallback.authenticateFail(activity.getString(R.string.fingerprint_authentication_failed), AuthenticationFailType.FINGERPRINT_NOT_VALIDATED, callbackId);
                        break;
                }
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                isShowing = false;
                authCallback.authenticatePass(callbackId);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                isShowing = false;
                authCallback.authenticateFail(activity.getString(R.string.fingerprint_authentication_failed), AuthenticationFailType.FINGERPRINT_NOT_VALIDATED, callbackId);
            }
        });

        /*ActivityResultLauncher<Intent> getPINPassword = ((ComponentActivity)activity).registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    isShowing = false;
                    if (result.getResultCode() == RESULT_OK)
                    {
                        authCallback.authenticatePass(callbackId);
                    }
                    else
                    {
                        authCallback.authenticateFail(activity.getString(R.string.authentication_failed), AuthenticationFailType.PIN_FAILED, callbackId);
                    }
                });*/

        /*BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(activity.getString(R.string.unlock_private_key))
                .setNegativeButtonText(activity.getString(R.string.use_pin))
                .setAllowedAuthenticators(BIOMETRIC_STRONG) // | DEVICE_CREDENTIAL  //BIOMETRIC_STRONG
                .build();

        biometricPrompt.authenticate(promptInfo); */
        showAuthenticationScreen(activity, authCallback, callbackId);

        isShowing = true;
    }


    private void showAuthenticationScreen(Activity activity, AuthenticationCallback authCallback, Operation callBackId)
    {
        KeyguardManager km = (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
        if (km != null)
        {
            Intent intent = km.createConfirmDeviceCredentialIntent(activity.getString(R.string.unlock_private_key), "");
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS + callBackId.ordinal());
        }
        else
        {
            authCallback.authenticateFail("Device unlocked", AuthenticationFailType.DEVICE_NOT_SECURE, callBackId);
        }
    }


    public void close()
    {
        if (biometricPrompt != null)
        {
            try
            {
                biometricPrompt.cancelAuthentication();
            }
            catch (Exception e)
            {
                //
            }
        }
    }

    public boolean isShowing()
    {
        return isShowing;
    }

    private enum AuthenticationStrength
    {
        STRONG_AUTHENTICATION,
        WEAK_AUTHENTICATION,
        NO_AUTHENTICATION
    }
}