APP说明：
修改了APP背景图片，APP图标，APP名称，编译生成了正式的APK文件安装。
其它文件还未修改

目前完成的工作：
首先是下载安装Git ，选择要Git下载Demo的文件夹，
git clone https://github.com/jjhyt/tuya-home-android-sdk.git

1 编辑build.gradle文件，更改了包名。applicationId "APPshizhanyin.Android"

2 app/src/main下新建了assets目录，安全图片改名为t_s.bmp放这里。

3 编辑AndroidManifest.xml文件，修改app key和secret值（使用的是正式版Key）。

4 app/src/main/res/drawable-xhdpi 下替换APP背景图片ty_pre为自己的

5 app/src/main/res/drawable-xhdpi 中粘贴入自己的ICO图片，ic_launcher_background和ic_launcher_foreground
  菜单tools---ResouceMenega---Mip Map----ic_launcher.xml也可在这里修改ICO。
  app/src/main/res/mipmap  好几个文件夹替换ic_launcher文件为自己ICO。（这一步可以不用改）
  新方法  app上点右键--选择new--Image asset--选图片调整大小确定！这个设定好后比较美观

6 app/src/main/res/values和 values-zh的 Strings.xml 中更换APP的英文名和中文名。

7 编辑build.gradle文件,指定编译的VersionName，指定生成的apk文件名。
   android.applicationVariants.all{
        variant ->
            variant.outputs.all{
                //这里是APK名字
                outputFileName = "ZhihuiZhaoming.apk"
            }
    }
8 TuyaSmartApp.java中修改 String TAG = "ZhihuiZhaoming" 
  去掉 @SuppressLint("StaticFieldLeak")
  TuyaSmartApp.java上点右键，选 Refactor-Rename 改名字

9 菜单build--Generate Signed Bundle/APK--选择单个APK安装的，下一步--Key store path这里签名文件新建一个，输入密码，别名等。
  最后选择release,打勾V2(Full APK),就可以Build出APK文件了。

修改好了代码，然后用git diff来查看一下修改的区别。用git status可以清楚看到被修改的是什么文件。
git add -A 把代码加入到暂缓区。
git commit -m "这里我们加入留言注释。"
git pull origin master
git push origin master
分别执行这两条指令，然后输入远程仓库的相应信息，这样让本地和远程同步了。