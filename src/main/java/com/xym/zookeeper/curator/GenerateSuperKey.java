package com.xym.zookeeper.curator;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

/**
 * desc
 *
 * @author xym
 */
public class GenerateSuperKey {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(DigestAuthenticationProvider.generateDigest("root:123456"));
    }

}