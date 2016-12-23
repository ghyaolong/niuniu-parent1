
/**
 * 客户集合 Controller
 * @param $scope 当前作用域
 * @param $http 当前http
 */
function AppController($scope,$http){
	$scope.url = "../api/api.htm";
	
	var requestParam = {
		"source":"PC",
		'code':'1002',
		'imei':'864181027234767',
		'data':{"customerId":97}
	};
	
	$scope.requestParam =  JSON.stringify(requestParam);
	$scope.responseParam = "";
	
	$scope.$bindData = function(data){
		$scope.responseParam = JSON.stringify(data);
		$scope.$apply();
	}
	
	/**
	 * 必须在 Data 之后调用
	 */
	var mak = function(param){
		var mak = 'tjz2015com' + param['code'] + param['imei'] + param['data'];
		mak = faultylabs.MD5(mak);
		console.log(faultylabs.MD5("1").toLowerCase());
		return mak.toLowerCase();
	}
	$scope.test = function(){
		var _param = $.parseJSON($scope.requestParam);
		//解析数据
		_param["data"] = JSON.stringify(_param["data"]);
		//生成mak,若秘钥参数不存在，则进行自动装载
		if(Object.getClass(_param["mak"]) !== 'String'){
			_param["mak"] = mak(_param);
		}
		Utils.ajaxJson({
			url: $scope.url,
			param : _param,
			fnSuccess:function(data){
				$scope.$bindData(data);
			}
		});
	}
};