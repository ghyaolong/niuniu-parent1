/**
 * common pager  -douzy 
 */
define(function(require, exports, module){
	var $=require("$");
	 require("bootstrap-paginator")($);
	 var pager={
		init:function(e,option){
			var op={
				bootstrapMajorVersion:3,
				totalPages:option.totalPages||20,
				currentPage:option.currentPage+1|| 1,
				numberOfPages:10,
				innerWindow:4,
				onPageClicked: option.callback||function(){}
			};
			e.bootstrapPaginator(op);
		}
	 };
	 module.exports=pager;
});