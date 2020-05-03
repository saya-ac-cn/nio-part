package ac.cn.saya.netty.rpc;

/**
 * @Title: UserServiceImpl
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 21:35
 * @Description:
 */

public class UserServiceImpl implements UserService {

    @Override
    public String get(String name) {
        return "查询：'"+name+"'成功";
    }
}
