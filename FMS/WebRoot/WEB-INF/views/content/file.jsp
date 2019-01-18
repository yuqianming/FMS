<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../inc.jsp"></jsp:include>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="edge"/>

<style type="text/css">

</style>


<script type="text/javascript">
var dataGrid;
var file_url='${ctx}/files/dataGrid';
	$(function(){
		dataGrid = $('#dataGrid').datagrid({
								url : file_url,
								striped : true,    //隔行变色
								rownumbers : true, //显示序号
								pagination : true,
								singleSelect : true,
								fitColumn : false, //为true的时候没有竖状滚动条
								fit : true, //适应整个屏幕
								sortName : 'create_time',
								sortOrder : 'desc',
								pageSize : 20,
								pageList : [ 20, 50, 100, 200 ],
								columns : [[{
									width : '180',
									title : '文件名',
									field : 'file_name',
									align:'center'
								},{
									width : '200',
									title : '备注',
									field : 'remark',
									align:'center'
								},{
									width : '150',
									title : '发布人',
									field : 'user_name',
									sortable : true,
									align:'center'
								},{
									width : '150',
									title : '发布时间',
									field : 'create_time',
									sortable : true,
									align:'center'
								}] ]
							});
		
	})
	
	function searchData()
	{
		var queryParams=dataGrid.datagrid('options').queryParams;
		queryParams.file_name=$("#file_name_query").val();
		queryParams.remark=$("#remark_query").val();
		dataGrid.datagrid('reload');
	}
	
	function addData()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
		$('#importForm').form('submit',{
			url : '${ctx}/files/add',
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
					parent.$.messager.alert('提示',result.msg,'info');
					emptyFile('upFile','file_td');
					$("#remark").textbox('setValue','')
					dataGrid.datagrid('load');
				} else {
					progressClose();
					parent.$.messager.alert('错误', result.msg, 'error');
					emptyFile('upFile','file_td');
				}
			}
		});
	}
	
	function exportData()
	{
		var row = dataGrid.datagrid('getSelected');
		if(row)
		{
			var id=row.id;
			progressLoad();
			$.ajax({
				type : "POST", //提交方式  
				url : "${ctx}/files/queryFile",//路径  
				//dataType : "json",
				data : {
					id:id
				},//数据，这里使用的是Json格式进行传输  
				success : function(result) {//返回数据根据结果进行相应的处理  
					//$.messager.progress('close');
					progressClose();
					result = $.parseJSON(result);
					if (result.success) {
						$("#export").form('submit',{
							url:"${ctx}/files/exportFile"
						}); 
					} else {
						parent.$.messager.alert('错误', result.msg, 'error');
					}
				}
			});
		}
		else
		{
			parent.$.messager.alert('提示', "请选择要下载的文件！", 'info');
		}
	}
	
	function deleteData()
	{
		var row = dataGrid.datagrid('getSelected');
		if(row)
		{
			parent.$.messager.confirm('询问', '您是否要删除？', function(b) {
				if (b) {
					var id=row.id;
					progressLoad();
					$.ajax({
						type : "POST", //提交方式  
						url : "${ctx}/files/deleteFile",//路径  
						//dataType : "json",
						data : {
							id:id
						},//数据，这里使用的是Json格式进行传输  
						success : function(result) {//返回数据根据结果进行相应的处理  
							//$.messager.progress('close');
							progressClose();
							result = $.parseJSON(result);
							if (result.success) {
								parent.$.messager.alert('提示',result.msg,'info');
								dataGrid.datagrid('load');
							} else {
								parent.$.messager.alert('错误', result.msg, 'error');
							}
						}
					});
				}
			})
		}
		else
		{
			parent.$.messager.alert('提示', "请选择要删除的文件！", 'info');
		}
	}
</script>

	
</head>
<body>
<div style="display:none"><form id="export" method="post"></form></div>
	<div title="文档管理" class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
		<div style="overflow: auto; width: 100%;height: 100%;">
			<div id="cc1" class="easyui-layout" style="width:100%;height:100%;">
			
				<div title="文档发布" data-options="region:'west',collapsible:false" style="width:400px;">
					<br>
					<form id="importForm" enctype="multipart/form-data" method="post">
						<table style="padding-left:15px;">
							<tr>
								<td>文件:</td>
								<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" value="选择文件" /></td>
							</tr>
							<tr></tr>
							<tr>
								<td>备注:</td>
								<td>
									<input class="easyui-textbox" data-options="multiline:true" id="remark" name="remark" style="width:300px;height:250px;"></input>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div style="margin: 5% 30%;">
										<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="width:100%;height: 30px;" onclick="addData()">发布</a>
									</div>
								</td>
							</tr>
						</table>
					</form>
					
				</div>
			    
			    <div title="文档查询" data-options="region:'center',collapsible:false">
					
					<table id="dataGrid" data-options="fit:true,border:false,toolbar:'#tb'"></table>
					<div id="tb" style="padding:3px;">
					   <span>文件名：</span>
		               <input class="textbox" id="file_name_query" style="width:150px;height:23px;">&nbsp;&nbsp;&nbsp;&nbsp;
		               <span>备注：</span>
		               <input class="textbox" id="remark_query" style="width:150px;height:23px;">&nbsp;&nbsp;&nbsp;&nbsp;
		               <a href="#" id="btnSearch" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData()">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
		               <a href="#" id="btnExport" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">下载</a>&nbsp;&nbsp;&nbsp;&nbsp;
		               <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteData()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			    
			</div>
			
		</div>
	</div>
	
</body>
</html>