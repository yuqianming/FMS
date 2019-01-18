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
   /* $.canSign=false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT04010')
		{
			$.canSign=true;
		}
	}*/
	</script>
<style type="text/css">
.datagrid-row-selected {
    background: #CC0000;
    color:#fff;
}
</style>
<title></title>

	<script type="text/javascript">
	var supplierGrid;
	var payGrid;
	var project_code_1='';
	var supplier_name_1='';
	var is_all_1;
	$(function() {
		buttonHandle();
		supplierGrid = $('#supplierGrid').datagrid({
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
			columns : [[{
				width : '180',
				title : '供应商名称',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '项目编号',
				field : 'project_code',
				sortable : true,
				align:'center'
			}, {
				width : '80',
				title : '成本单金额',
				field : 'account_amt_txt',
				sortable : true,
				align:'right'
			}, {
				width : '100',
				title : '已付款（不含税）',
				field : 'pay_amt_e_tax_txt',
				sortable : true,
				align:'right'
			}, {
				width : '130',
				title : '成本单减已付款金额',
				field : 'dvalue',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
				$("#supplierGrid").datagrid("clearSelections");
			},
			onClickRow:function(index,row)
			{
				supplier_name_1=row.supplier_name;
				project_code_1=row.project_code;
				viewPayDetail(row.supplier_name,row.project_code,false);
			}
		});
		
		payGrid = $('#payGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'order_code',
			sortName : 'order_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"供应商明细信息列表",
			showFooter:true,
			columns : [[{
				width : '100',
				title : '付款日期',
				field : 'month',
				sortable : true,
				align:'center'
			}, {
				width : '100',
				title : '凭证号',
				field : 'voucher_no',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '科目名称',
				field : 'account_name',
				sortable : true,
				align:'center'
			}, {
				width : '200',
				title : '订单编号',
				field : 'order_code',
				sortable : true,
				align:'center'
			}, {
				width : '100',
				title : '发票金额',
				field : 'invoice_amt_txt',
				sortable : true,
				align:'right'
			}, {
				width : '80',
				title : '本次付款',
				field : 'pay_amt_txt',
				sortable : true,
				align : 'right'
			}, {
				width : '120',
				title : '本次付款（不含税）',
				field : 'pay_amt_e_tax_txt',
				sortable : true,
				align : 'right'
			}]],
			onLoadSuccess:function(){

			}
		});
	});
	
	function buttonHandle()
	{
		/*if(!$.canSign)
		{
			$('#btnSign').hide();
		}*/
	}
	

	function viewPayDetail(supplier_name,project_code,is_all)
	{
		is_all_1=is_all;
		if(is_all)
		{
			$("#supplierGrid").datagrid("clearSelections");
		}
		
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
		var url='${ctx}' + '/overPay/payGrid';
		var queryParams=payGrid.datagrid('options').queryParams;
		queryParams.supplier_name=supplier_name;
		queryParams.project_code=project_code;
		queryParams.is_all=is_all;
		queryParams.cancelStock=cancelStock;
		queryParams.cancelAudit=cancelAudit;
		queryParams.noMaterial=noMaterial;
		queryParams.deviation=$("#deviation").val();
		payGrid.datagrid('reload',url);
	}
	
	function startCheck()
	{
		payGrid.datagrid('loadData', { total: 0, rows: [],footer:[]});
		payGrid.datagrid('clearSelections');
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
		var url = '${ctx}/overPay/supplierGrid';
		var queryParams=supplierGrid.datagrid('options').queryParams;
		queryParams.cancelStock=cancelStock;
		queryParams.cancelAudit=cancelAudit;
		queryParams.noMaterial=noMaterial;
		queryParams.deviation=$("#deviation").val();
		supplierGrid.datagrid('reload',url);
	}
	
	function exportExcel()
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
			url : "${ctx}/overPay/createExcel",//路径  
			data : {
				cancelStock:cancelStock,
				cancelAudit:cancelAudit,
				noMaterial:noMaterial
			},  
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/overPay/exportExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	
	function detailExport()
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
			url : "${ctx}/overPay/createExcelDetail",//路径  
			data : {
				supplier_name:supplier_name_1,
				project_code:project_code_1,
				is_all:is_all_1,
				cancelStock:cancelStock,
				cancelAudit:cancelAudit,
				noMaterial:noMaterial,
				deviation:$("#deviation").val()
			},  
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/overPay/exportExcelDetail"
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
	  <div data-options="region:'west'" style="width:55%;height:100%;">
		<table id="supplierGrid" data-options="fit:true,border:false,toolbar:'#tb1'"></table>
	  </div>
	  
	 <div id="tb1" style="padding:3px;line-height:26px;">
		<input id="cancelStock" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>不含退库</span>&nbsp;
		<input id="cancelAudit" style="vertical-align: middle;" name="hide" type="checkbox"/>
		<span>不含审计调整</span>&nbsp;
		<input id="noMaterial" style="vertical-align: middle;" name="noMaterial" type="checkbox"/>
		<span>不含物资采购订单</span>&nbsp;&nbsp;&nbsp;
		<span>误差值：</span>
		<input class="textbox" id="deviation" style="width:80px;height:20px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnStart" class="easyui-linkbutton" data-options="iconCls:'icon-start'" onclick="startCheck()">开始检查</a>&nbsp;
		<a href="#" id="btnAll" class="easyui-linkbutton" data-options="iconCls:'icon-check'" onclick="viewPayDetail('','',true)">全选</a>
		<a href="#" id="btnExport" style="float:right;margin-right:18px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportExcel()">导出Excel</a>
	</div>
	
	<div data-options="region:'center'" style="width:45%;height:100%;">
		<table id="payGrid" data-options="fit:true,border:false,toolbar:'#tb2'"></table>
	</div>

	<div id="tb2" style="padding:3px;line-height:26px;">
		<span></span>&nbsp;
		<a href="#" id="btnDetailExport" style="position:absolute;right:18px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="detailExport()">导出Excel</a>
	</div>

</body>
</html>