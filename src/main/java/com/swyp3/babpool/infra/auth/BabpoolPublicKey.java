package com.swyp3.babpool.infra.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class BabpoolPublicKey {
    //The key type parameter setting. You must set to "RSA".
    private String kty;
    //A 10-character identifier key, obtained from your developer account
    private String kid;
    //The intended use for the public key
    private String use;
    //The encryption algorithm used to encrypt the token
    private String alg;
    //The modulus value for the RSA public key
    private String n;
    //The exponent value for the RSA public key
    private String e;
}
