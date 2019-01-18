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
var noticeGrid;
var notice_url='${ctx}/notice/getNotices';
	$(function(){
		noticeGrid = $('#noticeGrid').datagrid({
								url : notice_url,
								striped : true,    //隔行变色
								//rownumbers : true, //显示序号
								pagination : true,
								singleSelect : false,
								selectOnCheck : true,
								checkOnSelect : true,
								checkbox : true,
								fitColumn : false, //为true的时候没有竖状滚动条
								fit : true, //适应整个屏幕
								sortName : 'notice_time',
								sortOrder : 'desc',
								pageSize : 20,
								pageList : [ 20, 50, 100, 200 ],
								columns : [[{
									width : '',
									field : 'ck',
									checkbox : true
								},{
									width : '180',
									title : '标题',
									field : 'notice_title',
									align:'center'
								},{
									width : '400',
									title : '内容',
									field : 'notice_content',
									align:'center'
								},{
									width : '150',
									title : '发布时间',
									field : 'notice_time',
									sortable : true,
									align:'center'
								},{
									width : '120',
									title : '发布人',
									field : 'user_name',
									align:'center'
								}] ]
							});
		
	})
	
	function searchData()
	{
		var queryParams=noticeGrid.datagrid('options').queryParams;
		queryParams.notice_title=$("#notice_title_query").val();
		queryParams.notice_content=$("#notice_content_query").val();
		noticeGrid.datagrid('reload');
	}
	
	function addData()
	{
		$('#addForm').form({
			url : '${ctx}/notice/add',
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
					//$('#upFile').val('');
					$("#notice_title").textbox('setValue','');
					$("#notice_content").textbox('setValue','');
					parent.$.messager.alert('提示',result.msg,'info');
					noticeGrid.datagrid('load');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	
		$('#addForm').submit();
	}
	
	function deleteData() {
		var rows = noticeGrid.datagrid('getSelections');
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
					$.post('${ctx}/notice/deleteNotice', {
						ids : ids.toString()
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							noticeGrid.datagrid('load');
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
</script>

	
</head>
<body>
	<div title="公告管理" class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
		<div style="overflow: auto; width: 100%;height: 100%;">
			<div id="cc1" class="easyui-layout" style="width:100%;height:100%;">
			
				<div title="公告发布" data-options="region:'west',collapsible:false" style="width:400px;">
					<br>
					<form id="addForm" method="post">
						<table style="padding-left:15px;">
							<tr>
								<td>标题:</td>
								<td><input class="easyui-textbox" id="notice_title" name="notice_title" type="text" style="width:300px;height:25px;" data-options="required:true"></input></td>
							</tr>
							<tr></tr>
							<tr>
								<td>内容:</td>
								<td>
									<input class="easyui-textbox" data-options="multiline:true,required:true" id="notice_content" name="notice_content" style="width:300px;height:250px;"></input>
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
			    
			    <div title="公告查询" data-options="region:'center',collapsible:false">
					
					<table id="noticeGrid" data-options="fit:true,border:false,toolbar:'#tb'"></table>
					<div id="tb" style="padding:3px;">
					   <span>标题：</span>
		               <input class="textbox" id="notice_title_query" style="width:150px;height:23px;">&nbsp;&nbsp;&nbsp;&nbsp;
		               <span>内容：</span>
		               <input class="textbox" id="notice_content_query" style="width:150px;height:23px;">&nbsp;&nbsp;&nbsp;&nbsp;
		               <a href="#" id="btnSearch" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData()">查询</a>&nbsp;&nbsp;&nbsp;&nbsp;
		               <a href="#" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-del'" onclick="deleteData()">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			    
			</div>
			
		</div>
	</div>
	
</body>
</html>