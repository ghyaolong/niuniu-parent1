package com.mouchina.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Static functions to simplifiy common {@link java.security.MessageDigest} tasks.  This
 * class is thread safe.
 *
 * @author 99bill
 *
 */
public class MD5Util
{
    private MD5Util(  )
    {
    }

    /**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            The MessageDigest algorithm name.
     * @return An MD5 digest instance.
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is
     *             caught
     */
    static MessageDigest getDigest(  )
    {
        try
        {
            return MessageDigest.getInstance( "MD5" );
        } catch ( NoSuchAlgorithmException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5( byte[] data )
    {
        return getDigest(  ).digest( data );
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5( String data )
    {
        return md5( data.getBytes(  ) );
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex( byte[] data )
    {
        return HexUtil.toHexString( md5( data ) );
    }

    public static String md5Octal( byte[] data )
    {
        return  HexUtil.binary(md5( data ), 8);
    }
    
    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     *
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex( String data )
    {
        return HexUtil.toHexString( md5( data ) );
    }

    public static String md5Octal( String data )
    {
        return HexUtil.binary(md5( data ),8);
    }
    public static String md5Hex16( String data )
    {
        return HexUtil.toHexString( md5( data ) ).substring( 8, 24 );
    }

    public static void main( String[] args )
    {
//        System.out.println( md5Hex( "11111111" ) );
        System.out.println( md5Hex( "phone=1347568368"
        		+ "&udid=F365614D-9A32-418B-9846-89F98884B555&loginKey=FA8D192328289398JJKUOOO_01"
        		+ "&t=1893283868632&ua=2&uadetail=iPad4,4+9.200000"
        		+ "&appid=11002&bbb981f42b0d4f629dc5d3612b86acde" ) );
    }
}
