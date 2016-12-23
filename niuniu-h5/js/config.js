// 主机地址的获取
var origin_href = window.location.href.substring(0, window.location.href.lastIndexOf('/'));

//请求地址
var originAPI = "http://test.mouchina.com";
//var originAPI = "http://sai1.quizii.com:8080/mtJingSaiService/service";
///var originAPI = "http://192.168.1.148/niuniu/1.txt?"

//请求参数
var requestInfo = {    
    "advert": "/user/advert/fetch",//用户获取广告
    "register": "/user/register",//用户注册
    "checkphone": "/user/register/phone/check",//注册手机验证
    "userinfo": "/user/info",//获取邀请码
    "userupd": "/user/info/update",//用户信息修改
    "collect": "/user/social/collect/add",//用户收藏    
    "guanzhu": "/user/social/fans/add",//用户关注
    "haobao": "/user/advert/redenvelope/top/list",//用户红包
};



//全局记录
var open="";//全局弹窗变量
var errorCode = "";
//缓存中记录的变量
//loginKey = "";
//business = "";//商家信息
//advert = "";//广告信息
//userinfo = "";//用户信息

//适配
window.use_screen_base = '640';

//css js images加载  把这里改成固定值，浏览器就会缓存
var nocache = new Date().getTime();
publicResourceLoader = new PublicResourceLoader({
	css: [
		//'./css/main.min.css?ver=' + nocache,
		'./css/normalize.css',
		'./css/flickity.min.css',
		'./css/css.css?ver=' + nocache,
	],
	scripts: [
		//'./js/main.min.js?ver=' + nocache,		
		 './js/jquery-1.11.0.min.js' ,
		 './js/jquery.easing.1.3.js',
		 './js/store.min.js',
		 './js/flickity.pkgd.min.js',
		 './js/public.js?ver=' + nocache,
	],
	images: [
		//'images/paihang.png',
		//'images/tp1.jpg',
	]
});




var errorArr=[];
errorArr[100000] = "服务器错误";
errorArr[100001] = "信息不完整";
errorArr[100002] = "签名验证失败";
errorArr[100003] = "请求异常";
errorArr[100004] = "验证码错误";
errorArr[100005] = "重复操作";
errorArr[100015] = "邀请码错误";
errorArr[100101] = "用户不存在";
errorArr[100102] = "用户验证错误";
errorArr[100111] = "用户无此权限";
errorArr[200001] = "没有获取到广告";
errorArr[200002] = "没有获取到红包";
errorArr[200011] = "广告奖励已经发完";
errorArr[200012] = "非法广告操作";
errorArr[200101] = "广告发布余额不足";
errorArr[200100] = "广告发布金额错误";

function showerror(errornum,strs){
	var errornum = errornum || "";
	var strs = strs || "请求出错";
	console.log(errorArr[errornum] );
	if(errorArr[errornum] != undefined){
		strs += ","+errorArr[errornum];
	}else{
		strs += ","+errornum;
	}
	return mess_tusi(strs);
}


function PublicResourceLoader(config) {
    this.css = config.css;
    this.scripts = config.scripts;
    this.images = config.images;
    this.head = document.getElementsByTagName('head')[0];


    this.load = function() {
        this.loadCSS();
        this.loadScript();
        this.loadImages();
    }
    this.loadCSS = function() {
        var that = this;
        this.css.forEach(function(csslink) {
            document.write(' <link href="' + csslink + '" rel="stylesheet" />')
        });
    }
    this.loadScript = function() {
        var that = this;
        this.scripts.forEach(function(scriptlink) {
            document.write('<script type="text/javascript" src="' + scriptlink + '"></script>')
        });
    }
    this.loadImages = function() {
        var that = this;
        this.images.forEach(function(imagelink) {
        	var images = new Image();	        
	        if(images.complete){
	        	console.log(imagelink+"提前加载完成");
	        	return false;
	        }
	        images.onload = function(){  
                console.log(imagelink+"提前加载完成");
            } 
            images.src = imagelink; 
        });
    }
   
    
    this.load();
}


