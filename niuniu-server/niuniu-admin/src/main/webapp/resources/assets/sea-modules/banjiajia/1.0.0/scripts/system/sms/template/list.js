define("banjiajia/web/1.0.0/scripts/system/sms/template/list",["$","pager","util","ace-elements","jggrid","gridlocale","validation"],function(a){function b(a){var b="";for(var d in a)b+=c(a[d]);h.html(b)}function c(a){var b="";return b+='<tr class="odd"><td class="center  sorting_1"><label><input type="checkbox" class="ace"><span class="lbl"></span></label></td><td class=" ">'+a.templateKey+"</td>"+'<td class="">'+a.templateValue+"</td>"+'<td class=" ">'+a.desb+"</td>"+'<td class=" ">'+'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+'<a class="blue" href="'+_contentPath+"/order/schedule/show/"+a.id+'.html">'+'<i class="icon-zoom-in bigger-130"></i>'+"</a>"+'<a class="green" href="'+_contentPath+"/order/schedule/edit/"+a.id+'.html">'+'<i class="icon-pencil bigger-130"></i>'+"</a>"+'<a class="red" href="#">'+'<i class="icon-trash bigger-130"></i>'+"</a>"+"</div>"+"</td>"+"</tr>"}function d(a,c,d,f){var g=(parseInt(f)-1)*i;e.ajax({url:_contentPath+"/system/sms/template/list/query.json?begin="+g,async:!0,dataType:"json",success:function(a){b(a.smsTemplates)},error:function(a,b,c){console.log("error="+c)}})}var e=a("$"),f=(a("pager"),a("util")),g=new f;g.ajaxGlobalSetting(),a("ace-elements"),a("jggrid"),a("gridlocale"),a("validation")(e);var h=e("#theacertbody"),i=0,j={init:function(){d(null,null,null,0)}};j.init()});
