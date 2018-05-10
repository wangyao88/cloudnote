var Type = [{ id: 'INCOME', text: '收入' }, { id: 'OUTCOME', text: '支出'}];

function addRow() {
	mini.parse();
	var grid = mini.get("tallyGrid");
    var newRow = {};
    grid.addRow(newRow, 0);
    grid.beginEditCell(newRow, "accountBook");
}

function onAccountBookChanged(e){
	mini.parse();
	var grid = mini.get("tallyGrid");
	var combo = e.sender;
    var row = grid.getEditorOwnerRow(combo);
    var type = row.type || "";
    var editor = grid.getCellEditor("category", row);

    var accountBookId = combo.getValue();
    var url = basePath + "accountsystem/category/getCategory?accountBookId="+accountBookId+"&type="+type;
    editor.setUrl(url);
    editor.setValue("");
}

function onTypeChanged(e){
	mini.parse();
	var grid = mini.get("tallyGrid");
	var combo = e.sender;
    var row = grid.getEditorOwnerRow(combo);
    var accountBookId = row.accountBook || "";
    var editor = grid.getCellEditor("category", row);

    var type = combo.getValue();
    var url = basePath + "accountsystem/category/getCategory?accountBookId="+accountBookId+"&type="+type;
    editor.setUrl(url);
    editor.setValue("");
}

function saveData(){
	mini.parse();
	var grid = mini.get("tallyGrid");
	var data = grid.getChanges();
    var json = mini.encode(data);
    grid.loading("保存中，请稍后......");        
    $.ajax({
    	url : basePath + "accountsystem/tally/saveChanges",
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

function onSearchAccountBookChanged(){
	var accountBookId = mini.get("searchAccountBookCombobox").getValue();
	var type = mini.get("searchTypeCombobox").getValue() || "";
	var url = basePath + "accountsystem/category/getCategory?accountBookId="+accountBookId+"&type="+type;
	var searchCategoryCombobox = mini.get("searchCategoryCombobox")
    searchCategoryCombobox.setUrl(url);
	searchCategoryCombobox.setValue("");
}

function onSearchTypeChanged(){
	var type = mini.get("searchTypeCombobox").getValue();
	var accountBookId = mini.get("searchAccountBookCombobox").getValue() || "";
	var url = basePath + "accountsystem/category/getCategory?accountBookId="+accountBookId+"&type="+type;
	var searchCategoryCombobox = mini.get("searchCategoryCombobox")
    searchCategoryCombobox.setUrl(url);
	searchCategoryCombobox.setValue("");
}

function searchTally(){
	var accountBookId = mini.get("searchAccountBookCombobox").getValue() || "";
	var type = mini.get("searchTypeCombobox").getValue() || "";
	var categoryId = mini.get("searchCategoryCombobox").getValue() || "";
	var mark = mini.get("searchMark").getValue() || "";
	var beginDate = mini.get("searchBeginDate").getValue() || "";
	var endDate = mini.get("searchEndDate").getValue() || "";
	mini.parse();
	var grid = mini.get("tallyGrid");
    data = {
			mark : mark,
			beginDate : configurateDate(beginDate),
			endDate : configurateDate(endDate),
			accountBook : accountBookId,
			type : type,
			categoryId : categoryId
	};
	grid.load(data);
}

function configurateDate(value){
	if(!value){
		return "";
	}
	return value.getFullYear() + "-" + (value.getMonth()+1) + "-" + value.getDay();
}

function removeRow(){
	mini.parse();
	var grid = mini.get("tallyGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要删除的账目！");
		return false;
	}
	$.ajax({
    	url : basePath + "accountsystem/tally/delete",
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