# CatMVVM
## 前言
即是是码农，也该在战场上像猫一样高贵，像猫一样优雅，像猫一样高傲，不是吗，至少不应该重复发明轮子，这是我对这个架构的期冀，另一个目的是验证自己对mvvm的新的理解
## 注意
由于时间原因，大量的代码使用了反射进行类的加载与方法的调用，对于性能有所介意的请勿使用，有能力的同学也可以改用apt去重构相关类
## 导入
[![](https://jitpack.io/v/mskmz/CatMVVM.svg)](https://jitpack.io/#mskmz/CatMVVM)
```
   allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
   }
```
```
    dependencies {
       implementation 'com.github.mskmz:CatMVVM:V1.0.0'
    }
```

## 功能
精简了重复性较高的代码逻辑  如布局和vm的引入 
可以通过如下的代码声明一个类
```
    class MainActivity :CatActivity<ActivityMainBinding>()
```
统一使用notice方法作为交互方式
```
    noticeAllVm(msgTag, msgObj)
```
使用注解完成全局类的绑定和复用 便于实现数据驱动ui的方式
```
    @CatAutoInjection
    lateinit var baseModel: BaseModel
```
使用注解注册notice的传值
```
    @CatOnNoticeListen(NOTICE_MSG)
    var testName = ObservableField<String>("notice_value")
```
