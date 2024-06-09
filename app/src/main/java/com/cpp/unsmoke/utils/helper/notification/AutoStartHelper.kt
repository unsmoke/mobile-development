import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.os.Build
import com.cpp.unsmoke.ui.main.MainActivity

class AutoStartHelper private constructor() {

    private val BRAND_XIAOMI = "xiaomi"
    private val PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter"
    private val PACKAGE_XIAOMI_COMPONENT = "com.miui.permcenter.autostart.AutoStartManagementActivity"

    private val BRAND_LETV = "letv"
    private val PACKAGE_LETV_MAIN = "com.letv.android.letvsafe"
    private val PACKAGE_LETV_COMPONENT = "com.letv.android.letvsafe.AutobootManageActivity"

    private val BRAND_ASUS = "asus"
    private val PACKAGE_ASUS_MAIN = "com.asus.mobilemanager"
    private val PACKAGE_ASUS_COMPONENT = "com.asus.mobilemanager.powersaver.PowerSaverSettings"

    private val BRAND_HONOR = "honor"
    private val PACKAGE_HONOR_MAIN = "com.huawei.systemmanager"
    private val PACKAGE_HONOR_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity"

    private val BRAND_OPPO = "oppo"
    private val PACKAGE_OPPO_MAIN = "com.coloros.safecenter"
    private val PACKAGE_OPPO_FALLBACK = "com.oppo.safe"
    private val PACKAGE_OPPO_COMPONENT = "com.coloros.safecenter.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK = "com.oppo.safe.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK_A = "com.coloros.safecenter.startupapp.StartupAppListActivity"

    private val BRAND_VIVO = "vivo"
    private val PACKAGE_VIVO_MAIN = "com.iqoo.secure"
    private val PACKAGE_VIVO_FALLBACK = "com.vivo.permissionmanager"
    private val PACKAGE_VIVO_COMPONENT = "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK = "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK_A = "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"

    private val BRAND_NOKIA = "nokia"
    private val PACKAGE_NOKIA_MAIN = "com.evenwell.powersaving.g3"
    private val PACKAGE_NOKIA_COMPONENT = "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity"

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    companion object {
        fun getInstance(): AutoStartHelper {
            return AutoStartHelper()
        }
    }

    fun getAutoStartPermission(context: Context) {
        sharedPreferences = context.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        val buildInfo = Build.BRAND.lowercase()
        when (buildInfo) {
            BRAND_XIAOMI -> autoStartXiaomi(context)
            BRAND_LETV -> autoStartLetv(context)
            BRAND_ASUS -> autoStartAsus(context)
            BRAND_HONOR -> autoStartHonor(context)
            BRAND_OPPO -> autoStartOppo(context)
            BRAND_VIVO -> autoStartVivo(context)
            BRAND_NOKIA -> autoStartNokia(context)
        }
    }

    private fun autoStartAsus(context: Context) {
        if (isPackageExists(context, PACKAGE_ASUS_MAIN)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_ASUS_MAIN, PACKAGE_ASUS_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartXiaomi(context: Context) {
        if (isPackageExists(context, PACKAGE_XIAOMI_MAIN)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_XIAOMI_MAIN, PACKAGE_XIAOMI_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartLetv(context: Context) {
        if (isPackageExists(context, PACKAGE_LETV_MAIN)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_LETV_MAIN, PACKAGE_LETV_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartHonor(context: Context) {
        if (isPackageExists(context, PACKAGE_HONOR_MAIN)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_HONOR_MAIN, PACKAGE_HONOR_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartOppo(context: Context) {
        if (isPackageExists(context, PACKAGE_OPPO_MAIN) || isPackageExists(context, PACKAGE_OPPO_FALLBACK)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        startIntent(context, PACKAGE_OPPO_FALLBACK, PACKAGE_OPPO_COMPONENT_FALLBACK)
                        editor?.putString("autoStart", "yes")?.apply()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        try {
                            startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT_FALLBACK_A)
                            editor?.putString("autoStart", "yes")?.apply()
                        } catch (exx: Exception) {
                            exx.printStackTrace()
                        }
                    }
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartVivo(context: Context) {
        if (isPackageExists(context, PACKAGE_VIVO_MAIN) || isPackageExists(context, PACKAGE_VIVO_FALLBACK)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        startIntent(context, PACKAGE_VIVO_FALLBACK, PACKAGE_VIVO_COMPONENT_FALLBACK)
                        editor?.putString("autoStart", "yes")?.apply()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        try {
                            startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT_FALLBACK_A)
                            editor?.putString("autoStart", "yes")?.apply()
                        } catch (exx: Exception) {
                            exx.printStackTrace()
                        }
                    }
                }
                dialog.dismiss()
            })
        }
    }

    private fun autoStartNokia(context: Context) {
        if (isPackageExists(context, PACKAGE_NOKIA_MAIN)) {
            showAlert(context, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    startIntent(context, PACKAGE_NOKIA_MAIN, PACKAGE_NOKIA_COMPONENT)
                    editor?.putString("autoStart", "yes")?.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }
    }

    private fun showAlert(context: Context, onClickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
            .setTitle("Allow AutoStart")
            .setMessage("Please enable auto start in settings to receive push notification.")
            .setPositiveButton("Allow", onClickListener)
            .setCancelable(false)
            .show()
    }

    private fun startIntent(context: Context, packageName: String, componentName: String) {
        try {
            val intent = Intent().apply {
                component = ComponentName(packageName, componentName)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun isPackageExists(context: Context, targetPackage: String): Boolean {
        val packages: List<ApplicationInfo> = context.packageManager.getInstalledApplications(0)
        return packages.any { it.packageName == targetPackage }
    }
}
