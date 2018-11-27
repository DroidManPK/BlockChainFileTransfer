package dbc.core;

import dbc.utils.StringUtil;

import java.security.PublicKey;

public class data {
    private PublicKey rsaKey;
    private String hash;//hash
    private String rsaDat;//RSA encrypt data

    public data(PublicKey rsaKey,String has,String dat){
        this.rsaDat=dat;
        this.rsaKey=rsaKey;
        this.hash=has;
    }

    @Override
    public String toString() {
        return StringUtil.getStringFromKey(this.rsaKey)+this.hash+this.rsaDat;
    }
}
