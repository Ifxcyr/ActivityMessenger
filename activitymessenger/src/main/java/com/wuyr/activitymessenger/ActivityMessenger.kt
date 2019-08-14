@file:Suppress("UNCHECKED_CAST", "unused", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "SpellCheckingInspection")

package com.wuyr.activitymessenger

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.BaseBundle
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.ArrayMap
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author wuyr
 * @github https://github.com/wuyr/ActivityMessenger
 * @since 2019-08-05 上午11:56
 */
object ActivityMessenger {
    private var sRequestCode = 0
        set(value) {
            field = if (value >= Integer.MAX_VALUE) 1 else value
        }

    /**
     *  作用同[Activity.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity<TestActivity>(this)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.startActivity<TestActivity>(this, "Key" to "Value")
     *  </pre>
     *
     * @param TARGET 要启动的Activity
     * @param starter 发起的Activity
     * @param params extras键值对
     */
    inline fun <reified TARGET : Activity> startActivity(
        starter: FragmentActivity,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    /**
     *  作用同[Activity.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity(this, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.startActivity(
     *         this, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     *  </pre>
     *
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: FragmentActivity,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity>(this) {
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun <reified TARGET : Activity> startActivityForResult(
        starter: FragmentActivity,
        vararg params: Pair<String, Any>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = startActivityForResult(starter, TARGET::class, *params, callback = callback)

    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult(this, TestActivity::class) {
     *          if (it == null) {
     *              //未成功处理，即（ResultCode != RESULT_OK）
     *          } else {
     *              //处理成功，这里可以操作返回的intent
     *          }
     *      }
     *  </pre>
     *  携带参数同[startActivity]
     *
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun startActivityForResult(
        starter: FragmentActivity,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) {
        val intent = Intent(starter, target.java).putExtras(*params)
        val fm = starter.supportFragmentManager
        val fragment = GhostFragment()
        fragment.init(++sRequestCode, intent) { result ->
            callback(result)
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        fm.beginTransaction().add(fragment, GhostFragment::class.java.simpleName).commitAllowingStateLoss()
    }

    /**
     *  作用同[Activity.finish]
     *  示例：
     *  <pre>
     *      ActivityMessenger.finish(this, "Key" to "Value")
     *  </pre>
     *
     * @param src 发起的Activity
     * @param params extras键值对
     */
    fun finish(src: Activity, vararg params: Pair<String, Any>) = with(src) {
        setResult(Activity.RESULT_OK, Intent().putExtras(*params))
        finish()
    }
}

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
fun <O> Intent.get(key: String): O? {
    try {
        val extras = IntentFieldMethod.mExtras.get(this) as Bundle
        IntentFieldMethod.unparcel.invoke(extras)
        val map = IntentFieldMethod.mMap.get(extras) as ArrayMap<String, Any>
        return map[key] as O
    } catch (e: Exception) {
        //Ignore
    }
    return null
}

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
fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
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

class GhostFragment : Fragment() {

    private var requestCode = -1
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null

    fun init(requestCode: Int, intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intent?.let { startActivityForResult(it, requestCode) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            val result = if (resultCode == Activity.RESULT_OK && data != null) data else null
            callback?.let { it(result) }
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
    }
}

internal object IntentFieldMethod {
    lateinit var mExtras: Field
    lateinit var mMap: Field
    lateinit var unparcel: Method

    init {
        try {
            mExtras = Intent::class.java.getDeclaredField("mExtras")
            mMap = BaseBundle::class.java.getDeclaredField("mMap")
            unparcel = BaseBundle::class.java.getDeclaredMethod("unparcel")
            mExtras.isAccessible = true
            mMap.isAccessible = true
            unparcel.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}