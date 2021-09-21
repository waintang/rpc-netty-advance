package ml.wpxm.java.rpc.netty.advance.serial;

import ml.wpxm.java.rpc.netty.advance.constants.SerialType;

import java.io.*;

public class JavaSerializer implements ISerializer {
    @Override
    public byte getType() {
        return SerialType.JAVA_SERIAL.code();
    }

    public <T> byte[] encode(T object) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);){
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new byte[0];
    }

    public <T> T decode(byte[] bytes, Class<T> clz) {
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);){
            return (T)objectInputStream.readObject();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
