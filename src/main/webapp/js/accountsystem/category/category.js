mini.parse();

//右键菜单
function onBeforeOpenIncomeCategoryTreeMenu(e) {
    var menu = e.sender;
    var tree = mini.get("incomeCategoryTree");

    var node = tree.getSelectedNode();
    if (!node) {
        e.cancel = true;
        return;
    }
    if (node && node.text == "Base") {
        e.cancel = true;
        //阻止浏览器默认右键菜单
        e.htmlEvent.preventDefault();
        return;
    }

    ////////////////////////////////
    var editItem = mini.getbyName("edit", menu);
    var removeItem = mini.getbyName("remove", menu);
    editItem.show();
    removeItem.enable();

    if (node.id == "forms") {
        editItem.hide();
    }
    if (node.id == "lists") {
        removeItem.disable();
    }
}

function onBeforeOpenOutcomeCategoryTreeMenu(e) {
    var menu = e.sender;
    var tree = mini.get("outcomeCategoryTree");

    var node = tree.getSelectedNode();
    if (!node) {
        e.cancel = true;
        return;
    }
    if (node && node.text == "Base") {
        e.cancel = true;
        //阻止浏览器默认右键菜单
        e.htmlEvent.preventDefault();
        return;
    }

    ////////////////////////////////
    var editItem = mini.getbyName("edit", menu);
    var removeItem = mini.getbyName("remove", menu);
    editItem.show();
    removeItem.enable();

    if (node.id == "forms") {
        editItem.hide();
    }
    if (node.id == "lists") {
        removeItem.disable();
    }
}

//新增节点
function onAddIncomeNode(e){
	var incomeAccountBook = mini.get("incomeAccountBook");
	var accountBookId = incomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在添加类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("incomeCategoryTree");
    var type = "INCOME";
    var url = basePath + "accountsystem/category/getIncomeCategoryTree?accountBookId="+accountBookId;
    addNode(tree,type,accountBookId,url);
}

function onAddOutcomeNode(e){
	var outcomeAccountBook = mini.get("outcomeAccountBook");
	var accountBookId = outcomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在添加类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("outcomeCategoryTree");
    var type = "OUTCOME";
    var url = basePath + "accountsystem/category/getOutcomeCategoryTree?accountBookId="+accountBookId;
    addNode(tree,type,accountBookId,url);
}

function addNode(tree,type,accountBookId,url){
	var node = tree.getSelectedNode();
    var level = tree.getLevel(node);
	mini.prompt("请输入类别名称：", "请输入",
       function (action, value) {
           if (action == "ok") {
                if(value == null || value == ""){
               		 mini.alert("类别名称不能为空！");
               		 return false;
               	}else{
               		$.ajax({
               			url : basePath + "accountsystem/category/add",
               			type : "post",
               			data : {
               				parentId : node.id,
               				name : value,
               				type : type,
               				accountBookId : accountBookId,
               				level : level
               			},
               			success : function(){
               				tree.load(url);
               	        },
               	        error : function(){
               	        	mini.alert("类别添加失败，请稍候重试！");
               	        }
               		});
               	}
           }
       }
    );
}

function onEditIncomeNode(){
	var incomeAccountBook = mini.get("incomeAccountBook");
	var accountBookId = incomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在修改类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("incomeCategoryTree");
    var url = basePath + "accountsystem/category/getIncomeCategoryTree?accountBookId="+accountBookId;
    editNode(tree,url);
}

function onEditOutcomeNode(){
	var outcomeAccountBook = mini.get("outcomeAccountBook");
	var accountBookId = outcomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在修改类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("outcomeCategoryTree");
    var url = basePath + "accountsystem/category/getOutcomeCategoryTree?accountBookId="+accountBookId;
    editNode(tree,url);
}

function editNode(tree,url){
	var node = tree.getSelectedNode();
	mini.prompt("请输入类别名称：", "请输入",
       function (action, value) {
           if (action == "ok") {
                if(value == null || value == ""){
               		 mini.alert("类别名称不能为空！");
               		 return false;
               	}else{
               		$.ajax({
               			url : basePath + "accountsystem/category/update",
               			type : "post",
               			data : {
               				id : node.id,
               				name : value
               			},
               			success : function(){
               				tree.load(url);
               	        },
               	        error : function(){
               	        	mini.alert("类别修改失败，请稍候重试！");
               	        }
               		});
               	}
           }
       }
    );
}

function onRemoveIncomeNode(){
	var incomeAccountBook = mini.get("incomeAccountBook");
	var accountBookId = incomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在删除类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("incomeCategoryTree");
    var url = basePath + "accountsystem/category/getIncomeCategoryTree?accountBookId="+accountBookId;
    removeNode(tree,url);
}

function onRemoveOutcomeNode(){
	var outcomeAccountBook = mini.get("outcomeAccountBook");
	var accountBookId = outcomeAccountBook.getValue();
	if(!accountBookId){
		mini.alert("请选择账本，在删除类别。如果没有账本，请先创建账本！");
		return false;
	}
	var tree = mini.get("outcomeCategoryTree");
    var url = basePath + "accountsystem/category/getOutcomeCategoryTree?accountBookId="+accountBookId;
    removeNode(tree,url);
}

function removeNode(tree,url){
	var node = tree.getSelectedNode();
	$.ajax({
			url : basePath + "accountsystem/category/delete",
			type : "post",
			data : {
				id : node.id
			},
			dataType : "json",
			success : function(result){
				if(result.status){
					tree.load(url);
					return;
				}
				mini.alert(result.msg);
	        },
	        error : function(){
	        	mini.alert("类别删除失败，请稍候重试！");
	        }
		});
}

function onIncomeChange(){
	var incomeAccountBook = mini.get("incomeAccountBook");
	var accountBookId = incomeAccountBook.getValue();
	var url = basePath + "accountsystem/category/getIncomeCategoryTree?accountBookId="+accountBookId;
	var tree = mini.get("incomeCategoryTree");
	tree.load(url);
}

function onOutcomeChange(){
	var outcomeAccountBook = mini.get("outcomeAccountBook");
	var accountBookId = outcomeAccountBook.getValue();
	var url = basePath + "accountsystem/category/getOutcomeCategoryTree?accountBookId="+accountBookId;
	var tree = mini.get("outcomeCategoryTree");
	tree.load(url);
}