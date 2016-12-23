package com.mouchina.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 *
 */
public class DateUtils
{
    /** 取得服务器时间 */
    public static Date serverTime(  )
    {
        return new Date(  );
    }

    public static Date parseDate( final String date )
                          throws Exception
    {
        return parse( date, "yyyy-MM-dd" );
    }

    /**
     * @param date
     *            格式必须为yyyy-MM-dd HH:mm:ss
     * @throws PubException
     *             如果参数错误或解析错误，抛出此异常
     */
    public static Date parse( final String date )
                      throws Exception
    {
        return parse( date, "yyyy-MM-dd HH:mm:ss" );
    }

    public static Date parseStartDate( final String date )
                               throws Exception
    {
        return parse( date + " 00:00:00", "yyyy-MM-dd HH:mm:ss" );
    }

    public static Date parseEndDate( final String date )
                             throws Exception
    {
        return parse( date + " 23:59:59", "yyyy-MM-dd HH:mm:ss" );
    }

    /**
     * @param date
     *            日期字符串
     * @param format
     *            日期解析格式
     * @throws PubException
     *             如果参数错误或解析错误，抛出此异常
     * @return 转换后的日期类
     */
    public static Date parse( final String date, final String format )
                      throws Exception
    {
        if ( date == null )
        {
            throw new Exception( "参数date不能为空！" );
        }

        if ( format == null )
        {
            throw new Exception( "参数format不能为空！" );
        }

        Map<String, SimpleDateFormat> map = cache.get(  );

        if ( map == null )
        {
            map = new HashMap<String, SimpleDateFormat>(  );
            cache.set( map );
        }

        SimpleDateFormat dateFormat = map.get( format );

        if ( dateFormat == null )
        {
            dateFormat = new SimpleDateFormat( format );
            map.put( format, dateFormat );
        }

        try
        {
            return dateFormat.parse( date );
        } catch ( final ParseException e )
        {
            throw new Exception( "日期格式解析错误。日期字符串：" + date + "；日期解析格式：" + format );
        }
    }

    /**
     * 时间戳转换成对应的Date
     * @param timeMills
     * @return
     * @throws Exception
     */
    public static Date timeStampToDate(long timeMills) throws Exception{
    	Date date;
    	SimpleDateFormat format;
		try {
			format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String d = format.format(timeMills);  
	        
			date = format.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new Exception( "时间戳转换成Date解析错误。时间戳值 ：" + timeMills);
		}
		
		return date;
    }
    
    /**
     * @param int 数字类型时间
     * @throws PubException
     *             如果参数错误或解析错误，抛出此异常
     * @return 返回格式为m分s秒格式
     */
    public static String IntToDate( final int usetime )
                            throws Exception
    {
        String relUsetime = null;

        if ( ( (int) usetime / 60000 ) >= 1 )
        {
            relUsetime = ( (int) usetime / 60000 ) + "分" + ( (int) ( usetime / 1000 ) % 60 ) + "秒";
        } else
        {
            relUsetime = ( usetime / 1000 ) + "秒";
        }

        return relUsetime;
    }

    public static String formatTime( final Date date )
                             throws Exception
    {
        return format( date, "HH:mm:ss" );
    }

    public static String formatDate( final Date date )
                             throws Exception
    {
        return format( date, "yyyy-MM-dd" );
    }

    public static String formatSolrTime( final Date date )
                                 throws Exception
    {
        return format( date, "yyyy-MM-dd-HH-mm-ss" );
    }

    /**
     * @throws PubException
     *             如果参数错误或解析错误，抛出此异常
     * @return 返回格式为yyyy-MM-dd HH:mm:ss的日期字符串
     */
    public static String format( final Date date )
                         throws Exception
    {
        return format( date, "yyyy-MM-dd HH:mm:ss" );
    }

    /**
     * @param format
     *            "example:yyyy/MM/dd"
     * @throws PubException
     *             如果参数错误或解析错误，抛出此异常
     */
    public static String format( final Date date, final String format )
                         throws Exception
    {
        if ( date == null )
        {
            throw new Exception( "参数date不能为空！" );
        }

        if ( format == null )
        {
            throw new Exception( "参数format不能为空！" );
        }

        Map<String, SimpleDateFormat> map = cache.get(  );

        if ( map == null )
        {
            map = new HashMap<String, SimpleDateFormat>(  );
            cache.set( map );
        }

        SimpleDateFormat dateFormat = map.get( format );

        if ( dateFormat == null )
        {
            dateFormat = new SimpleDateFormat( format );
            map.put( format, dateFormat );
        }

        return dateFormat.format( date );
    }

    /** 为减小创建SimpleDateFormat的开销，设立缓存。 (Map的key为日期转换格式) */
    private static final ThreadLocal<Map<String, SimpleDateFormat>> cache =
        new ThreadLocal<Map<String, SimpleDateFormat>>(  );

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate(  )
    {
        return new Date(  );
    }

