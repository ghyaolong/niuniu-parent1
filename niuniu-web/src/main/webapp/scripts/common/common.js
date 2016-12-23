/*js的异常e除了e.message 和 e.lineNumber之外还有一个重要的属性 e.name 
e.name 错误类型, 
e.message 错误的详细信息. 
Error.name的六种值对应的信息： 
1. EvalError：eval()的使用与定义不一致 
2. RangeError：数值越界 
3. ReferenceError：非法或不能识别的引用数值 
4. SyntaxError：发生语法解析错误 
5. TypeError：操作数类型错误 
6. URIError：URI处理函数使用不当 
*/
/**
 * 获取当前参数的数据类型
 * "Array", "Boolean", "Date", "Number", 
 * "Object", "RegExp", "String", "Window","Function",
 * "HTMLDocument"
 * @param obj 获取 clss[type] 目标
 */
Object.getClass = function(obj){
	var _type = typeof(obj);
	if(_type === 'object'){
		return toString.call(obj).match(/^\[object\s(.*)\]$/)[1];
	}
	// typeof(obj) 返回值 首字母转大写
	return _type.cvt_firstCharUpper();
};

Object.cvt_springParam = function(item){
	var type = toString.call(item);
	if(type !== '[object Object]'){
		return item;
	}
	//递归处理数据
	function recursion(item,key,obj){
		var type = Object.getClass(obj);
		var __key = key;
		if(type === 'Array'){
			__key = __key + '[?]';
		}else if(type === 'Object'){
			__key = __key + '.?';
		}
		
		if(type === 'Array' || type === 'Object'){
			for(var _key in obj){
				//若当前对象的存在Function 则删除
				var _type = Object.getClass(obj[_key]);
				if(_type === 'Function'){
					delete obj[_key];continue;
				}
				recursion.call(this,item,__key.replace(/\?/g,_key),obj[_key]);
			}
		}else{
			item[key] = obj;
		}
	};
	for(var key in item){
		var obj = item[key];
		type = Object.getClass(obj);
		if(type === 'Array' || type === 'Object'){
			recursion.call(this,item,key,obj);
			delete item[key];
		}else if(type === 'Function'){
			delete item[key]; continue;
		}
	}
	return item;
};

/**
 * 去除 String 左右空格
 * ' aa '  	=> 'aa'
 * '  bb'	=> 'bb'
 * 'cc 	'	=> 'cc'
 * 'aa bb'	=> 'aa bb'
 */
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g,'');
};

/**
 * 边界单词 首字母大写
 * 例：
 * 有效
 * ' function '		=> 'Function'
 * 'function array'	=> 'Function Array'
 * 无效
 * 'function_array' => 'Function_array'
 * 'function*array' => 'Function*array'
 */
String.prototype.cvt_firstCharUpper = function(){
	return this.trim().replace(/\b(\w)/g,function($){return $.toUpperCase();});
};

/**
 * 边界单词 首字母小写
 * 例：
 * 有效
 * ' Function '		=> 'function'
 * 'Function array'	=> 'function array'
 * 无效
 * 'Function_array' => 'function_array'
 * 'Function*array' => 'function*array'
 */
String.prototype.cvt_firstCharLower = function(){
	return this.trim().replace(/\b(\w)/g,function($){return $.toLowerCase();});
};

/**
 * 驼峰命名：小驼峰
 * 例：
 * 'one_two_three_four' => 'oneTwoThreeFour'
 * 'one__two__three__four' => 'oneTwoThreeFour'
 */
String.prototype.cvt_littleHumpCase = function(separator){
	//先转换为大驼峰 例：'one_two_three_four' => 'OneTwoThreeFour'
	var value = this.cvt_bigHumpCase(separator);
	
	//首字母转小写 例：'OneTwoThreeFour' => 'oneTwoThreeFour'
	// 大驼峰转换为小驼峰 例：'OneTwoThreeFour' => 'oneTwoThreeFour'
	return value.cvt_firstCharLower();
};

/**
 * 驼峰命名：大驼峰
 * 例：
 * 'one_two_three_four' => 'OneTwoThreeFour'
 * 'one__two__three__four' => 'OneTwoThreeFour'
 */
String.prototype.cvt_bigHumpCase = function(separator){
	/*-------------separator verify begin-------------------------------*/
	var separatorList = ['_',',','*','$','#','@'];
	if(Object.getClass(separator) === 'String'){
		if(separatorList.exist(separator) === false){
			separator = '_';
		}
	}else{
		separator = '_';
	}
	/*-------------separator verify end---------------------------------*/
	//去除左右空格
	var value = this.trim();
	// 把 分隔符(separator) 替换 为 ' '
	// 例： 'one_two_three_four' => 'one two three four'
	value = value.replace(new RegExp(separator,'g'),' ');
	//设置边界字符 首字母大写
	//例：'one two three four' => 'One Two Three Four'
	value = value.cvt_firstCharUpper();
	//替换空白字符为 ''
	//例：'One Two Three Four' => 'OneTwoThreeFour'
	return value.replace(/\s*/g,'');
};


/**
 * 去除 String 左右空格
 * ' aa '  	=> 'aa'
 * '  bb'	=> 'bb'
 * 'cc 	'	=> 'cc'
 * 'aa bb'	=> 'aa bb'
 * @param value 当前要转换的字符串
 */
String.trim = function(value){
	if(String.getClass(value) !== 'String') {
		return '';
	}
	return value.trim();
};

/**
 * 检测 Array 是否存在 某个 值
 * type arg === 'Function' 回调检测
 * type arg !== 'Function' 值比较
 * @param arg 参数 Function or String
 * @return @type Boolean true 存在 or false 不存在
 */
Array.prototype.exist = function(arg){
	/*
	 * 当前数组 元素 为 0 表示不存在
	 */
	if(this.length <= 0){ return false;}
	
	if(Object.getClass(arg) === 'undefined'){
		throw new TypeError( arg + 'is undefined.');
	}
	
	var type = function(arg){
		var _type = Object.getClass(arg);
		/*
		 * 若是 arg 若是 Function 则调用方法
		 */
		if(_type === 'Function'){
			return arg;
		}
		/*
		 * 若是 arg 若不是 Function 则 直接进行 值得比较
		 */
		return function (item){
			return (item === arg ? true : false);
		};
	};
	
	var callBack = type(arg);
	for(var index in this) {
		/*
		 * 若存在则返回 true
		 */
		if(callBack.call(this,index,this[index]) === true){
			return true;
		}
	}
	return false;
};

/**
 * 检测 Array 是否存在 某个 值
 * type arg === 'Function' 回调检测
 * type arg !== 'Function' 值比较
 * @param target 参数 Array 容器
 * @param arg 参数 Function or String
 * @return true 存在 or false 不存在
 */
//Array.exist = function(target,arg){
//	if(Object.getClass(target) !== 'Array'){
//		throw new TypeError( target + 'is not Array.');
//	}
//	return target.exist(arg);
//};
function Utils(){};
Utils.ajaxJson = function(config){
	if(Object.getClass(config) === 'Object'){
		delete config['success'];
	}
	var _config = {
		type:'POST',
		data: Object.cvt_springParam(config.param),
		success: function(data){
	   		if(data.success == false){
	   			layer.alert(data.message);
	   			return false;
	   		}
	   		_config.fnSuccess(data);
		}
	};
	
	_config = $.extend(_config,config || {});
	
	
	if(Object.getClass(_config.fnSuccess) !== 'Function'){
		_config.fnSuccess = function(data){};
	}
	
	$.ajax(_config);
	
	
};

