##  ActivityMessenger，借助Kotlin特性，简化Activity之间传参和回调的逻辑代码。
### 博客详情： <https://blog.csdn.net/u011387817/article/details/99844066>

### 唠叨：
#### 在日常开发中，每次使用startActivityForResult时，要做的事情都超级多：

 1. 定义一个`RequestCode`；
 
 2. 重写`onActivityResult`并在这里判断`RequestCode`和`ResultCode`；
 
 3. 如果有携带参数，还要一个个通过`putExtra`方法`put`进*Intent*里；
 
 4. 目标*Activity*处理完成后还要把数据一个个`put`进*Intent*中，`setResult`然后`finish`；
 
 5. 如果参数是可序列化的泛型类对象(如*ArrayList\<Model\>*)，取出来的时候不但要显式强制转型，还要把 UNCHECKED_CAST 警告抑制；
 <br/>

#### 在饱受这些繁琐折磨之后，便诞生出了**ActivityMessenger**。
#### 它有以下特点：

 1. `startActivityForResult`不用另外定义`RequestCode`；
 
 2. 跟其他库一样，`startActivityForResult`不用重写`onActivityResult`方法，也不用判断`RequestCode`和`ResultCode`；
 
 3. `startActivity`如有携带参数不须一个个调用`putExtra`方法；
 
 4. 从*Intent*中取出数据时，无须调用对应类型的方法，如`getStringExtra()`、`getIntExtra()`、`getSerializableExtra()`等；
 
 5. 如果参数是可序列化的泛型类对象(如*ArrayList\<Model\>*)，取出来的时候不用强制转型，也没有可怕的 ***UNCHECKED_CAST*** 警告了；
<br/>

### 示例：
### startActivity：
***方式1***（假设`TestActivity`是要启动的Activity）：
```kotlin
    //不携带参数
    startActivity<TestActivity>()
    
    //携带参数（可连续多个键值对）
    startActivity<TestActivity>("Key" to "Value")
```
***方式2***（假设`TestActivity`是要启动的Activity）：
```kotlin
    //不携带参数
    startActivity(TestActivity::class)
    
    //携带参数（可连续多个键值对）
    startActivity(
        TestActivity::class,
        "Key1" to "Value",
        "Key2" to 123
    )
```
<br/>

### startActivityForResult：
***方式1***（假设`TestActivity`是要启动的Activity）：
```kotlin
    //不携带参数
    startActivityForResult<TestActivity> {
        if (it == null) {
            //未成功处理，即（ResultCode != RESULT_OK）
        } else {
            //处理成功，这里可以操作返回的intent
        }
    }
    
    //携带参数同startActivity。
```
***方式2***（假设`TestActivity`是要启动的Activity）：
```kotlin
    //不携带参数
    startActivityForResult(TestActivity::class) {
        if (it == null) {
            //未成功处理，即（ResultCode != RESULT_OK）
        } else {
            //处理成功，这里可以操作返回的intent
        }
    }
    
    //携带参数同startActivity。    
```
<br/>

### finish：
```kotlin
    //退出并设置参数
    finish("Key1" to "Value1", "Key2" to 2)
```
<br/>

### getExtras（获取Intent参数）：
***方式1***
```kotlin
    //预先声明好类型
    var mData: List<String>? = null
    mData = intent.get("Key1")
```
***方式2***
```kotlin
    //取出时再决定类型
    val result = intent.get<String>("Key2")
```

***方式3***
```kotlin
    // 使用委托
    val key3 by extraAct("key3", "test")
    var key4: String? by extraAct("key4")
```
<br/>

### toIntent：
```kotlin
    val action = "android.media.action.IMAGE_CAPTURE"
    val intent = action.toIntent()
```
<br/>

### 使用方式：
#### 添加依赖：
```
implementation 'com.wuyr:activitymessenger:1.2.0'
```

### Demo源码地址： <https://github.com/wuyr/ActivityMessenger>
---
### 感谢：
#### Jowan: <https://www.jowanxu.top/>
#### RxActivityResult: <https://github.com/nekocode/RxActivityResult>
