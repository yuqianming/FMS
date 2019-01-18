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
    $.canAdd=false;
	$.canDelete = false;
	$.canImport = false;
	$.canEdit = false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT0501060')
		{
			$.canAdd=true;
		}
		else if(buttonList[i].id=='BT0501061')
		{
			$.canDelete=true;
		}
		else if(buttonList[i].id=='BT0501062')
		{
			$.canImport=true;
		}
		else if(buttonList[i].id=='BT0501063')
		{
			$.canEdit=true;
		}
	}
	</script>
	
<title></title>

	<script type="text/javascript">
	
	var dataGrid;
	$(function() {
		buttonHandle();
		$('#addDialog').dialog('close');
		$('#importDialog').dialog('close');
		$('#editDialog').dialog('close');
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/oldProj/dataGrid',
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
			title:"旧项目信息列表",  
			columns : [ [ {
				title : '选择',
				field : 'ck',
				checkbox:true
			},{
				width : '150',
				title : '项目代码',
				field : 'project_code',
				sortable : true,
				align:'center'
			}, {
				width : '400',
				title : '项目名称',
				field : 'project_name',
				sortable : true,
				align:'center'
			}] ],
			onLoadSuccess:function(){
				initTip();
			    $(".datagrid .panel-title").each(function(i,e){
			    	if($(e).html()=='旧项目信息列表')
			    	{
			    		$(this).append('<label class="l-tips">最后一次导入：<span id="tip"></span></label>');
			    	}
			    });
			    $("#dataGrid").datagrid("clearSelections");
			},
			onDblClickRow:function(index,row)
			{
				if($.canEdit)
				{
					$("#id").val(row.id);
					$("#project_code_edit").val(row.project_code);
					$("#project_name_edit").val(row.project_name);
					$('#editDialog').dialog('open');
				}
			}
		});
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
			parent.$.messager.confirm('询问', '您是否要删除所有勾选项？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/oldProj/delete', {
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
	
	function addFun()
	{
		$('#addForm').form({
			url : '${ctx}/oldProj/add',
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
					$("#project_name").val('');
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
		$("#project_code").val('');
		$("#project_name").val('');
	}
	
	function importFun()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
		parent.$.messager.confirm('询问', '导入数据将会覆盖原有数据，是否继续？', function(b) {
			if (b) {
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
		});
	}
	
	function importCancel()
	{
		$('#upFile').val('');
		$('#importDialog').dialog('close');
	}
	
	function editFun()
	{
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/oldProj/edit",//路径  
			//dataType : "json",
			data : {
				id:$("#id").val(),
				project_code:$("#project_code_edit").val(),
				project_name:$("#project_name_edit").val()
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
	</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
	
	 <div id="addDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'旧项目新增',
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
					<td nowrap>项目代码:</td>
					<td><input id="project_code" name="project_code" style="width:200px;" type="text" placeholder="请输入项目代码" class="easyui-validatebox" data-options="required:true" value="" maxlength="30"></td>	
				</tr>
				<tr>
					<td>项目名称:</td>
					<td><input id="project_name" name="project_name" style="width:200px;" type="text" placeholder="请输入项目名称" class="easyui-validatebox" data-options="required:true" value="" maxlength="100"></td>	
				</tr>
			</table>
		</form>
     </div>
     
     <div id="editDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'旧项目修改',
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
					<td nowrap>项目代码:<input id="id" name="id" type="hidden"></td>
					<td><input id="project_code_edit" name="project_code_edit" style="width:200px;" type="text" disabled="disabled"></td>	
				</tr>
				<tr>
					<td nowrap>项目名称:</td>
					<td><input id="project_name_edit" name="project_name_edit" style="width:200px;" type="text" placeholder="请输入科目名称" class="easyui-validatebox" data-options="required:true" value="" maxlength="100"></td>	
				</tr>
			</table>
		</form>
     </div>
	
	    <div id="importDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'旧项目批量导入',
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
					<td nowrap>旧项目上传:
					 <input id="tableName" name="tableName" type="hidden" value="t_old_proj"/>
					</td>
					<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" accept=".xls,.xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>
	
	<div data-options="region:'center'">
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<a href="#" id="btnAdd" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="popAddDialog()">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteFun()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">批量导入</a>
	</div>

</body>
</html>