package my.rpc.serialization;

import java.io.IOException;

public interface Serializer {

    byte[] serialize(Object obj) throws IOException;

    <T> T deserilize(byte[] data, Class<T> type) throws IOException;

}
