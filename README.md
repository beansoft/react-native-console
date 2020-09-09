# React Native Console

An IDEA/WebStorm/Android Studio Plugin for run React Native commands, the No.1 WebStorm / IDEA plugin for React Native developers.

https://plugins.jetbrains.com/plugin/9564-react-native-console 

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/9564-react-native-console.svg)](https://plugins.jetbrains.com/plugin/9564-react-native-console)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/9564-react-native-console.svg)](https://plugins.jetbrains.com/plugin/9564-react-native-console) ![Rating](https://img.shields.io/jetbrains/plugin/r/rating/9564)  ![Vistor](https://visitor-badge.glitch.me/badge?page_id=react-native-console)

# React Native Console Free
This repo holds the source code of free version. 
https://plugins.jetbrains.com/plugin/15016-react-native-console-free

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/15016-react-native-console-free.svg)](https://plugins.jetbrains.com/plugin/15016-react-native-console-free)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15016-react-native-console-free)](https://plugins.jetbrains.com/plugin/15016-react-native-console-free) ![Rating](https://img.shields.io/jetbrains/plugin/r/rating/15016-react-native-console-free) 

[Chinese Instruction Here](README_CN.m)

## Installation
First, please setup your React Native dev env:
https://facebook.github.io/react-native/docs/getting-started.html

Second, you can install it through your IDE, bring up  Preferences > Plugins > Browse repositories... , search for '**React Native Console**',
then you can install this plugin there.<br/>

Or
you can munally install jar file react-native-console.jar as a plugin to your IDE. And the plugin home page is here: https://plugins.jetbrains.com/plugin/9564-react-native-console<br/><br/>

Now restart IDEA and enjoy!


## Note
Java 8 or plus required to run the IDE.<br/>

## Settings(Optional, only need if js project dir can't be auto find)
Specify js project work directory rather than root directory, eg ./jsapp , now can from welcome screen or toolbar to edit this property. And the final value is stored in a file named .idea/.rnconsole, which content should like this:

```json
{
  currentPath: './jsapp'
}
```

after that all npm commands will use this as the working dir.

## Linux Users PLEASE README FIRST

If you found issue when click on the "Debug Android" button, error message: 
 "SDK location not found ", please fix it like this :
add a android local config file:
yourapp/android/local.properties
`sdk.dir=/Users/xxxx/Documents/Java/android-sdk-macosx`
let sdk.dir point to your ANDROID_HOME environment 
if can't find adb, try this shell command:

```sh
sudo ln -s ~/Android/Sdk/platform-tools/adb /usr/bin/adb
```


More info please ref this issue:
https://github.com/beansoftapp/react-native-console/issues/17


## Features
- open current Android activity source code in project
- run React Native Debugger https://github.com/jhen0409/react-native-debugger(Mac Only)
- Ctrl + F2 to reload android js bundle
- list and run scripts in package.json
- react-native run-android
- list all connected android devices(includes simulator and physical) and run as target
- react-native link
- react-native run-ios
- run on physical iOS device
- list all iOS devices(includes simulator and physical) and run as target
- npm run start
- npm install
- Open dev menu on Android device(adb shell input keyevent 82)
- forward android device request to dev machine(adb reverse tcp:8081 tcp:8081)
- Android Reloading JavaScript
- open React Native debugger ui(Chrome browser required)
- react-native log-android
- react-native log-ios
- gradlew assembleRelease
- react-native bundle --platform android/ios --dev false
- yarn
- jest
- react-native uninstall
- react-native start
- gradlew clean
- react-native react-native-git-upgrade
- yarn add project, dev, global dependencies
- npm add project, dev, global dependencies
- auto install and run react-devtools
- Last Edit Location(main toolbar)
- Reveal file in Finder/Explorer(main toolbar)
- Reveal project folder in Finder/Explorer
- Specify js project work directory, eg ./jsapp, from welcome screen or toolbar (optional, see https://github.com/beansoftapp/react-native-console for more info)
- Modify Metro Bundler port(need React Native 0.56+)
- Auto detect React Native package.json or build.gradle in current folder and parent/children folder(eg only coding Java in Android Studio), thus the command will auto execute in the correct folder



##Contributors

https://github.com/facebook/react-native/commit/33d710e8c58ef1dc69816a59ac1cf390894e7cb9


https://github.com/beansoftapp beansoft@126.com

https://github.com/troublediehard dmportenko@gmail.com

## ScreenShot

![](https://plugins.jetbrains.com/files/9564/screenshot_17784.png)

## Demo Gif
![](https://raw.githubusercontent.com/beansoftapp/react-native-console/master/screenshot/rnconsole.gif)


This project is developed with free [JetBrains.](https://www.jetbrains.com/?from=ReactNativeConsole) Open Source license(s).
 
[![jetbrains](jetbrains.png)](https://www.jetbrains.com/?from=ReactNativeConsole)
