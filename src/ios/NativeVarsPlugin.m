// Copyright (c) 2017 Kano Applications Inc. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#import "NativeVarsPlugin.h"
#import <Cordova/CDV.h>

@implementation NativeVarsPlugin

- (void)getNativeVars:(CDVInvokedUrlCommand*)command
{
    NSDictionary *infoDict = [[NSBundle mainBundle] infoDictionary];
    NSString *appVersion = [infoDict objectForKey:@"CFBundleShortVersionString"];
    NSNumber *buildVersion = [infoDict objectForKey:@"CFBundleVersion"];
    NSString *launchTimestamp = [NSString stringWithFormat:@"%f",[[NSDate date] timeIntervalSince1970] * 1000];

    //get old version
    NSString* appVersionOldReference = @"CFBundleShortVersionStringOld";
    NSString* appVersionOld = [[NSUserDefaults standardUserDefaults] stringForKey:appVersionOldReference] ?: @"";

    //save old version string
    if (![appVersion isEqualToString: appVersionOld])
    {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject: appVersion forKey: appVersionOldReference];
        [defaults synchronize];
    }

    //get data directory
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *cordovaDataDirectory = @"file://";
    cordovaDataDirectory = [cordovaDataDirectory stringByAppendingString:[paths objectAtIndex:0]];
    cordovaDataDirectory = [cordovaDataDirectory stringByAppendingString:@"/NoCloud/"];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                     messageAsDictionary: @{@"appVersion": appVersion,
                                                                            @"appVersionOld": appVersionOld,
                                                                            @"buildVersion": buildVersion,
                                                                            @"launchTimestamp": launchTimestamp,
                                                                            @"cordovaDataDirectory": cordovaDataDirectory}];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end