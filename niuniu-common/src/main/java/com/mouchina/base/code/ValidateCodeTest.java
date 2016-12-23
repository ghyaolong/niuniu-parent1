package com.mouchina.base.code;

import java.io.IOException;
import java.util.Date;

public class ValidateCodeTest
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        ValidateCode vCode = new ValidateCode( 100, 40, 4, 100 );

        try
        {
            String path = "/Users/larry/Desktop/test/test/" + new Date(  ).getTime(  ) + ".png";
            System.out.println( vCode.getCode(  ) + " >" + path );
            System.out.println( vCode.getCode(  ) + " >" + path );
            vCode.write( path );
        } catch ( IOException e )
        {
            e.printStackTrace(  );
        }
    }
}
