jQuery.support.cors = true;
var e = function(){
    if(document.all){
      window.event.returnValue = false;
    }else{
      event.preventDefault();
      event.stopPropagation();
    }
}
////event.preventDefault();
//event.stopPropagation();


/*ajax  提交到指定url处理数据，成功后执行指定函数
*@prame url  要提交的url
*@prame param  参数可以是对象类型 {"name":"zhi","name":"zhi"} 可以是字符串类型 id=54&tt=6
*@prame fun  提交成功后执行的函数,可以是字符串表示函数名 "test"  也可以直接是函数代码 function(){ 代码 }
*/
function ajaxrun(url,param,fun){  
       
    if(typeof(param)=="string"){ var params=param; }    
    if(typeof(param)=="object"){ var params=jQuery.param(param);    }
       $.ajax({
            url:url,
            type:"post",
            data:params,
            dataType:"json",
            cache:false,
            timeout : 10000, //超时时间设置，单位毫秒
            success: function(data){ 
                  if(typeof(fun)=="function"){
                      fun(data);
                  }else if(typeof(fun)=="string"){
                      eval(fun+"(data)"); 
                  }
            },
            complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
                
                if(status=='timeout'){//超时,status还有success,error等值的情况
                    alert("请求超时");
                    showloaddinghtml5(0);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){    
                //200-确定。客户端请求已成功
                 
                //0 － （未初始化）还没有调用send()方法 
                //1 － （载入）已调用send()方法，正在发送请求 
                //2 － （载入完成）send()方法执行完成，已经接收到全部响应内容 
                //3 － （交互）正在解析响应内容  
                //4 － （完成）响应内容解析完成，可以在客户端调用了
                //alert(XMLHttpRequest.readyState);
               
                // "timeout", "error", "notmodified" 和 "parsererror"。
                //alert(textStatus);
		if(textStatus=='timeout'){//超时
                    alert("请求超时");
                    showloaddinghtml5(0);
                }
            }
      });        
      return false;
}


/*ajax公共提交处理
* 
*@prame data  对象形式
* 
* var paramsData = {}
* paramsData['url'] = '';
* paramsData['cache'] = '';
* paramsData['debug'] = '';
* paramsData['funSuccess'] = '';
* paramsData['api'] = '';
*
*/
function comajax(data){  
	   
	    var url = data.url || originAPI;//请求地址
	    var cache = data.cache || false;//false为异步 true为同步
	    var type = data.type || "post";//get请求还是post
	    var debug = data.debug || false;//是否输出请求数据与返回数据
	    var funSuccess = data.funSuccess || function(){};//请求成功后的处理函数
	    delete data.url;
	    delete data.cache;
	    delete data.type;
	    delete data.debug;	    
	    delete data.funSuccess;

	    
	    var api = data.api || "";
	    delete data.api;
	    
	
		//拼装请求数据                                           
	    var url = originAPI + requestInfo[api];	 
	    var params = data;	   
	    var paramsStr = JSON.stringify(params);
	    console.log(jQuery.param(params));
	   
	    if(type=="get"){
	    	url = url + "?"  + jQuery.param(params);
	    	
	    } 
	 
	    //输出请求数据
	    if(debug==true){
	    	console.log("请求地址:"+url);
	        console.log("请求数据:"+paramsStr);
	    }
	    

        $.ajax({
            "url":url,
            "type":type,
            "data": (type=="get" ? "" : jQuery.param(params) ) ,
            "dataType":"json",
            "cache":false,
            "timeout" : 5000, //超时时间设置，单位毫秒
            success: function(result){ 
            	
            	if(debug==true){
	    	      console.log("返回结果:");
	    	      console.log(result);   
		        }
            	
            	if(result.result==0) {
            		errorCode = result.errorCode;
            		showloaddinghtml5(0);
            		//return mess_tusi("请求失败，错误代码:"+result.errorCode);
                }
                
               
		    	if (result.result == 1) {
		    				       	            
		        }    
		        		        
		        //返回处理函数
            	if(typeof(funSuccess)=="function"){
                      funSuccess(result);
                }
		        return false;
                  
            },
            complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数                
                if(status=='timeout'){//超时,status还有success,error等值的情况
                    mess_tusi("请求超时");
                    showloaddinghtml5(0);
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){  
            	console.log(XMLHttpRequest);
            	console.log(textStatus);
                //200-确定。客户端请求已成功
                 
                //0 － （未初始化）还没有调用send()方法 
                //1 － （载入）已调用send()方法，正在发送请求 
                //2 － （载入完成）send()方法执行完成，已经接收到全部响应内容 
                //3 － （交互）正在解析响应内容  
                //4 － （完成）响应内容解析完成，可以在客户端调用了
                //alert(XMLHttpRequest.readyState);
               
                // "timeout", "error", "notmodified" 和 "parsererror"。
                //alert(textStatus);
                //console.log("textStatus...."+textStatus);
		        if(textStatus=='timeout'){//超时
                    mess_tusi("请求超时");
                    showloaddinghtml5(0);
                }
		        
		        if(textStatus=='error'){//出错
                    mess_tusi("请求出错");
                    showloaddinghtml5(0);
                }
		        
            }
      });        
      return false;
	    
	    

}










/**打开本页面div弹窗 把此div移动到创建的遮罩层里，关闭时再移出来并删除遮罩层
 * 
 * 前台定义 
 * <div class="daanlist" style="display: none;width:800px;height:300px;overflow-y: auto;">
 * <ul style="margin: 10px;">   </ul>
 * </div>
 * 打开方法  var open = new opendivAll({'divclassname':'classname','title':'标题',"isshowclose":'1',"offsetleft":"0","offsettop":"-50","edgestyle":1});
   关闭访法  open.close(); 如果想自己写关闭控件，只需要加上 class = "openclose" 如 <a class="openclose">关闭</a>
    divclassname : 要弹出的层的class
    title : 标题  为空时不显示标题栏
    isshowclose : 默认为1，为1显示关闭按钮
    offsetleft : 默认为中间，这里可以设置从中间向左边偏移
    offsettop : 默认为中间，这里可以设置从中间向顶部偏移
    edgestyle : 默认为1，为0则不显示渐变的边框样式及圆角等样式
 */
