package ml.wpxm.java.rpc.netty.advance.constants;

public enum ReqType {

    REQUEST((byte)1),
    RESPONSE((byte)2),
    PING((byte)3),
    PONG((byte)4)
    ;

    private byte code;

    ReqType(byte code){
        this.code = code;
    }

    public byte code(){
        return this.code;
    }

    public static ReqType findByCode(byte code){
        for(ReqType reqType : ReqType.values()){
            if(reqType.code() == code){
                return reqType;
            }
        }
        return null;
    }
}
