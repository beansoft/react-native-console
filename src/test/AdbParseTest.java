import com.github.pedrovgs.androidwifiadb.adb.ADB;
import com.github.pedrovgs.androidwifiadb.adb.ADBParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbParseTest {
  private static final String PATTERN_RUN_ACTIVITY = "Run #0";
  private static final String PATTERN_FOCUSED_ACTIVITY = "mFocusedActivity";
  private static final Pattern PATTERN_ACTIVITY_NAME = Pattern.compile(".* ([a-zA-Z0-9.]+)/([a-zA-Z0-9.]+).*");
  private static final String PATTERN_MULTIPLE_DEVICE = "more than one device";
  private static final String PATTERN_DEVICE_LIST_HEADER = "List";
  private static final Pattern PATTERN_DEVICE_LIST_ITEM = Pattern.compile("(.+)\\p{Space}+(.+)");
  private static final String PATTERN_DEVICE_NOT_FOUND = "device not found";

  @Test
  public void testParseDump() throws IOException {
    String dumpAndroid9 = "ACTIVITY MANAGER ACTIVITIES (dumpsys activity activities)\n" +
        "Display #0 (activities from top to bottom):\n" +
        "\n" +
        "  Stack #1: type=standard mode=fullscreen\n" +
        "  isSleeping=false\n" +
        "  mBounds=Rect(0, 0 - 0, 0)\n" +
        "    Task id #6\n" +
        "    mBounds=Rect(0, 0 - 0, 0)\n" +
        "    mMinWidth=-1\n" +
        "    mMinHeight=-1\n" +
        "    mLastNonFullscreenBounds=null\n" +
        "    * TaskRecord{1e361c5 #6 A=cn.pilipa.custapp U=0 StackId=1 sz=1}\n" +
        "      userId=0 effectiveUid=u0a70 mCallingUid=u0a26 mUserSetupComplete=true mCallingPackage=com.android.launcher3\n" +
        "      affinity=cn.pilipa.custapp\n" +
        "      intent={act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10200000 cmp=cn.pilipa.custapp/.MainActivity}\n" +
        "      realActivity=cn.pilipa.custapp/.MainActivity\n" +
        "      autoRemoveRecents=false isPersistable=true numFullscreen=1 activityType=1\n" +
        "      rootWasReset=true mNeverRelinquishIdentity=true mReuseTask=false mLockTaskAuth=LOCK_TASK_AUTH_PINNABLE\n" +
        "      Activities=[ActivityRecord{a8d5302 u0 cn.pilipa.custapp/.MainActivity t6}]\n" +
        "      askedCompatMode=false inRecents=true isAvailable=true\n" +
        "      mRootProcess=ProcessRecord{39bf6a 3043:cn.pilipa.custapp/u0a70}\n" +
        "      stackId=1\n" +
        "      hasBeenVisible=true mResizeMode=RESIZE_MODE_RESIZEABLE mSupportsPictureInPicture=false isResizeable=true lastActiveTime=330259 (inactive for 429s)\n" +
        "      * Hist #0: ActivityRecord{a8d5302 u0 cn.pilipa.custapp/.MainActivity t6}\n" +
        "          packageName=cn.pilipa.custapp processName=cn.pilipa.custapp\n" +
        "          launchedFromUid=10026 launchedFromPackage=com.android.launcher3 userId=0\n" +
        "          app=ProcessRecord{39bf6a 3043:cn.pilipa.custapp/u0a70}\n" +
        "          Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10200000 cmp=cn.pilipa.custapp/.MainActivity bnds=[843,1072][1045,1354] }\n" +
        "          frontOfTask=true task=TaskRecord{1e361c5 #6 A=cn.pilipa.custapp U=0 StackId=1 sz=1}\n" +
        "          taskAffinity=cn.pilipa.custapp\n" +
        "          realActivity=cn.pilipa.custapp/.MainActivity\n" +
        "          baseDir=/data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/base.apk\n" +
        "          dataDir=/data/user/0/cn.pilipa.custapp\n" +
        "          splitDir=[/data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_dependencies_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_resources_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_0_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_1_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_2_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_3_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_4_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_5_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_6_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_7_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_8_apk.apk, /data/app/cn.pilipa.custapp-rJI3v9-vbwgznhZ-swLimg==/split_lib_slice_9_apk.apk]\n" +
        "          stateNotNeeded=false componentSpecified=true mActivityType=standard\n" +
        "          compat={420dpi} labelRes=0x7f0d0029 icon=0x7f0c0002 theme=0x7f0e0007\n" +
        "          mLastReportedConfigurations:\n" +
        "           mGlobalConfig={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 0, 0) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=undefined} s.6}\n" +
        "           mOverrideConfig={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 1080, 1794) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=standard} s.6}\n" +
        "          CurrentConfiguration={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 1080, 1794) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=standard} s.6}\n" +
        "          taskDescription: label=\"null\" icon=null iconResource=0 iconFilename=null primaryColor=fff5f5f5\n" +
        "           backgroundColor=fffafafa\n" +
        "           statusBarColor=ff757575\n" +
        "           navigationBarColor=ff000000\n" +
        "          launchFailed=false launchCount=1 lastLaunchTime=-7m9s369ms\n" +
        "          haveState=false icicle=null\n" +
        "          state=RESUMED stopped=false delayedResume=false finishing=false\n" +
        "          keysPaused=false inHistory=true visible=true sleeping=false idle=true mStartingWindowState=STARTING_WINDOW_SHOWN\n" +
        "          fullscreen=true noDisplay=false immersive=false launchMode=0\n" +
        "          frozenBeforeDestroy=false forceNewConfig=false\n" +
        "          mActivityType=standard\n" +
        "          waitingVisible=false nowVisible=true lastVisibleTime=-6m55s648ms\n" +
        "          resizeMode=RESIZE_MODE_RESIZEABLE\n" +
        "          mLastReportedMultiWindowMode=false mLastReportedPictureInPictureMode=false\n" +
        "\n" +
        "    Running activities (most recent first):\n" +
        "      TaskRecord{1e361c5 #6 A=cn.pilipa.custapp U=0 StackId=1 sz=1}\n" +
        "        Run #0: ActivityRecord{a8d5302 u0 cn.pilipa.custapp/.MainActivity t6}\n" +
        "\n" +
        "    mResumedActivity: ActivityRecord{a8d5302 u0 cn.pilipa.custapp/.MainActivity t6}\n" +
        "\n" +
        "  Stack #0: type=home mode=fullscreen\n" +
        "  isSleeping=false\n" +
        "  mBounds=Rect(0, 0 - 0, 0)\n" +
        "\n" +
        "    Task id #5\n" +
        "    mBounds=Rect(0, 0 - 0, 0)\n" +
        "    mMinWidth=-1\n" +
        "    mMinHeight=-1\n" +
        "    mLastNonFullscreenBounds=null\n" +
        "    * TaskRecord{8f8d71a #5 I=com.android.launcher3/.Launcher U=0 StackId=0 sz=1}\n" +
        "      userId=0 effectiveUid=u0a26 mCallingUid=0 mUserSetupComplete=true mCallingPackage=null\n" +
        "      intent={act=android.intent.action.MAIN cat=[android.intent.category.HOME] flg=0x10000100 cmp=com.android.launcher3/.Launcher}\n" +
        "      realActivity=com.android.launcher3/.Launcher\n" +
        "      autoRemoveRecents=false isPersistable=true numFullscreen=1 activityType=2\n" +
        "      rootWasReset=false mNeverRelinquishIdentity=true mReuseTask=false mLockTaskAuth=LOCK_TASK_AUTH_PINNABLE\n" +
        "      Activities=[ActivityRecord{1362e31 u0 com.android.launcher3/.Launcher t5}]\n" +
        "      askedCompatMode=false inRecents=true isAvailable=true\n" +
        "      mRootProcess=ProcessRecord{b99b24b 2646:com.android.launcher3/u0a26}\n" +
        "      stackId=0\n" +
        "      hasBeenVisible=true mResizeMode=RESIZE_MODE_RESIZEABLE mSupportsPictureInPicture=false isResizeable=true lastActiveTime=329320 (inactive for 430s)\n" +
        "      * Hist #0: ActivityRecord{1362e31 u0 com.android.launcher3/.Launcher t5}\n" +
        "          packageName=com.android.launcher3 processName=com.android.launcher3\n" +
        "          launchedFromUid=0 launchedFromPackage=null userId=0\n" +
        "          app=ProcessRecord{b99b24b 2646:com.android.launcher3/u0a26}\n" +
        "          Intent { act=android.intent.action.MAIN cat=[android.intent.category.HOME] flg=0x10000100 cmp=com.android.launcher3/.Launcher }\n" +
        "          frontOfTask=true task=TaskRecord{8f8d71a #5 I=com.android.launcher3/.Launcher U=0 StackId=0 sz=1}\n" +
        "          taskAffinity=null\n" +
        "          realActivity=com.android.launcher3/.Launcher\n" +
        "          baseDir=/system/priv-app/Launcher3QuickStep/Launcher3QuickStep.apk\n" +
        "          dataDir=/data/user/0/com.android.launcher3\n" +
        "          stateNotNeeded=true componentSpecified=false mActivityType=home\n" +
        "          compat={420dpi} labelRes=0x7f110035 icon=0x7f08001b theme=0x7f120002\n" +
        "          mLastReportedConfigurations:\n" +
        "           mGlobalConfig={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 0, 0) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=undefined} s.6}\n" +
        "           mOverrideConfig={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 1080, 1794) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=home} s.6}\n" +
        "          CurrentConfiguration={1.0 310mcc270mnc [en_US] ldltr sw411dp w411dp h659dp 420dpi nrml port finger qwerty/v/v dpad/v winConfig={ mBounds=Rect(0, 0 - 1080, 1794) mAppBounds=Rect(0, 0 - 1080, 1794) mWindowingMode=fullscreen mActivityType=home} s.6}\n" +
        "          OverrideConfiguration={0.0 ?mcc?mnc ?localeList ?layoutDir ?swdp ?wdp ?hdp ?density ?lsize ?long ?ldr ?wideColorGamut ?orien ?uimode ?night ?touch ?keyb/?/? ?nav/? winConfig={ mBounds=Rect(0, 0 - 0, 0) mAppBounds=null mWindowingMode=undefined mActivityType=home}}\n" +
        "          taskDescription: label=\"null\" icon=null iconResource=0 iconFilename=null primaryColor=fff5f5f5\n" +
        "           backgroundColor=fffafafa\n" +
        "           statusBarColor=0\n" +
        "           navigationBarColor=0\n" +
        "          launchFailed=false launchCount=0 lastLaunchTime=-8m50s527ms\n" +
        "          haveState=true icicle=Bundle[mParcelledData.dataSize=3740]\n" +
        "          state=STOPPED stopped=true delayedResume=false finishing=false\n" +
        "          keysPaused=false inHistory=true visible=false sleeping=false idle=true mStartingWindowState=STARTING_WINDOW_NOT_SHOWN\n" +
        "          fullscreen=true noDisplay=false immersive=false launchMode=2\n" +
        "          frozenBeforeDestroy=false forceNewConfig=false\n" +
        "          mActivityType=home\n" +
        "          waitingVisible=false nowVisible=false lastVisibleTime=-8m48s235ms\n" +
        "          resizeMode=RESIZE_MODE_RESIZEABLE\n" +
        "          mLastReportedMultiWindowMode=false mLastReportedPictureInPictureMode=false\n" +
        "\n" +
        "    Running activities (most recent first):\n" +
        "      TaskRecord{8f8d71a #5 I=com.android.launcher3/.Launcher U=0 StackId=0 sz=1}\n" +
        "        Run #0: ActivityRecord{1362e31 u0 com.android.launcher3/.Launcher t5}\n" +
        "\n" +
        "    mLastPausedActivity: ActivityRecord{1362e31 u0 com.android.launcher3/.Launcher t5}\n" +
        "\n" +
        "  ResumedActivity: ActivityRecord{a8d5302 u0 cn.pilipa.custapp/.MainActivity t6}\n" +
        "  mFocusedStack=ActivityStack{5c5f428 stackId=1 type=standard mode=fullscreen visible=true translucent=false, 1 tasks} mLastFocusedStack=ActivityStack{5c5f428 stackId=1 type=standard mode=fullscreen visible=true translucent=false, 1 tasks}\n" +
        "  mCurTaskIdForUser={0=6}\n" +
        "  mUserStackInFront={}\n" +
        "  displayId=0 stacks=2\n" +
        "   mHomeStack=ActivityStack{49d5541 stackId=0 type=home mode=fullscreen visible=false translucent=true, 1 tasks}\n" +
        "  isHomeRecentsComponent=true  KeyguardController:\n" +
        "    mKeyguardShowing=false\n" +
        "    mAodShowing=false\n" +
        "    mKeyguardGoingAway=false\n" +
        "    mOccluded=false\n" +
        "    mDismissingKeyguardActivity=null\n" +
        "    mDismissalRequested=false\n" +
        "    mVisibilityTransactionDepth=0\n" +
        "  LockTaskController\n" +
        "    mLockTaskModeState=NONE\n" +
        "    mLockTaskModeTasks=\n" +
        "    mLockTaskPackages (userId:packages)=\n" +
        "      u0:[]";

    BufferedReader in = new BufferedReader(new StringReader(dumpAndroid9));

    String line;
    while ((line = in.readLine()) != null) {
      line = line.trim();
//      System.out.println("line='" + line + "'");

      if (line.contains(PATTERN_FOCUSED_ACTIVITY)) {
        Matcher matcher = PATTERN_ACTIVITY_NAME.matcher(line);
        if (!matcher.matches()) {
          System.out.println("Could not find the focused Activity in the line");

        }
        System.out.println( "Activity name:" +  matcher.group(2) );
      }

      if (line.contains(PATTERN_RUN_ACTIVITY)) {
        Matcher matcher = PATTERN_ACTIVITY_NAME.matcher(line);
        if (!matcher.matches()) {
          System.out.println("Could not find the focused Activity in the line");

        }
        System.out.println( "Activity name:" + matcher.group(1) + matcher.group(2) );

        int cnt = matcher.groupCount();
        System.out.println(cnt);
        for(int i = 1; i <= cnt; i++) {
          System.out.println(matcher.group(i));
        }
        break;
      }
    }
  }

  @Test
  public void testADBGetDevicesConnectedByUSB() {
    ADB adb = new ADB();
    System.out.println(adb.getDevicesConnectedByUSB());
  }

  @Test
  public void testParseAndroid() {
    String s = "List of devices attached\n" +
        "192.168.56.103:5555    device product:vbox86p model:Google_Nexus_5 device:vbox86p transport_id:1";
    ADBParser adb = new ADBParser();
    System.out.println(adb.parseGetDevicesOutput(s));
  }


}