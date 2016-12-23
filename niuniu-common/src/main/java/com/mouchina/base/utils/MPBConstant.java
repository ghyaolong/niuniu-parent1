package com.mouchina.base.utils;

import org.springframework.stereotype.Component;

/**
 * MPB常量
 * @author dbb
 *
 */
@Component
public class MPBConstant
{
    /*public MPBConstant(){
            try
            {
                    reInit();
            } catch (Exception e)
            {
                    e.printStackTrace();
                    System.exit(1);
            }
    }
    private Properties props;


    public void reInit() throws FileNotFoundException, IOException
    {
            Properties prop = new Properties();
            prop.load(this.getClass()
                            .getResourceAsStream("/mongodb.properties"));
            this.props = prop;
            MPBConstant.init(this);
    }

    public String getProp(String key)
    {
            return (String) this.props.get(key);
    }

    */

    /**
    * @deprecated 转用MongoDB存储
    */

    /*
    public static String VoicePath = "/tmp/";

    // public static String ImgPath = "/app/mpbroker/imgpath/";
    // public static String SendVoicePath = "/tmp/";
    public static String ResourceURL = "http://res.365wos.org/";
    public static String CMSURL_WCMS_CONTENTURL = "http://we.365wos.org/mpcontent.action?articleid=";

    // MongoDB配置
    public static String MONGO_HOST = "192.168.101.101";
    public static int MONGO_PORT = 27017;
    public static String MONGO_USERNAME = "";
    public static String MONGO_PASSWORD = "";
    public static int MONGO_POOLSIZE = 10;
    public static int MONGO_BLOCKSIZE = 10;

    public static String getMPBrokerServiceURL(String nodeid){
         return "http://" + nodeid + ".mpb.weduty.com/i/service.action";
    }

    public static String getMPBrokerManageURL(String nodeid){
         return "http://" + nodeid + ".mpb.weduty.com/i/mpmanage.action";
    }

    public static void init(MPBConstant pu){
         CMSURL_WCMS_CONTENTURL = pu.getProp("mpbroker.cms.contenturl");
         MONGO_HOST = pu.getProp("mpbroker.mongodb.host");
         MONGO_PORT = Integer.parseInt(pu.getProp("mpbroker.mongodb.port"));
         MONGO_USERNAME = pu.getProp("mpbroker.mongodb.username");
         MONGO_PASSWORD = pu.getProp("mpbroker.mongodb.password");
    }*/
}
