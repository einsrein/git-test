import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.neulion.savanna.common.util.DateTimeUtil;
import com.neulion.savanna.common.util.HttpClient;
import com.neulion.savanna.common.util.TextUtil;

/*
 * Copyright (c) 2006 NeuLion, Inc. All Rights Reserved.
 */

public class QuickTest
{
    public static void decode(String code) throws Exception
    {
        String algorithm = "Blowfish";
        
        // Build secret key
        String s = "8669034D5A974991A8BCAC61A9D14FA1DD8283E73EB849868EFA59762588E086";
        byte[] b = new byte[s.length() / 2];
        for (int n = 0, k = 0; n < s.length(); n++, k++)
        {
            int k1 = Character.digit(s.charAt(n++), 16);
            int k2 = Character.digit(s.charAt(n), 16);
            b[k] = (byte) ((k1 * 16) + k2);
        }

        // Create cipher
        // For this to work, you need to modify policy files of the JRE
        // Go to JRE /lib/security folder.
        // Replace local_policy.jar with US_export_policy.jar
        Key key = new SecretKeySpec(b, algorithm);
        Cipher cipher;
        try
        {
            cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (GeneralSecurityException ex)
        {
            throw new IllegalStateException(ex.toString());
        }
        
        b = TextUtil.decode(code);
        System.out.println(new String(cipher.doFinal(b)));
    }
    
    public static String toAscii(String s) throws UnsupportedEncodingException
    {
        return TextUtil.toHexString(s.getBytes("UTF-16BE"));
    }
    
    public static void testDST()
    {
        TimeZone zone = TimeZone.getTimeZone("America/New_York");
        Calendar cal = new GregorianCalendar(zone);
        cal.set(2014, 10, 2, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        System.out.println(dstInfo(zone, cal));
        cal.add(Calendar.HOUR, 1);
        System.out.println(dstInfo(zone, cal));
        cal.add(Calendar.SECOND, 1);
        System.out.println(dstInfo(zone, cal));
        cal.add(Calendar.HOUR, 1);
        System.out.println(dstInfo(zone, cal));
        cal.add(Calendar.SECOND, 1);
        System.out.println(dstInfo(zone, cal));
        cal.add(Calendar.HOUR, 1);
        System.out.println(dstInfo(zone, cal));
    }

    private static String dstInfo(TimeZone zone, Calendar cal)
    {
        return DateTimeUtil.format(cal.getTime(), "yyyy-MM-dd HH:mm:ssZ", zone)
                + " - " + zone.inDaylightTime(cal.getTime());
    }
    
    public static void main(String[] args) throws Exception
    {
        // -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8087 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=8087
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8087");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "8087");
        
        HttpClient hc = new HttpClient(new URL("http://boxun.com/"));
        System.out.println(hc.getResponseBody(hc.openConnection()));
    }
    
}

class Comp
{

    String a;
    
    public Comp(String a)
    {
        this.a = a;
    }
    
    public String toString()
    {
        return this.a;
    }
    
}
