@file:Suppress(
    "unused",
    "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE",
    "SpellCheckingInspection", "DEPRECATION"
)

package com.wuyr.activitymessenger

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
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
        starter: Activity, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    /**
     *  Fragment跳转，同[Activity.startActivity]
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
     * @param starter 发起的Fragment
     * @param params extras键值对
     */
    inline fun <reified TARGET : Activity> startActivity(
        starter: Fragment, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter.activity, TARGET::class.java).putExtras(*params))

    /**
     * Adapter跳转，同[Context.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity<TestActivity>(context)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.startActivity<TestActivity>(context, "Key" to "Value")
     *  </pre>
     *
     * @param TARGET 要启动的Context
     * @param starter 发起的Fragment
     * @param params extras键值对
     */
    inline fun <reified TARGET : Activity> startActivity(
        starter: Context, vararg params: Pair<String, Any?>
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
        starter: Activity, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Activity, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target).putExtras(*params))

    /**
     *  Fragment跳转，同[Activity.startActivity]
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
     * @param starter 发起的Fragment
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: Fragment, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter.activity, target.java).putExtras(*params))

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Fragment, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter.activity, target).putExtras(*params))

    /**
     *  Adapter里面跳转，同[Context.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity(context, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.startActivity(
     *         context, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     *  </pre>
     *
     * @param starter 发起的Context
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: Context, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Context, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target).putExtras(*params))

    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {
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
        starter: Activity, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = startActivityForResult(starter, TARGET::class, *params, callback = callback)

    /**
     *  Fragment跳转，同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {
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
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = startActivityForResult(starter.activity, TARGET::class, *params, callback = callback)

    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {resultCode, result->
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun <reified TARGET : Activity> startActivityForResult2(
        starter: Activity, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) = startActivityForResult2(starter, TARGET::class, *params, callback = callback)

    /**
     *  Fragment跳转，同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {resultCode, result->
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun <reified TARGET : Activity> startActivityForResult2(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) = startActivityForResult2(starter.activity, TARGET::class, *params, callback = callback)

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
        starter: Activity?, target: KClass<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        startActivityForResult(it, Intent(it, target.java).putExtras(*params), callback)
    }

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    inline fun startActivityForResult(
        starter: Activity?, target: Class<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        startActivityForResult(it, Intent(it, target).putExtras(*params), callback)
    }

    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult(this, TestActivity::class) {resultCode, result->
     *          if (resultCode == RESULT_OK) {
     *              //处理成功，这里可以操作返回的intent
     *          } else {
     *             //未成功处理
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
    inline fun startActivityForResult2(
        starter: Activity?, target: KClass<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        startActivityForResult2(it, Intent(it, target.java).putExtras(*params), callback)
    }

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    inline fun startActivityForResult2(
        starter: Activity?, target: Class<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        startActivityForResult2(it, Intent(it, target).putExtras(*params), callback)
    }

    inline fun startActivityForResult(
        starter: Activity?, intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        val fragment = GhostFragment()
        fragment.init(++sRequestCode, intent) { result ->
            callback(result)
            it.fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        it.fragmentManager.beginTransaction().add(fragment, GhostFragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    inline fun startActivityForResult2(
        starter: Activity?,
        intent: Intent,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) = starter.runIfNonNull {
        val fragment = GhostFragment()
        fragment.init(++sRequestCode, intent) { resultCode, result ->
            callback(resultCode, result)
            it.fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        it.fragmentManager.beginTransaction().add(fragment, GhostFragment::class.java.simpleName)
            .commitAllowingStateLoss()
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
    fun finish(src: Activity, vararg params: Pair<String, Any?>) = src.run {
        setResult(Activity.RESULT_OK, Intent().putExtras(*params))
        finish()
    }

    /**
     *  Fragment调用，作用同[Activity.finish]
     *  示例：
     *  <pre>
     *      ActivityMessenger.finish(this, "Key" to "Value")
     *  </pre>
     *
     * @param src 发起的Fragment
     * @param params extras键值对
     */
    fun finish(src: Fragment, vararg params: Pair<String, Any?>) =
        src.activity?.run { finish(this, *params) }
}

class GhostFragment : Fragment() {

    private var requestCode = -1
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null
    private var callback2: ((resultCode: Int, result: Intent?) -> Unit)? = null

    fun init(requestCode: Int, intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback = callback
    }

    fun init(
        requestCode: Int, intent: Intent,
        callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback2 = callback
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        intent?.let { startActivityForResult(it, requestCode) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intent?.let { startActivityForResult(it, requestCode) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            callback?.let { it(data) }
            callback2?.let { it(resultCode, data) }
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
        callback2 = null
    }
}