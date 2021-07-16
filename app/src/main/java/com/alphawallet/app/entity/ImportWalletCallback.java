package com.alphawallet.app.entity;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.alphawallet.app.entity.cryptokeys.KeyEncodingType;
import com.alphawallet.app.service.KeyService;

public interface ImportWalletCallback
{
    void walletValidated(String address, KeyEncodingType type, KeyService.AuthenticationLevel level);
    //void KeystoreValidated(String newPassword, KeyService.AuthenticationLevel level);
    //void KeyValidated(String newPassword, KeyService.AuthenticationLevel authLevel);
}
