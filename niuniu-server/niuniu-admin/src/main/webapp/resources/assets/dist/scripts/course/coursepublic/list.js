define("banjiajia/web/1.0.0/scripts/course/coursepublic/list",["$","pager","ace-elements","jggrid","gridlocale","validation","util"],function(a){function b(a){var b="";for(var h in a.data)b+=c(a.data[h]);g.html(b),j=a.page.length;var i={totalPages:a.page.total,currentPage:a.page.currPage,callback:d};f.init(e(".pagination"),i)}function c(a){var b="";return b+='<tr class="odd"><td class="center  sorting_1"><label><input type="checkbox" class="ace"><span class="lbl"></span></label></td><td class=" "><a href="'+_contentPath+"/course/couserpublic/show/"+a.id+'.html">'+a.id+"</a>"+"</td>"+'<td class="">'+a.courseIds+"</td>"+'<td class=" ">'+a.teacherId+"</td>"+'<td class=" ">'+a.startTime+"</td>"+'<td class=" ">'+a.endTime+"</td>"+'<td class=" ">'+a.state+"</td>"+'<td class=" ">'+a.serviceAreas+"</td>"+'<td class=" ">'+a.coursePublicNum+"</td>"+'<td class=" ">'+a.createTime+"</td>"+'<td class=" ">'+a.modifyTime+"</td>"+'<td class=" ">'+'<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">'+'<a class="blue" href="'+_contentPath+"/course/couserpublic/show/"+a.id+'.html">'+'<i class="icon-zoom-in bigger-130"></i>'+"</a>"+'<a class="green" href="'+_contentPath+"/course/couserpublic/edit/"+a.id+'.html">'+'<i class="icon-pencil bigger-130"></i>'+"</a>"+'<a class="red" href="#">'+'<i class="icon-trash bigger-130"></i>'+"</a>"+"</div>"+"</td>"+"</tr>"}function d(a,c,d,f){var g=(parseInt(f)-1)*j;e.ajax({url:_contentPath+"/course/coursepublic/list/query.json?begin="+g,async:!0,dataType:"json",success:function(a){b(a.coursePublics)},error:function(a,b,c){console.log("error="+c)}})}var e=a("$"),f=a("pager");a("ace-elements"),a("jggrid"),a("gridlocale"),a("validation")(e);var g=e("#theacertbody"),h=a("util"),i=new h,j=0,k={init:function(){i.ajaxGlobalSetting(),d(null,null,null,0)}};k.init()});