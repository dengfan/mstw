/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class Class1 {

    public static String A() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ZEVZEVZEV";
    }

    public static String C() throws UnknownHostException, SocketException {
        InetAddress address = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        byte[] mac = ni.getHardwareAddress();
        String sMAC = "";
        Formatter formatter = new Formatter();
        for (int i = 0; i < mac.length; i++) {
            sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                    (i < mac.length - 1) ? ":" : "").toString();
        }
        return sMAC;
    }

    public static String M(String info) {
        try {
            byte[] res = info.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(res);
            for (int i = 0; i < result.length; i++) {
                md.update(result[i]);
            }
            byte[] hash = md.digest();
            StringBuffer d = new StringBuffer("");
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    d.append("0");
                }
                d.append(Integer.toString(v, 16).toUpperCase());
            }
            return d.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {
        String a = A();
        String c = C();
        System.out.println(a + c);
        System.out.println(M(a + c));
    }
}
