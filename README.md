## English version is being re-translated, coming soon...

```
Android平台中对页面、原生路由功能的中间件，我的目标是 —— 简单且完美
```
#### 最新版本
V1.0.0

#### 同类对比
1. **完美配套插件化的实现方案;(目前市面上路由组件都很难配套动态插件化方案)**
2. **接入和使用简单易懂;**
3. **扩展性极强,满足项目苛刻要求;**
4. 请求对象池复用;
5. 代码简洁优美;

#### Demo展示
![demo演示](demo/router.gif)
##### [Demo apk下载](https://github.com/wenzhonghu/MyRouter/tree/master/demo/myrouter-1.0.0.apk)、[Demo Gif](https://github.com/wenzhonghu/MyRouter/tree/master/demo/router.gif)

#### 一、功能介绍
1. **模块间调用**
2. **网页和原生之间调用**
3. **实现模块间入参和结果的调用**
4. **实现模块方法的调用**
5. **支持获取Fragment**
6. **代码简洁健壮易扩展**
7. **支持权限配置**
8. **支持路由路径的匹配规则**
9. **支持和配套[组件化和插件化路由方案](https://www.jianshu.com/p/df2a6717009d)**

#### 二、典型应用
1. 从外部URL映射到内部页面，以及参数传递与解析
2. 跨模块页面跳转，模块间解耦
3. 拦截跳转过程，处理登陆、埋点等逻辑
4. 跨模块API调用
5. 跨模块方法调用(入参和结果值)

#### 三、基础功能
1. 添加依赖和配置
``` gradle
android {
    defaultConfig {
	...
	    javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = [moduleName: '填写自己的模块名称', libPackageName: project.readPackage()]
                    }
        }
    }
}

dependencies {
    // 替换成最新版本, 需要注意的是api
    // 要与compiler匹配使用，均使用最新版可以保证兼容
    compile 'com.xiaoniu.corelib:xnrouter:1.0.0'
    annotationProcessor 'com.xiaoniu.corelib:xnprocessor:1.0.0'
    compile 'com.xiaoniu.corelib:xnannotation:1.0.0'
    ...

    //注意:如果模块仅仅是使用路由访问其他模块则只需要配置
    compile 'com.xiaoniu.corelib:xnrouter:1.0.0'
}
```
2. 添加混淆规则(如果使用了Proguard)
``` gradle
-keep interface * implements com.xiaoniu.finance.router.core.XnAbstractTrack
```
3. 添加注解
``` java
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是保障其全局唯一，一般可以通过 ""/模块/功能path""
@Router(value = "/fixed/cross")
public class FixedCrossCatTracker extends XnAbstractTrack {
    @Override
    public  XnRouterResult fire(Context context, Bundle requestData) {
        ...
    }
}
```
详见com.xiaoniu.finance.annotation.router.Router注解类的说明


4. 初始化路由
```
由于是模块分层,因此每个模块都有对应的代码分布,而每个模块都有一个初始化功能类,在这个类的初始化过程添加进去.
其原理:通过扫描 dex 的方式进行加载通过 gradle 插件进行自动注册
App(启动的时候加载初始化各个子模块的初始化功能的总管类InitProxyManager,管理调用子模块的初始化XxxxProxyManager)
|____Fund基金模块(实现基金模块的初始化类FundProxyManager)
|____Fixed定期模块(实现定期模块的初始化类FixedProxyManager)
```
``` java
@Inject
class FundProxyManager{
   void init(Context context){
       //此类通过XNProcessor代码产生器自动产生此类代码(格式XxxRouterManager,其中Xxx就是gradle配置moduleName参数时)
       FundRouterManager.setup();
   }
}
```
注:其中@Inject就是在代码编译时自动注入InitProxyManager的init(Context context) // 尽可能早，推荐在Application中初始化

5. **发起路由操作**

5.1  应用内简单的跳转(通过URL跳转在'进阶用法'中)
``` java
 XnRouter.getInstance().from(context, new XnRouterRequest.Builder().build("/fund/result"));
```

5.2. 跳转并携带参数
``` java
 XnRouter.getInstance().from(context, new XnRouterRequest.Builder().build("/fund/result2")
   .withInt("request", REQ_CODE)
   .withLong("key1", 666L)
   .withString("key3", "888"))
```

5.3. 携带fragment对象
``` java
 XnRouterResponse response = XnRouter.getInstance().from(context,
                        new XnRouterRequest.Builder().build("/fixed/fragment")
                        .withString("one", "onesssssssssssssss")
                        .withString("two", "two0000ooooooooooo"));
if (response.parall()) {
            Fragment fragment = (Fragment) response.getObject();
            //步骤一：添加一个FragmentTransaction的实例
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //步骤二：用add()方法加上Fragment的对象rightFragment
            transaction.add(R.id.contrainter, fragment);
            //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
            transaction.commit();

}
```

5.4. 添加访问权限
``` java
XnRouter.getInstance().setPermissionDeniedListener(new XnRouter.PermissionDeniedListener() {
    @Override
    public void onPermissionDenied(Context context) {
        Toast.makeText(context, "没有权限访问此地址", Toast.LENGTH_SHORT).show();
    }
}).from(context, new XnRouterRequest.Builder().build("/fix/home")
.permission(PermissionType.ACTIVITY.getPermission()));

//注:权限配置可以通过PermissionType类查看,内部维护一套可扩展性的权限规则系统
```

5.5. 调用其他模块的方法和结果
``` java
XnRouterResponse response =
XnRouter.getInstance().from(context, new XnRouterRequest.Builder().build("/fixed/sum")
                        .withInt("count", Integer.parseInt(et.getText().toString()));
Toast.makeText(context, (int) response.getObject() + "", Toast.LENGTH_SHORT).show();
```

#### 四、进阶用法
1. 打开日志信息
``` java
XnRouter.isDebug = true;
```
2. 通过URL跳转
``` java
// 新建一个跳转类用于监听Schame事件,之后直接把url传递给路由即可
//注解跳转类实现URL跳转
@Router(value = "xnoapp://xno.cn/fund/list")
public class OuterUrlTracker extends XnAbstractTrack {
```
网页跳转代码:
``` javascript
<a href="xnoapp://xno.cn/fund/list?type=bbb" >地址跳转原生界面</a>
```
AndroidManifest.xml(核心点就是运用隐式跳转的方式实现)

``` java
片段代码:
<!-- Schame -->
<intent-filter>
   <data android:host="xno.cn" android:scheme="xnoapp" />
   <action android:name="android.intent.action.VIEW" />
   <category android:name="android.intent.category.DEFAULT" />
   <category android:name="android.intent.category.BROWSABLE" />
</intent-filter>
```
注:目前android:host和android:scheme写死,如果需要符合自己条件请修改打包

#### 五、未来规划
  1.新增线程模式
  ``` java
      ThreadMode threadMode() default ThreadMode.POSTING

      public enum ThreadMode {
          /**
           * 在调用post所在的线程执行回调
           */
          POSTING,

          /**
           * 在UI线程回调
           */
          MAIN,

          /**
           * 在Backgroud线程回调
           */
          BACKGROUND,

          /**
           * 交给线程池来管理
           */
          ASYNC
      }
  ```
  2.完善路由权限系统

  3.增加模块间接口的路由调用

#### 六、Q&A
  1. "如何自定义权限不足的操作"

     这个可以通过如下代码实现:
   ``` java
        XnRouter.getInstance().setPermissionDeniedListener(
        new XnRouter.PermissionDeniedListener() {
               @Override
                public void onPermissionDenied(Context context) {
                       Dialog.builder(context).show();
                }
        })
   ```
#### 七、其他

  1. 沟通和交流

    1. 邮箱 (wenzhonghu@qq.com)