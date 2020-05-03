package ac.cn.saya.netty.rpc;

/**
 * @Title: ClientTest
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 22:35
 * @Description:
 */

public class ClientTest {

    public static void main(String[] args) {
        RPCClient service = new RPCClient();
        UserService bean = (UserService)service.getBean(UserService.class, "get");
        String result = bean.get("saya");
        System.out.println(result);
    }

}
