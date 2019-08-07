# React Native Console v1.3.1
一键执行React Native 命令的 IDEA/WebStorm/Android Studio 插件, 排名第一的 WebStorm / IDEA  React Native 开发插件

## 安装
首先, 请按照官方文档搭建 React Native开发环境:
https://facebook.github.io/react-native/docs/getting-started.html

然后, 可以在开发工具中安装插件, 依次点开  Preferences > Plugins > Browse repositories... , 搜索  '**React Native Console**', 然后点击安装即可.<br/>

或者<br/>手工下载插件文件 react-native-console.jar 并在IDE的插件设置中选择 Install Plugin from disk. 插件中心的页面地址在这里: https://plugins.jetbrains.com/plugin/9564-react-native-console<br/><br/>

重启开发工具即可享受秒速体验!


## 注意事项
需要 Java 8+版本运行开发工具.<br/>

## 设置(可选, 仅当js工程目录无法自动招到时配置)
当js项目的工作目录不在根目录时,例如 ./jsapp , 现在可以从欢迎页面 或者工具栏便捷的修改这个值. 最终的设置值存储于文件 .idea/.rnconsole, 示例内容如下:

```json
{
  currentPath: './jsapp'
}
```

设置之后所有的 npm 命令都会用这个目录作为启动目录<br>

## Linux 用户请注意

如果您点击 "Debug Android" 按钮后出现了如下错误信息: 
 "SDK location not found ", 可以这样修复 :
添加 android 本地配置文件:
应用目录/android/local.properties
`sdk.dir=/Users/xxxx/Documents/Java/android-sdk-macosx`
让 sdk.dir 指向 ANDROID_HOME 环境变量. 
如果找不到adb,可以试试如下终端命令(执行一次即可):

```sh
sudo ln -s ~/Android/Sdk/platform-tools/adb /usr/bin/adb
```

更多信息参考这个issue:
https://github.com/beansoftapp/react-native-console/issues/17

## 功能



一键运行下列功能:<br/>

 在项目中打开当前安卓 Activity 的源码(支持主要IDEA开发工具例如 WebStorm, IDEA, Android Studio 等).

运行 React Native Debugger https://github.com/jhen0409/react-native-debugger(仅支持Mac)列出并执行package.json中的scripts定义<br>

react-native run-android<br/>
react-native link<br/>
react-native run-ios<br/>
一键真机运行<br/>
列出所有iOS设备(包括模拟器和真机设备)并选中运行<br/>
npm run start<br/>
npm install<br/>
安卓设备上打开开发菜单(adb shell input keyevent 82)<br/>
安卓设备网络请求转发到开发机(adb reverse tcp:8081 tcp:8081)<br/>
安卓设备重新载入JavaScript<br/>
打开 React Native debugger ui(需要Chrome浏览器)<br/><br/>
react-native log-android<br/>
react-native log-ios<br/>
gradlew assembleRelease<br/>
react-native bundle --platform android/ios --dev false<br/>
yarn<br/>
jest<br/>
react-native uninstall<br/>
react-native start<br/>
gradlew clean<br/>
react-native react-native-git-upgrade<br/>
yarn 添加项目, 开发和全局依赖<br/>
npm 添加项目, 开发和全局依赖<br/>
自动安装运行 react-devtools<br/>

最近编辑位置(主工具栏)<br/>
在Finder/Explorer中显示文件(main toolbar)<br/>
在Finder/Explorer中显示项目目录<br/>
指定JS项目工作目录, 例如 ./jsapp, 通过欢迎屏幕或者工具栏按钮 (**可选**, 设置后在.idea目录创建文件 `.rnconsole` , 内容和下面类似:

```json
{
  currentPath: './jsapp'
}
```

设置之后所有的 npm 命令都会用这个目录作为启动目录<br/>
<br/>

最近更新: 指定JS项目工作目录, 例如 ./jsapp, 可通过欢迎屏幕或者工具栏按钮来完成.<br/>

自动在当前目录和父级目录检测 React Native的package.json文件位置(比如在Android Studio中只开发Java代码时), 这样所有的npm相关的命令都会自动在正确的目录执行<br/>



##贡献者

https://github.com/facebook/react-native/commit/33d710e8c58ef1dc69816a59ac1cf390894e7cb9


https://github.com/beansoftapp beansoft@126.com

https://github.com/troublediehard dmportenko@gmail.com

## 屏幕快照

![](https://plugins.jetbrains.com/files/9564/screenshot_17784.png)

## 演示动图
![](https://raw.githubusercontent.com/beansoftapp/react-native-console/master/screenshot/rnconsole.gif)


