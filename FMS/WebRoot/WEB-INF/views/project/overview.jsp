<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />

	<script type="text/javascript">
	$.canExport = false;
	$.canSearch=false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT01010')
		{
			$.canExport=true;
		}
		else if(buttonList[i].id=='BT01011')
		{
			$.canSearch=true;
		}
	}
	
	</script>
	
<title></title>

	<script type="text/javascript">
	
	var dataGrid;
	var url ='${ctx}/overview/dataGrid';
	$(function() {
		buttonHandle();
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'project_code',
			sortName : 'project_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"项目信息列表",
			showFooter:true,
			columns : [ [{
				width : '130',
				title : '项目编号',
				field : 'project_code',
				sortable : true,
				align:'center'
			}/*, {
				width : '140',
				title : '站址编号',
				field : 'address_code',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '站址名称',
				field : 'address_name',
				sortable : true,
				align:'center'
			}*/,{
				width : '230',
				title : '供应商',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '科目名称',
				field : 'account_name',
				sortable : true,
				align:'center'
			}, {
				width : '80',
				title : '入账金额',
				field : 'account_amt',
				sortable : true,
				align:'right'
			},{
				width : '100',
				title : '已付款不含税',
				field : 't_pay_amt',
				sortable : true,
				align:'right'
			},{
				width : '100',
				title : '签字未付款不含税',
				field : 's_pay_amt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '未付款金额',
				field : 'n_pay_amt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '已开发票',
				field : 't_invoice_amt',
				sortable : true,
				align:'right'
			},{
				width : '100',
				title : '开票不含税金额',
				field : 't_invoice_amt_e_tax',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
			}
		});
		initCombobxSelect();
	});

	function buttonHandle()
	{
		if(!$.canSearch)
		{
			$('#btnSearch').hide();
		}
		if(!$.canExport)
		{
			$('#btnExport').hide();
		}
	}
	
	function initCombobxSelect()
	{
		$("#supplier").combobox({
			url:'${ctx}/supplier/getSupplierList',
			valueField:'supplier_name',
			textField:'supplier_name'
		});
		
	}
	
	function searchData()
	{
		var queryParams=dataGrid.datagrid('options').queryParams;
		queryParams.project_code=$("#project_code").val();
		queryParams.supplier_name=$("#supplier").combobox("getText");
		queryParams.project_category=$("#project_category").combobox("getValue");
		if($('#hide_zero').is(':checked')) {
			queryParams.hide_zero=true;
		}
		else
		{
			queryParams.hide_zero=false;
		}
		if($('#hide_account').is(':checked')) {
			queryParams.hide_account=true;
		}
		else
		{
			queryParams.hide_account=false;
		}
		dataGrid.datagrid('reload',url);
	}
	
	function clearData()
	{
		$("#project_code").val('');
		$("#supplier").combobox("setText","");
		$("#supplier").combobox("setValue","");
		$("#project_category").combobox("setValue",0);
		$("#hide_zero").prop("checked",false);
		$("#hide_account").prop("checked",false);
	}
	
	function exportData()
	{
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		var hide_zero;
		var hide_account;
		if($('#hide_zero').is(':checked')) {
			hide_zero=true;
		}
		else
		{
			hide_zero=false;
		}
		if($('#hide_account').is(':checked')) {
			hide_account=true;
		}
		else
		{
			hide_account=false;
		}
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/overview/createExcel",//路径  
			//dataType : "json",
			data : {
				project_code:$("#project_code").val(),
				supplier_name:$("#supplier").combobox("getText"),
				project_category:$("#project_category").val(),
				hide_zero:hide_zero,
				hide_account:hide_account
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/overview/exportExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
	<div style="display:none"><form id="export" method="post"></form></div>
	<div data-options="region:'center',border:false" data-options="fit:true,border:false">
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<span>项目编号：</span>
		<input class="textbox" id="project_code" style="width:180px;height:20px;">&nbsp;&nbsp;
		<span>供应商：</span>
	    <select class="easyui-combobox" id="supplier" style="width:180px;"></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>项目类别：</span>
		<select class="easyui-combobox" id="project_category" style="width:80px;height:21px;" data-options="editable:false" panelHeight="auto">
		     <option value ="0">全部</option>
		     <option value ="1">销项</option>
		     <option value ="2">非销项</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="hide_zero" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>隐藏余额为0的记录</span>&nbsp;&nbsp;&nbsp;
		<input id="hide_account" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>不分科目</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSearch" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData();">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSearch" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clearData();">清空</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>