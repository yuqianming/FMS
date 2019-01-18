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
	</script>
<title></title>
<style type="text/css">
.datagrid-row-selected {
    background: #CC0000;
    color:#fff;
}
</style>
	<script type="text/javascript">
	var url='${ctx}' + '/projectCheck/poolGrid';
	var poolGrid;
	var monthGrid;
	var categoryGrid;
	var detailGrid;
	var IsCheckFlag = true;
	//查询条件
	var tableName;
	var orderStart;
	var month;
	var category;
	var batchNo;
	$(function() {
		poolGrid = $('#poolGrid').datagrid({
			striped : true,    //隔行变色
			//rownumbers : true, //显示序号
			//pagination : true,
			singleSelect : true,
			fitColumns : true, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			columns : [[
              {width : '180',title : '类型',field : 'amt_type',align:'center'},
              {width : '180',title : '总金额',field : 'amt_total',align:'right'},
              {width : '180',title : 'PO订单',field : 'amt_po',align:'right'},
              {width : '180',title : 'OFP订单',field : 'amt_ofp',align:'right'},
              {width : '180',title : 'SK订单',field : 'amt_sk',align:'right'},
              {width : '180',title : 'SJ订单',field : 'amt_sj',align:'right'},
              {width : '180',title : '利息资本化',field : 'amt_zb',align:'right'},
              {width : '180',title : '其他',field : 'amt_other',align:'right'}
		     ]],
		     onClickCell:function(rowIndex, field, value){
		         IsCheckFlag=false;
		         if(field=='amt_type')
		         {
		        	 return;
		         }
		         var trs = $('#poolGrid').prev().find('div.datagrid-body').find('tr');
		         for(var i=0;i<trs.length;i++)
		         {
		        	 for(var j=0;j<trs[i].cells.length;j++)
		        	 {
		        		 $(trs[i].cells[j]).removeAttr("style");
		        	 }
		         }
		         var cells=trs[rowIndex].cells;
		         for(var i=0;i<cells.length;i++)
		         {
		        	 if($(cells[i]).attr('field')==field)
		        	 {
		        		 cells[i].style.cssText='background:#CC0000;color:#fff';
		        	 }
		         }
		         orderStart=field;
		         var queryParams=monthGrid.datagrid('options').queryParams;
		         var tt=poolGrid.datagrid('getRows')[rowIndex];
		         if(tt.amt_type=='成本单')
		         {
		        	 tableName='t_cost';
		         }
		         else if(tt.amt_type=='已付款')
		         {
		        	 tableName='t_payment';
		         }
		         else if(tt.amt_type=='已签字未付款')
		         {
		        	 tableName='t_signature';
		         }
		         queryParams.tableName=tableName;
		         queryParams.orderStart=orderStart;
		         monthGrid.datagrid('reload','${ctx}/projectCheck/monthGrid');
		         categoryGrid.datagrid('loadData', { total: 0, rows: [] });
		         detailGrid.datagrid('loadData', { total: 0, rows: [] });
		         monthGrid.datagrid('clearSelections');
		         categoryGrid.datagrid('clearSelections');
		         detailGrid.datagrid('clearSelections');
		     },
		     onSelect: function (rowIndex, rowData) {
		         /*if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             poolGrid.datagrid("unselectRow", rowIndex);
		         }*/
		    	 poolGrid.datagrid("unselectRow", rowIndex);
		     },                    
		     onUnselect: function (rowIndex, rowData) {
		         if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             poolGrid.datagrid("selectRow", rowIndex);
		         }
		     }
		});
		
		poolGrid.datagrid('reload',url);

		
		
		monthGrid = $('#monthGrid').datagrid({
			striped : true,    //隔行变色
			pagination : true,
			singleSelect : true,
			fitColumn : false,
			fit : true, //适应整个屏幕
			idField : 'month',
			sortName : 'month',
			sortOrder : 'desc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"月度分类汇总",  
			columns : [ [ {
				width : '120',
				title : '月度',
				field : 'month',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '金额',
				field : 'amt',
				align:'right'
			}, {
				width : '120',
				title : '不含税金额',
				field : 'amt_e_tax',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
				//var dv2=$(".datagrid-view2");
				//var width=dv2.children(".datagrid-body").width();
				//dv2.children(".datagrid-body").width(width-20);
			},
			onClickRow:function(index,row)
			{
		        categoryGrid.datagrid('clearSelections');
		        detailGrid.datagrid('clearSelections');
		        detailGrid.datagrid('loadData', { total: 0, rows: [] });
				month=row.month;
		        var queryParams=categoryGrid.datagrid('options').queryParams;
		        queryParams.tableName=tableName;
		        queryParams.orderStart=orderStart;
		        queryParams.month=month;
		        categoryGrid.datagrid('reload','${ctx}/projectCheck/categoryGrid');
			}
		});
		
		
		categoryGrid = $('#categoryGrid').datagrid({
			striped : true,    //隔行变色
			pagination : true,
			singleSelect : true,
			fitColumn : false,
			fit : true, //适应整个屏幕
			idField : 'category',
			sortName : 'category',
			sortOrder : 'desc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"可变分类汇总",  
			columns : [ [ {
				width : '180',
				title : '分类',
				field : 'category',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '金额',
				field : 'amt',
				align:'right'
			}, {
				width : '120',
				title : '不含税金额',
				field : 'amt_e_tax',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
			},
			onClickRow:function(index,row)
			{
				detailGrid.datagrid('clearSelections');
				category=row.category;
				batchNo=row.batch_no;
		        var queryParams=detailGrid.datagrid('options').queryParams;
		        queryParams.tableName=tableName;
		        queryParams.orderStart=orderStart;
		        queryParams.month=month;
		        queryParams.category=category;
		        queryParams.batchNo=batchNo;
		        detailGrid.datagrid('reload','${ctx}/projectCheck/detailGrid');
			}
		});
		
		
		detailGrid = $('#detailGrid').datagrid({
			striped : true,    //隔行变色
			pagination : true,
			singleSelect : true,
			fitColumn : false,
			fit : true, //适应整个屏幕
			idField : 'project_code',
			sortName : 'project_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"明细",  
			columns : [ [ {
				width : '150',
				title : '项目编号',
				field : 'project_code',
				sortable : true,
				align:'center'
			}, {
				width : '200',
				title : '供应商',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '金额',
				field : 'amt',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
			},
			onDblClickRow:function(index,row)
			{

			}
		});
		/*
		var dv2=$(".datagrid-view2");
		var width=dv2.children(".datagrid-body").width();
		dv2.children(".datagrid-body").width(width-20);*/
	});

	
	function viewMonthAll()
	{
		month='all';
        var queryParams=categoryGrid.datagrid('options').queryParams;
        queryParams.tableName=tableName;
        queryParams.orderStart=orderStart;
        queryParams.month=month;
        categoryGrid.datagrid('reload','${ctx}/projectCheck/categoryGrid');
        detailGrid.datagrid('loadData', { total: 0, rows: [] });
        monthGrid.datagrid('clearSelections');
        categoryGrid.datagrid('clearSelections');
        detailGrid.datagrid('clearSelections');
	}
	
	function viewCategoryAll()
	{
		category='all';
        var queryParams=detailGrid.datagrid('options').queryParams;
        queryParams.tableName=tableName;
        queryParams.orderStart=orderStart;
        queryParams.month=month;
        queryParams.category=category;
        detailGrid.datagrid('reload','${ctx}/projectCheck/detailGrid');
        categoryGrid.datagrid('clearSelections');
        detailGrid.datagrid('clearSelections');
	}
	
	function categoryExport()
	{
		if(tableName==undefined)
		{
			parent.$.messager.alert('提示','请选择类型！', 'info');
			return;
		}
		if(month==undefined)
		{
			parent.$.messager.alert('提示','请选择月度！', 'info');
			return;
		}
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectCheck/createCategoryExcel",//路径  
			data : {
				tableName:tableName,
				orderStart:orderStart,
				month:month
			},  
			success : function(result) {
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/projectCheck/exportCategoryExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	function detailExport()
	{
		if(tableName==undefined)
		{
			parent.$.messager.alert('提示','请选择类型！', 'info');
			return;
		}
		if(month==undefined)
		{
			parent.$.messager.alert('提示','请选择月度！', 'info');
			return;
		}
		if(category==undefined)
		{
			parent.$.messager.alert('提示','请选择分类！', 'info');
			return;
		}
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectCheck/createDetailExcel",//路径  
			data : {
				tableName:tableName,
				orderStart:orderStart,
				month:month,
				category:category,
				batchNo:batchNo
			},  
			success : function(result) {
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/projectCheck/exportDetailExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	</script>
</head>

<body class="easyui-layout" ondragstart='return false'>
    <div style="display:none"><form id="export" method="post"></form></div>
	<div data-options="region:'north'" style="height:130px;width:400px;">
		<table id="poolGrid" data-options="fit:false,border:false" class="easyui-datagrid"  style="width:400px;height:100px;"></table>
	</div>
	<div data-options="region:'west'" style="width:420px;">
		<table id="monthGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	<div data-options="region:'center'" style="width:420px;background-color:black">
		<table id="categoryGrid" data-options="fit:false,border:false,toolbar:'#tb1'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	<div data-options="region:'east'" style="width:500px;background-color:blue">
		<table id="detailGrid" data-options="fit:false,border:false,toolbar:'#tb2'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;line-height:25px;background-color:#fff;">
		<span style="padding-left:20px;color:#9e9898;">月度分类汇总</span>
		<a href="#" id="btnMonthAll" style="position:absolute;right:18px;" class="easyui-linkbutton" data-options="iconCls:'icon-check'" onclick="viewMonthAll()">全选</a>
	</div>
		<div id="tb1" style="padding:3px;line-height:25px;background-color:#fff;">
		<span style="padding-left:20px;color:#9e9898;">可变分类汇总</span>
		<a href="#" id="btnCategoryAll" style="position:absolute;right:120px;" class="easyui-linkbutton" data-options="iconCls:'icon-check'" onclick="viewCategoryAll()">全选</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnCategoryExport" style="position:absolute;right:18px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="categoryExport()">导出Excel</a>
	</div>
		<div id="tb2" style="padding:3px;line-height:25px;background-color:#fff;">
		<span style="padding-left:20px;color:#9e9898;">明细</span>
		<a href="#" id="btnDetailExport" style="position:absolute;right:18px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="detailExport()">导出Excel</a>
	</div>
</body>
</html>