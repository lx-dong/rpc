package my.rpc;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by lx-dong on 2019/5/6.
 */
public interface Caller {

    Response call(Request request) throws Exception;
}
