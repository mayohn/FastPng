package com.suwell.fastpng;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 应用程序被禁用项判断，如：是否禁止在通知栏显示通知、是否禁用悬浮窗
 * DateTime:2016/6/15 23:17
 * Builder:Android Studio
 *
 * @see AppOpsManager
 */
public class AppOpsUtils {
    private static final String TAG = "AppOpsUtils";
    public static final int OP_NONE = -1;
    /**
     * Access to coarse location information.
     */
    public static final int OP_COARSE_LOCATION = 0;
    /**
     * Access to fine location information.
     */
    public static final int OP_FINE_LOCATION = 1;
    /**
     * Causing GPS to run.
     */
    public static final int OP_GPS = 2;
    public static final int OP_VIBRATE = 3;
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_WRITE_CONTACTS = 5;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_WRITE_CALL_LOG = 7;
    public static final int OP_READ_CALENDAR = 8;
    public static final int OP_WRITE_CALENDAR = 9;
    public static final int OP_WIFI_SCAN = 10;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_NEIGHBORING_CELLS = 12;
    public static final int OP_CALL_PHONE = 13;
    public static final int OP_READ_SMS = 14;
    public static final int OP_WRITE_SMS = 15;
    public static final int OP_RECEIVE_SMS = 16;
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    public static final int OP_RECEIVE_MMS = 18;
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_SEND_SMS = 20;
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_SETTINGS = 23;
    /**
     * Required to draw on top of other apps.
     */
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    public static final int OP_CAMERA = 26;
    public static final int OP_RECORD_AUDIO = 27;
    public static final int OP_PLAY_AUDIO = 28;
    public static final int OP_READ_CLIPBOARD = 29;
    public static final int OP_WRITE_CLIPBOARD = 30;
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    public static final int OP_AUDIO_RING_VOLUME = 35;
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int OP_WAKE_LOCK = 40;
    /**
     * Continually monitoring location data.
     */
    public static final int OP_MONITOR_LOCATION = 41;
    /**
     * Continually monitoring location data with a relatively high power request.
     */
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    /**
     * Retrieve current usage stats via .
     */
    public static final int OP_GET_USAGE_STATS = 43;
    public static final int OP_MUTE_MICROPHONE = 44;
    public static final int OP_TOAST_WINDOW = 45;
    /**
     * Capture the device's display contents and/or audio
     */
    public static final int OP_PROJECT_MEDIA = 46;
    /**
     * Activate a VPN connection without user intervention.
     */
    public static final int OP_ACTIVATE_VPN = 47;
    /**
     * Access the WallpaperManagerAPI to write wallpapers.
     */
    public static final int OP_WRITE_WALLPAPER = 48;
    /**
     * Received the assist structure from an app.
     */
    public static final int OP_ASSIST_STRUCTURE = 49;
    /**
     * Received a screenshot from assist.
     */
    public static final int OP_ASSIST_SCREENSHOT = 50;
    /**
     * Read the phone state.
     */
    public static final int OP_READ_PHONE_STATE = 51;
    /**
     * Add voicemail messages to the voicemail content provider.
     */
    public static final int OP_ADD_VOICEMAIL = 52;
    /**
     * Access APIs for SIP calling over VOIP or WiFi.
     */
    public static final int OP_USE_SIP = 53;
    /**
     * Intercept outgoing calls.
     */
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    /**
     * User the fingerprint API.
     */
    public static final int OP_USE_FINGERPRINT = 55;
    /**
     * Access to body sensors such as heart rate, etc.
     */
    public static final int OP_BODY_SENSORS = 56;
    /**
     * Read previously received cell broadcast messages.
     */
    public static final int OP_READ_CELL_BROADCASTS = 57;
    /**
     * Inject mock location into the system.
     */
    public static final int OP_MOCK_LOCATION = 58;
    /**
     * Read external storage.
     */
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    /**
     * Write external storage.
     */
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    /**
     * Turned on the screen.
     */
    public static final int OP_TURN_SCREEN_ON = 61;
    /**
     * Get device accounts.
     */
    public static final int OP_GET_ACCOUNTS = 62;
    /**
     * Control whether an application is allowed to run in the background.
     */
    public static final int OP_RUN_IN_BACKGROUND = 63;
    public static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    /**
     * Read the phone number.
     */
    public static final int OP_READ_PHONE_NUMBERS = 65;
    /**
     * Request package installs through package installer
     */
    public static final int OP_REQUEST_INSTALL_PACKAGES = 66;
    /**
     * Enter picture-in-picture.
     */
    public static final int OP_PICTURE_IN_PICTURE = 67;
    /**
     * Instant app start foreground service.
     */
    public static final int OP_INSTANT_APP_START_FOREGROUND = 68;
    /**
     * Answer incoming phone calls
     */
    public static final int OP_ANSWER_PHONE_CALLS = 69;
    /**
     * Run jobs when in background
     */
    public static final int OP_RUN_ANY_IN_BACKGROUND = 70;
    /**
     * Change Wi-Fi connectivity state
     */
    public static final int OP_CHANGE_WIFI_STATE = 71;
    /**
     * Request package deletion through package installer
     */
    public static final int OP_REQUEST_DELETE_PACKAGES = 72;
    /**
     * Bind an accessibility service.
     */
    public static final int OP_BIND_ACCESSIBILITY_SERVICE = 73;
    /**
     * Continue handover of a call from another app
     */
    public static final int OP_ACCEPT_HANDOVER = 74;
    /**
     * Create and Manage IPsec Tunnels
     */
    public static final int OP_MANAGE_IPSEC_TUNNELS = 75;
    /**
     * Any app start foreground service.
     */
    public static final int OP_START_FOREGROUND = 76;
    public static final int OP_BLUETOOTH_SCAN = 77;
    /**
     * Use the BiometricPrompt/BiometricManager APIs.
     */
    public static final int OP_USE_BIOMETRIC = 78;
    /**
     * Physical activity recognition.
     */
    public static final int OP_ACTIVITY_RECOGNITION = 79;
    /**
     * Financial app sms read.
     */
    public static final int OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    /**
     * Read media of audio type.
     */
    public static final int OP_READ_MEDIA_AUDIO = 81;
    /**
     * Write media of audio type.
     */
    public static final int OP_WRITE_MEDIA_AUDIO = 82;
    /**
     * Read media of video type.
     */
    public static final int OP_READ_MEDIA_VIDEO = 83;
    /**
     * Write media of video type.
     */
    public static final int OP_WRITE_MEDIA_VIDEO = 84;
    /**
     * Read media of image type.
     */
    public static final int OP_READ_MEDIA_IMAGES = 85;
    /**
     * Write media of image type.
     */
    public static final int OP_WRITE_MEDIA_IMAGES = 86;
    /**
     * Has a legacy (non-isolated) view of storage.
     */
    public static final int OP_LEGACY_STORAGE = 87;
    /**
     * Accessing accessibility features
     */
    public static final int OP_ACCESS_ACCESSIBILITY = 88;
    /**
     * Read the device identifiers (IMEI / MEID, IMSI, SIM / Build serial)
     */
    public static final int OP_READ_DEVICE_IDENTIFIERS = 89;
    public static final int _NUM_OP = 90;

    /**
     * 是否禁用通知
     */
    public static boolean allowNotification(Context context) {
        return isAllowed(context, OP_POST_NOTIFICATION);
    }

    /**
     * 是否禁用悬浮窗
     */
    public static boolean allowFloatWindow(Context context) {
        return isAllowed(context, OP_SYSTEM_ALERT_WINDOW);
    }

    /**
     * 是否禁用某项操作
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isAllowed(Context context, int op) {
        Log.d(TAG, "api level: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        Log.d(TAG, "op is " + op);
        String packageName = context.getApplicationContext().getPackageName();
        AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class<?>[] types = new Class[]{int.class, int.class, String.class};
        Object[] args = new Object[]{op, Binder.getCallingUid(), packageName};
        try {
            Method method = aom.getClass().getDeclaredMethod("checkOpNoThrow", types);
            Object mode = method.invoke(aom, args);
            Log.d(TAG, "invoke checkOpNoThrow: " + mode);
            if ((mode instanceof Integer) && ((Integer) mode == AppOpsManager.MODE_ALLOWED)) {
                Log.d(TAG, "allowed");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "invoke error: " + e);
            e.printStackTrace();
        }
        return false;
    }

}
