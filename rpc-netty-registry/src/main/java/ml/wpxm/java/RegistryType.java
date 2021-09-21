package ml.wpxm.java;

public enum RegistryType {

    ZOOKEEPER((byte)0),
    EUREKA((byte)1);

    private byte code;

    RegistryType(byte code){
        this.code = code;
    }

    public byte code(){
        return this.code;
    }

    public static RegistryType findByCode(byte code){
        for(RegistryType registryType:RegistryType.values()){
            if(registryType.code() == code){
                return registryType;
            }
        }
        return null;
    }
}
