// Copyright (c) 2017 Kano Applications Inc. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.kanoapps.cordova.nativevars;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.SharedPreferences;

import android.provider.Settings;
/**
 * Cordova plugin that returns custom native variables
 */
public class NativeVarsPlugin extends CordovaPlugin {

    private static final String TAG = "NativeVarsPlugin";
    private static final String PREFS_NAME = "NativeVarsPlugin";

    /**
     * Executes the request and returns PluginResult
     *
     * @param  action
     * @param  args
     * @param  callbackContext
     * @return boolean
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("getNativeVars")) {

            try {
                Context context = this.cordova.getActivity().getApplicationContext();
                PackageManager packageManager = this.cordova.getActivity().getPackageManager();
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String packageName = this.cordova.getActivity().getPackageName();
                String appVersionOld = prefs.getString("appVersionOld", "");
                String appVersion = packageManager.getPackageInfo(packageName, 0).versionName;

                if (!appVersion.equals(appVersionOld))
                {
                    editor.putString("appVersionOld", appVersion);
                    editor.apply();
                }

                JSONObject r = new JSONObject();
                r.put("appVersion", appVersion);
                r.put("appVersionOld", appVersionOld);
                r.put("buildVersion", packageManager.getPackageInfo(packageName, 0).versionCode);

                Long launchTimestamp = System.currentTimeMillis();
                r.put("launchTimestamp", launchTimestamp.toString());
                r.put("cordovaDataDirectory", "file://" + this.cordova.getActivity().getFilesDir().toString() + "/");

                String testLabSetting = Settings.System.getString(this.cordova.getActivity().getContentResolver(), "firebase.test.lab");
                if ("true".equals(testLabSetting)) {
                    r.put("isTestLab", testLabSetting);
                }

                callbackContext.success(r);

            } catch (NameNotFoundException e) {
                callbackContext.error("Exception thrown");
            }

            return true;
        }

        // Default response to say the action hasn't been handled
        return false;
    }
}