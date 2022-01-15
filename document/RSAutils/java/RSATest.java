package top.hityx.web.rsaUtils;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATest {
    public static void main(String[] args) throws Exception {
        /**
         * There are 2 ways to get public/private key(s)
         * Method 1: generate keys from String/BigInteger exponent and modulus
         * Method 2: generate a KeyPair from RSAUtils, and then get public/private key from the KeyPair
         *
         * If you need to import keys from external source, please use method 1.
         * Else if you need to generate a new RSA key pair, please use method 2.
         */

        /* Method 1 to get keys: generate a key from exponent/modulus string
        获取公私密钥的方式1: 从外部导入e和m的值对应的字符串然后生成密钥 */
        String modulusString = "146728024340719746322106248275489970172399885237007248537544435910724438259837462724201109895588166230877712938083134068111708154144193719025805548588492552639515372188915515488605378333762662255282101708026505879562300376893466634551337654628246492306221900014316859719558679393987564925083954314542258204129";
        String publicExponentString = "65537";
        String privateExponentString = "77021186404160103650962039049291330910339210704236131714735243972118682957946326390252324375971193290426096411125562326795229929945962621449345598403059645097894237899802795242668428154764635727884137134613087741768178630486732531599211117701165051519386374446534830430372215719567944469156274026591468929537";
        BigInteger modulus = new BigInteger(modulusString);
        BigInteger publicExponent = new BigInteger(publicExponentString);
        BigInteger privateExponent = new BigInteger(privateExponentString);
        RSAPrivateKey rsaPrivateKey = RSAUtils.generateRSAPrivateKey(privateExponent, modulus);
        RSAPublicKey rsaPublicKey = RSAUtils.generateRSAPublicKey(publicExponent, modulus);

        /* Method 2 to get keys: generate a new key pair and get keys from the key pair
        获取公私密钥的方式2: 用RSAUtils生成KeyPair，然后从key pair中生成密钥 */
        int keySize = 512;
        KeyPair keyPair = RSAUtils.generateKeyPair(keySize);
        RSAPrivateKey rsaPrivateKey1 = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey rsaPublicKey1 = (RSAPublicKey) keyPair.getPublic();

        /* encryption steps
        加密流程 */
        String plainText = "我能吞下玻璃而不伤身体";
        // we recommended you encoding the string with base64 first
        String plainTextBase64Encoded = RSAUtils.base64Encrypt(plainText);
        BigInteger encrypt = RSAUtils.encrypt(rsaPublicKey, plainTextBase64Encoded);
        System.out.println(encrypt); // the cipher big integer
        String decrypt = RSAUtils.decrypt(rsaPrivateKey, encrypt); // the decrypted text, still need to be base64 decoded
        String decryptText = RSAUtils.base64Decrypt(decrypt);
        System.out.println(decryptText);
    }

}
