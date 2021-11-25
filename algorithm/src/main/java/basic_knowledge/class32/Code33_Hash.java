package basic_knowledge.class32;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Code33_Hash {

    private MessageDigest hash;

    public Code33_Hash(String algorithm) {
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hashCode(String input) {
        return DatatypeConverter.printHexBinary(hash.digest(input.getBytes())).toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println("支持的算法 : ");
        for (String str : Security.getAlgorithms("MessageDigest")) {
            System.out.println(str);
        }
        System.out.println("=======");

        String algorithm = "SHA";
        Code33_Hash hash = new Code33_Hash(algorithm);

        String input1 = "zuochengyunzuochengyun1";
        String input2 = "zuochengyunzuochengyun2";
        String input3 = "zuochengyunzuochengyun3";
        String input4 = "zuochengyunzuochengyun4";
        String input5 = "zuochengyunzuochengyun5";
		System.out.println(hash.hashCode(input1));
		System.out.println(hash.hashCode(input2));
		System.out.println(hash.hashCode(input3));
		System.out.println(hash.hashCode(input4));
		System.out.println(hash.hashCode(input5));

    }

}
