package my.rpc.serialization;

import java.io.*;

public class JavaBeanSerializer implements Serializer{


    @Override
    public byte[] serialize(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bs = null;
        try {
            bs = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bs);
            oos.writeObject(obj);
            oos.flush();
            return bs.toByteArray();

        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bs != null) {
                try {
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public <T> T deserilize(byte[] data, Class<T> type) throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T)ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IOException("class " + type.toString() + " not found", e);
        }
        finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
