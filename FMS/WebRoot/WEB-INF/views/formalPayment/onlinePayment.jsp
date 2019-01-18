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
	$.canImport = false;
	$.canConfirm = false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT04021')
		{
			$.canConfirm=true;
		}
		else if(buttonList[i].id=='BT04020')
		{
			$.canImport=true;
		}
	}
	</script>
	
<title></title>

	<script type="text/javascript">
	
	var dataGrid;
	var url='${ctx}/onlinePayment/dataGrid';
	$(function() {
		buttonHandle();
		$('#importDialog').dialog('close');
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
			title:"线上付款信息列表",  
			columns : [ [ {
				width : '120',
				title : '项目编号',
				field : 'project_code',
				sortable : true,
				align:'center'
			}, {
				width : '100',
				title : '供应商代码',
				field : 'supplier_code',
				sortable : true,
				align:'center'
			}, {
				width : '180',
				title : '供应商名称',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '220',
				title : '订单编号',
				field : 'order_code',
				sortable : true,
				align:'center'
			}, {
				width : '130',
				title : '站址编号',
				field : 'address_code',
				sortable : true,
				align:'center'
			}, {
				width : '120',
				title : '科目名称',
				field : 'account_name',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '站址名称',
				field : 'address_name',
				sortable : true,
				align:'center'
			}, {
				width : '80',
				title : '本次发票',
				field : 'invoice_amt',
				sortable : true,
				align:'right'
			}, {
				width : '90',
				title : '本次发票净额',
				field : 'invoice_amt_e_tax',
				sortable : true,
				align:'right'
			}, {
				width : '80',
				title : '本次交付',
				field : 'pay_amt',
				sortable : true,
				align:'right'
			}, {
				width : '90',
				title : '本次交付净额',
				field : 'pay_amt_e_tax',
				sortable : true,
				align:'right'
			}, {
				width : '80',
				title : '日期',
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
				width : '100',
				title : '线上线下',
				field : 'online_offline',
				sortable : true,
				align:'center'
			}, {
				width : '200',
				title : '结算单号',
				field : 'settle_no',
				sortable : true,
				align:'center'
			}, {
				width : '200',
				title : '备注',
				field : 'remark',
				sortable : true,
				align:'center'
			}, {
				width : '80',
				title : '税率（%）',
				field : 'tax_rate',
				sortable : true,
				align:'center'
			}, {
				width : '220',
				title : '核算组织',
				field : 'accounting_org',
				sortable : true,
				align:'center'
			}] ],
			onLoadSuccess:function(){
				/*    $("#dataGrid").datagrid("resize",{  
			            height: ($(window).height())  
			        });   */
			}
		});
	});

	function buttonHandle()
	{
		if(!$.canConfirm)
		{
			$('#btnConfirm').hide();
		}
		if(!$.canImport)
		{
			$('#btnImport').hide();
		}
	}
	
	function popImportDialog()
	{
		$('#importDialog').dialog('open');
	}
	
	
	function importFun()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
		var month=$('#month').val();
		$('#importForm').form('submit',{
			url : '${ctx}/onlinePayment/upload',
			onSubmit : function() {
				importLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				result = $.parseJSON(result);
				importHand("读取完成，共有"+result.msg+"条记录。");
				if (result.success) {
					$('#importForm').form('submit',{
						url : '${ctx}/onlinePayment/dataHandle',
						onSubmit : function() {
							importHand("正在拆分导入，请稍候...");
							var isValid = $(this).form('validate');
							if (!isValid) {
								progressClose();
							}
							return isValid;
						},
						success : function(result) {
							result = $.parseJSON(result);
							if (result.success) {
								importHand("导入完成。");
								setTimeout(function(){
									progressClose();
									$('#importDialog').dialog('close');
									$('#importForm').form('clear');
									emptyFile('upFile','file_td');
									dataGrid.datagrid('load',url);
								}, 1000); 
							} else {
								progressClose();
								parent.$.messager.alert('错误', result.msg, 'error');
								emptyFile('upFile','file_td');
							}
						}
					});
				} else {
					progressClose();
					parent.$.messager.alert('错误', result.msg, 'error');
					emptyFile('upFile','file_td');
				}
			}
		});
		//$('#importForm').submit();
	}
	
	function importCancel()
	{
		$('#importDialog').dialog('close');
		$('#importForm').form('clear');
	}
	
	
	function confirmFun()
	{
		progressLoad();
		//parent.$.messager.alert('提示','有'+$("#not_pass").val()+"条记录不通过！",'info');
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/onlinePayment/check",//路径  
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {
				//progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					if(!result.obj.has)
					{
						//progressLoad();
						$.post('${ctx}/onlinePayment/confirm', {
						
						}, function(result) {
							if (result.success) {
								progressClose();
								parent.$.messager.alert('提示', result.msg, 'info'); 
								dataGrid.datagrid('reload');
							}
							else
							{
								progressClose();
								parent.$.messager.alert('错误', result.msg, 'error');
							}
						}, 'JSON');
					}
					else
					{
						parent.$.messager.confirm('询问', result.obj.tip, function(b) {
							if (b) {
								progressLoad();
								$.post('${ctx}/onlinePayment/confirm', {
								}, function(result) {
									if (result.success) {
										parent.$.messager.alert('提示', result.msg, 'info');
										progressClose();
										dataGrid.datagrid('reload');
										//detailGrid.datagrid('reload', { total: 0, rows: [] });
									}
									else
									{
										progressClose();
										parent.$.messager.alert('错误', result.msg, 'error');
									}			
								}, 'JSON');
							}
						});
					}
				} else {
					progressClose();
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
	
	    <div id="importDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'线上付款导入',
						modal:true,
						buttons:[{
							text:'确定',
							iconCls:'icon-ok',
							handler:function(){importFun()}
						},{
							text:'取消',
							iconCls:'icon-no',
							handler:function(){importCancel()}
						}]">
	     <form id="importForm" enctype="multipart/form-data" method="post">
			<table class="grid">
			    <tr>
			     <td>日期</td>
			     <td><input id="month" name="month" type="text" class="easyui-validatebox Wdate" data-options="required:true" style="width:100%;padding:0;" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly"/> </td>
			    </tr>
				<tr>
					<td nowrap>线上付款上传:
					 <input id="tableName" name="tableName" type="hidden" value="payment_temp"/>
					</td>
					<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" accept=".xls,.xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>
	
	<div data-options="region:'center',border:false">
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:auto;height:100px;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">批量导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnConfirm" class="easyui-linkbutton" data-options="iconCls:'icon-confirm'" onclick="confirmFun()">确认导入</a>
	</div>

</body>
</html>