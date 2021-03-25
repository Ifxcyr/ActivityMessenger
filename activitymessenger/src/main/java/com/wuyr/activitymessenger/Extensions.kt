@file:Suppress("DEPRECATION", "unused", "UNCHECKED_CAST")

package com.wuyr.activitymessenger

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.wuyr.activitymessenger.ActivityMessenger.startActivity
import com.wuyr.activitymessenger.IntentFieldMethod.internalMap
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author wuyr
 * @github https://github.com/wuyr/ActivityMessenger
 * @since 2020-04-28 下午2:08
 */

/**
 * [Intent]的扩展方法，此方法可无视类型直接获取到对应值
 * 如getStringExtra()、getIntExtra()、getSerializableExtra()等方法通通不用
 * 可以直接通过此方法来获取到对应的值，例如：
 * <pre>
 *     var mData: List<String>? = null
 *     mData = intent.get("Data")
 * </pre>
 * 而不用显式强制转型
 *
 * @param key 对应的Key
 * @return 对应的Value
 */
fun <O> Intent?.get(key: String, defaultValue: O? = null) =
    this?.internalMap()?.get(key) as? O ?: defaultValue

/**
 * 作用同Intent.[get]
 */
fun <O> Bundle?.get(key: String, defaultValue: O? = null) =
    this?.internalMap()?.get(key) as? O ?: defaultValue

/**
 *  [Intent]的扩展方法，用来批量put键值对
 *  示例：
 *  <pre>
 *      intent.putExtras(
 *          "Key1" to "Value",
 *          "Key2" to 123,
 *          "Key3" to false,
 *          "Key4" to arrayOf("4", "5", "6")
 *      )
 * </pre>
 *
 * @param params 键值对
 */
fun <T> Intent.putExtras(vararg params: Pair<String, T>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}

/**
 *  作用同[Activity.startActivity]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivity<TestActivity>()
 *
 *      //携带参数（可连续多个键值对）
 *      startActivity<TestActivity>("Key" to "Value")
 *  </pre>
 *
 * @param TARGET 要启动的Activity
 * @param params extras键值对
 */
inline fun <reified TARGET : Activity> Activity.startActivity(
    vararg params: Pair<String, Any?>
) = startActivity(Intent(this, TARGET::class.java).putExtras(*params))

inline fun <reified TARGET : Activity> Fragment.startActivity(
    vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

inline fun <reified TARGET : Activity> androidx.fragment.app.Fragment.startActivity(
    vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

/**
 *  Fragment跳转，同[Activity.startActivity]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivity(this, TestActivity::class)
 *
 *      //携带参数（可连续多个键值对）
 *     startActivity(
 *         TestActivity::class,
 *         "Key1" to "Value",
 *         "Key2" to 123
 *     )
 *  </pre>
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 */
fun Activity.startActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>
) = startActivity(Intent(this, target.java).putExtras(*params))

fun Fragment.startActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target.java).putExtras(*params))
}

fun androidx.fragment.app.Fragment.startActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target.java).putExtras(*params))
}

/**
 * 作用同上，以下三个方法为了兼容Java Class
 */
fun Activity.startActivity(
    target: Class<out Activity>, vararg params: Pair<String, Any?>
) = startActivity(Intent(this, target).putExtras(*params))

fun Fragment.startActivity(
    target: Class<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target).putExtras(*params))
}

fun androidx.fragment.app.Fragment.startActivity(
    target: Class<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target).putExtras(*params))
}

/**
 *  作用同[Activity.startActivityForResult]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivityForResult<TestActivity> {
 *          if (it == null) {
 *              //未成功处理，即（ResultCode != RESULT_OK）
 *          } else {
 *              //处理成功，这里可以操作返回的intent
 *          }
 *      }
 *  </pre>
 *  携带参数同[startActivity]
 *
 * @param TARGET 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun <reified TARGET : Activity> Activity.startActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) = startActivityForResult(TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> Fragment.startActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.startActivityForResult(TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> androidx.fragment.app.Fragment.startActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.startActivityForResult(TARGET::class, *params, callback = callback)

/**
 *  作用同[Activity.startActivityForResult]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivityForResult<TestActivity> {resultCode, result->
 *          if (resultCode == RESULT_OK) {
 *              //处理成功，这里可以操作返回的intent
 *          } else {
 *             //未成功处理
 *          }
 *      }
 *  </pre>
 *  携带参数同[startActivity]
 *
 * @param TARGET 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun <reified TARGET : Activity> Activity.startActivityForResult2(
    vararg params: Pair<String, Any?>, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = startActivityForResult2(TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> Fragment.startActivityForResult2(
    vararg params: Pair<String, Any?>, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.startActivityForResult2(TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> androidx.fragment.app.Fragment.startActivityForResult2(
    vararg params: Pair<String, Any?>, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.startActivityForResult2(TARGET::class, *params, callback = callback)

/**
 *  作用同[Activity.startActivityForResult]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivityForResult(this, TestActivity::class) {
 *          if (it == null) {
 *              //未成功处理，即（ResultCode != RESULT_OK）
 *          } else {
 *              //处理成功，这里可以操作返回的intent
 *          }
 *      }
 *  </pre>
 *  携带参数同[startActivity]
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun Activity.startActivityForResult(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)

inline fun Fragment.startActivityForResult(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)
}

/**
 * 作用同上，以下三个方法为了兼容Java Class
 */
