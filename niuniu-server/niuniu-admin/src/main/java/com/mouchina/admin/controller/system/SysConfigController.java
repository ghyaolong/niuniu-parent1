package com.mouchina.admin.controller.system;

import com.mouchina.admin.service.api.system.StateService;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.system.SystemGlobalConfig;
import com.mouchina.moumou_server_interface.system.SystemService;
import com.mouchina.moumou_server_interface.view.SystemResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping( "/system/config" )
public class SysConfigController
{
    @Resource
    private SystemService systemService;
    @Resource
    private StateService stateService;

    @RequestMapping( value = "/new", method = RequestMethod.GET )
    private String configNew( ModelMap modelMap )
    {
        return "system/config/new";
    }

    @RequestMapping( value = "/add", method = RequestMethod.POST )
    private String configAdd( SystemGlobalConfig config, ModelMap modelMap ) {
        config.setCreateTime(new Date());
        SystemResult<SystemGlobalConfig> systemGlobalConfigSystemResult= systemService.addSystemGlobalConfig(config);
        return "redirect:list.html";
    }

    @RequestMapping( value = "/show", method = RequestMethod.GET )
    private String configShow( ModelMap modelMap )
    {
        return "config/config/show";
    }

    @RequestMapping( value = "/list", method = RequestMethod.GET )
    private String configList( ModelMap modelMap )
    {
        return "system/config/list";
    }
    @RequestMapping( value = "/edit/{id}", method = RequestMethod.GET )
    private String configEdit( @PathVariable
                             Integer id, ModelMap modelMap )
    {
        modelMap.put("systemConfig",
                systemService.selectSystemGlobalConfig(id).getContent());

        return "system/config/edit";
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    private String configUpdate(SystemGlobalConfig systemGlobalConfig,HttpServletRequest request,ModelMap modelMap){
        systemGlobalConfig.setModifyTime(new Date());
        systemService.updateSystemGlobalConfig(systemGlobalConfig);
        return "system/config/list";
    }
    @RequestMapping( value = "/list/query", method = RequestMethod.GET )
    private String configQuery( HttpServletRequest request, ModelMap modelMap )
    {
        Map<String,Object> map = new HashMap<>(  );
        Page page = new Page( 0, 10 );
        int begin = 0;

        if ( request.getParameter( "begin" ) != null )
        {
            begin = Integer.valueOf( request.getParameter( "begin" ).toString(  ) ).intValue(  );
        }

        page.setBegin( begin );
        map.put( "page", page );

        List<SystemGlobalConfig> systemGlobalConfigs = systemService.selectListSystemGlobalConfig( map ).getContent(  );
        modelMap.put( "systemGlobalConfigs", systemGlobalConfigs );

        return "";
    }
    @RequestMapping(value = "/get/order_state",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getOrderState(ModelMap modelMap) {
        return stateService.getOrderState();
    }

    @RequestMapping(value = "/get/order_detail_state",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getOrderDetailState(ModelMap modelMap) {
        return stateService.getOrderDetailState();
    }

    @RequestMapping(value = "/get/cash_coupon_state",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getCashCouponState(ModelMap modelMap) {
        return stateService.getCashCouponState();
    }

    @RequestMapping(value = "/get/pay_channel_state",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getPayChannelState(ModelMap modelMap) {
        return stateService.getPayChannelState();
    }
    @RequestMapping(value = "/get/getPayOrderState",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getPayOrderStateState(ModelMap modelMap) {
        return stateService.getPayOrderState();
    }
    @RequestMapping(value = "/get/getOrderRemiburseState",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getOrderRemiburseState(ModelMap modelMap) {
        return stateService.getOrderRemiburseState();
    }
    @RequestMapping(value = "/get/withbrawalState",method = RequestMethod.GET)
    @ResponseBody
    private Map<Integer,String> getWithbrawalState(ModelMap modelMap) {
        return stateService.getWithbrawalState();
    }


//    @RequestMapping(value = "/get/getRange",method = RequestMethod.GET)
//    @ResponseBody
//    private Map<Integer,String> getRange(ModelMap modelMap,HttpServletRequest _req) {
//
//        String city=_req.getParameter("city");
//        String x = _req.getParameter("x");
//        String y = _req.getParameter("y");
//        Map searchMap = new HashMap();
//        searchMap.put("city", city);
//
//        LocationResult<List<CustomDistrict>> locationResult = customDistrictService.getCustomDistricts(searchMap);
//
//        List<CustomDistrict> customDistricts = locationResult.getContent();
//        List<String> strArr = new ArrayList<String>();
//        for (CustomDistrict customDistrict : customDistricts) {
//
//            String str = customDistrict.getRange();
//            String[] arr = str.split(" ");
//            List<PointJ> rangeArr = new ArrayList<PointJ>();
//            for (String s : arr) {
//                String[] point = s.split(",");
//                double lng = Double.valueOf(point[0]);
//                double lat = Double.valueOf(point[1]);
//
//                PointJ pointJ = new PointJ();
//                pointJ.setX(lng);
//                pointJ.setY(lat);
//                rangeArr.add(pointJ);
//            }
//            PointJ pointJ = new PointJ();
//            pointJ.setX(Double.valueOf(x));
//            pointJ.setY(Double.valueOf(y));
//            if (IsPtInPoly(pointJ, rangeArr)) {
//                strArr.add(customDistrict.getName());
//            }
//        }
//        Map map = new HashMap();
//        map.put("result", strArr);
//        return map;
//    }

    /**
     *
     * @param PointJ 当前经度
     * @param APoints 多边形点集合
     * @return
     */
    public boolean IsPtInPoly( PointJ pointJ, List<PointJ> APoints) {

        int iSum = 0, iCount;
//        double dLon1, dLon2, dLat1, dLat2, dLon;
        PointJ pointJ1, pointJ2;
        double precision = 2e-10;
        boolean boundOrVertex = true;//如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true

        if (APoints == null || APoints.size() < 3)
            return false;

        iCount = APoints.size();

        pointJ1 = APoints.get(0);
        for (int i = 1; i < iCount; i++) {
            if (pointJ.equals(pointJ1))
                return boundOrVertex;

            pointJ2 = APoints.get(i%iCount);

            if (pointJ.getY() < Math.min(pointJ1.getY(), pointJ2.getY())) {
                pointJ1 = pointJ2;
                continue;
            }

            if (pointJ.getY() > Math.min(pointJ1.getY(), pointJ2.getY()) && pointJ.getY() < Math.max(pointJ1.getY(), pointJ2.getY())) {
                if (pointJ.getX() <= Math.max(pointJ1.getX(), pointJ2.getX())) {
                    if (pointJ1.getY() == pointJ2.getY() && pointJ.getX() >= Math.min(pointJ1.getX(), pointJ2.getX())) {
                        return boundOrVertex;
                    }
                    if (pointJ1.getX() == pointJ2.getX()) {
                        if (pointJ1.getX() == pointJ.getX()) {
                            return boundOrVertex;
                        } else
                            ++iSum;
                    } else {
                        double xinters = (pointJ.getY() - pointJ1.getY()) * (pointJ2.getX() - pointJ1.getX()) / (pointJ2.getY() - pointJ1.getY()) + pointJ1.getX();
                        if (Math.abs(pointJ.getX() - xinters) < precision) {
                            return boundOrVertex;
                        }
                        if (pointJ.getX() < xinters) {
                            ++iSum;
                        }
                    }
                }
            } else { //线穿过顶点处理   特殊情况
                if (pointJ.getY() == pointJ2.getY() && pointJ.getX() <= pointJ2.getX()) {   //p过p2
                    PointJ pointJ3 = APoints.get((i + 1) % iCount);
                    if (pointJ.getY() >= Math.min(pointJ1.getY(), pointJ3.getY()) && pointJ.getY() <= Math.max(pointJ1.getY(), pointJ3.getY())) {
                        ++iSum;
                    } else
                        iSum += 2;

                }
            }
            pointJ1 = pointJ2;

//            if (i == iCount - 1)
//            {
//                dLon1 = APoints.get(i).getX();
//                dLat1 = APoints.get(i).getY();
//                dLon2 = APoints.get(0).getX();
//                dLat2 = APoints.get(0).getY();
//            }
//            else
//            {
//                dLon1 = APoints.get(i).getX();
//                dLat1 = APoints.get(i).getY();
//                dLon2 = APoints.get(i+1).getX();
//                dLat2 = APoints.get(i+1).getY();
//            }
//            //以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
//            if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1)))
//            {
//                if (Math.abs(dLat1 - dLat2) > 0)
//                {
//                    //得到 A点向左射线与边的交点的x坐标：
//                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat)) / (dLat1 - dLat2);
//
//                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
//                    if (dLon < ALon)
//                        iSum++;
//                }
//            }
        }
        if (iSum % 2 == 0)
            return false;
        return true;
    }
    public class PointJ{
         private double x;
         private double y;

         public double getX() {
             return x;
         }

         public void setX(double x) {
             this.x = x;
         }

         public double getY() {
             return y;
         }

         public void setY(double y) {
             this.y = y;
         }
     }
//    public static  void main(String[] arg) {
//        Point point = new Point();
//        point.setLocation(108.95378, 34.315718);
//        Point point1 = new Point();
//        point1.setLocation(109.002073, 34.315837);
//        Point point2 = new Point();
//        point2.setLocation(109.002217, 34.30713);
//        Point point3 = new Point();
//        point3.setLocation(108.978214, 34.307011);
//        Point point4 = new Point();
//        point4.setLocation(108.979364, 34.283033);
//        Point point5 = new Point();
//        point5.setLocation(108.929059, 34.281959);
//        Point point6 = new Point();
//        point6.setLocation(108.929921, 34.314764);
//        Point point7 = new Point();
//        point7.setLocation(108.954211, 34.315479);
//
//        Point tPoint = new Point();
//        tPoint.setLocation(108.947743, 34.29049);
//        Point[] points = new Point[]{
//                point, point1, point2, point3, point4, point5, point6, point7
//        };
//
//    }

}
