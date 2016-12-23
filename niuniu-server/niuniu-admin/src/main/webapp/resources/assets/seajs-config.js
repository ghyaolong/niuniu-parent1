/*
 * sea-config  -douzy  
 */
seajs.production=false;//deploy:true debug:false

var url={
	domain:"http://"+location.host,
	directory_top:_contentPath+"/resources/assets/"
};

var base_url=url.directory_top+"sea-modules/"  
   ,gallery_base=""
   ,package_info="banjiajia/1.0.0/";

if(!seajs.production)
{
	base_url=url.directory_top+"src/";
	gallery_base=url.domain+url.directory_top+"sea-modules/";
	package_info="";
}
/*map end*/
seajs.config({
	plugins :['style'],
	base:base_url,
    alias:{
    	//system
    	"system-smstemplate-list":package_info+"scripts/system/sms/template/list",
    	"system-config-list":package_info+"scripts/system/config/list",
    	//order
    	"order-order-list":package_info+"scripts/order/order/list",
		"order-order-detail":package_info+"scripts/order/order/detail",
    	"order-pay-list":package_info+"scripts/order/payorder/list",
    	"order-schedule-list":package_info+"scripts/order/schedule/list",
    	"order-userremiburse-list":package_info+"scripts/order/userremiburse/list",
    	"order-userremiburse-edit":package_info+"scripts/order/userremiburse/edit",
    	"order-usertransactiondetails-list":package_info+"scripts/order/usertransactiondetails/list",
    	"order-userluckybagdetails-list":package_info+"scripts/order/userluckybagdetails/list",
    	//welfare
    	"welfare-cashcoupon-config-list":package_info+"scripts/welfare/cashcoupon/config/list",
    	"welfare-cashcoupon-config-new":package_info+"scripts/welfare/cashcoupon/config/new",
    	"welfare-cashcoupon-config-edit":package_info+"scripts/welfare/cashcoupon/config/edit",
    	"welfare-cashcoupon-list":package_info+"scripts/welfare/cashcoupon/list",
    	"welfare-discount-list":package_info+"scripts/welfare/discount/list",
		"welfare-discount-edit":package_info+"scripts/welfare/discount/edit",
		"welfare-edit":package_info+"scripts/welfare/cashcoupon/edit",
    	//coures
    	"course-coursepublic-zizhu":package_info+"scripts/course/coursepublic/zizhu",
    	"course-coursepublic-list":package_info+"scripts/course/coursepublic/list",
    	"course-coursepublic-new":package_info+"scripts/course/coursepublic/new",
    	"course-coursepublic-show":package_info+"scripts/course/coursepublic/show",
    	"course-course-list":package_info+"scripts/course/course/list",
    	"course-course-new":package_info+"scripts/course/course/new",
    	"course-course-show":package_info+"scripts/course/course/show",
		"course-course-edit":package_info+"scripts/course/course/edit",
		"course-course-manage":package_info+"scripts/course/course/manage",
    	"course-service-list":package_info+"scripts/course/service/list",
    	"course-service-new":package_info+"scripts/course/service/new",
    	"course-service-edit":package_info+"scripts/course/service/edit",
		//comments
		"comments-order-list":package_info+"scripts/comments/teacher/list",
		//agent
		"agent-income-list":package_info+"scripts/agent/income/list",
		"agent-incomesum-list":package_info+"scripts/agent/incomesum/list",
		"agent-sub-list":package_info+"scripts/agent/sub/list",
		"agent-show":package_info+"scripts/agent/show",
		"agent-summary-list":package_info+"scripts/summary/list",
		"agent-statistics-list":package_info+"scripts/agent/statistics/list",
		"agent-statistics-new":package_info+"scripts/agent/statistics/new",
		"agent-recommended-list":package_info+"scripts/agent/recommended/list",
		"agent-promotion-list":package_info+"scripts/agent/promotion/list",
		//service---members
		"members-agent-income-list":package_info+"scripts/members/agent/income/list",
		"members-agent-invite-list":package_info+"scripts/members/agent/invite/list",
		"members-agent-incomesum-list":package_info+"scripts/members/agent/incomesum/list",
		"members-agent-incomesum-edit":package_info+"scripts/members/agent/incomesum/edit",
		"members-agent-commission-list":package_info+"scripts/members/agent/commission/list",
		"members-agent-new":package_info+"scripts/members/agent/new",
    	"members-agent-edit":package_info+"scripts/members/agent/edit",
		"members-agent-show":package_info+"scripts/members/agent/show",
		"members-agent-list":package_info+"scripts/members/agent/list",
		
		//商户店铺审核
		"business-shop-list":package_info+"scripts/members/businessshop/list",
		//认证信息审核
		"business-approve-list":package_info+"scripts/business/approve/list",
		//Banner位
		"banner-list":package_info+"scripts/advert/banner/list",
		//公益广告
		"publicWelFare-list":package_info+"scripts/advert/publicWelFare/list",
		//添加公益广告
		"advert-publicWelfareAdd":package_info+"scripts/advert/publicWelFare/add",
		
    	"members-user-new":package_info+"scripts/members/user/new",
    	"members-user-edit":package_info+"scripts/members/user/edit",
		"members-user-show":package_info+"scripts/members/user/show",
    	
    	"members-user-query":package_info+"scripts/members/user/query",
    	"members-teacher-new":package_info+"scripts/members/teacher/new",
    	"members-teacher-edit":package_info+"scripts/members/teacher/edit",
    	"members-teacher-list":package_info+"scripts/members/teacher/list",
		"assets-teacher-list":package_info+"scripts/assets/teacher/list",
		"welfare-withbrawal-list":package_info+"scripts/welfare/withbrawal/list",
    	//module
    	"user":package_info+"scripts/user/login",
    	"product-main":package_info+"scripts/product/main",
    	"template_main":package_info+"scripts/template/main",
    	"detail-main":package_info+"scripts/product/detail-main",
    	"wap-detail":package_info+"scripts/product/wap_detail",
    	"car":package_info+"scripts/product/car",
    	"list":package_info+"scripts/product/list",
		"resume":package_info+"scripts/members/teacher/resume",
		//role
		"permission-role":package_info+"scripts/permission/role/list",
		"permission-role-add":package_info+"scripts/permission/role/add",
		"permission-list":package_info+"scripts/permission/list",
		"permission-add":package_info+"scripts/permission/add",
		//statistics
		"statistics-user-new":package_info+"scripts/statistics/user/new",
		"statistics-order-new":package_info+"scripts/statistics/order/new",
		"statistics-schedule-chart":package_info+"scripts/statistics/schedule/chart",
		//feedback
		"feedback-list":package_info+"scripts/feedback/list",
		"advert-list":package_info+"scripts/advert/list",
		"advert-listOperatingActivities":package_info+"scripts/advert/listOperatingActivities",
		"advert-operatingActivitiesAdd":package_info+"scripts/advert/operatingActivitiesAdd",
		
		"activityConfig-list":package_info+"scripts/activityConfig/list",
		
		"business-list":package_info+"scripts/business/list",
    	//common
    	"map":package_info+"scripts/common/Map",
    	"pager":package_info+"scripts/common/pager",
    	"util":package_info+"scripts/common/util",
    	"DropDownMenu":package_info+"scripts/common/DropDownMenu",
    	"option":package_info+"scripts/common/option",
    	"proImgsLoad":package_info+"scripts/common/proImgsLoad",
		"plugin":package_info+"scripts/common/pluginInIt",
		"baiduMap":package_info+"scripts/common/baiduMap",
		"state":package_info+"scripts/common/state",
		"inspinia":package_info+"scripts/common/inspinia",
		"bjjChart":package_info+"scripts/common/bjjChart",

		//location
		"pictureMap":package_info+"scripts/location/pictureMap",
		"picMapDataEx":package_info+"scripts/location/picMapDataEx",
		//business common
		"area":package_info+"scripts/business_common/area",
		//tree
		"tree":package_info+"scripts/course/course/tree",
		"menuTree":package_info+"scripts/permission/menu/tree",
		//menu tree
		"menu-tree":package_info+"scripts/common/menu",
    	//lib
		"bootstrap-paginator":gallery_base+"gallery/bootstrap-paginator/0.5.0/bootstrap-paginator",
    	"validation":gallery_base+"gallery/bootstrap3-validation/validation",
    	"bootstrap":gallery_base+"gallery/bootstrap/3.1.1/bootstrap.min",
    	"zoom":gallery_base+"gallery/zoom/2.3/jquery.imagezoom.min",
    	"showLoading":gallery_base+"gallery/showLoading/1.0.0/jquery.showLoading.min",
    	"owl-carousel":gallery_base+"gallery/owl/carousel/1.0.0/owl.carousel.min",
    	"modernizr":gallery_base+"gallery/modernizr/2.6.2/modernizr",
    	"catslider":gallery_base+"gallery/catslider/1.0.0/jquery.catslider",
    	"jPanelMenu":gallery_base+"gallery/jquery/jPanelMenu/1.3.0/jquery.jpanelmenu.min",
    	"jquery":gallery_base+"gallery/jquery/jquery/2.1.0/jquery.js",
    	"jquery-UI":gallery_base+"gallery/jquery/UI/1.10.4/jquery-ui-1.10.4.custom.min.js",
    	"spinner":gallery_base+"gallery/spinner/1.0.0/jquery.spinner",
    	"swiper":gallery_base+"gallery/swiper/2.7.0/idangerous.swiper.min",
    	"masonry":gallery_base+"gallery/masonry/3.1.1/masonry",
   		"imagesLoaded":gallery_base+"gallery/imagesLoaded/3.0.4/imagesLoaded",
   		"lightbox":gallery_base+"gallery/lightbox/2.6.0/lightbox",
   		"handlebars":gallery_base+"gallery/handlebars/1.3.0/handlebars",
    	"popimg":gallery_base+"gallery/popimg/1.0.0/popimg",
    	"bootstrap":gallery_base+"gallery/bootstrap/3.0.0/bootstrap",
    	"bootstrap-pagy":gallery_base+"gallery/bootstrap-pagy/2.0.0/pagy.min",
    	"bootstrap-datepicker":gallery_base+"gallery/date-time/bootstrap-datepicker.min.js",
    	"bootstrap-timepicker":gallery_base+"gallery/date-time/bootstrap-timepicker.min.js",
		"bootstrap-datetimepicker":gallery_base+"gallery/date-time/bootstrap-datetimepicker.min.js",
		/*"bootstrap-datetimepicker-locales":gallery_base+"gallery/date-time/locales/bootstrap-datetimepicker.zh-CN.js",*/
    	"daterangepicker":gallery_base+"gallery/date-time/daterangepicker.min.js",
    	"moment":gallery_base+"gallery/date-time/moment.min.js",
    	"bootstrap-select":gallery_base+"gallery/bootstrap-select/1.5.2/bootstrap-select",
    	"form":gallery_base+"gallery/Form/1.0.0/form",
    	"$":gallery_base+"gallery/jquery/jquery/2.1.0/jquery.js",
    	"$183":gallery_base+"gallery/jquery/jquery/1.8.3/jquery-1.8.3.min",
		"zTree":gallery_base+"gallery/z-tree/3.5/jquery.ztree.all-3.5.min",
		"colpick":gallery_base+"gallery/colpick/colpick.js",
    	 "ace":gallery_base+"gallery/ace/ace.min.js",
    	 "ace-extra":gallery_base+"gallery/ace/ace-extra.min.js",
    	 "ace-elements":gallery_base+"gallery/ace/ace-elements.min.js",
    	 "ace-html5":gallery_base+"gallery/ace/html5shiv.min.js",
    	 "ace-respond":gallery_base+"gallery/ace/respond.min.js",
    	 "dropzone":gallery_base+"gallery/ace/dropzone.min.js",
    	 "chosen":gallery_base+"gallery/ace/chosen.jquery.min.js",
    	 "jggrid":gallery_base+"gallery/jquery/jqGrid/jquery.jqGrid.min.js",
    	 "gridlocale":gallery_base+"gallery/jquery/jqGrid/i18n/grid.locale-en.js"
    	
    },
    map: [
//         [/^(.*(user|).*\.js)(.*)$/i, '$1?' + (new Date()).valueOf()]
        ],
    charset: 'utf-8' 
});