inline fun Activity.startActivityForResult(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)

inline fun Fragment.startActivityForResult(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)
}

/**
 *  作用同[Activity.startActivityForResult]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivityForResult(this, TestActivity::class) {resultCode, result->
 *          if (resultCode == RESULT_OK) {
 *              //处理成功，这里可以操作返回的intent
 *          } else {
 *             //未成功处理
 *          }
 *      }
 *  </pre>
 *  携带参数同[startActivity]
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun Activity.startActivityForResult2(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)

inline fun Fragment.startActivityForResult2(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult2(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)
}

/**
 * 作用同上，以下三个方法为了兼容Java Class
 */
inline fun Activity.startActivityForResult2(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)

inline fun Fragment.startActivityForResult2(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult2(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, target, *params, callback = callback)
}

/**
 *  作用同[Activity.startActivityForResult]
 *
 * @param intent intent
 * @param callback onActivityResult的回调
 */
inline fun Activity?.startActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = this?.run {
    ActivityMessenger.startActivityForResult(this, intent, callback)
}

inline fun Fragment.startActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, intent, callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, intent, callback)
}

inline fun Activity?.startActivityForResult2(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = this?.run {
    ActivityMessenger.startActivityForResult2(this, intent, callback)
}

inline fun Fragment.startActivityForResult2(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, intent, callback)
}

inline fun androidx.fragment.app.Fragment.startActivityForResult2(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult2(this, intent, callback)
}

/**
 *  作用同[Activity.finish]
 *  示例：
 *  <pre>
 *      finish(this, "Key" to "Value")
 *  </pre>
 *
 * @param params extras键值对
 */
fun Activity.finish(vararg params: Pair<String, Any?>) = run {
    setResult(Activity.RESULT_OK, Intent().putExtras(*params))
    finish()
}

fun Activity.finish(intent: Intent) = run {
    setResult(Activity.RESULT_OK, intent)
    finish()
}

/**
 * String转Intent对象
 *
 *  示例：
 *  <pre>
 *      val action = "android.media.action.IMAGE_CAPTURE"
 *      val intent = action.toIntent()
 *  </pre>
 *
 * @param flags [Intent.setFlags]
 */
fun String.toIntent(flags: Int = 0): Intent = Intent(this).setFlags(flags)

/**
 * 可空对象转非空对象
 */
inline fun <O> O?.runIfNonNull(block: (O) -> Unit) {
    this?.let { block(it) }
}

/**
 * 不报错执行
 */
inline fun <T, R> T.runSafely(block: (T) -> R) = try {
    block(this)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

internal object IntentFieldMethod {
    private val bundleClass =
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) BaseBundle::class else Bundle::class).java

    private val mExtras: Field? by lazy {
        Intent::class.java.getDeclaredField("mExtras").also { it.isAccessible = true }
    }

    private val mMap: Field? by lazy {
        runSafely {
            bundleClass.getDeclaredField("mMap").also {
                it.isAccessible = true
            }
        }
    }

    private val unparcel: Method? by lazy {
        runSafely {
            bundleClass.getDeclaredMethod("unparcel").also {
                it.isAccessible = true
            }
        }
    }

    internal fun Intent.internalMap() = runSafely {
        mMap?.get((mExtras?.get(this) as? Bundle).also {
            it?.run { unparcel?.invoke(this) }
        }) as? Map<String, Any?>
    }

    internal fun Bundle.internalMap() = runSafely {
        unparcel?.invoke(it)
        mMap?.get(it) as? Map<String, Any?>
    }
}