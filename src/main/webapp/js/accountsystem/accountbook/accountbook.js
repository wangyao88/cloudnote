function addAccountBookPage() {
	mini.open({
		url : bootPATH + "accountsystem/accountbook/form.jsp",
		title : "添加账本",
		width : 300,
		height : 160,
		onload : function() {
			
		},
		ondestroy : function(action) {
			if (action == "ok") {
				mini.parse();
				mini.mask({
					el : document.body,
					cls : 'mini-mask-loading',
					html : '账本列表加载中...'
				});
				var grid = mini.get("accountbookGrid");
				grid.loading();
				grid.load();
				mini.unmask(document.body);
			}
		}
	})
}

function addAccountBook(){
	mini.parse();
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '账本保存中...'
	});
	$.ajax({
		url : basePath + "accountsystem/accountbook/add",
		type : "post",
		data : {
			name : mini.get("name").value,
			mark : mini.get("mark").value
		},
		success : function() {
			mini.unmask(document.body);
			closeWindow("ok");
		},
		error : function() {
			mini.unmask(document.body);
			closeWindow("bad");
		}
	});
}

function closeWindow(action) {
	if (window.CloseOwnerWindow) {
		return window.CloseOwnerWindow(action);
	}
	window.close();
}

function saveAccountBook(){
	mini.parse();
	var grid = mini.get("accountbookGrid");
	var data = grid.getChanges();
    var json = mini.encode(data);
    grid.loading("保存中，请稍后......");        
    $.ajax({
    	url : basePath + "accountsystem/accountbook/saveChanges",
        data: { 
        	data : json
        },
        type: "post",
        success: function (text) {
            grid.reload();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            mini.alert("保存失败");
        }
    });
}

function removeAccountBook(){
	mini.parse();
	var grid = mini.get("accountbookGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要删除的账本！");
		return false;
	}
	$.ajax({
    	url : basePath + "accountsystem/accountbook/remove",
        data: { 
        	id : record.id
        },
        type: "post",
        success: function (text) {
            grid.reload();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            mini.alert("删除失败");
        }
    });
}

function searchAccountBookByName(){
	var name = mini.get("searchAccountBookByNameText").value;
	mini.parse();
	var grid = mini.get("accountbookGrid");
	grid.load({
		name : name
	});
}