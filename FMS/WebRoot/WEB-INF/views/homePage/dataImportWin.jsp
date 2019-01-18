<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title></title>
</head>

<script type="text/javascript">

	$(function(){
		
		var dataImportGrid = $('#dataImportGrid').datagrid({
			url : '${ctx}' + '/home/getDataImportGrid',
			striped : true,    //隔行变色
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			singleSelect : true, //设置为 true，则只允许选中一行。
			pagination:true,
			pageSize : 20,
			pageList : [ 20, 50, 100, 200 ],
			sortName : 'log_time',
			sortOrder : 'desc',
			columns : [[/* {
				width : '10%',
				title : '类型',
				field : 'log_type',
				formatter: function(value,row,index){
					if (row.log_type == 1){
						return "导入";
					} else if (row.log_type == 2) {
						return "同步";
					}
				},
				align:'center'
			}, */{
				width : '45%',
				title : '文件名',
				field : 'file_name',
				align:'center'
			},{
				width : '25%',
				title : '上传人',
				field : 'user_name',
				align:'center'
			},{
				width : '20%',
				title : '上传时间',
				field : 'log_time',
				sortable : true,
				align:'center'
			}]]
		});
		
	});

</script>

	<table id="dataImportGrid"></table>
	
</html>