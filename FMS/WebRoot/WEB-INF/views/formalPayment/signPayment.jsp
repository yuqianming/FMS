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
    $.canSign=false;
    $.canDel=false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT04010')
		{
			$.canSign=true;
		}
		if(buttonList[i].id=='BT04011')
		{
			$.canDel=true;
		}
	}
	</script>
	
<title></title>

	<script type="text/javascript">
	/*easyui编辑行*/
	//var editIndex=undefined;
	var supplierDataGrid;
	var accountDataGrid;
	var supplier_name='';
	var batch_no='';
	$(function() {
		buttonHandle();
		supplierDataGrid = $('#supplierDataGrid').datagrid({
			url : '${ctx}' + '/signPayment/supplierDataGrid',
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'batch_no',
			sortName : 'batch_no',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [ 20, 50, 100, 200 ],
			title:"已签字未付款列表",  
			columns : [[{
				width : '160',
				title : '批次号',
				field : 'batch_no',
				sortable : true,
				align:'center'
			},{
				width : '170',
				title : '供应商名称',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '100',
				title : '金额',
				field : 'pay_amt',
				sortable : true,
				align:'right'
			}] ],
			onLoadSuccess:function(){
				 $("#supplierDataGrid").datagrid("clearSelections");
			},
			onClickRow:function(index,row)
			{
				supplier_name=row.supplier_name;
				batch_no=row.batch_no;
				viewSupplierDetail(row.supplier_name,row.batch_no);
			}
		});
		
		accountDataGrid = $('#accountDataGrid').datagrid({
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
			title:"付款明细列表",
			//onDblClickCell:onDblClickCell,
			//onClickCell:endEditing,
			columns : [[{
				width : '180',
				title : '项目编码',
				field : 'project_code',
				sortable : true,
				align:'center'
			}, {
				width : '200',
				title : '科目名称',
				field : 'account_name',
				sortable : true,
				align:'center'
			}, {
				width : '100',
				title : '本次开票',
				field : 'invoice_amt',
				sortable : true,
				align:'right'
			}, {
				width : '100',
				title : '本次交付',
				field : 'pay_amt',
				sortable : true,
				align:'right'
			}]]
		});
		initCombobxSelect();
	});
	
	function initCombobxSelect()
	{
		$("#supplier").combobox({
			url:'${ctx}/supplier/getSupplierList',
			valueField:'supplier_name',
			textField:'supplier_name'
		});
	}
	
	function buttonHandle()
	{
		if(!$.canSign)
		{
			$('#btnSign').hide();
		}
		if(!$.canDel)
		{
			$('#btnDelete').hide();
		}
	}
	
	function searchFun()
	{
		var queryParams = supplierDataGrid.datagrid('options').queryParams;
		queryParams.supplier_name=$("#supplier").combobox("getText");
		supplierDataGrid.datagrid('reload');
	}
	
	function viewSupplierDetail(supplier_name,batch_no)
	{
		//var url='${ctx}' + '/signPayment/accountDataGrid?supplier_name='+supplier_name+'&batch_no='+batch_no;
		//accountDataGrid.datagrid('reload',url);
        var queryParams=accountDataGrid.datagrid('options').queryParams;
        queryParams.supplier_name=supplier_name;
        queryParams.batch_no=batch_no;
        accountDataGrid.datagrid('reload','${ctx}/signPayment/accountDataGrid');
	}
	
	/*function onDblClickCell(index)
	{
		if(editIndex!=index)
		{
			if(endEditing())
			{
				accountDataGrid.datagrid('selectRow',index).datagrid('beginEdit',index);
				editIndex=index;
			}
			else
			{
				accountDataGrid.datagrid('selectRow',editIndex);
			}
		}
	}
	
	function endEditing()
	{
		if(editIndex==undefined)
		{
			return true;
		}
		if(accountDataGrid.datagrid('validateRow',editIndex))
		{
			accountDataGrid.datagrid('endEdit',editIndex);
			editIndex=undefined;
			return true;
		}
		else
		{
			return false;
		}
	}*/
	
	function signPayment()
	{
		/*if(editIndex!=undefined)
		{
			parent.$.messager.alert('提示', '请编辑完行数据！', 'info');
			return;
		}*/
		if(batch_no=='')
		{
			parent.$.messager.alert('提示', '请在"已签字未付款列表"中选择批次！', 'info');
			return;
		}
		var month=$("#month").val();
		if(month=='')
		{
			parent.$.messager.alert('提示', '请选择月份！', 'info');
			return;
		}
		
		var voucher_no=$("#voucher_no").val();
		if(voucher_no.trim()=='')
		{
			parent.$.messager.alert('提示', '凭证号不能为空！', 'info');
			return;
		}
		var remark=$("#remark").val();
		var param={};
		/*var signList=[];
		var rows=accountDataGrid.datagrid('getRows');
		if(rows.length<=0)
		{
			parent.$.messager.alert('提示', '没有需要处理的记录！', 'info');
			return;
		}
		param.signList=rows;
		param.month=month;
		param.voucher_no=voucher_no;
		console.log(JSON.stringify(param));*/
		param.month=month;
		param.voucher_no=voucher_no;
		param.remark=remark;
		param.batch_no=batch_no;
		param.supplier_name=supplier_name;
		progressLoad();
		$.ajax({
			type:"POST",
			dataType:"json",
			url:'${ctx}' + '/signPayment/save',
			data:JSON.stringify(param),
			contentType:'application/json;charset=UTF-8',
			success:function(data)
			{
				progressClose();
				parent.$.messager.alert('提示',data.msg,'info');
				supplierDataGrid.datagrid('load');
				accountDataGrid.datagrid('load');
				//$("#month").val('');
				$("#voucher_no").val('');
				$("#remark").val('');
				batch_no='';
				supplier_name='';
			},
		    error:function()
		    {
		    	progressClose();
		    }
		});
	}
	
	function deleteFun() {
		var row = supplierDataGrid.datagrid('getSelected');
		if(row)
		{
			var batchs = row.batch_no;
			parent.$.messager.confirm('询问', '您是否要删除所有勾选项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/signPayment/delete', {
						batchs : batchs
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							supplierDataGrid.datagrid('reload');
							accountDataGrid.datagrid('clearSelections');
							accountDataGrid.datagrid('loadData', { total: 0, rows: [] });
						}
						progressClose();
					}, 'JSON');
				}
			});
		}
		else
		{
			parent.$.messager.alert('提示', "请选择要删除的记录！", 'info');
		}
	}

	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
	  <div data-options="region:'west'" style="width:35%;height:100%;">
		<table id="supplierDataGrid" data-options="fit:true,border:false,toolbar:'#tb1'"></table>
	  </div>
	  
	 <div id="tb1" style="padding:3px;">
	    <span>供应商：</span>
	    <input class="easyui-combobox" id="supplier" style="width:180px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSearch" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchFun()">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>
	</div>
	
	<div data-options="region:'center'" style="width:65%;height:100%;">
		<table id="accountDataGrid" data-options="fit:true,border:false,toolbar:'#tb2'"></table>
	</div>
	
	<div id="tb2" style="padding:3px;">
		<span>月份：</span>
		<input type="text" id="month" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;
		<span>凭证号：</span>
		<input type="text" id="voucher_no" style="width:150px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<span>备注：</span>
		<input type="text" id="remark" style="width:250px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnSign" class="easyui-linkbutton" data-options="iconCls:'icon-pay'" onclick="signPayment()">付款完成</a>&nbsp;&nbsp;&nbsp;&nbsp;
	</div>

</body>
</html>