    /**
     * 获取现在时间字符串
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateString(  )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    /**
     * 获取现在时间字符串
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateString( String fmt )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( fmt );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    public static Date getTodayBeginDate(  )
    {
        Date now = new Date(  );
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

        try
        {
            rs = sdf.parse( sdf.format( now ) );
        } catch ( ParseException e )
        {
        }

        return rs;
    }

    public static Date getBeginDate( String dateString )
                             throws ParseException
    {
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

        try
        {
            rs = sdf.parse( sdf.format( parseDate( dateString ) ) );
        } catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        return rs;
    }

    public static Date getDateTime( String dateString )
    {
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

        try
        {
            rs = sdf.parse( sdf.format( parse( dateString ) ) );
        } catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        return rs;
    }

    public static Date getDate( Date date )
    {
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

        try
        {
            rs = sdf.parse( sdf.format( date ) );
        } catch ( ParseException e )
        {
        }

        return rs;
    }
    public static Date getDateByDay( String date,String day)
    {
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-"+day );

        try
        {
            rs = sdf.parse(date);
        } catch ( ParseException e )
        {
        }

        return rs;
    }
    public static Date getDateyyyyMMddHHmm( Date date )
    {
        Date rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );

        try
        {
            rs = sdf.parse( sdf.format( date ) );
        } catch ( ParseException e )
        {
        }

        return rs;
    }

    public static String getPayOrderMDHMDate( Date startDate, Date endDate )
    {
        String rs = getMDHMDate( startDate ) + "-" + getHMDate( endDate );

        return rs;
    }

    public static String getMDHMDate( Date date )
    {
        String rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "MM月dd日HH:mm" );
        rs = sdf.format( date );

        return rs;
    }

    public static String getHMDate( Date date )
    {
        String rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
        rs = sdf.format( date );

        return rs;
    }

    public static String getNowDateStringNo(  )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmm" );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    public static String getNowDateStringALL(  )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmssSSS" );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    public static String getDatailsNowDateStringNo(  )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmm" );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    public static String getPayNowDateStringNo(  )
    {
        Date currentTime = new Date(  );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmm" );
        String dateString = formatter.format( currentTime );

        return dateString;
    }

    public static String getDateStringNo( Date date )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmm" );
        String dateString = formatter.format( date );

        return dateString;
    }

    public static String getDateStringYYYY( Date date )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy" );
        String dateString = formatter.format( date );

        return dateString;
    }

    public static String getDateStringYMD( Date date )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd" );
        String dateString = formatter.format( date );

        return dateString;
    }

    public static String getDateString( Date date )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
        String dateString = formatter.format( date );

        return dateString;
    }

    public static Long getDateLongNo( Date date )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmm" );
        String dateString = formatter.format( date );

        return Long.valueOf( dateString );
    }

    /***
     * 日期月份减一个月
     *
     * @param datetime
     *            日期(2014-11)
     * @return 2014-10
     */
    public static String dateFormat( String datetime )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM" );
        Date date = null;

        try
        {
            date = sdf.parse( datetime );
        } catch ( ParseException e )
        {
            e.printStackTrace(  );
        }

        Calendar cl = Calendar.getInstance(  );
        cl.setTime( date );
        cl.add( Calendar.MONTH, -1 );
        date = cl.getTime(  );

