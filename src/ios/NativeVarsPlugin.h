// Copyright (c) 2017 Kano Applications Inc. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#import <Cordova/CDV.h>

@interface NativeVarsPlugin : CDVPlugin <UIWebViewDelegate>
{
}

- (void)getNativeVars:(CDVInvokedUrlCommand*)command;

@end
