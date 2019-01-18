<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<style type="text/css">
.tb{
padding-left:20px;
padding-top:5px;
}
.td_txt{
width:170px;
font-size:14px;
font-weight:bold;
text-align:right;
}
.td_amt{
width:100px;
font-size:14px;
font-weight:bold;
text-align:right;
}
</style>
	<script type="text/javascript">
    $.canAdd=false;
	$.canDelete = false;
	$.canImport = false;
	$.canStart = false;
	$.canConfirm = false;
	$.canExport = false;
	$.canEdit=false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT03020')
		{
			$.canAdd=true;
		}
		else if(buttonList[i].id=='BT03021')
		{
			$.canDelete=true;
		}
		else if(buttonList[i].id=='BT03022')
		{
			$.canImport=true;
		}
		else if(buttonList[i].id=='BT03023')
		{
			$.canStart=true;
		}		
		else if(buttonList[i].id=='BT03024')
		{
			$.canConfirm=true;
		}
		else if(buttonList[i].id=='BT03025')
		{
			$.canExport=true;
		}
		else if(buttonList[i].id=='BT03026')
		{
			$.canEdit=true;
		}
	}
	</script>
	
<title></title>

	<script type="text/javascript">
	var optionList;//供应商下拉框值list
	var url='${ctx}' + '/projectTest/dataGrid';
	var dataGrid;
	$(function() {
		buttonHandle();
		$('#addDialog').dialog('close');
		$('#editDialog').dialog('close');
		$('#importDialog').dialog('close');
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : false,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'id',
			sortName : 'project_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			//title:"项目信息列表",  
			columns : [ [ {
				title : '选择',
				field : 'ck',
				checkbox:true
			},{
				width : '150',
				title : '项目编号',
				field : 'project_code',
				sortable : true,
				align:'center'
			},{
				width : '150',
				title : '供应商名称',
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
				width : '150',
				title : '站址名称',
				field : 'address_name',
				sortable : true,
				align:'center'
			}, {
				width : '80',
				title : '已入账金额',
				field : 'c_account_amt',
				sortable : true,
				align:'right'
			}, {
				width : '80',
				title : '已开票金额',
				field : 'c_invoice_amt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '已付款金额',
				field : 'c_pay_amt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '签字未登记',
				field : 'n_pay_amt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '本次开票金额',
				field : 't_invoice_amt_txt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '本次付款金额',
				field : 't_pay_amt_txt',
				sortable : true,
				align:'right'
			},{
				width : '80',
				title : '符合性测试',
				field : 'is_pass',
				sortable : true,
				align:'center',
				formatter: function (value, row, index) { 
					if(value==undefined)
					{
						return '';
					}
					else if(value=='通过')
					{
						return value;
					}
					else
					{
						res = '<font style="color:red;">'+value+'</font>';
						return res;
					}
				}
			},{
				width : '80',
				title : '本次付款比例',
				field : 'pay_rate',
				sortable : true,
				align:'center',
				formatter: function (value, row, index) {
					if(value=='错误')
					{
						res = '<font style="color:red;">'+value+'</font>';
						return res;
					}
					else
					{
						return value;
					}
				}
			},{
				width : '80',
				title : '建设方式',
				field : 'build_mode',
				sortable : true,
				align:'center'
			},{
				width : '80',
				title : '内验时间',
				field : 'accept_date',
				sortable : true,
				align:'center'
			},{
				width : '80',
				title : '交付时间',
				field : 'deliver_date',
				sortable : true,
				align:'center'
			},{
				width : '80',
				title : '项目状态',
				field : 'project_status',
				sortable : true,
				align:'center',
				formatter: function (value, row, index) { 
					if(value=='已销项')
					{
						res = '<font style="color:red;">已销项</font>';
						return res;
					}
					else
					{
						return value;
					}
				}
			},{
				width : '80',
				title : '起租信息',
				field : 'rent_status',
				sortable : true,
				align:'center'
			},{
				width : '80',
				title : '是否销项',
				field : 'is_cancel',
				sortable : true,
				align:'center'
			},{
				width : '120',
				title : '是否旧项目',
				field : 'is_old',
				sortable : true,
				align:'center'
			}] ],
			onLoadSuccess:function(){
				$("#dataGrid").datagrid("clearSelections");
			},
			onDblClickRow:function(index,row)
			{
				if($.canEdit)
				{
					$("#project_code_edit").val(row.project_code);
					$("#t_invoice_amt_edit").val(row.t_invoice_amt);
					$("#t_pay_amt_edit").val(row.t_pay_amt);
					$("#id").val(row.id);
					$('#editDialog').dialog('open');
				}
			}
		});
		
		initCombobxSelect();
		optionList=$('#supplier option');
	});

	function buttonHandle()
	{
		if(!$.canAdd)
		{
			$('#btnAdd').hide();
		}
		if(!$.canDelete)
		{
			$('#btnDelete').hide();
		}
		if(!$.canImport)
		{
			$('#btnImport').hide();
		}
		if(!$.canStart)
		{
			$('#btnStart').hide();
		}
		if(!$.canConfirm)
		{
			$('#btnConfirm').hide();
		}
		if(!$.canExport)
		{
			$('#btnExport').hide();
		}
	}
	
	function initCombobxSelect()
	{
		$("#tax_rate").combobox({
			url:'${ctx}/taxRate/getRateList',
			valueField:'tax_rate',
			textField:'rate_txt',
			editable:false
		});
		$("#tax_rate").combobox('setValue',0);
		$("#supplier").combobox({
			url:'${ctx}/supplier/getSupplierList',
			valueField:'supplier_name',
			textField:'supplier_name',
			onSelect:function(rec)
			{
				$("#supplier_name").val(rec.supplier_name);
				$("#account_name").combobox({
					url:'${ctx}/projectTest/getAccountBySupplier',
					queryParams:{
						"supplier_name":rec.supplier_name
					},
					valueField:'account_name',
					textField:'account_name',
					multiple:true,
					editable:false
				});
				/*$.ajax({
					type : "POST",
					url : "${ctx}/projectTest/getAccountBySupplier",
					data : {
						supplier_name:rec.supplier_name
					},
					success : function(result) {
						result = $.parseJSON(result);
						console.log(result);
						$("#account_name").combobox({
							url:'${ctx}/account/getAccountList',
							valueField:'account_name',
							textField:'account_name',
							multiple:true,
							editable:false,
							onLoadSuccess:function(){
								if(result.success)
								{
									$('#account_name').combobox('setValues',result.msg.split(','));
								}
							}
						});

					}
				});*/
			}
		});
		
	}
	
	function popAddDialog()
	{
		$('#addDialog').dialog('open');
	}
	
	function popImportDialog()
	{
		$('#importDialog').dialog('open');
	}
	
	function deleteFun() {
		var rows = dataGrid.datagrid('getSelections');
		if(rows.length>0)
		{
			var ids = [];
			for(var i=0;i<rows.length;i++)
			{
				ids.push(rows[i].id);
			}
			parent.$.messager.confirm('询问', '您是否要删除？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/projectTest/delete', {
						ids : ids.toString()
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}
						else
						{
							parent.$.messager.alert('错误', result.msg, 'error');
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
	
	function addFun()
	{
		$('#addForm').form({
			url : '${ctx}/projectTest/add',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#addDialog').dialog('close');
					$("#project_code").val('');
					$("#t_invoice_amt").val('');
					$("#t_pay_amt").val('');
					dataGrid.datagrid('reload',url);
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	
		$('#addForm').submit();
	}
	
	function addCancel()
	{
		$('#addDialog').dialog('close');
		$("#project_code").val('');
		$("#t_invoice_amt").val('');
		$("#t_pay_amt").val('');
	}
	
	function editFun()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectTest/edit",//路径  
			//dataType : "json",
			data : {
				project_code:$("#project_code_edit").val(),
				t_invoice_amt:$("#t_invoice_amt_edit").val(),
				t_pay_amt:$("#t_pay_amt_edit").val(),
				id:$("#id").val()
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#editDialog').dialog('close');
					dataGrid.datagrid('reload',url);
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	function editCancel()
	{
		$('#editDialog').dialog('close');
	}
	
	function importFun()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
		clearData();
		$('#importForm').form({
			url : '${ctx}/projectTest/upload',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#importDialog').dialog('close');
					emptyFile('upFile','file_td');
					dataGrid.datagrid('load',url);
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
					emptyFile('upFile','file_td');
				}
			}
		});
		$('#importForm').submit();
	}
	
	function importCancel()
	{
		$('#importDialog').dialog('close');
	}
	
	
	function startTest()
	{
		var supplier_name=$("#supplier_name").val();
		if(supplier_name=='')
		{
			parent.$.messager.alert('提示','必须选择供应商！','info');
			return;
		}
		var account_name=$("#account_name").combobox("getText");
		if(account_name=='')
		{
			parent.$.messager.alert('提示','至少选择一个科目！','info');
			return;
		}
		
		var tax_rate=$("#tax_rate").combobox("getValue");
		if(tax_rate=='')
		{
			parent.$.messager.alert('提示','必须选择税率！','info');
			return;
		}
		var deviation=$("#deviation").val();
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectTest/startTest",//路径  
			//dataType : "json",
			data : {
				tax_rate:tax_rate,
				account_name:account_name,
				supplier_name:supplier_name,
				deviation:deviation
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					dataGrid.datagrid('load',url);
					$("#invoice_amt_i_tax_txt").text(result.obj.invoice_amt_i_tax_txt);
					$("#invoice_amt_e_tax_txt").text(result.obj.invoice_amt_e_tax_txt);
					$("#pay_amt_i_tax_txt").text(result.obj.pay_amt_i_tax_txt);
					$("#pay_amt_e_tax_txt").text(result.obj.pay_amt_e_tax_txt);
					$("#invoice_amt_i_tax").val(result.obj.invoice_amt_i_tax);
					$("#invoice_amt_e_tax").val(result.obj.invoice_amt_e_tax);
					$("#pay_amt_i_tax").val(result.obj.pay_amt_i_tax);
					$("#pay_amt_e_tax").val(result.obj.pay_amt_e_tax);
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	function confirmSign()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectTest/getNopassCount",//路径  
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				result = $.parseJSON(result);
				if (result.success) {
					//parent.$.messager.alert('提示',result.msg,'info');
					//dataGrid.datagrid('load');
					var no_pass=result.obj.not_pass;
					var is_pass=result.obj.is_pass;
					var no_test=result.obj.no_test;
					var is_cancel=result.obj.is_cancel;
					if(no_test!=0)
					{
						progressClose();
						parent.$.messager.alert('提示',"有"+no_test+"笔记录未进行测试，在点击\"确认签字\"前请先进行测试！", 'info');
						return;
					}
					else
					{
						var txt='';
						if(no_pass!=0)
						{
							txt+='有'+no_pass+'笔测试符合性不通过，';
						}
						if(is_cancel!=0)
						{
							txt+='有'+is_cancel+'笔销项项目，';
						}
						if(txt)
						{
							txt='<font style="color:red;">'+txt+'</font>'
						}
						parent.$.messager.confirm('询问', txt+'确认签字吗？', function(b) {
							if (b) {
								$.post('${ctx}/projectTest/confirmSign', {
									invoice_amt_e_tax:$("#invoice_amt_e_tax").val(),
									pay_amt_e_tax:$("#pay_amt_e_tax").val()
								}, function(result) {
									if (result.success) {
										progressClose();
										parent.$.messager.alert('提示', result.msg, 'info');
										dataGrid.datagrid('reload');
										clearData();
										//detailGrid.datagrid('reload', { total: 0, rows: [] });
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
								progressClose();
							}
						});
					}
				} else {
					progressClose();
					parent.$.messager.alert('提示',"请确认已添加或导入项目测试数据！", 'info');
				}
			}
		});
	}
	
	function exportData()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/projectTest/createExcel",//路径  
			//dataType : "json",
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/projectTest/exportExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	function clearData()
	{
		$("#invoice_amt_i_tax_txt").text("");
		$("#invoice_amt_e_tax_txt").text("");
		$("#pay_amt_i_tax_txt").text("");
		$("#pay_amt_e_tax_txt").text("");
		$("#invoice_amt_i_tax").val("");
		$("#invoice_amt_e_tax").val("");
		$("#pay_amt_i_tax").val("");
		$("#pay_amt_e_tax").val("");
	}
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart="return false;">
	
	 <div id="addDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'项目测试新增',
						modal:true,
						buttons:[{
							text:'确定',
							iconCls:'icon-ok',
							handler:function(){addFun()}
						},{
							text:'取消',
							iconCls:'icon-no',
							handler:function(){addCancel()}
						}]">
	     <form id="addForm" enctype="multipart/form-data" method="post">
			<table class="grid">
				<tr>
					<td nowrap>项目编号:</td>
					<td><input id="project_code" name="project_code" style="width:200px;" type="text" placeholder="请输入订单编号" class="easyui-validatebox" data-options="required:true" value="" maxlength="50"></td>	
				</tr>
				<tr>
					<td>本次开票金额:</td>
					<td><input id="t_invoice_amt" name="t_invoice_amt" style="width:200px;" type="text" placeholder="请输入本次开票金额" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次付款金额:</td>
					<td><input id="t_pay_amt" name="t_pay_amt" style="width:200px;" type="text" placeholder="请输入本次付款金额" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
			</table>
		</form>
     </div>
     <div style="display:none"><form id="export" method="post"></form></div>
      <div id="editDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'项目测试修改',
						modal:true,
						buttons:[{
							text:'确定',
							iconCls:'icon-ok',
							handler:function(){editFun()}
						},{
							text:'取消',
							iconCls:'icon-no',
							handler:function(){editCancel()}
						}]">
	     <form id="editForm" enctype="multipart/form-data" method="post">
			<table class="grid">
				<tr>
					<td nowrap>项目编号:<input id="id" name="id" type="hidden"></td>
					<td><input id="project_code_edit" name="project_code_edit" style="width:200px;" type="text" class="easyui-validatebox" data-options="required:true" value="" ></td>	
				</tr>
				<tr>
					<td>本次开票金额:</td>
					<td><input id="t_invoice_amt_edit" name="invoice_amt_edit" style="width:200px;" type="text" placeholder="请输入本次开票金额" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次付款金额:</td>
					<td><input id="t_pay_amt_edit" name="pay_amt_edit" style="width:200px;" type="text" placeholder="请输入本次付款金额" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
			</table>
		</form>
     </div>
	
	    <div id="importDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'项目测试批量导入',
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
					<td nowrap>项目测试上传:
					</td>
					<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" accept=".xls,.xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>
	
	<div data-options="region:'center',border:false">
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	    <a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">导入数据</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="popAddDialog()">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <span>供应商：</span>
	    <select class="easyui-combobox" id="supplier" style="width:180px;"></select>&nbsp;&nbsp;&nbsp;&nbsp;
	    <span>科目：</span>
	    <input class="easyui-combobox" id="account_name" style="width:180px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<span>税率：</span>
		<input class="easyui-combobox" id="tax_rate" style="width:80px;">&nbsp;&nbsp;&nbsp;&nbsp;
		<span>误差值：</span>
		<input class="textbox" id="deviation" style="width:80px;height:20px;">&nbsp;&nbsp;&nbsp;&nbsp;
	    <input type="hidden" id="supplier_name">
		<a href="#" id="btnStart" class="easyui-linkbutton" data-options="iconCls:'icon-start'" onclick="startTest()">开始测试</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnConfirm" class="easyui-linkbutton" data-options="iconCls:'icon-confirm'" onclick="confirmSign()">确认签字</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
		<div style="height:30px;background:#e2dfdf;">
		  <table class="tb">
		    <tr>
		      <td class="td_txt">本次开票总金额：</td>
		      <td class="td_amt"><label id="invoice_amt_i_tax_txt"></label><input id="invoice_amt_i_tax" type="hidden"></td>
		      <td class="td_txt">本次开票不含税总金额：</td>
		      <td class="td_amt"><label id="invoice_amt_e_tax_txt"></label><input id="invoice_amt_e_tax" type="hidden"></td>
		      <td class="td_txt">本次付款总金额：</td>
		      <td class="td_amt"><label id="pay_amt_i_tax_txt"></label><input id="pay_amt_i_tax" type="hidden"></td>
		      <td class="td_txt">本次付款不含税总金额：</td>
		      <td class="td_amt"><label id="pay_amt_e_tax_txt"></label><input id="pay_amt_e_tax" type="hidden"></td>
		    </tr>
		  </table>
		</div>
	</div>
</body>
</html>