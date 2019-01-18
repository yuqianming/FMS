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
		$.canEdit = false;
		$.canDelete = false;
		$.canExport = false;
		var buttonList=${sessionInfo.buttonList};
		for(var i=0;i<buttonList.length;i++)
		{
            if(buttonList[i].id=='BT02040')
			{
				$.canDelete=true;
			}
            else if(buttonList[i].id=='BT02041')
			{
				$.canEdit=true;
			}
            else if(buttonList[i].id=='BT02042')
			{
				$.canImport=true;
			}
            else if(buttonList[i].id=='BT02043')
			{
				$.canExport=true;
			}
		}
	</script>
	
<title></title>

	<script type="text/javascript">
	var url='${ctx}' + '/signature/dataGrid';
	var dataGrid;
	$(function() {
		buttonHandle();
		$('#editDialog').dialog('close');
		$('#importDialog').dialog('close');
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : false,
			fitColumns : true, //为true的时候没有竖状滚动条
			fit : false, //适应整个屏幕
			idField : 'id',
			sortName : 'project_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200],
			title:"签字未付款信息列表",  
			columns : [[
                 {title : '选择',field : 'ck',checkbox:true},
                 {width : '150',title : '批次号',field : 'batch_no',sortable : true,align:'center'},
                 {width : '150',title : '项目编码',field : 'project_code',sortable : true,align:'center'},
                 {width : '120',title : '供应商编码',field : 'supplier_code',sortable : true,align:'center'},
                 {width : '150',title : '供应商名称',field : 'supplier_name',sortable : true,align:'center'},
                 {width : '250',title : '采购订单号',field : 'order_code',sortable : true,align:'center'},
                 {width : '100',title : '科目名称',field : 'account_name',sortable : true,align:'center'},
                 {width : '150',title : '站址编码',field : 'address_code',sortable : true,align:'center'},
                 {width : '120',title : '站址名称',field : 'address_name',sortable : true,align:'center'},
                 {width : '80',title : '本次发票',field : 'invoice_amt_txt',sortable : true,align:'right'},
                 {width : '90',title : '本次发票净额',field : 'invoice_amt_e_tax_txt',sortable : true,align:'right'},
                 {width : '80',title : '本次付款',field : 'pay_amt_txt',sortable : true,align:'right'},
                 {width : '90',title : '本次付款净额',field : 'pay_amt_e_tax_txt',sortable : true,align:'right'},
                 {width : '150',title : '备注',field : 'remark',sortable : true,align:'center'},
                 {width : '80',title : '税率（%）',field : 'rate_txt',sortable : true,align:'center'},
                 {width : '80',title : '创建人',field : 'update_by',sortable : true,align:'center'},
                 {width : '150',title : '创建时间',field : 'update_time',sortable : true,align:'center'}
			     ]],
				 onLoadSuccess:function(){
						$("#dataGrid").datagrid("clearSelections");
						initTip();
					    $(".datagrid .panel-title").each(function(i,e){
					    	if($(e).html()=='签字未付款信息列表')
					    	{
					    		$(this).append('<label class="l-tips">最后一次导入：<span id="tip"></span></label>');
					    	}
					    });
				},
				onDblClickRow:function(index,row)
				{
					if($.canEdit)
					{
						$("#id").val(row.id);
						$("#batch_no").val(row.batch_no);
						$("#project_code").val(row.project_code);
						$("#supplier_code").val(row.supplier_code);
						$("#supplier_name").val(row.supplier_name);
						$("#order_code").val(row.order_code);
						$("#account_name").val(row.account_name);
						$("#address_code").val(row.address_code);
						$("#address_name").val(row.address_name);
						$("#invoice_amt").val(row.invoice_amt);
						$("#invoice_amt_e_tax").val(row.invoice_amt_e_tax);
						$("#pay_amt").val(row.pay_amt);
						$("#pay_amt_e_tax").val(row.pay_amt_e_tax);
						$("#remark").val(row.remark);
						$("#tax_rate").val(row.tax_rate);
						$("#uuid").val(row.uuid);
						$('#editDialog').dialog('open');
					}
				}
		});
		
		dataGrid.datagrid('reload',url);
	});
	
	function initTip()
	{
		$.ajax({
			type : "POST", 
			url : "${ctx}/common/tip", 
			data : {
				tableName:$("#tableName").val()
			},
			success : function(data) {
				data = $.parseJSON(data);
				$("#tip").text(data.msg);
			}
		});
	}
	
	function buttonHandle()
	{
		if(!$.canImport)
		{
			$('#btnImport').hide();
		}
		if(!$.canDelete)
		{
			$('#btnDelete').hide();
		}
		if(!$.canExport)
		{
			$('#btnExport').hide();
		}
	}
	
	
	function popImportDialog()
	{
		var val = $('input:radio[name="import_type"]:checked').val();
		$("#isFull").val(val);
		$('#importDialog').dialog('open');
	}
	
	function importFun()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
		$('#importForm').form('submit',{
			url : '${ctx}/common/upload',
			onSubmit : function() {
				//progressLoad();
				importLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				result = $.parseJSON(result);
				if (result.success) {
					importHand("读取完成，共有"+result.msg+"条记录。");
					//parent.$.messager.alert('提示',result.msg,'info');
					$('#importForm').form('submit',{
						url : '${ctx}/common/save',
						onSubmit : function() {
							//progressLoad();
							importHand("正在导入中，请稍候...");
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
									emptyFile('upFile','file_td');
									dataGrid.datagrid('load');
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
	}
	
	function importCancel()
	{
		$('#upFile').val('');
		$('#importDialog').dialog('close');
	}
	
	function deleteFun() {
		var rows = $('#dataGrid').datagrid('getSelections');
		if(rows.length>0)
		{
			var ids = [];
			for(var i=0;i<rows.length;i++)
			{
				ids.push(rows[i].id);
			}
			parent.$.messager.confirm('询问', '您是否要删除所有勾选项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/signature/delete', {
						ids : ids.toString()
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
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
	
	function editFun()
	{
		$('#editForm').form('submit',{
			url : '${ctx}/signature/edit',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				result = $.parseJSON(result);
				if (result.success) {
					progressClose();
					$('#editDialog').dialog('close');
					dataGrid.datagrid('reload');
				} else {
					progressClose();
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	function editCancel()
	{
		$('#editDialog').dialog('close');
	}
	
	function exportData()
	{
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/signature/createExcel",//路径  
			//dataType : "json",
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/signature/exportExcel"
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
 <div id="editDialog" style="height:400px;width:400px;overflow-y:auto;" class="easyui-dialog" 
			data-options="title:'签字未付款修改',
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
					<td>批次号:</td>
					<td><input id="batch_no" name="batch_no" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true"></td>	
				</tr>
				<tr>
					<td nowrap>项目编码:<input id="id" name="id" type="hidden"></td>
					<td><input id="project_code" name="project_code" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true"></td>	
				</tr>
				<tr>
					<td>供应商编码:</td>
					<td><input id="supplier_code" name="supplier_code" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>供应商名称:</td>
					<td><input id="supplier_name" name="supplier_name" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>采购订单号:</td>
					<td><input id="order_code" name="order_code" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>科目名称:</td>
					<td><input id="account_name" name="account_name" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>站址编码:</td>
					<td><input id="address_code" name="address_code" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>站址名称:</td>
					<td><input id="address_name" name="address_name" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次发票:</td>
					<td><input id="invoice_amt" name="invoice_amt" style="width:250px;" type="text"  value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次发票净额:</td>
					<td><input id="invoice_amt_e_tax" name="invoice_amt_e_tax" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次付款:</td>
					<td><input id="pay_amt" name="pay_amt" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>本次付款净额:</td>
					<td><input id="pay_amt_e_tax" name="pay_amt_e_tax" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>备注:</td>
					<td><input id="remark" name="remark" style="width:250px;" type="text" value="" maxlength="14"></td>	
				</tr>
				<tr>
					<td>税率:<input id="uuid" name="uuid" type="hidden" ></td>
					<td><input id="tax_rate" name="tax_rate" style="width:250px;" type="text" class="easyui-validatebox" data-options="required:true" value="" maxlength="14"></td>	
				</tr>
			</table>
		</form>
     </div>

    <div id="importDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'签字未付款批量导入',
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
					<td nowrap>签字未付款上传:
					 <input id="tableName" name="tableName" type="hidden" value="t_signature"/>
					 <input id="isFull" name="isFull" type="hidden"/>
					</td>
					<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" accept=".xls,.xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>

	<div data-options="region:'center',fit:false,border:false">
		<table id="dataGrid" data-options="fit:true,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	    <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>
	    <input style="vertical-align: text-bottom;" name="import_type" type="radio" value="0" checked/>
		<span>增量导入</span>&nbsp;&nbsp;
		<input style="vertical-align: text-bottom;" name="import_type" type="radio" value="1"/>
		<span>全量导入</span>&nbsp;&nbsp;
		<a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">批量导入</a>
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>