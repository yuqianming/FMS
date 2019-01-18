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
	   /* $.canAdd=false;
		$.canEdit = false;
		$.canDelete = false;
		$.canView = false;
		var buttonList=${sessionInfo.buttonList};
		for(var i=0;i<buttonList.length;i++)
		{
			if(buttonList[i].id=='BT06020')
			{
				$.canAdd=true;
			}
			else if(buttonList[i].id=='BT06021')
			{
				$.canEdit=true;
			}
			else if(buttonList[i].id=='BT06022')
			{
				$.canView=true;
			}
			else if(buttonList[i].id=='BT06023')
			{
				$.canDelete=true;
			}
		}*/
	</script>
	
<title>角色管理</title>

	<script type="text/javascript">
	
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/roleAdmin/dataGrid',
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			idField : 'role_id',
			sortName : 'role_name',
			sortOrder : 'asc',
			pageSize : 10,
			pageList : [ 10, 20, 50, 100, 200 ],
			title:"角色信息列表",  
			columns : [ [ {
				field:'ck',checkbox:true
			}, {
				width : '200',
				title : '角色名称',
				field : 'role_name',
				sortable : true,
				align:'center'
			},{
				width : '300',
				title : '角色描述',
				field : 'remark',
				sortable : true,
				align:'center'
			},{
				width : '200',
				title : '所属机构',
				field : 'org_name',
				sortable : true,
				align:'center'
			} , {
				width : '150',
				title : '创建时间',
				field : 'create_time',
				sortable : true,
				align:'center'
			} ] ],
			onLoadSuccess:function(){
			    //buttonHandle();
			    $('#dataGrid').datagrid('clearSelections');//清除之前datagrid记住的选择状态
			}
		});
	});

	function buttonHandle()
	{
		if(!$.canAdd)
		{
			$('#btnAdd').hide();
		}
		if(!$.canEdit)
		{
			$('#btnEdit').hide();
		}
		if(!$.canView)
		{
			$('#btnView').hide();
		}
		if(!$.canDelete)
		{
			$('#btnDelete').hide();
		}
	}
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 800,
			height : 480,
			href : '${ctx}/roleAdmin/addPage',
			buttons : [ {
				text : '添加',
				iconCls: "icon-save",
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#roleAddForm');
					f.submit();
				}
			} ]
		});
	}
	
	function deleteFun() {
		var row = dataGrid.datagrid('getSelected');
		if(row)
		{
			var id = row.role_id;
			parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/roleAdmin/delete', {
						id : id
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
	
	function editFun() {
		var row = dataGrid.datagrid('getSelected');
		if(row)
		{
			var id = row.role_id;
			parent.$.modalDialog({
				title : '角色信息修改',
				width : 800,
				height : 480,
				href : '${ctx}/roleAdmin/editPage?id=' + id,
				buttons : [ {
					text : '编辑',
					iconCls: "icon-save",
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleEditForm');
						f.submit();
					}
				} ]
			});
		}
		else
		{
			parent.$.messager.alert('提示', "请选择要修改的记录！", 'info');
		}
	}
	
	function viewFun(id) {
		var row = dataGrid.datagrid('getSelected');
		if(row)
		{
			var id = row.role_id;
			parent.$.modalDialog({
				title : '角色信息详情',
				width : 800,
				height : 480,
				href : '${ctx}/roleAdmin/viewPage?id=' + id
			});
		}
		else
		{
			parent.$.messager.alert('提示', "请选择要查看的记录！", 'info');
		}
	}
	
	function searchFun() {
		/*var idStr = $("#role_id").val(); 
		var z= /^[0-9]*$/;
		if(!z.test(idStr)){
			$.messager.alert('', "输入的角色编号必须为数字", 'error');
			return;
		}
		if(idStr == ""){
			$("#role_id").val(0);
		}*/
		dataGrid.datagrid('load', $.serializeObject($('#roleSearchForm')));
		/*if(idStr == 0){
			$("#role_id").val("");
		}*/
	}
	function cleanFun() {
		$('#roleSearchForm input').val('');
		dataGrid.datagrid('load', {});
	}
	
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>

	<div data-options="region:'center',fit:false,border:false" >
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	  <form id="roleSearchForm">
	    <a href="#" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addFun()">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnEdit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="editFun()">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnView" class="easyui-linkbutton" data-options="iconCls:'icon-tip'" onclick="viewFun()">查看</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <span>角色名称：</span>
	    <input name="role_name" placeholder="请输入角色名称"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchFun();">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
	    <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="cleanFun();">清空</a>
	 </form>
	</div>
</body>
</html>