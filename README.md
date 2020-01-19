# 网络框架:java_chainMarket
* 由本人独立开发对通讯底层NETTY4进行二次封装的轻量级网络框架
* 为集成本人所有开源技术包的轻量级网络框架
##  网络框架有很多稳定且功能全面的重量级框架比如ssm,boot之类的，为什要做这个？
* 底层是NETTY4，所以很稳定，我只是给NETTY做二次封装了，所以不要怕这种轻量级的网络框架有风险之类的问题
* 第一目的：降低入门门槛。因为轻量级，超级傻瓜化，有点javaSE基础的人应该十分钟就能上手。
* 第二目的：自由度高。比如可以随意定义返回的HTTP状态码，起码对于我这种老鸟来说，随意定义很舒服
* 第三目的：我可以持续集成，不断的集成我其他开源技术包的融合进来，比如集成我的ImageMarket机器视觉包
，又比如之后要集成的自然语言包TalkMarket（对用户语言做分类识别，实现纯语言交互类似苹果siri）
* 第四目的：让我的其他技术包集成进来之后，一包导入就可使用我所有人工智能或者其他开源技术服务
### 关于持续集成技术包
* 目前正在更新的开源项目是 ImageMarket,机器视觉包，也将会集成进chainMarket。
地址：https://github.com/thenk008/ImageMarket
* 准备增加的还有一个JAVA热更新组件，原理就是自己开发了一套脚本解释器，用户使用脚本写业务逻辑，
通过我的解释器运行，因为是解释器运行，所以修改脚本就可实现动态热更业务逻辑代码。
* 还要增加一个自然语言处理包，类似siri，对用户说话的语义进行只能分类分析提供交互，比如用户说"我渴了"
，我会给用户推喝水的信息，前端就会产生响应的反应。
### 此轻量级框架必须依赖NETTY4
* <dependency>
      <groupId>io.netty</groupId>
      <artifactId>netty-all</artifactId>
      <version>4.1.13.Final</version>
      <scope>compile</scope>
</dependency>

* 因为是对NETTY做封装，所以肯定要依赖NETTY。
* 我提供的这个NETTY4版本有些老了,所以NETTY大版本只要是NETTY4，你可以在POM文件里随意换一个
NETTY版本，不会对框架产生影响。
    
### HTTP服务
    public class App {
        public static void main(String[] args) {
            init();
            //初始化chainMarket服务
            HttpServer httpServer = new HttpServer();
            //参数分别是 端口号
            //control类所在的包路径（接口入口类所在的包路径），
            // 允许接收的最大字节数（超过这个数量，将直接返回400错误码）
            //websocket url,
            //websocket 回调类
            //启动chainMarket服务
            httpServer.start(8080, "com.shareData.chainMarket.test", 1024,
                    null, null);
            //若要启动WEBSOCKET服务，请将后两个参数传入
           //httpServer.start(8080, "com.shareData.chainMarket.test", 1024,
           //"ws://127.0.0.1:8080/websocket", new MyWebSocket());
        }
    
        public static void init() {
            //是否启动SSL服务，若启动则服务器无法接收HTTP请求，只能接收HTTPS请求
            //反之也一样
            HttpsSetting.sslEnabled = false;//不启动SSL服务，不设置就是默认不启动
            HttpsSetting.keystorePath = "SSL证书在磁盘上的地址";
            HttpsSetting.certificatePassword = "SSL证书密码";
            HttpsSetting.keystorePassword = "SSL证书密码";//上下两者等同
            Config.setFileMaxLength(6553666);//上传文件的最大大小,单位BIT
        }
    
    }
    //在设置的control包路径下，创建control类
    //注解 Central代表虚拟路径名称，在内部的URL中填写虚拟路径
    //这样这两个接口地址分别是 /login/wxLogin,/login/getCommunity
    //不区分post,get,message是 POST体的参数，map 是url？后面的健值对（get参数）
    //return 是返回内容
    @Central(url = "/login")
    public class Login {
    @Central(url = "/wxLogin")
    public String wxLogin(String message, Map<Object, Object> map) {//微信小程序登录
        return wxLogin(message);
    }

    @Central(url = "/getCommunity")
    public String land(String message, Map<Object, Object> map) {//获取全部社区
        return getSmallCity();
    }
    //若是FORM表单提交信息，请使用List<FileAndName> forms 来接收
    @Central(url = "/list")
        public String list(List<FileAndName> forms) {
            return "a";
            }
    }
    //List<FileAndName>
    //FileAndName 是一个健值对，LIST就是接收的一个健值对集合
    //FileAndName 中有三个参数
    //name,键名
    //text,文本类型的值
    //inputStream，二进制文件类型的值
    //注意文本类型和二进制类型互斥，有其中一种，另一种就是null。
    //从inputStream中读取字节码后，无需关闭字节流，return之后底层会自动关闭
        public class FileAndName {
            private InputStream inputStream;
            private String name;
            private String text;
        }
        
        
### WEBSOCKET 服务
            public class MyWebSocket extends WebSocketBack {
            //若启用WEBSOCKET,创建回调类
            //且必须继承WebSocketBack，并重写active方法和 getText方法
            @Override
            public void active(ChannelHandlerContext ctx) {
            //当用户进行WEBSOCKET握手时激活，一个用户构成一个连接有且只执行一次
                System.out.println("激活了======");
            }
        
            @Override
            public String getText(String message, ChannelHandlerContext ctx) {
              //收到一个WEBSOCKET通讯
              //将一个通讯的信道注册起来，与主键绑定
              WebSocketManager.register(1,ctx);
              //通过绑定的主键，服务端主动给用户PUSH信息
              WebSocketManager.putMessage(1,"发送信息");
        
                return "ok";
            }}
##### 若有疑问，可联系作者 Q：794757862，或者可看com.shareData.chainMarket.test包下的演示案例
##### 机器视觉技术包在我另一个项目 https://github.com/thenk008/ImageMarket 可下载参详
