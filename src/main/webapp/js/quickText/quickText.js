var currentQuickTextId = null;
var switchChange = true;

$(document).ready(function() {
    addListener();
    reLoad();
});

function addListener() {
    mini.parse();
    var grid = mini.get("quickTextGrid");
    grid.on("load", function () {
        grid.select(0);
        var record = grid.getSelected();
        if (record && record.id) {
            getQuickTextById(record.id);
        }
    });
    editor.addListener("contentChange", saveOrUpdateQuickText);
}

function getQuickTextById(id) {
    currentQuickTextId = id;
    mini.mask({
        el : document.body,
        cls : 'mini-mask-loading',
        html : '加载中...'
    });
    editor.ready(function(){
        $.ajax({
            url : basePath + "/quickText/findOne",
            type : "get",
            data : {
                id : id
            },
            dataType : 'json',
            success : function(result) {
                var operationResult = JSON.parse(result);
                switchChange = false;
                editor.setContent(operationResult.data.content);
                switchChange = true;
                mini.unmask(document.body);
            },
            error : function() {
				mini.unmask(document.body);
                mini.alert("获取QuickText详情失败，请稍候重试！");
            }
        });
    });
}

function saveOrUpdateQuickText() {
    if(!switchChange) {
        return;
    }
    if(!currentQuickTextId) {
        doSaveOrUpdateQuickText("add", "保存");
        return;
    }
    doSaveOrUpdateQuickText("update", "更新");
}

function doSaveOrUpdateQuickText(operation, msg) {
    mini.mask({
        el : document.body,
        cls : 'mini-mask-loading',
        html : 'QuickText' + msg + '中...'
    });
    $.ajax({
        url : basePath + "/quickText/" + operation,
        type : "post",
        data : {
            id : currentQuickTextId,
            content : editor.getContent()
        },
        dataType: 'json',
        success : function(result) {
            var operationResult = JSON.parse(result);
            if(operationResult.status) {
                currentQuickTextId = operationResult.data;
                if(operation === 'add') {
                    reLoad();
                }
            }else {
                mini.alert("QuickText" + msg + "失败，请稍候重试！");
            }
            mini.unmask(document.body);
        },
        error : function(error) {
            console.log(error);
            mini.alert("QuickText" + msg + "失败，请稍候重试！");
            mini.unmask(document.body);
        }
    });
}

function reLoad() {
    mini.parse();
    var grid = mini.get("quickTextGrid");
    grid.loading();
    grid.load();
    grid.hideColumn("idColumn");
}

function gridRowClick() {
    var grid = mini.get("quickTextGrid");
    var record = grid.getSelected();
    getQuickTextById(record.id);
}

function newQuickText() {
    switchChange = false;
    editor.setContent('');
    currentQuickTextId = null;
    switchChange = true;
}

function removeQuickText() {
    var grid = mini.get("quickTextGrid");
    var record = grid.getSelected();
    if (!record) {
        mini.alert("请选择需要删除的QuickText！");
        return false;
    }
    mini.confirm("确定删除QuickText[" + record.title + "]吗？", "确定？",
        function(action) {
            if (action === "ok") {
                $.ajax({
                    url : basePath + "/quickText/delete",
                    type : "post",
                    data : {
                        id : record.id
                    },
                    success : function() {
                        reLoad();
                    },
                    error : function() {
                        mini.alert("笔记删除失败，请稍候重试！");
                    }
                });
            }
        }
    );
}