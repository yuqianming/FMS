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
	var url ='${ctx}/storeReport/dataGrid';
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
	
	
	function searchData()
	{
		var queryParams=dataGrid.datagrid('options').queryParams;
		queryParams.startMonth=$("#startMonth").val();
		queryParams.endMonth=$("#endMonth").val();
		if($('#no_out').is(':checked')) {
			queryParams.no_out=true;
		}
		else
		{
			queryParams.no_out=false;
		}		
		if($('#no_in').is(':checked')) {
			queryParams.no_in=true;
		}
		else
		{
			queryParams.no_in=false;
		}
		dataGrid.datagrid('reload',url);
	}
	
	
	function exportData()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/storeReport/createExcel",//路径  
			//dataType : "json",
			data : {
				startMonth:$("#startMonth").val(),
				endMonth:$("#endMonth").val(),
				no_out:$('#no_out').is(':checked'),
				no_in:$('#no_in').is(':checked')
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/storeReport/exportExcel"
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
				<th field="org_name" width="120" align="left">分公司</th>
			</tr>
		 </thead>
		 <thead>
			<tr>
				<th colspan="6">核算系统</th>
				<th colspan="6">仓储系统</th>
				<th colspan="6">差异</th>
			</tr>
			<tr>
				<th field="deal_total" width="100" align="right">合计</th>
				<th field="deal_rk" width="100" align="right">入库</th>
				<th field="deal_ck" width="100" align="right">出库</th>
				<th field="deal_ckhg" width="100" align="right">出库回滚</th>
				<th field="deal_tk" width="100" align="right">退库</th>
				<th field="deal_cz" width="100" align="right">其他</th>
				<th field="store_total" width="100" align="right">合计</th>
				<th field="store_rk" width="100" align="right">入库</th>
				<th field="store_ck" width="100" align="right">出库</th>
				<th field="store_ckhg" width="100" align="right">出库回滚</th>
				<th field="store_tk" width="100" align="right">退库</th>
				<th field="store_cz" width="100" align="right">其他</th>
				<th field="dvalue_total" width="100" align="right">合计</th>
				<th field="dvalue_rk" width="100" align="right">入库</th>
				<th field="dvalue_ck" width="100" align="right">出库</th>
				<th field="dvalue_ckhg" width="100" align="right">出库回滚</th>
				<th field="dvalue_tk" width="100" align="right">退库</th>
				<th field="dvalue_cz" width="100" align="right">其他</th>
			</tr>
		</thead>
		</table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<span>起始年月：</span>
		<input type="text" id="startMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span>终止年月：</span>
		<input type="text" id="endMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="no_out" style="vertical-align: middle;" name="no_out" type="checkbox"/>
		<span>不含调拨出库</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="no_in" style="vertical-align: middle;" name="no_in" type="checkbox"/>
		<span>不含调拨入库</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnStart" class="easyui-linkbutton" data-options="iconCls:'icon-start'" onclick="searchData();">开始分析</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>