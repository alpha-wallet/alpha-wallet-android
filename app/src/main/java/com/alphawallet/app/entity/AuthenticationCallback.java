package com.alphawallet.app.entity;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

/**
 * Created by James on 9/06/2019.
 * Stormbird in Sydney
 */

public interface AuthenticationCallback
{
    void authenticatePass(Operation callbackId);
    void authenticateFail(String fail, AuthenticationFailType failType, Operation callbackId);
    void legacyAuthRequired(Operation callbackId, String dialogTitle, String desc);
}