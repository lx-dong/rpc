package my.rpc;

/**
 * Created by lx-dong on 2019/5/6.
 */
public interface MessageHandler {
    Response handle(Request request);
}
