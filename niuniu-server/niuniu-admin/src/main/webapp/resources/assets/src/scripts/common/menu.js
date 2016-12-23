/**
 * 左侧菜单加载
 */
define(function(require,exports,module){
     var $=require("$"),Handlebars= require("handlebars");
    var Util=require("util");
    var util=new  Util();
    function leftMenu(){
        this.Flag=true
        this.$menuTemp=$("#leftMenuTemplate")
        this.$navMenuUL=$("#navMenuUL")
    }
    leftMenu.prototype.init=function(){
        var self=this;
        util.ajaxGlobalSetting();
        self.loadMenuData();
    }
    leftMenu.prototype.loadMenuData=function() {
        var self=this;
        //if(self.Flag) {
            $.ajax({
                url: _contentPath + '/menu/getLeftMenu.json',
                async:false,
                dataType: 'json',
                //beforeSend: function () {
                //    self.Flag = false;
                //},
                success: function (data) {
                    self.evalMenu(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("error=" + errorThrown);
                }, complete: function () {
                    //self.Flag = true;
                }
            });
        //}
    }
    /**
     * handlebars 解析
     * @param data
     */
    leftMenu.prototype.evalMenu=function(data) {
        var self = this;
        var $menuObj = util.JsonEval(data);
        if(!!$menuObj) {
            var menus=$menuObj.result;
            $.each(menus, function (i) {
                  var menu=menus[i];
                var permissionHTMLArr=new Array();
                var resultHTML="<li>{0}</li>";
                var $parentPermission=menu.staticPermission;
                var $childrenPermissions=menu.staticPermissionList;
                if(!!$parentPermission){
                    permissionHTMLArr.push(self.getParentMenuHTML($parentPermission));
                }
                if(!!$childrenPermissions){
                    permissionHTMLArr.push(self.getChildrenMenuHTML($childrenPermissions));
                }
                self.$navMenuUL.append(String.format(resultHTML,permissionHTMLArr.join("")));
            });
            require("ace-elements");

        }
    }
    leftMenu.prototype.eachChildren=function($childrenPermissions) {
        var html = "";
        $.each($childrenPermissions, function (i) {
            var childrenPer=$childrenPermissions[i].staticPermission;
            html += '<li data-nav='+childrenPer.permissionCode+'>'
            html += '<a href="'+_contentPath+childrenPer.permissionAction+'.html">'
            html += '<i class="icon-double-angle-right"></i>'+childrenPer.permissionName
            html += '</a>'
            html += '</li>'
        });
        return html;
    }
    /**
     * children menu left HTMl
     */
    leftMenu.prototype.getChildrenMenuHTML=function($childrenPermissions) {
        var self = this;
        return '<ul class="submenu">'
            + self.eachChildren($childrenPermissions)
            + '</ul>'
    }
    /**
     * parent menu left HTML
     * @returns {string}
     */
    leftMenu.prototype.getParentMenuHTML=function($parentPermission) {
        return  '<a class="dropdown-toggle" href="#">'
                    + '<i class="'+$parentPermission.permissionIcon+'"></i>'
                    + '<span class="menu-text">'
                    + $parentPermission.permissionName
                    + '</span>'
                    + '<b class="arrow icon-angle-down"></b>'
                + '</a>'
    }
    new leftMenu().init();
});