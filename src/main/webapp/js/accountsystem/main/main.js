function loadMenu(){
	 var list = [
	        { id: "1", text: "首页", url: "accountsystem/main/index"},            
	        { id: "2", text: "账本管理", url: "accountsystem/accountbook/index"},
	        { id: "3", text: "收支类别管理"},
	        { id: "4", text: "收入类别管理", pid: "3"},
	        { id: "5", text: "支出类别管理", pid: "3"},
	        { id: "6", text: "记账管理"},
	        { id: "7", text: "记账", pid: "6"},
	        { id: "8", text: "查账", pid: "6"},
	        { id: "9", text: "财富统计"},
            { id: "10", text: "收入统计", pid: "9"},
	        { id: "11", text: "支出统计", pid: "9"},
	        { id: "12", text: "总和统计"}
     ];
     var tree = mini.get("menuTree");
     tree.loadList(list, "id", "pid");

}

function menuClick(){
	var tree = mini.get("menuTree")
    var node = tree.getSelectedNode();
	openTab(node);
}

function openTab(node){
    var tabsEle = mini.get("tabs");
    var tabs = tabsEle.getTabs();
    var length = tabs.length;
    var isExists = false;
    $(tabs).each(function(index,tab){
    	if(node.text == tab.title){
    		tabsEle.activeTab(tab);
    		isExists = true;
    	}
    });
    if(isExists){
    	return true;
    }
    initTab(tabsEle,node);
}

function initTab(tabsEle,node){
	var tab = {title: node.text};
    tab = tabsEle.addTab(tab);            
    var el = tabsEle.getTabBodyEl(tab);
    var width =  $("#tabs").width()-20;
    var height = $(el).height()-20;
    el.innerHTML = '<iframe width="'+width+'" height="'+height+'" frameborder="0" src="'+node.url+'" scrolling="auto" ></iframe>';
    tabsEle.activeTab(tab);
}

$(document).ready(function(){
	mini.parse();
	loadMenu();
});