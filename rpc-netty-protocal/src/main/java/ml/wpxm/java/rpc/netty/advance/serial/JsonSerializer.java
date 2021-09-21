package ml.wpxm.java.rpc.netty.advance.serial;

import ml.wpxm.java.rpc.netty.advance.constants.SerialType;

import java.io.*;

public class JsonSerializer implements ISerializer{
    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.code();
    }

    @Override
    public <T> byte[] encode(T object) {
        try(ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bao);){
            oos.writeObject(object);
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clz) {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);){
            return (T)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