function opendivAll(params){ 
    //获取变量
    var g=function(key,defaultValue){
          if(defaultValue==undefined) defaultValue = "";          
          var value = params[key] ? params[key] : "";
          if(value!="") return value;
          return defaultValue;
    }
     var me=this;
     var params=params;
     var $obj = $("."+g("divclassname"));//获取div对象  
     
     var edgestyle = g("edgestyle")==1 ? "background: #fff;box-shadow:0 0 5px 0px rgba(255,255,255,.6),0 0 1px 2px rgba(0,0,0,.3),0 0 2px 5px rgba(0,0,0,.3);border-radius:5px !important;" : "";
     
     var w=$obj.width();//读取给定的层的高度
     var h=$obj.height();//读取给定的层的宽度
     var left=($(window).width()-w)/2+parseInt(g("offsetleft",0));//计算弹出层里的左边距离
     var top=($(window).height()-h)/2+parseInt(g("offsettop",0));//计算弹出层里的右边距离
     

    //显示弹出的遮罩层 
    var opendivH = h ;
    if(g("title")!="") opendivH = opendivH + 36;
    var strs='<div id="opendiv" style=" position: absolute;top:'+$(window).scrollTop()+'px;left:0;right:0;z-index: 99999999999; background:rgba(0,0,0,.3);">';
        strs+='<div style="width:'+w+'px;height:'+opendivH+'px;top:'+top+'px;left:'+left+'px; position:absolute;z-index:10001;overflow: hidden;'+edgestyle+'">';
        if(parseInt(g("isshowclose",1))){ strs+='<div class="openclose" style="position: absolute;right:0px;top:0px;border-radius:5px;"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABwAAAAdCAYAAAC5UQwxAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA0tpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDA3IDEuMTQ0MTA5LCAyMDExLzA5LzIwLTE4OjA5OjEwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2ICgxMy4wMjAxMTEwMTIubS4yNTggMjAxMS8xMC8xMjoyMTowMDowMCkgIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFN0MxRDBGQTJDMjQxMUU0Qjg2OURCM0U1ODA5QjAwRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFN0MxRDBGQjJDMjQxMUU0Qjg2OURCM0U1ODA5QjAwRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkU3QzFEMEY4MkMyNDExRTRCODY5REIzRTU4MDlCMDBFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkU3QzFEMEY5MkMyNDExRTRCODY5REIzRTU4MDlCMDBFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/SPEXgAABMhJREFUeNrclmtoHFUUx/8zs7O72WySzbb7aDfbBGtTCcTHEgShKcR0U/vBVuMLjATzpYhUYrQgQiUUhOoHDUEUih8iKakVsSi1tqRaiQlopY1Vmki2IanZ5rnZJGyyz3l5ZmfTbLKTGBX6oQNnHzPnzu+ec/733MsoioK7ebG4y5dhEz6FZH6yalEUfYIglFNWHAaDIcTzfIBhmH561kt2iSzyTy9jNkqpLMuNi4uLDbOzs3VkiMViYBIBMJAh8yXgzcWw2WxwOp3qd7fRaOyiYZ3/Gkj3vNFotGV6ero5ND7AMjNnkBf/CQZpEmZeS0pKlCAyeUgaH0bCVo+iEj9cLpdst9vbOY5rI5fgpoAUlXdubu745ESwKTnSDvP8F3BZgCKDCCMkdTrLQ+kfi6hswFwSWOAqIO54B64dVSq4g6Jt1YPmAMPh8IdTtwMt0o3DcGAYTqMITskG6U2bwazAYypugrjzBLbsPAi3291GNX5jQyDVq5Ei60hdfYl1SANwGQU1v5vUH4OIxOFWlAX30GfYUlYjU22bWJbt1F0WpMDCSCTSIA69z9qTg3BxBBMJ5nkW8H1EHnlIZ3St8Q6g6iTgqCE5i/CaFMi/v4alhQmWdNCQUXkukB76hYWhOmH0NLZRvSAQjLECe0h4u48Ae89StAQVsWIGgtX+AOw6TJP6IH2vmD4c7BLEm5+o76wjTfh1galUqjr+58fwmDkwgqTNPrEE9LdqDp4ngJqzK5Gqke0nWHEl/Y8DV97MRK3ARROO3zwFORFGIpGo1gWyiuCLj16AnRFXR3HtPXrZ25pTCUFrCWryAgcyMJFg5w8AY5fvjGFSErbyLFLBC5AkyafbaYS5gXKLKnRBztXDVYKqtx87AXgJ+sIgRWjVYOcINtGTM8RCYgvPXAeJplwXqEQnHUb1peI6qvyVoFw+8OgxDaaQ87mnKLIeXXcjAYX5v0BNwKGbUkWIhxiJWZ3ObFNrdt+hrFVAQ31HtZrq+LNUSzkZg9pzdYFG67ZASlWmnvRNBHuOarY1U7PrJ7VBpSTAQ98QPHfJCLSqeItbBQZ0gWabtz+VJKC4JkojwZ4nmINgKVLt53XAd68AP2aEVEbQ+m9zIk0KLEyFpapHvy4wf/uDvXEUQJS5lZmqv18kmDMDO00CudWnPeujml5ehj4OHOzKipC6TgIoKNuDzNaVC2RY7pKj6uXuqRiXHpCeqboeI+Ma7BTBRvtWR99D0O8zUDG1Ep3IISzk06p5ujuzT+r30uT8eOO11gc6HilIsHnqerwTPqU1Glq/jTorgNkhyoiUbuQjCRP42la5ZP9bTbRBd657xDAVezpLn3y3fTxOq0VhV1IUCemLadkmB7VsyAxuxwyYt+yCZ19L+1qY7pnG429uE3fXdwwv8bRRcBuDsi0N4zAmFKHiyNcdrMHUtvkdX5a8w12vtwi/fNq83SSzNk7ccJtSGNqWEhxCZq9cefRie57r/s3v+NnXzM9nGke+PNZgjQbrHCbAykrI57TWl1LU3Z7FPMk/uChTZl7tLnvmeJfBUtT5nw9R6SOHkCwM/3beH7ryVfXS2B++aPCG2hsdvM0dsnoqAsWV+/rdext7TXbP/z+13RMH4Xsf+LcAAwCZ0kXZqZMRwQAAAABJRU5ErkJggg==")"></div>'; }    
        if(g("title")) strs+='<div class="opentitle" style="padding: 8px 0px;text-align: center;background: #efefef;border-radius: 5px 5px 0px 0px;font-size: 14px;">'+g("title")+'</div>';
        strs+='<div class="opencon" ></div>';   
        strs+='</div>';
        strs+='</div>'; 
	
    if($("#opendiv").size()<=0) $("body").append(strs);
    //$("body").css('overflow-y','hidden');
    
    //把div移动到创建的遮罩层内部 并显示
    $obj.appendTo($("#opendiv").find(".opencon")).show();
    $("#opendiv").css({"height":$(window).height()+"px"});
    
    //给关闭按钮添加点击事件
    $(".openclose").click(function(){
        me.close();
     });
     //提供给外部调用关闭弹出层的方法
     this.close=function(){
           $obj.appendTo($("body")).hide();
           $("#opendiv").remove();
           //$("body").css('overflow-y','auto');
     } 

}


