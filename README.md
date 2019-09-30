# chainMarket
此轻量级框架必须依赖NETTY4
<dependency>
      <groupId>io.netty</groupId>
      <artifactId>netty-all</artifactId>
      <version>4.1.13.Final</version>
      <scope>compile</scope>
    </dependency>
    Main入口类启动
     HttpServer httpServer = new HttpServer();
        httpServer.start(8080, "com.wlld.entity", "$_", 1024,
                "ws://127.0.0.1:8080/websocket", new MyWebSocket());
     参数说明：
     第一个参数是端口，第二个参数是Control类扫描包地址，第三个是SOCKET分隔符，第四个一次收包最大字节数（超过则返回404）
     第五个参数WEBSOCKT地址，最后一个用来接收WEBSOCKET 回调的类
     
     public class MyWebSocket extends WebSocketBack {
    @Override
    public String getText(String message, ChannelHandlerContext ctx) {
        return super.getText(message, ctx);
    }

    @Override
    public byte[] getBuf(byte[] message, ChannelHandlerContext ctx) {
        return super.getBuf(message, ctx);
    }
}
websocket 回调类 要继承WebSocketBack 并重写getText 和getBuf 方法，一个是接受字符串并返回字符串，一个是接收字节码并返回字节码
///////////////////////////////
Control  MAIN启动的时候会扫描启动方法第二个参数包路径下面的所有类，并将有Central注解的路径的类加载，下面的第一个方法就是
url/login/wxLogin为路径
方法内的参数类型及数量固定，message 接收POST消息体，MAP为URL 参数的键值对，接口不区分POST和GET，都可以接受到
@Central(url = "/login")
public class Login extends Business {
    static final Logger logger = LogManager.getLogger(Login.class);

    @Central(url = "/wxLogin")
    public String wxLogin(String message, Map<Object, Object> map) {//微信小程序登录
        return wxLogin(message);
    }

    @Central(url = "/getCommunity")
    public String land(String message, Map<Object, Object> map) {//获取全部社区
        return getSmallCity();
    }

    @Central(url = "/setAddress")
    public String setAddress(String message, Map<Object, Object> map) {//添加或编辑收货地址
        return setAddresses(message);
    }

    @Central(url = "/getAddress")
    public String getAddress(String message, Map<Object, Object> map) {//获取收货地址
        return getAddresses(message);
    }

    @Central(url = "/wxIosLogin")
    public String wxIosLogin(String message, Map<Object, Object> map) {//IOS 微信登录
        return wxIosLogin(message);
    }
}