        return sdf.format( date );
    }

    public static String dateFormat( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM" );

        return sdf.format( date );
    }
 
    /**
     * 返回给定date对象interval分钟之前的date
     * @param date
     * @return
     */
    public static Date getSeveralMinutesAgoDate(Date date, int interval){
    	Calendar now = Calendar.getInstance();
    	now.setTime(date);
    	now.add(Calendar.MINUTE, -interval);
    	
    	return now.getTime();
    }

    /****
     * 传入具体日期 ，返回具体日期减一个月。
     *
     * @param date
     *            日期(2014-04-20)
     * @return 2014-03-20
     * @throws java.text.ParseException
     */
    public static String subMonth( String date )
                           throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date dt = sdf.parse( date );
        Calendar rightNow = Calendar.getInstance(  );
        rightNow.setTime( dt );

        rightNow.add( Calendar.MONTH, -1 );

        Date dt1 = rightNow.getTime(  );
        String reStr = sdf.format( dt1 );

        return reStr;
    }

    /****
     * 获取月末最后一天
     *
     * @param sDate
     *            2014-11-24
     * @return 30
     */
    public static String getMonthMaxDay( String sDate )
    {
        SimpleDateFormat sdf_full = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance(  );
        Date date = null;

        try
        {
            date = sdf_full.parse( sDate + "-01" );
        } catch ( ParseException e )
        {
            e.printStackTrace(  );
        }

        cal.setTime( date );

        int last = cal.getActualMaximum( Calendar.DATE );

        return String.valueOf( last );
    }
    /****
     * 获取月末最后一天
     *
     * @param sDate
     *            2014-11-24
     * @return 30
     */
    public static Date getMonthEndDay( Date sDate )
    {
    	
    	
    	Calendar ca = Calendar.getInstance();  
        ca.setTime( sDate );
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        
        
       
        Calendar cale = Calendar.getInstance();  
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天

        return cale.getTime();
    }
   

    // 判断是否是月末
    public static boolean isMonthEnd( Date date )
    {
        Calendar cal = Calendar.getInstance(  );
        cal.setTime( date );

        if ( cal.get( Calendar.DATE ) == cal.getActualMaximum( Calendar.DAY_OF_MONTH ) )
        {
            return true;
        } else{
            return false;
        }
    }

    /***
     * 日期减一天、加一天
     *
     * @param option
     *            传入类型 pro：日期减一天，next：日期加一天
     * @param _date
     *            2014-11-24
     * @return 减一天：2014-11-23或(加一天：2014-11-25)
     */
    public static String checkOption( String option, String _date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;

        try
        {
            date = (Date) sdf.parse( _date );
        } catch ( ParseException e )
        {
            e.printStackTrace(  );
        }

        return checkOptionOne( option, date );
    }

    public static String checkOptionOne( String option, Date date )
    {
        return checkOption( option, date, 1 );
    }

    public static String checkOption( String option, Date date, int day )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

        return sdf.format( checkOptionRetrunDate( option, date, day ) );
    }

    public static Date checkOptionRetrunDate( String option, Date date, int day )
    {
        Calendar cl = Calendar.getInstance(  );
        cl.setTime( date );

        if ( "pre".equals( option ) )
        {
            // 时间减一天
            cl.add( Calendar.DAY_OF_MONTH, day );
        } else if ( "next".equals( option ) )
        {
            // 时间加一天
            cl.add( Calendar.DAY_OF_YEAR, day );
        } else
        {
        }

        return cl.getTime(  );
    }

    /***
     * 判断日期是否为当前月， 是当前月返回当月最小日期和当月目前最大日期以及传入日期上月的最大日和最小日
     * 不是当前月返回传入月份的最大日和最小日以及传入日期上月的最大日和最小日
     *
     * @param date
     *            日期 例如：2014-11
     * @return String[] 开始日期，结束日期，上月开始日期，上月结束日期
     * @throws java.text.ParseException
     */
    public static String[] getNowPreDate( String date )
                                  throws ParseException
    {
        String[] str_date = new String[4];
        Date now = new Date(  );
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM" );
        SimpleDateFormat sdf_full = new SimpleDateFormat( "yyyy-MM-dd" );
        String stMonth = sdf.format( now );
        String stdate = ""; // 开始日期
        String endate = ""; // 结束日期
        String preDate_start = ""; // 上月开始日期
        String preDate_end = ""; // 上月结束日期

        // 当前月
        if ( date.equals( stMonth ) )
        {
            stdate = stMonth + "-01"; // 2014-11-01
            endate = sdf_full.format( now ); // 2014-11-24
            preDate_start = subMonth( stdate ); // 2014-10-01
            preDate_end = subMonth( endate ); // 2014-10-24
        } else
        {
            // 非当前月
            String monthMaxDay = getMonthMaxDay( date );
            stdate = date + "-01"; // 2014-10-01
            endate = date + "-" + monthMaxDay; // 2014-10-31
            preDate_start = subMonth( stdate ); // 2014-09-01
            preDate_end = subMonth( endate ); // 2014-09-30
        }

        str_date[0] = stdate;
        str_date[1] = endate;
        str_date[2] = preDate_start;
        str_date[3] = preDate_end;

        return str_date;
    }

    public static Date[] getWeekBeiginAndEndByDate( Date time )
    {
        time = getDate( time );

        Calendar cal = Calendar.getInstance(  );

        cal.setTime( time );

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get( Calendar.DAY_OF_WEEK ); // 获得当前日期是一个星期的第几天

        if ( 1 == dayWeek )
        {
            cal.add( Calendar.DAY_OF_MONTH, -1 );
        }

        cal.setFirstDayOfWeek( Calendar.MONDAY ); // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get( Calendar.DAY_OF_WEEK ); // 获得当前日期是一个星期的第几天

        cal.add( Calendar.DATE, cal.getFirstDayOfWeek(  ) - day ); // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

        Date beginDate = cal.getTime(  );

        cal.add( Calendar.DATE, 6 );

        Date endDate = cal.getTime(  );
        cal.add( Calendar.DATE, 1 );

        Date nexDate = cal.getTime(  );

        return new Date[] { beginDate, endDate, nexDate };
    }

    public static void main( String[] args )
    {
       Date date=new Date();
       System.out.println(getMonthEndDay(new Date()));
    }
}