var tusitemp="";
/** 显示吐丝消息
 * 
 * @param {str} strs    消息内容
 * @returns {undefined}
 */
function mess_tusi(strs){ 
    
    //清除事件
    clearTimeout(tusitemp);
    $("#mess_tusi").remove();
    //创建吐丝层并写入内容
    if(!$("#mess_tusi").attr("id")){ //吐丝层不存在创建
       $("body").append("<div id='mess_tusi' style='z-index: 100002;position:fixed;font-size:16px;border-radius:4px !important;background:rgba(0,0,0,.7);color:#fff;display:none;'><span style='display:block;padding:5px 15px;'>"+strs+"</span></div>"); //写入内容
    }else{
       $("#mess_tusi").html(strs);  //写入内容
    }  

    //定义吐丝层位置
    var left=($(window).width()-$("#mess_tusi").width())/2;//居中
    //var top=($(window).height()-$("#mess_tusi").height())/2;//居中
    var top=$(window).height()*0.8;//偏下
    $("#mess_tusi").css({"left":left+"px","top":top+"px"});

    //显示吐丝层
    $("#mess_tusi").css("display",'');
    
    //2秒后关闭
    tusitemp =  setTimeout(function (){          
           $("#mess_tusi").remove();
           $("#mess_tusi").html("");         
    },2000);
    return false;
}




/** 显示弹窗提示，并有确定和取消操作
 * 如果只传入字符串,字符串将作为显示的内容使用，其它参数将会启用默认值 
 * @param {obj} params {"content":"","width":"400px","isQueding":"1","isQuxiao":"1"}  提示内容，是否显示确定按钮，是否显示关闭按钮
 * @param {fun} funok      确定后执行的js内容 function(){代码}
 * @param {array} funno    取消后执行的js内容 function(){代码}
 * @returns {undefined}
 */
function mess_tanchuang(data,funok,funno){   
      var  params={}   
      params.content = (data.content == undefined ) ? data : data.content ;
      params.width = (data.width == undefined ) ? "200px" : data.width ;
      params.isQueding =(data.isQueding == undefined ) ? "1" :data.isQueding ;
      params.isQuxiao = (data.isQuxiao == undefined ) ? "0" :data.isQuxiao ;
	  
	if( params.isQueding == 1 && params.isQuxiao ==1){
		var tempW = "width:49.5%;";
	}else{
		var tempW = "width:100%;";
	}
     
      var anniustyle="color: #888787;font-size: 16px;text-align: center;float: left;display:block;"+tempW+"line-height: 40px;";
       var strs="<div style='position:fixed;top:0px;left:0px;right:0px;bottom:0px;background:rgba(0,0,0,0.2);z-index:99999999999'>\n\
                <div id='mess_tanchuang' style='width: "+params.width+" ; position: fixed; font-size:16px; border-radius: 4px; left: 50%; top: 50%;background: rgb(255, 255, 255);box-shadow: 0px 0px 15px rgba(0,0,0,.5),-0px -0px 15px rgba(0,0,0,.5);'>\n\
                <div style='padding:30px 15px;border-bottom: solid #ccc 1px;text-align:center;'>"+params.content+"</div>";
            strs=strs+"<center style='display:flex'>";
            if( params.isQueding != 0 ) strs=strs+"<a href='javascript:void(0);' class='queding' style='"+anniustyle+"'>确定</a>";
            if( params.isQuxiao != 0 ) strs=strs+"<a href='javascript:void(0);' class='quxiao' style='"+anniustyle+";border-left: solid 1px #ECEAEA;'>取消</a>";
            strs=strs+"</center>\n\
                </div>\n\
                </div>";
       $("body").append(strs); //写入内容
       $("#mess_tanchuang .quxiao").click(function(event){ //取消直接删除层
           event.preventDefault();
           event.stopPropagation();
            if(typeof(funno)=="function"){
                  funno();
            }else if(typeof(funno)=="string"){
                  eval(funno); 
            } 
           $("#mess_tanchuang").parent().remove();
           
       });
       $("#mess_tanchuang .queding").click(function(event){//确定时，执行代码删除层
              event.preventDefault();
              event.stopPropagation();
              if(typeof(funok)=="function"){
                  funok();
               }else if(typeof(funok)=="string"){
                  eval(funok); 
               }                                
               $("#mess_tanchuang").parent().remove();
       });
       
       //
        //位置
        var mleft=$("#mess_tanchuang").width()/2;//居中
        var mtop=$("#mess_tanchuang").height()/2;//居中   
        $("#mess_tanchuang").css({"margin-left":"-"+mleft+"px","margin-top":"-"+mtop+"px"});
        return false;
}



//在移动端，click 改为touchstart
(function(){
        var isTouch = ('ontouchstart' in document.documentElement) ? 'touchstart' : 'click', _on = $.fn.on;
            $.fn.on = function(){
                arguments[0] = (arguments[0] === 'click') ? isTouch: arguments[0];
                return _on.apply(this, arguments); 
            };
})();




