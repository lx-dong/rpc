package my.rpc.javabean;

public class HelloImpl implements IHello {

    public String sayHi(String name) {
        return "hi! " + name;
    }
}
