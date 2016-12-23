/**
 * common pager  -douzy 
 */
define("banjiajia/web/1.0.0/scripts/common/pager-debug", [ "$-debug", "bootstrap-paginator-debug" ], function(require, exports, module) {
    var $ = require("$-debug");
    require("bootstrap-paginator-debug")($);
    var pager = {
        init: function(e, option) {
            var op = {
                bootstrapMajorVersion: 3,
                totalPages: option.totalPages || 20,
                currentPage: option.currentPage + 1 || 1,
                numberOfPages: 10,
                innerWindow: 4,
                onPageClicked: option.callback || function() {}
            };
            e.bootstrapPaginator(op);
        }
    };
    module.exports = pager;
});
