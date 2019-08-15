// Copyright (c) 2017 Kano Applications Inc. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

var exec = require('cordova/exec');
var channel = require('cordova/channel');

channel.createSticky('onCordovaAppVersionReady');
channel.waitForInitialization('onCordovaAppVersionReady');

var NativeVarsPlugin = function () {
    var that = this;

    channel.onCordovaReady.subscribe(function() {
        that.init(function(info) {
            that.appVersion = info.appVersion;
            that.appVersionOld = info.appVersionOld;
            that.cordovaDataDirectory = info.cordovaDataDirectory;
            that.launchTimestamp = info.launchTimestamp;
            that.isTestLab = info.hasOwnProperty('isTestLab') ? true : false;
            that.buildVersion = parseInt(info.buildVersion, 10);
            channel.onCordovaAppVersionReady.fire();
        },function(e) {

        });
    });
};

NativeVarsPlugin.prototype.init = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "NativeVarsPlugin", "getNativeVars", []);
};

module.exports = new NativeVarsPlugin();
