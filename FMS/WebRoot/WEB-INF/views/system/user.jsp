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
	   /* $.canAdd = false;
		$.canEdit = false;
		$.canDelete = false;
		var buttonList=${sessionInfo.buttonList};
		for(var i=0;i<buttonList.length;i++)
		{
			if(buttonList[i].id=='BT06010')
			{
				$.canAdd=true;
			}
			else if(buttonList[i].id=='BT06012')
			{
				$.canDelete=true;
			}
			else if(buttonList[i].id=='BT06011')
			{
				$.canEdit=true;
			}
		}*/
	</script>
	
<title>用户管理</title>

	<script type="text/javascript">
	
	var dataGrid;
	$(function() {
		$('#addDialog').dialog('close');
		$('#editDialog').dialog('close');
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/userAdmin/dataGrid',
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : false,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'user_id',
			sortName : 'user_id',
			sortOrder : 'asc',
			pageSize : 10,
			pageList : [ 10, 20, 50, 100, 200 ],
			title:"人员信息列表",  
			columns : [ [ {
				title : '选择',
				field : 'ck',
				checkbox:true
			},{
				width : '100',
				title : '用户ID',
				field : 'user_id',
				align:'center',
				sortable : true
			}, {
				width : '150',
				title : '姓名',
				field : 'user_name',
				align:'center',
				sortable : true
			} , {
				width : '120',
				title : '手机号',
				field : 'mobile',
				align:'center'
			} , {
				width : '150',
				title : '所属机构',
				field : 'org_name',
				align:'center'
			}]],
			onLoadSuccess:function(){
				$("#dataGrid").datagrid("clearSelections");
			   // buttonHandle();
			},
			onDblClickRow:function(index,row)
			{
				//if($.canEdit)
				//{
					$("#id").val(row.id);
					$("#user_id_edit").val(row.user_id);
					$("#user_name_edit").val(row.user_name);
					$("#mobile_edit").val(row.mobile);
					//$("#org_id_edit").val(row.org_id);
					//$('#org_id_edit').combobox('setText', row.org_name);
					$("#org_id_edit").combotree({
			           url:'${ctx}/departAdmin/departList',
			           parentField : 'pid',
			           lines : true,
			           editable:false
		            });
					$('#org_id_edit').combotree('setValue', row.org_id);
					$('#editDialog').dialog('open');
				//}
			}
		});
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
	}
	function popAddDialog()
	{
		$("#org_id").combotree({
			url:'${ctx}/departAdmin/departList',
			parentField : 'pid',
			lines : true,
			editable:false
		});
		var org_id='${sessionInfo.orgId}';
		$('#org_id').combotree('setValue',  org_id);
		$('#addDialog').dialog('open');
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
			parent.$.messager.confirm('询问', '您是否要删除所有勾选项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/userAdmin/delete', {
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
	
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	
	function addFun()
	{
		$('#addForm').form({
			url : '${ctx}/userAdmin/add',
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
					$("#user_id").val('');
					$("#user_name").val('');
					$("#mobile").val('');
					$("#org_id").val('');
					dataGrid.datagrid('load');
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
		$("#user_id").val('');
		$("#user_name").val('');
		$("#mobile").val('');
		$("#org_id").val('');
	}
	
	function editFun()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/userAdmin/edit",//路径  
			//dataType : "json",
			data : {
				id:$("#id").val(),
				user_id:$("#user_id_edit").val(),
				user_name:$("#user_name_edit").val(),
				mobile:$("#mobile_edit").val(),
				org_id:$("#org_id_edit").combotree('getValue')
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#editDialog').dialog('close');
					dataGrid.datagrid('load');
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
	
	function resetPassword()
	{
		var rows = dataGrid.datagrid('getSelections');
		if(rows.length>0)
		{
			var ids = [];
			for(var i=0;i<rows.length;i++)
			{
				ids.push(rows[i].id);
			}
			parent.$.messager.confirm('询问', '您是否要重置所有勾选用户的密码？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/userAdmin/reset', {
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
			parent.$.messager.alert('提示', "请选择要重置密码的用户！", 'info');
		}
	}
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>

    	 <div id="addDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'用户新增',
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
					<td nowrap>用户ID:</td>
					<td><input id="user_id" name="user_id" style="width:200px;" type="text" placeholder="请输入用户ID" class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>	
				</tr>
				<tr>
					<td>姓名:</td>
					<td><input id="user_name" name="user_name" style="width:200px;" type="text" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true" value="" maxlength="50"></td>	
				</tr>
				<tr>
					<td>手机号:</td>
					<td><input id="mobile" name="mobile" style="width:200px;" type="text" placeholder="请输入手机号" class="easyui-validatebox"  maxlength="11"></td>	
				</tr>
				<tr>
					<td>所属机构:</td>
					<td><input id="org_id" name="org_id" style="width:200px;" class="easyui-combobox" class="easyui-validatebox" data-options="required:true"></td>	
				</tr>
			</table>
		</form>
     </div>
     
     <div id="editDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'用户修改',
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
					<td nowrap>用户ID:<input id="id" name="id" type="hidden"></td>
					<td><input id="user_id_edit" name="user_id_edit" style="width:200px;" type="text" disabled="disabled"></td>	
				</tr>
				<tr>
					<td>姓名:</td>
					<td><input id="user_name_edit" name="user_name_edit" style="width:200px;" type="text" placeholder="请输入姓名" class="easyui-validatebox" data-options="required:true" value="" maxlength="50"></td>	
				</tr>
				<tr>
					<td>手机号:</td>
					<td><input id="mobile_edit" name="mobile_edit" style="width:200px;" type="text" placeholder="请输入手机号" class="easyui-validatebox"  maxlength="11"></td>	
				</tr>
				<tr>
					<td>所属机构:</td>
					<td><input id="org_id_edit" name="org_id_edit" style="width:200px;" class="easyui-combobox"></td>	
				</tr>
			</table>
		</form>
     </div>

	
	<div data-options="region:'center',fit:false,border:false" >
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	  <form id="searchForm">
	    <a href="#" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="popAddDialog()">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnReset" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="resetPassword()">重置密码</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <span>用户ID：</span>
	    <input name="user_id" placeholder="请输入用户ID" style="ime-mode:disabled;"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>用户姓名：</span>
		<input name="user_name" placeholder="请输入用户姓名"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    <span>所属机构：</span>
	    <input name="org_name" placeholder="请输入部门名称"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchFun();">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="cleanFun();">清空</a>
	 </form>
	</div>
</body>
</html>