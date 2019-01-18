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
	$.canExport = true;
	$.canSearch=true;
	/*var buttonList=${sessionInfo.buttonList};
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
	}*/
	
	</script>
	
<title></title>

	<script type="text/javascript">
	
	var dataGrid;
	var url ='${ctx}/overpayReport/dataGrid';
	var export_type;
	$(function() {
		buttonHandle();
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'org_id',
			sortName : 'org_id',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			onLoadSuccess:function(){
			}
		});
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
	
	
	function searchData(type)
	{
		var queryParams=dataGrid.datagrid('options').queryParams;
		queryParams.startMonth=$("#startMonth").val();
		queryParams.endMonth=$("#endMonth").val();
		var cancelStock=false;
		if($('#cancelStock').is(':checked')) {
			cancelStock=true
		}
		var cancelAudit=false;
		if($('#cancelAudit').is(':checked')) {
			cancelAudit=true
		}
		var noMaterial=false;
		if($('#noMaterial').is(':checked')) {
			noMaterial=true
		}
		queryParams.type=type;
		queryParams.cancelStock=cancelStock;
		queryParams.cancelAudit=cancelAudit;
		queryParams.noMaterial=noMaterial;
		export_type=type;
		dataGrid.datagrid('reload',url);
	}
	
	
	function exportData()
	{
		var cancelStock=false;
		if($('#cancelStock').is(':checked')) {
			cancelStock=true
		}
		var cancelAudit=false;
		if($('#cancelAudit').is(':checked')) {
			cancelAudit=true
		}
		var noMaterial=false;
		if($('#noMaterial').is(':checked')) {
			noMaterial=true
		}
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/overpayReport/createExcel",//路径  
			//dataType : "json",
			data : {
				startMonth:$("#startMonth").val(),
				endMonth:$("#endMonth").val(),
				cancelStock:cancelStock,
				cancelAudit:cancelAudit,
				noMaterial:noMaterial,
				type:export_type
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/overpayReport/exportExcel"
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
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;">
		  <thead frozen="true">
			<tr>
				<th field="group_name" width="200" align="left">分公司/供应商</th>
			</tr>
		 </thead>
		 <thead>
			<tr>
				<th colspan="2">超额付款项目</th>
				<th colspan="2">超额已付款项目</th>
				<th rowspan="2" field="over_amt" width="100" align="center">超额付款净额</th>
			</tr>
			<tr>
				<th field="cost_num" width="80" align="center">个数</th>
				<th field="cost_amt" width="120" align="right">金额</th>
				<th field="pay_num" width="80" align="center">个数</th>
				<th field="pay_amt" width="120" align="right">付款金额（不含税)</th>
			</tr>
		</thead>
		</table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<span>起始年月：</span>
		<input type="text" id="startMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span>终止年月：</span>
		<input type="text" id="endMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="cancelStock" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>不含退库</span>&nbsp;
		<input id="cancelAudit" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>不含审计调整</span>&nbsp;
		<input id="noMaterial" style="vertical-align: middle;" name="noMaterial" type="checkbox"/>
		<span>不含物资采购订单</span>&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSearchOrg" class="easyui-linkbutton"  onclick="searchData(1);">按分公司统计</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSearchSupplier" class="easyui-linkbutton"  onclick="searchData(2);">按供应商统计</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>