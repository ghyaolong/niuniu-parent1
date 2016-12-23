
function Pageing(){
	/***
	 * 分页请求数据回调
	 * RequestCallBack function();
	 */
	var _pageSize = 2;
	
	var _config = {
			pageSize:_pageSize
	};
	
	this.currentPage = 1;
	
	this.totalRecord = 50;
	
	this.totalPage = 4;
	
	this.limit = _config.pageSize || _pageSize;
	
	/**
	 * 校验并执行数据请求
	 * 若无异常，则请求数据
	 */
	this.verifAndExecute = function(){
		if(Object.getClass(this.RequestCallBack) !== 'Function'){
			throw ReferenceError("");
		}
		/*
		 * 执行数据请求回调
		 */
		this.RequestCallBack({
			limit:this.limit,
			currentPage:this.currentPage
		});
	};
	/**
	 * 跳转至首页
	 */
	this.page_first = function(){
		if(this.currentPage === 1){ return false; }
		this.currentPage = 1; 
		/*
		 * 校验并执行数据请求
		 * 若无异常，则请求数据
		 */
		this.verifAndExecute();
	};
	
	/**
	 * 跳转至当前页的上一页
	 */
	this.page_prev = function(){
		if(this.currentPage <= 1){ return false; }
		
		this.currentPage = this.currentPage - 1; 
		/*
		 * 校验并执行数据请求
		 * 若无异常，则请求数据
		 */
		this.verifAndExecute();
	};
	
	/**
	 * 跳转至当前页的下一页
	 */
	this.page_next = function(){
		if(this.currentPage >= this.totalPage){ return false; }
		this.currentPage = this.currentPage + 1; 
		/*
		 * 校验并执行数据请求
		 * 若无异常，则请求数据
		 */
		this.verifAndExecute();
	};
	
	/**
	 * 跳转至最后一页
	 */
	this.page_last = function(){
		if(this.currentPage === this.totalPage){ return false; }
		this.currentPage = this.totalPage; 
		/*
		 * 校验并执行数据请求
		 * 若无异常，则请求数据
		 */
		this.verifAndExecute();
	};
	
	this.bindData = function(data){
		this.list = data.list;
		this.totalPage = data.totalPage;
		this.totalRecord = data.totalRecord;
	};
}
