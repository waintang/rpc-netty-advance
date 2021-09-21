package ml.wpxm.java.rpc.netty.advance.serial;

import java.util.concurrent.ConcurrentHashMap;

public class SerializerManager {

    private final static ConcurrentHashMap<Byte,ISerializer> serializer = new ConcurrentHashMap<>();

    static{
        ISerializer json = new JsonSerializer();
        ISerializer java = new JavaSerializer();
        serializer.put(java.getType(),java);
        serializer.put(json.getType(),json);
    }

    public static ISerializer getSerializer(byte key){
        ISerializer iSerializer =  serializer.get(key);
        if( iSerializer == null){
            return new JavaSerializer();
        }
        return iSerializer;
    }

}
