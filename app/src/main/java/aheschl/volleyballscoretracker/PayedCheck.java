package aheschl.volleyballscoretracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

class PayedCheck {
    static boolean installedPayedVersion(Context context){
        final String appPackageName = "aheschl.VolleyballScorekeeperPro";

        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationInfo(appPackageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    static void openProKey(Context context){
        final String appPackageName = "aheschl.VolleyballScorekeeperPro";
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException error) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


}
