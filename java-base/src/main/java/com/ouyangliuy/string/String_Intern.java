package com.ouyangliuy.string;

import java.util.UUID;

/**
 *
 *
 * JVM 中 SymbolTable
 *
 */
public class String_Intern {


    public static void main(String[] args) {

        String str1 = UUID.fromString("3eb4a9d7-c529-4573-a13f-9be33aba7349").toString();
        String str2 = "3eb4a9d7-c529-4573-a13f-9be33aba7349";
        System.out.println(str1.intern() == str2);
        System.out.println(str1.intern() == str1);
        String str3 = UUID.randomUUID().toString();
        System.out.println(str3.intern() == str3);
        String str4 = new String("aaa");
        System.out.println(str4.intern() == str4);

        String s5 = "abb";
        String s6 = s5.intern();
        System.out.println(s6 == s5);
        String s7 = new StringBuilder("123").append("2").toString();

        String s8 = "123"+"2";
        System.out.println(s7.intern() == s7);

    }
}
