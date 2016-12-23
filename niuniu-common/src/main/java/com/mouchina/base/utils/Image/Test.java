package com.mouchina.base.utils.Image;

import com.mouchina.base.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class Test
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        // TODO Auto-generated method stub
        List<String> textList = new ArrayList<String>(  );
        textList.add( "名称: 秦镇凉皮秦镇凉皮秦镇凉" );
        textList.add( "产地: 山西运城" );
        textList.add( "材质: 大理石" );
        textList.add( "风格: 欧美" );
        textList.add( "价格: 100.00" );
        textList.add( "规则: 100mm*100mm" );

        String qRCodeUrl = "http://res.oddzone.cn/image/2014/10/07/10/10/44bbe9d537c44c58b13bc4bd6aa98963.png";
        TextQRCodeHelp.createTEXTImage( textList, qRCodeUrl );

        byte[] b = TextQRCodeHelp.createTEXTImageUrl( textList, qRCodeUrl );

        String imgStr = ImageUtils.GetBetysImageStr( b );
        ImageUtils.GenerateImage( imgStr, "/Users/larry/Desktop/img222.png" );
    }
}
