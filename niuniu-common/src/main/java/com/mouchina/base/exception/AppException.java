package com.mouchina.base.exception;

public class AppException
    extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 6948341692366409728L;

    public Integer getStateCode(  )
    {
        return stateCode;
    }

    public void setStateCode( Integer stateCode )
    {
        this.stateCode = stateCode;
    }

    private Integer stateCode;

    public AppException( int stateCode )
    {
        super(  );

        if ( stateCode > 10000000 )
        {
            this.stateCode = stateCode;
        } else
        {
            this.stateCode = 10000000;
        }
    }
}
