<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title></title>
</head>

<script type="text/javascript">

	var fileGrid;
	$(function(){
		
		var file_url='${ctx}/files/dataGrid';
		fileGrid = $('#fileGrid').datagrid({
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
									align:'center'
								},{
									width : '150',
									title : '发布时间',
									field : 'create_time',
									sortable : true,
									align:'center'
								},{
									width : '50',
									title : '下载',
									field:'operate',
									align:'center',
									formatter:function(value,row,index){
										var downButton = $.formatString('<a href="#" class="easyui-linkbutton" onclick="exportData(\'{0}\');" >下载</a>', row.id);
										return downButton;
									}
								}]]
							});
		
		
	});
	
	function exportData(rowId)
	{
		if(rowId)
		{
			progressLoad();
			$.ajax({
				type : "POST", //提交方式  
				url : "${ctx}/files/queryFile",//路径  
				//dataType : "json",
				data : {
					id:rowId
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

</script>

	<form id="export" method="post"></form>
	<table id="fileGrid"></table>
	
</html>