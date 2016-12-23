var pages = new HashTable();

$(function() {
    $('#tt').tabs({
        onClose: function (title, index) {
          
            pages.remove(title);
          
        }
    });


  
});

function addPanel(id,name, href) {

  
    if (pages.containsKey(name)) {
        var index = $('#tt').tabs('select', name);

    } else {
        pages.add(name, href);

        $('#tt').tabs('add', {
            title: name,

            content: "<iframe id='" + id + "' class=\"mainframe\" name='" + id + "' frameborder='0'  src='" + href + "' ></iframe>",
            
            closable: true
        });
    }

}
function removePanel(id) {
    var tab = $('#tt').tabs('getSelected');

    if (tab) {


        var index = $('#tt').tabs('getTabIndex', tab);
        $('#tt').tabs('close', index);
        pages.remove(title);

    }
}