/** 显示加载流动条HTML5版
 * 
 * @param {int} show     为1表示显示 为0表示隐藏
 * @returns {undefined}
 * 
 */
function showloaddinghtml5(show,strs){ 
     if(show==0){
         $("#mess_loaddinghtml5").remove();
     }else{
        if(strs=="" || strs==undefined)  strs = "正在处理中...";
        if(!$("#mess_loaddinghtml5").attr("id")){ //不存在创建
          $("body").append('<div id="mess_loaddinghtml5"><div class="loadding"><div class="loaddingtishi">'+strs+'</div><div class="spinner"><div class="spinner-container container1"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div><div class="spinner-container container2"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div><div class="spinner-container container3"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div></div></div></div>'); //写入内容
         }
         $("#mess_loaddinghtml5").css("display",'');  
    }   
}




 

//jquery  定义静态访求直接用 $.方法名来访问
$.extend({
  //$.log() 代替js的 console.log  
  log: function(message) {
      var now = new Date(),
      y = now.getFullYear(),
      m = now.getMonth() + 1, //！JavaScript中月分是从0开始的
      d = now.getDate(),
      h = now.getHours(),
      min = now.getMinutes(),
      s = now.getSeconds(),
      time = y + '/' + m + '/' + d + ' ' + h + ':' + min + ':' + s;
      console.log(time);
      console.log(message);
  }
})



function getUrlParams(name,url){
    if(!url) url = window.location.href;
	var params = {};
	var url = decodeURI(url);
	var idx = url.indexOf("?");
	if(idx > 0)
	{
		var queryStr = url.substring(idx + 1);
		var args = queryStr.split("&");
		for(var i = 0, a, nv; a = args[i]; i++)
		{
			nv = args[i] = a.split("=");
			params[nv[0]] = nv.length > 1 ? nv[1] : true;
		}
	}
	return params[name];
}

/**
 * 倒计时  控件只能使用button ，这样可以有button的disabled属性控制是否允许点击
 * <button class="sendyzm">获取验证码</button>
 * @param {Object} obj  //button控件的jquery 对象
 * @param {Object} time //倒计时时间
 */
function daojishi(obj,time){
	    //定义内部对象
	    var Me = function(){	    	
	    	this.obj = obj;//DOM对象
    	    this.time = time;//倒计时时间
    	    this.ttt ="";  //循环事件
    	    this.init();
	    }    
	    
    	Me.prototype = {
    		init:function(){
    			var me = this;
    			
    			console.log(me.time);
	    		me.obj.attr("disabled",true).html(this.time+"秒后重新获取").css({"background-color":"#ccc"});
	    		
	    		me.ttt = setInterval(function(){
		    		me.time--;
		    		if(me.time==0){
		    			clearInterval(me.ttt);
		    			me.obj.removeAttr("disabled").html("获取验证码").css({"background-color":"#3eb5fa"});
		    		}else{
		    		    me.obj.html(me.time+"秒后重新获取");    		    
		    		}
	    	    },1000) 
    	    }
    	}
    	//初始化对象
    	var me = new Me(obj,time);
}




/**** 自动适配rem的比值。
//use_screen_base为320时 表示以iphone4的宽320px为标准设计网页,在iphone4下默认的font-size为100px,网页中1rem表示10px.
//use_screen_base为375时 表示以iphone6的宽375px为标准设计网页,在iphone6下默认的font-size为100px,网页中1rem表示10px.

第一步：引入 此js
第一步：在head中引入代码段	window.use_screen_base = '375';		
第三步：把所有px为单位的值都可以换成以rem为单位
************************************/ 
(function(a, b) {
	var c = "orientationchange" in b ? "orientationchange" : "resize",
		d = use_screen_base.indexOf("_mate"),
		e = parseInt(use_screen_base),
		f = a.documentElement,
		g = function() {
			var a = f.clientWidth,
				c = b.innerWidth;
			f.style.fontSize = 100 * (c / e) + "px"
		};
	if (/iPad.*OS|iPhone.*OS/.test(navigator.userAgent) && d > 0) {
		var h = a.querySelectorAll("meta[name=viewport]");
		h[0] && h[0].setAttribute("content", "width=device-width, user-scalable=no, initial-scale=" + 1 / b.devicePixelRatio)
	}
	g(), b.addEventListener(c, g, !1), delete use_screen_base
})(document, window);




