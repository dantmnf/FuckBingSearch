package xyz.cirno.fuckbingsearch;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MouleEntry implements IXposedHookLoadPackage  {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!"com.microsoft.office.outlook".equals(lpparam.packageName)) return;
        XposedHelpers.findAndHookMethod("com.microsoft.office.outlook.boothandlers.BingAppSessionFirstActivityPostResumedEventHandler",
                lpparam.classLoader, "onAppFirstActivityPostResumed", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = (Context)XposedHelpers.getObjectField(param.thisObject, "mContext");
                        PackageManager pm = context.getPackageManager();
                        XposedBridge.log("FuckBingSearch: disabling SearchBridgeActivity");
                        pm.setComponentEnabledSetting(new ComponentName(context, "com.microsoft.emmx.webview.search.SearchBridgeActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        return null;
                    }
                });
        XposedHelpers.findAndHookMethod("com.microsoft.office.outlook.edgeintegration.BingIntentFilterEnabler",
                lpparam.classLoader, "updateBingSearchInTextSelectionMenu", Context.class, boolean.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });
    }
}
