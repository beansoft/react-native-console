# React Native Console v1.3.1
An IDEA/WebStorm/Android Studio Plugin for One-Click run React Native command, the No.1 WebStorm / IDEA plugin for React Native developers.



[Chinese Instruction Here](README_CN.m)

## Installation
First, please setup your React Native dev env:
https://facebook.github.io/react-native/docs/getting-started.html

Second, you can install it through your IDE, bring up  Preferences > Plugins > Browse repositories... , search for '**React Native Console**',
then you can install this plugin there.<br/>

Or<br/>you can munally install jar file react-native-console.jar as a plugin to your IDE. And the plugin home page is here: https://plugins.jetbrains.com/plugin/9564-react-native-console<br/><br/>

Now restart IDEA and enjoy!


## Note
Java 8+ required to run the IDE.<br/>

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
list and run scripts in package.json<br>One-Click run following commands:<br/>

run React Native Debugger https://github.com/jhen0409/react-native-debugger(Mac Only)<br>Open Current Activity source code in project(supports WebStorm, IDEA, Android Studio etc).<br>Ctrl + F2 to reload android js bundle<br>list and run scripts in package.json<br>react-native run-android<br/>react-native link<br/>react-native run-ios<br/>run with physical iOS device<br/>list all iOS devices(include simulator and physical) and run target one <br/>npm run start<br/>npm install<br/>Open dev menu on Android device(adb shell input keyevent 82)<br/>forward android device request to dev machine(adb reverse tcp:8081 tcp:8081)<br/>Android Reloading JavaScript<br/>open React Native debugger ui(Chrome browser required)<br/>react-native log-android<br/>react-native log-ios<br/>gradlew assembleRelease<br/>react-native bundle --platform android/ios --dev false<br/>yarn<br/>jest<br/>react-native uninstall<br/>react-native start<br/>gradlew clean<br/>react-native react-native-git-upgrade<br/>yarn add project, dev, global dependencies<br/>npm add project, dev, global dependencies<br/>auto install and run react-devtools<br/>Last Edit Location(main toolbar)<br/>Reveal file in Finder/Explorer(main toolbar)<br/>Reveal project folder in Finder/Explorer<br/><br/>
New: one-click rerun commands, reuse console window, tabs with title and icon, search in console output.<br/>
Auto detect React Native package.json or build.gradle in current folder and parent/children folder(eg only coding Java in Android Studio),
thus the command will auto execute in the correct folder<br/>



##Contributors

https://github.com/facebook/react-native/commit/33d710e8c58ef1dc69816a59ac1cf390894e7cb9


https://github.com/beansoftapp beansoft@126.com

https://github.com/troublediehard dmportenko@gmail.com

## ScreenShot

![](https://plugins.jetbrains.com/files/9564/screenshot_17784.png)

## Demo Gif
![](https://raw.githubusercontent.com/beansoftapp/react-native-console/master/screenshot/rnconsole.gif)


This project is powered by JetBrains IDE.

[![jetbrains](jetbrains.png)](https://www.jetbrains.com/?from=ReactNativeConsole)