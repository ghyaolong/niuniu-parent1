package com.mouchina.base.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.SHA1;

/**
 * 缓存Key工具类
 *
 * @author
 *
 */
public class CacheKey
{
    /**
     * 主键关键字分隔符
     */
    public static final String SEPARATOR = "$";

    /**
     * 其他键关键字分隔符
     */
    public static final String SEPARATOR_OTHER = "#";

    /**
     * 其他键关键字分隔符
     */
    public static final String SEPARATOR_MAP = "@";

    /**
     * 通过实体类和主键生成缓存中的key
     *
     * @param <T>
     *
     * @return key
     */
    @SuppressWarnings( "rawtypes" )
    public static String getEntityKeyByPrimaryKey( Class clazz, Serializable primaryKey )
    {
        return SHA1.hex_sha1( clazz.getName(  ) + SEPARATOR + primaryKey );
    }

    /**
     * 通过实体类和其他键生成缓存中的key
     *
     * @return key
     */
    @SuppressWarnings( "rawtypes" )
    public static String getEntityKeyByForeignKey( Class clazz, Serializable key )
    {
        return SHA1.hex_sha1( clazz.getName(  ) + SEPARATOR_OTHER + key );
    }

    /**
     * 通过查询条件生成缓存中的key
     *
     * @return map 查询条件
     */
    @SuppressWarnings( "rawtypes" )
    public static String getListKeyByMap( Class clazz, Map<String, Object> map )
    {
        String key = clazz.getName(  );

        if ( map != null )
        {
            Iterator<String> iterator = map.keySet(  ).iterator(  );

            while ( iterator.hasNext(  ) )
            {
                String k = iterator.next(  );

                if ( map.get( k ) instanceof Date )
                {
                    key += ( SEPARATOR_OTHER + k + SEPARATOR + ( (Date) map.get( k ) ).getTime(  ) );
                } else
                {
                    key += ( SEPARATOR_OTHER + k + SEPARATOR + map.get( k ) );
                }
            }
        }

        return SHA1.hex_sha1( key );
    }

    /**
	 * 通过查询条件生成缓存中的key
	 * 
	 * @return map 查询条件
	 */
	@SuppressWarnings("rawtypes")
	public static String getListKeyByMap(Class clazz,Serializable listKey, Map<String, Object> map) {
		String key = clazz.getName()+SEPARATOR_OTHER+listKey;

		if (map != null) {
			Iterator<String> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {
				String k = iterator.next();

				if (map.get(k) instanceof Date) {
					key += (SEPARATOR_OTHER + k + SEPARATOR + ((Date) map
							.get(k)).getTime());
				} else if (map.get(k) instanceof Page) {
					key += (SEPARATOR_OTHER + k + SEPARATOR
							+ ((Page) map.get(k)).getBegin() + "_"
							+ ((Page) map.get(k)).getCount() + "_"
							+ ((Page) map.get(k)).getCurrent() + "_"
							+ ((Page) map.get(k)).getEnd() + "_"
							+ ((Page) map.get(k)).getLength() + "_" + ((Page) map
							.get(k)).getTotal());
				} else {
					key += (SEPARATOR_OTHER + k + SEPARATOR + map.get(k));
				}

			}
		}
		return SHA1.hex_sha1(key);
	}
    /**
     * 通过查询条件生成缓存中的key
     *
     * @return map 查询条件
     */
    @SuppressWarnings( "rawtypes" )
    public static String getMapKeyByMap( Class mapKey, Class clazz, Map<String, Object> map )
    {
        String keyValue = clazz.getName(  );

        String key = mapKey.getName(  );

        if ( map != null )
        {
            Iterator<String> iterator = map.keySet(  ).iterator(  );

            while ( iterator.hasNext(  ) )
            {
                String k = iterator.next(  );

                if ( map.get( k ) instanceof Date )
                {
                    keyValue += ( SEPARATOR_MAP + k + SEPARATOR + ( (Date) map.get( k ) ).getTime(  ) );
                } else
                {
                    keyValue += ( SEPARATOR_MAP + k + SEPARATOR + map.get( k ) );
                }
            }
        }

        return SHA1.hex_sha1( key + keyValue );
    }

    public static void main( String[] args )
    {
        // 测试左侧菜单
        Map<String, Object> map = new HashMap<String, Object>(  );
        map.put( "parentid", 0 );
        map.put( "order", "seqence" );
    }
}