!function(t){"use strict";function e(){}function i(t,e){return e.indexOf(t)>-1}function n(t){return"[object Array]"===Object.prototype.toString.call(t)}function s(t){return"[object Object]"===Object.prototype.toString.call(t)}function a(t,e){return t.className.match(new RegExp("(\\s|^)("+e+")(\\s|$)"))}function r(t,e){a(t,e)||(t.className+=" "+e)}function h(t,e){a(t,e)&&(t.className=t.className.replace(RegExp("(\\s|^)("+e+")(\\s|$)"),"$3"))}function o(t){return/<\/?[^>]*>/.test(t)?!1:/^(?:(https|http|ftp|rtsp|mms):)?(\/\/)?(\w+:{0,1}\w*@)?([^\?#:\/]+\.[a-z]+|\d+\.\d+\.\d+\.\d+)?(:[0-9]+)?((?:\.?\/)?([^\?#]*)?(\?[^#]+)?(#.+)?)?$/.test(t)}function l(t){return Array.prototype.slice.apply(t,Array.prototype.slice.call(arguments,1))}function d(t){return t.replace(/^[a-z]/,function(t){return t.toUpperCase()})}var c=function(){var t=l(arguments,0,3);if(!t.length)throw new Error("Parameters required!");var e=s(t.slice(-1)[0])?t.pop():{};switch(t.length){case 2:e.data=e.data||t[1];case 1:e.dom=e.dom||t[0]}if(!e.dom)throw new Error("Container can not be empty!");if(!e.data||!e.data.length)throw new Error("Data must be an array and must have more than one element!");this._opts=e,e=null,t=null,this._setting(),this.fire("initialize"),this._renderWrapper(),this._initPlugins(),this._bindHandler(),this.fire("initialized"),this._autoPlay()};c.VERSION="2.1.7",c.EVENTS=["initialize","initialized","pluginInitialize","pluginInitialized","renderComplete","slide","slideStart","slideEnd","slideChange","slideChanged","slideRestore","slideRestored","loadData","reset","destroy"],c.EASING=[["linear","ease","ease-in","ease-out","ease-in-out"],/cubic-bezier\(([^\d]*(\d+.?\d*)[^\,]*\,?){4}\)/],c.FIX_PAGE_TAGS=["SELECT","INPUT","TEXTAREA","BUTTON","LABEL"],c.NODE_TYPE={unknown:"unknown",empty:"empty",pic:"pic",dom:"dom",html:"html",node:"node",element:"element"},c.TRANSITION_END_EVENT=null,c.BROWSER_PREFIX=null,function(){var t=document.createElement("fakeElement");[["WebkitTransition","webkitTransitionEnd","webkit"],["transition","transitionend",null],["MozTransition","transitionend","moz"],["OTransition","oTransitionEnd","o"]].some(function(e){return void 0!==t.style[e[0]]?(c.TRANSITION_END_EVENT=e[1],c.BROWSER_PREFIX=e[2],!0):void 0})}(),c.DEVICE_EVENTS=function(){var e=!!("ontouchstart"in t&&!/Mac OS X /.test(t.navigator.userAgent)||t.DocumentTouch&&document instanceof t.DocumentTouch);return{hasTouch:e,startEvt:e?"touchstart":"mousedown",moveEvt:e?"touchmove":"mousemove",endEvt:e?"touchend":"mouseup",cancelEvt:e?"touchcancel":"mouseout",resizeEvt:"onorientationchange"in t?"orientationchange":"resize"}}(),c.extend=function(){var t,e,i=arguments;switch(i.length){case 0:return;case 1:t=c.prototype,e=i[0];break;case 2:t=i[0],e=i[1]}for(var n in e)e.hasOwnProperty(n)&&(t[n]=e[n])},c.plugins={},c.regPlugin=function(t,e){c.plugins[t]=c.plugins[t]||e},c.styleProp=function(t,e){return c.BROWSER_PREFIX?e?c.BROWSER_PREFIX+d(t):"-"+c.BROWSER_PREFIX+"-"+t:t},c.setStyle=function(t,e,i){t.style[c.styleProp(e,1)]=i},c.getStyle=function(t,e){return t.style[c.styleProp(e,1)]},c._animateFuncs={normal:function(){function t(t,e,i,n,s){c.setStyle(t,"transform","translateZ(0) translate"+e+"("+(s+i*(n-1))+"px)")}return t.effect=c.styleProp("transform"),t}()};var u=c.prototype;u.extend=c.extend,u._setting=function(){var s=this;s._plugins=c.plugins,s._animateFuncs=c._animateFuncs,s._holding=!1,s._locking=!1,s._intermediateScene=null,s._transitionEndHandler=null,s._LSN={autoPlay:null,resize:null,transitionEnd:null},s.currentEl=null,s._EventHandle={},s.onMoving=!1,s.onSliding=!1,s.direction=null;var a=this._opts;s.wrap=a.dom,s.data=a.data,s.isVertical=!!a.isVertical,s.isOverspread=!!a.isOverspread,s.duration=a.duration||2e3,s.initIndex=a.initIndex>0&&a.initIndex<=a.data.length-1?a.initIndex:0,s.fixPage=function(){var t=a.fixPage;return t===!1||0===t?!1:n(t)&&t.length>0||"string"==typeof t&&""!==t?[].concat(t).toString():!0}(),s.fillSeam=!!a.fillSeam,s.slideIndex=s.slideIndex||s.initIndex||0,s.axis=s.isVertical?"Y":"X",s.reverseAxis="Y"===s.axis?"X":"Y",s.width="number"==typeof a.width?a.width:s.wrap.offsetWidth,s.height="number"==typeof a.height?a.height:s.wrap.offsetHeight,s.ratio=s.height/s.width,s.scale=s.isVertical?s.height:s.width,s.offset=s.offset||{X:0,Y:0},s.isTouchable=null==a.isTouchable?!0:!!a.isTouchable,s.isLooping=a.isLooping&&s.data.length>1?!0:!1,s.dampingForce=Math.max(0,Math.min(1,parseFloat(a.dampingForce)||0)),s.delay=a.delay||0,s.isAutoplay=a.isAutoplay&&s.data.length>1?!0:!1,s.animateType=a.animateType in s._animateFuncs?a.animateType:"normal",s._animateFunc=s._animateFuncs[s.animateType],s._animateReverse=function(){var t=[];for(var e in s._animateFuncs)s._animateFuncs.hasOwnProperty(e)&&s._animateFuncs[e].reverse&&t.push(e);return t}(),s.isVertical&&"card"===s.animateType&&(s.isOverspread=!0),s.log=a.isDebug?function(){t.console.log.apply(t.console,arguments)}:e,s._damping=function(){return function(t){return.62*Math.atan(Math.abs(t)/s.scale)*(1-s.dampingForce)*s.scale*(t>0?1:-1)}}(),s.animateTime=null!=a.animateTime&&a.animateTime>-1?a.animateTime:300,s.animateEasing=i(a.animateEasing,c.EASING[0])||c.EASING[1].test(a.animateEasing)?a.animateEasing:"ease",s.deviceEvents=c.DEVICE_EVENTS,s.fingerRecognitionRange=a.fingerRecognitionRange>-1?parseInt(a.fingerRecognitionRange):10,s.events={},c.EVENTS.forEach(function(t){var e=a["on"+t.toLowerCase()];"function"==typeof e&&s.on(t,e,1)}),s.pluginConfig=function(){var t={};return n(a.plugins)&&a.plugins.forEach(function(e){n(e)?t[e[0]]=e.slice(1):"string"==typeof e&&(t[e]=[])}),t}()},u._initPlugins=function(){var t=this.pluginConfig,e=this._plugins;for(var i in t)t.hasOwnProperty(i)&&e.hasOwnProperty(i)&&e[i]&&"function"==typeof e[i]&&typeof e[i].apply&&e[i].apply(this,t[i]);this.fire("pluginInitialized")},u._itemType=function(t){if(isNaN(t)||(t=this.data[t]),t.hasOwnProperty("type"))return t.type;var e,i=t.content,n=c.NODE_TYPE;return e=null==i?n.empty:Boolean(i.nodeName)&&Boolean(i.nodeType)?n.node:"string"==typeof i?o(i)?n.pic:n.html:n.unknown,t.type=e,e},u._renderItem=function(t,e){var i,n=this,s=this.data.length,a=function(){var e=' src="'+i.content+'"';e+=i.height/i.width>n.ratio?' height="100%"':' width="100%"',n.isOverspread&&(t.style.cssText+="background-image:url("+i.content+");background-repeat:no-repeat;background-position:50% 50%;background-size:cover",e+=' style="display:block;opacity:0;height:100%;width:100%;"'),t.innerHTML="<img"+e+" />"};if(t.innerHTML="",t.style.background="",this.isLooping||null!=this.data[e]){e=(s+e)%s,i=this.data[e];var r=this._itemType(i),h=c.NODE_TYPE;switch(t.className="islider-"+r,r){case h.pic:if(2===i.load)a();else{var o=new Image;o.src=i.content,o.onload=function(){i.height=o.height,i.width=o.width,a(),i.load=2}}break;case h.dom:case h.html:t.innerHTML=i.content;break;case h.node:case h.element:if(11===i.content.nodeType){var l=document.createElement("div");l.appendChild(i.content),i.content=l}t.appendChild(i.content)}}},u._renderIntermediateScene=function(){null!=this._intermediateScene&&(this._renderItem.apply(this,this._intermediateScene),this._intermediateScene=null)},u._changedStyles=function(){var t=["islider-prev","islider-active","islider-next"];this.els.forEach(function(e,i){h(e,t.join("|")),r(e,t[i]),this.fillSeam&&this.originScale(e)}.bind(this))},u._renderWrapper=function(){var e;this.outer?(e=this.outer,e.innerHTML=""):e=document.createElement("ul"),e.className="islider-outer",this.els=[];for(var i=0;3>i;i++){var n=document.createElement("li");this.els.push(n),this._animateFunc(n,this.axis,this.scale,i,0),this.fixPage||(n.style.overflow="auto"),!this.isVertical||"rotate"!==this.animateType&&"flip"!==this.animateType?this._renderItem(n,i-1+this.slideIndex):this._renderItem(n,1-i+this.slideIndex),e.appendChild(n)}this._changedStyles(),this.fillSeam&&this.els.forEach(function(t,e){r(t,"islider-sliding"+(1===e?"-focus":""))}),t.setTimeout(function(){this._preloadImg(this.slideIndex)}.bind(this),200),this.outer||(this.outer=e,this.wrap.appendChild(e)),this.currentEl=this.els[1],this.fire("renderComplete",this.slideIndex,this.currentEl,this)},u._preloadImg=function(t){if(this.data.length>3){var e=this.data,i=e.length,n=this,s=function(t){var i=e[t];if("pic"===n._itemType(i)&&!i.load){var s=new Image;s.src=i.content,s.onload=function(){i.width=s.width,i.height=s.height,i.load=2},i.load=1}};s((t+2)%i),s((t-2+i)%i)}},u._watchTransitionEnd=function(e,i){var n=function(){this._unWatchTransitionEnd(),"slideChanged"===i&&this._changedStyles(),this.fire.call(this,i,this.slideIndex,this.currentEl,this),this._renderIntermediateScene(),this.play(),this.onSliding=!1,this.direction=null}.bind(this);c.TRANSITION_END_EVENT&&(this.currentEl.addEventListener(c.TRANSITION_END_EVENT,n),this._transitionEndHandler={el:this.currentEl,handler:n}),this._LSN.transitionEnd=t.setTimeout(n,e)},u._unWatchTransitionEnd=function(){this._LSN.transitionEnd&&t.clearTimeout(this._LSN.transitionEnd),null!==this._transitionEndHandler&&(this._transitionEndHandler.el.removeEventListener(c.TRANSITION_END_EVENT,this._transitionEndHandler.handler),this._transitionEndHandler=null)},u._bindHandler=function(){var e=this.outer,i=this.deviceEvents;this.isTouchable&&(i.hasTouch||(e.style.cursor="pointer",e.ondragstart=function(t){return t?!1:!0}),e.addEventListener(i.startEvt,this),e.addEventListener(i.moveEvt,this),e.addEventListener(i.endEvt,this),!i.hasTouch&&e.addEventListener("mouseout",this)),t.addEventListener(i.resizeEvt,this),t.addEventListener("focus",this,!1),t.addEventListener("blur",this,!1)},u.handleEvent=function(t){var e=this.deviceEvents;switch(t.type){case"mousedown":if(0!==t.button)break;case"touchstart":this.startHandler(t);break;case e.moveEvt:this.moveHandler(t);break;case e.endEvt:case e.cancelEvt:this.endHandler(t);break;case e.resizeEvt:this.resizeHandler();break;case"focus":this.play();break;case"blur":this.pause()}},u.startHandler=function(t){if(this.fixPage&&c.FIX_PAGE_TAGS.indexOf(t.target.tagName.toUpperCase())<0&&!this._isItself(t.target)&&t.preventDefault(),!this._holding&&!this._locking){var e=this.deviceEvents;this.onMoving=!0,this.pause(),this.fire("slideStart",t,this),this.startTime=(new Date).getTime(),this.startX=e.hasTouch?t.targetTouches[0].pageX:t.pageX,this.startY=e.hasTouch?t.targetTouches[0].pageY:t.pageY}},u.moveHandler=function(t){if(this.onMoving){var e=this.deviceEvents,i=this.data.length,n=this.axis,s=this.reverseAxis,a={};t.hasOwnProperty("offsetRatio")?(a[n]=t.offsetRatio*this.scale,a[s]=0):(a.X=e.hasTouch?t.targetTouches[0].pageX-this.startX:t.pageX-this.startX,a.Y=e.hasTouch?t.targetTouches[0].pageY-this.startY:t.pageY-this.startY),this.offset=a,t.offsetRatio=a[n]/this.scale,Math.abs(a[n])-Math.abs(a[s])>10&&(t.preventDefault(),this._unWatchTransitionEnd(),this.isLooping||(a[n]>0&&0===this.slideIndex||a[n]<0&&this.slideIndex===i-1)&&(a[n]=this._damping(a[n])),this.els.forEach(function(t,e){t.style.visibility="visible",c.setStyle(t,"transition","none"),this._animateFunc(t,n,this.scale,e,a[n],a[n]),this.fillSeam&&this.seamScale(t)}.bind(this)),this.fire("slide",t,this))}},u.endHandler=function(e){function i(n){if(null!=n)if("A"===n.tagName){if(n.href)return"_blank"===n.getAttribute("target")?t.open(n.href):t.location.href=n.href,e.preventDefault(),!1}else{if("LI"===n.tagName&&n.className.search(/^islider\-/)>-1)return!1;i(n.parentNode)}}if(this.onMoving){this.onMoving=!1;var n=this.offset,s=this.axis,a=this.scale/2,r=(new Date).getTime(),h=this.fingerRecognitionRange;a=r-this.startTime>300?a:14;var o=Math.abs(n[s]),l=Math.abs(n[this.reverseAxis]);this.fire("slideEnd",e,this),n[s]>=a&&o>l?this.slideTo(this.slideIndex-1):n[s]<-a&&o>l?this.slideTo(this.slideIndex+1):Math.abs(this.offset[s])>=h&&this.slideTo(this.slideIndex),Math.abs(this.offset[s])<h&&this.fixPage&&e.target&&i(e.target),this.offset.X=this.offset.Y=0}},u.resizeHandler=function(){var e,i,n=this._LSN.resize,s=+new Date;this.deviceEvents.hasTouch?(n&&t.clearInterval(n),n=t.setInterval(function(){this.height!==this.wrap.offsetHeight||this.width!==this.wrap.offsetWidth?(n&&t.clearInterval(n),n=t.setInterval(function(){e===this.wrap.offsetWidth&&i===this.wrap.offsetHeight?(n&&t.clearInterval(n),this.reset()):(e=this.wrap.offsetWidth,i=this.wrap.offsetHeight)}.bind(this),12)):+new Date-s>=1e3&&n&&t.clearInterval(n)}.bind(this),12)):(n&&t.clearTimeout(n),n=t.setTimeout(function(){(this.height!==this.wrap.offsetHeight||this.width!==this.wrap.offsetWidth)&&(n&&t.clearInterval(n),this.reset())}.bind(this),200))},u.slideTo=function(t,e){if(this.isAutoplay&&this.pause(),!this._locking){this.unhold(),this.onSliding=!0;var n,s=this.animateTime,a=this.animateType,o=this._animateFunc,l=this.data,d=this.els,u=this.axis,f=t,p=t-this.slideIndex,g=this.offset,m=0;"object"==typeof e&&(e.animateTime>-1&&(s=e.animateTime),"string"==typeof e.animateType&&e.animateType in this._animateFuncs&&(a=e.animateType,o=this._animateFuncs[a])),0!==g[u]&&(m=Math.abs(g[u])/this.scale*s),Math.abs(p)>1&&this._renderItem(p>0?this.els[2]:this.els[0],f),this._preloadImg(f),l[f]?this.slideIndex=f:this.isLooping?this.slideIndex=p>0?0:l.length-1:p=0;var v,E,_;0===p?n="slideRestore":((this.isVertical&&i(a,this._animateReverse))^p>0?(d.push(d.shift()),v=d[2],E=d[0],_=1):(d.unshift(d.pop()),v=d[0],E=d[2],_=-1),this.currentEl=d[1],1===Math.abs(p)?(this._renderIntermediateScene(),this._renderItem(v,f+p)):Math.abs(p)>1&&(this._renderItem(v,f+_),this._intermediateScene=[E,f-_]),c.setStyle(v,"transition","none"),m=s-m,n="slideChange",this.fillSeam&&(d.forEach(function(t){h(t,"islider-sliding|islider-sliding-focus")}),r(this.currentEl,"islider-sliding-focus"),r(v,"islider-sliding")),this.direction=_);for(var y=0;3>y;y++)d[y]!==v&&c.setStyle(d[y],"transition",(o.effect||"all")+" "+m+"ms "+this.animateEasing),o.call(this,d[y],u,this.scale,y,0,_),this.fillSeam&&this.seamScale(d[y]);this._watchTransitionEnd(m,n+"d"),this.fire(n,this.slideIndex,this.currentEl,this)}},u.slideNext=function(){this.slideTo.apply(this,[this.slideIndex+1].concat(l(arguments)))},u.slidePrev=function(){this.slideTo.apply(this,[this.slideIndex-1].concat(l(arguments)))},u.regPlugin=function(){var t=l(arguments),e=t.shift(),n=t[0];(this._plugins.hasOwnProperty(e)||"function"==typeof n)&&("function"==typeof n&&(this._plugins[e]=n,t.shift()),i(e,this._opts.plugins)||(this._opts.plugins.push(t.length?[].concat([e],t):e),"function"==typeof this._plugins[e]&&this._plugins[e].apply(this,t)))},u.bind=u.delegate=function(e,i,n){function s(e){for(var s=t.event?t.event:e,a=s.target,r=document.querySelectorAll(i),h=0;h<r.length;h++)if(a===r[h]){n.call(a);break}}this.wrap.addEventListener(e,s,!1);var a=e+";"+i;this._EventHandle.hasOwnProperty(a)?(this._EventHandle[a][0].push(n),this._EventHandle[a][1].push(s)):this._EventHandle[a]=[[n],[s]]},u.unbind=u.unDelegate=function(t,e,i){var n=t+";"+e;if(this._EventHandle.hasOwnProperty(n)){var s=this._EventHandle[n][0].indexOf(i);if(s>-1)return this.wrap.removeEventListener(t,this._EventHandle[n][1][s]),this._EventHandle[n][0][s]=this._EventHandle[n][1][s]=null,!0}return!1},u.destroy=function(){var e=this.outer,i=this.deviceEvents;this.fire("destroy"),this.isTouchable&&(e.removeEventListener(i.startEvt,this),e.removeEventListener(i.moveEvt,this),e.removeEventListener(i.endEvt,this),!i.hasTouch&&e.removeEventListener("mouseout",this)),t.removeEventListener(i.resizeEvt,this),t.removeEventListener("focus",this),t.removeEventListener("blur",this);var n;for(n in this._EventHandle)for(var s=this._EventHandle[n][1],a=0;a<s.length;a++)"function"==typeof s[a]&&this.wrap.removeEventListener(n.substr(0,n.indexOf(";")),s[a]);this._EventHandle=null;for(n in this._LSN)this._LSN.hasOwnProperty(n)&&this._LSN[n]&&t.clearTimeout(this._LSN[n]);this._LSN=null,this.wrap.innerHTML=""},u.on=function(t,e,n){i(t,c.EVENTS)&&"function"==typeof e&&(!(t in this.events)&&(this.events[t]=[]),n?this.events[t].unshift(e):this.events[t].push(e))},u.has=function(t,e){return t in this.events?-1<this.events[t].indexOf(e):!1},u.off=function(t,e){if(t in this.events){var i=this.events[t].indexOf(e);i>-1&&delete this.events[t][i]}},u.fire=function(t){var e=l(arguments,1);t.split(/\x20+/).forEach(function(t){if(t in this.events)for(var i=this.events[t],n=0;n<i.length;n++)"function"==typeof i[n]&&i[n].apply&&i[n].apply(this,e)}.bind(this))},u.reset=function(){this.pause(),this._unWatchTransitionEnd(),this.width="number"==typeof this._opts.width?this._opts.width:this.wrap.offsetWidth,this.height="number"==typeof this._opts.height?this._opts.height:this.wrap.offsetHeight,this.ratio=this.height/this.width,this.scale=this.isVertical?this.height:this.width,this._renderWrapper(),this._autoPlay(),this.fire("reset slideRestored",this.slideIndex,this.currentEl,this)},u.loadData=function(t,e){this.pause(),this._unWatchTransitionEnd(),this.slideIndex=e||0,this.data=t,this._renderWrapper(),this._autoPlay(),this.fire("loadData slideChanged",this.slideIndex,this.currentEl,this)},u.pushData=function(t){if(null!=t){var e=this.data.length;this.data=this.data.concat(t),this.isLooping&&0===this.slideIndex?this._renderItem(this.els[0],this.data.length-1):e-1===this.slideIndex&&(this._renderItem(this.els[2],e),this._autoPlay())}},u.unshiftData=function(t){if(null!=t){n(t)||(t=[t]);var e=t.length;this.data=t.concat(this.data),0===this.slideIndex&&this._renderItem(this.els[0],e-1),this.slideIndex+=e}},u._autoPlay=function(){this.delay>0?t.setTimeout(this.play.bind(this),this.delay):this.play()},u.play=function(){this.pause(),this.isAutoplay&&(this.isLooping||this.slideIndex<this.data.length-1)&&(this._LSN.autoPlay=t.setTimeout(this.slideNext.bind(this),this.duration))},u.pause=function(){this._LSN.autoPlay&&t.clearTimeout(this._LSN.autoPlay)},u.hold=function(){this._holding=!0},u.unhold=function(){this._holding=!1,this.unlock()},u.lock=function(){this.hold(),this._locking=!0},u.unlock=function(){this._locking=!1},u.seamScale=function(t){var e=/scale([XY]?)\(([^\)]+)\)/,i=c.getStyle(t,"transform");e.test(i)?c.setStyle(t,"transform",i.replace(e,function(t,e,i){var n={};return e?(n[e]=parseFloat(i),"scale"+this.axis+"("+(e===this.axis?1.001*n[this.axis]:1.001)+")"):(i.indexOf(",")>-1?(i=i.split(","),n.X=parseFloat(i[0]),n.Y=parseFloat(i[1])):n.Y=n.X=parseFloat(i),n[this.axis]*=1.001,"scale("+n.X+", "+n.Y+")")}.bind(this))):c.setStyle(t,"transform",i+" scale"+this.axis+"(1.001)")},u.originScale=function(t){var e=/([\x20]?scale)([XY]?)\(([^\)]+)\)/;c.setStyle(t,"transform",c.getStyle(t,"transform").replace(e,function(t,e,i,n){return t={},i?"1.001"===n?"":(t[i]=parseFloat(n),"scale"+this.axis+"("+(i===this.axis?t[this.axis]/1.001:1)+")"):(n.indexOf(",")>-1?(n=n.split(","),t.X=parseFloat(n[0]),t.Y=parseFloat(n[1])):t.Y=t.X=parseFloat(n),t[this.axis]/=1.001,"scale("+t.X+", "+t.Y+")")}.bind(this)))},u._isItself=function(t){var e=this.fixPage;if("string"==typeof e){for(var i,n=[],s=t;!a(s,"islider-outer")&&s!==this.wrap;)n.push(s),s=s.parentNode;if(s=n.pop(),n.length)try{if(i=Array.prototype.slice.call(s.querySelectorAll(e)),i.length)for(var r=0;r<n.length;r++)if(i.indexOf(n[r])>-1)return!0}catch(h){return!1}}return!1},u.subjectTo=function(t,e){if(!(!t instanceof c)){var i=this;i.animateTime=t.animateTime,i.isLooping=t.isLooping,i.isAutoplay=!1,t.on("slideStart",function(t){i.startHandler(t)}),t.on("slide",function(t){i.moveHandler(t)}),t.on("slideEnd",function(t){i.endHandler(t)}),t.on("slideChange",function(t){var e=i.data.length,n=i.direction;n>0&&(t-i.slideIndex+e)%e===1?i.slideNext():0>n&&(t-i.slideIndex-e)%e===-1&&i.slidePrev()}),t.on("slideRestore",function(t){i.slideIndex!==t&&i.slideTo(t)})}},"function"==typeof require&&"object"==typeof module&&module&&"object"==typeof exports&&exports?module.exports=c:"function"==typeof define&&define.amd?define(function(){return c}):t.iSlider=t.iSlider||c}(window||this);


