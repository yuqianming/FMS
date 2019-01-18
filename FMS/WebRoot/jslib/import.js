/**
 * 
 * @requires jQuery
 * 
 * 导入文件时提示进度
 * **/
function importLoad(){  
    $("<div class=\"datagrid-mask\" style=\"position:absolute;z-index: 9999;\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");  
    $("<div class=\"datagrid-mask-msg\" style=\"position:absolute;z-index: 9999;width:200px;height:100px;\"></div>").html("数据读取中，请稍候...").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 290) / 2,top:($(window).height() - 145) / 2});  
}

/**
 * 
 * @requires jQuery
 * 
 * 导入文件时追加进度提示
 * **/
function importHand(msg){  
    //添加一行
    $(".datagrid-mask-msg").append("<br/>"+msg); 
}