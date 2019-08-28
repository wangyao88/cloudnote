mini.parse();

function loadTodoGridWithDateTreeClick(node) {
    var data = {
        nodeId: node.id,
        nodeText: node.text
    };
    mini.parse();
    var todoTreeGrid = mini.get("todoTreeGrid");
    todoTreeGrid.loading();
    todoTreeGrid.load(data);
    todoTreeGrid.hideColumn("idColumn");
}

function dateTreeClick(){
    var tree = mini.get("dateTree");
    var node = tree.getSelectedNode();
    loadTodoGridWithDateTreeClick(node);
}

function selectNodeOnLoad() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var id = year + "" + month;
    mini.parse();
    var tree = mini.get("dateTree");
    var node = tree.getNode(id);
    if(node){
        tree.scrollIntoView(node);
        tree.selectNode(node);
        dateTreeClick();
    }
}

function openEditWindowByAdd() {
    var todoTreeGrid = mini.get("todoTreeGrid");
    var row = todoTreeGrid.getSelected();
    var editWindow = mini.get("editTodoWindow");
    editWindow.show();
    if(row) {
        mini.get("parentId").setValue(row.id);
        mini.get("parentContent").setValue(row.content);
    }
}

function openEditWindowByUpdate() {
    var todoTreeGrid = mini.get("todoTreeGrid");
    var row = todoTreeGrid.getSelected();
    if(!row) {
        mini.alert("请选择需要修改的数据！");
        return false;
    }
    openEditWindow(row.id);
}

function openEditWindow(id) {
    var editWindow = mini.get("editTodoWindow");
    editWindow.show();
    var form = new mini.Form("#editTodoForm");
    form.loading();
    $.ajax({
        url : basePath + "/todo/findOne",
        type : "post",
        data : {
            id : id
        },
        success : function(result) {
            var operationResult = JSON.parse(result);
            var data = mini.decode(operationResult.data);
            form.setData(data);
            form.unmask();
        },
        error : function() {
            form.unmask();
            mini.alert("获取待办详情失败，请稍候重试！");
        }
    });
}

function saveTodo() {
    var todoTreeGrid = mini.get("todoTreeGrid");
    var form = new mini.Form("#editTodoForm");
    var data = form.getData();
    todoTreeGrid.loading("保存中，请稍后......");
    var json = mini.encode(data);
    $.ajax({
        url : basePath + "/todo/save",
        type : "post",
        data: {
            todo: json
        },
        success: function () {
            closeEditTodoWindow();
            var form = new mini.Form("#editTodoForm");
            form.clear();
            dateTreeClick();
        },
        error: function (jqXHR) {
            mini.alert(jqXHR.responseText);
        }
    });
}

function closeEditTodoWindow() {
    mini.get("editTodoWindow").hide();
}

function removeTodo() {
    var todoTreeGrid = mini.get("todoTreeGrid");
    var row = todoTreeGrid.getSelected();
    if(!row) {
        mini.alert("请选择需要删除的数据！");
        return false;
    }
    $.ajax({
        url : basePath + "/todo/delete",
        type : "post",
        data: {
            id: row.id
        },
        success: function () {
            dateTreeClick();
        },
        error: function (jqXHR) {
            mini.alert(jqXHR.responseText);
        }
    });
}

function rendererStatus(e) {
    if(e.row.status === "已完成") {
        e.rowStyle = 'background-color:#DCDCDC';
    }
    if(e.row.status === "未完成") {
        e.rowStyle = 'background-color:#D3D3D3';
    }
    return e.value;
}

function gridOnDrawcell() {
    var todoTreeGrid = mini.get("todoTreeGrid");
    todoTreeGrid.on("drawcell", function(e) {
        var field = e.field;
        var value = e.value;
        if (field === "status" && value === "已完成") {
            e.cellStyle = "background:#32CD32";
        }
        if (field === "status" && value === "未完成") {
            e.cellStyle = "background:#D3D3D3";
        }
    });
}

$(document).ready(function() {
    selectNodeOnLoad();
    gridOnDrawcell();
});