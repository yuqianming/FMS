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
	
<title>用户管理</title>

	<script type="text/javascript">
	
	var treeGrid;
	$(function() {
		$('#addDialog').dialog('close');
		$('#editDialog').dialog('close');
		treeGrid = $('#treeGrid').treegrid({
			url : '${ctx}' + '/departAdmin/treeGrid',
			idField : 'id',
			treeField:'text',
			title:"机构信息列表",  
			columns : [ [ {
				width : '150',
				title : '机构名称',
				field : 'text',
				align:'center'
			}, {
				width : '100',
				title : '机构编码',
				field : 'id',
				align:'center'
			} ,{
				field : 'action',
				title : '操作',
				width : 120,
				formatter : function(value, row, index) {
					var str = '&nbsp;';
					str += $.formatString('<a href="javascript:void(0)" onclick="popEditDialog(\'{0}\',\'{1}\',\'{2}\');" >编辑</a>', row.id,row.text,row.pid);
					str += '&nbsp;&nbsp;|&nbsp;&nbsp;'
					str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\',\'{1}\');" >删除</a>', row.id,row.text);
					return str;
				}
			} ]],
			onLoadSuccess:function(){
			}
		});
	});
	
	
	function popAddDialog()
	{
		$("#pid").combotree({
			url:'${ctx}/departAdmin/departList',
			parentField : 'pid',
			lines : true,
			editable:false
		});
		$('#addDialog').dialog('open');
	}
	
	
	function deleteFun(id,text) {
		parent.$.messager.confirm('询问', '您是否要删除机构"'+text+'"？', function(b) {
			if (b) {
				progressLoad();
				$.post('${ctx}/departAdmin/delete', {
					org_id : id,
					org_name:text
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						treeGrid.treegrid('reload');
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
	
	
	function addFun()
	{
		var pid=$("#pid").combotree("getValue");
		var org_id='${sessionInfo.orgId}';
		if(org_id!="all")
		{
			if(!pid)
			{
				parent.$.messager.alert('提示','您不能建顶级机构，请选择上级机构','info');
				return;
			}
		}
		$('#addForm').form({
			url : '${ctx}/departAdmin/add',
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
					$("#org_id").val('');
					$("#org_name").val('');
					$("#pid").val('');
					treeGrid.treegrid('reload');
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
		$("#org_id").val('');
		$("#org_name").val('');
		$("#pid").val('');
	}
	
	function popEditDialog(id,text,pid)
	{
		$("#pid_edit").combotree({
			url:'${ctx}/departAdmin/departList',
			parentField : 'pid',
			lines : true,
			editable:false
		});
		$("#org_id_edit").val(id);
		$("#org_name_edit").val(text);
		//$("#pid_edit").val(pid);
		if(pid!='undefined')
		{
			$("#pid_edit").combotree("setValue",pid);
		}
		$('#editDialog').dialog('open');
	}
	
	
	function editFun()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/departAdmin/edit",//路径  
			//dataType : "json",
			data : {
				org_id:$("#org_id_edit").val(),
				org_name:$("#org_name_edit").val(),
				pid:$("#pid_edit").combotree("getValue")
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#editDialog').dialog('close');
					treeGrid.treegrid('reload');
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
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>

    	 <div id="addDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'机构新增',
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
					<td nowrap>机构编码:</td>
					<td><input id="org_id" name="org_id" style="width:200px;" type="text"  class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>	
				</tr>
				<tr>
					<td>机构名称:</td>
					<td><input id="org_name" name="org_name" style="width:200px;" type="text"  class="easyui-validatebox" data-options="required:true" value="" maxlength="50"></td>	
				</tr>
				<tr>
					<td>上级机构:</td>
					<td><select id="pid" name="pid" style="width: 200px; height: 25px;"></select></td>	
				</tr>
			</table>
		</form>
     </div>
     
     <div id="editDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'机构修改',
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
					<td nowrap>机构编码:</td>
					<td><input id="org_id_edit" name="org_id_edit" style="width:200px;" type="text" disabled="disabled"></td>	
				</tr>
				<tr>
					<td>机构名称:</td>
					<td><input id="org_name_edit" name="org_name_edit" style="width:200px;" type="text"  class="easyui-validatebox" data-options="required:true" value="" maxlength="50"></td>	
				</tr>
				<tr>
					<td>上级机构:</td>
					<td><select id="pid_edit" name="pid_edit" style="width: 200px; height: 25px;"></select></td>	
				</tr>
			</table>
		</form>
     </div>

	
	<div data-options="region:'center',fit:false,border:false" >
		<table id="treeGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-treeGrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	   <a href="#" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="popAddDialog()">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
</body>
</html>