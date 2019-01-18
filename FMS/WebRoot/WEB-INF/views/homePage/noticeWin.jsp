<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<title></title>
</head>

<script type="text/javascript">

	var noticeGrid;
	var notice_url='${ctx}/notice/getNotices';
		$(function(){
			
			noticeGrid = $('#noticeGrid').datagrid({
									url : notice_url,
									striped : true,    //隔行变色
									rownumbers : true, //显示序号
									pagination : true,
									singleSelect : true,
									fitColumn : false, //为true的时候没有竖状滚动条
									fit : true, //适应整个屏幕
									sortName : 'notice_time',
									sortOrder : 'desc',
									pageSize : 20,
									pageList : [ 20, 50, 100, 200 ],
									columns : [[{
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
										width : '50',
										title : '查看',
										field:'operate',
										align:'center',
										formatter:function(value,row,index){
											var downButton = $.formatString('<a href="#" class="easyui-linkbutton" onclick="detailData(\'{0}\');" >查看</a>', row.id);
											return downButton;
										}
									}] ]
								});
			
			
			
		})
		
		function detailData(rowId)
		{
			
			//弹窗bug修复
			$('#noticeFrame').html("<div id='noticeView'></div>");
			
			$('#noticeView').dialog({
			    title: '公告展示弹窗',
			    width : 800,
				height : 500,
			    closable: true,
				closed: false,
			    cache: false,
			    modal: true,
			    onLoad : function() {
			    	
                },
                onClose : function() {
                	$(this).dialog('destroy');
                	$('#noticeFrame').html("");
                },
                
			});
			
			
			//$('#noticeView').dialog("open");

			$('#noticeView').dialog('refresh',"${ctx}/home/noticeView?id="+rowId);
			
			/* parent.$.modalDialog({
				title : "公告展示弹窗",
				width : 800,
				height : 500,
				href : "${ctx}/home/noticeView?id="+rowId
			}); */
			
			//window.open("${ctx}/home/noticeView?id="+rowId,"_buttom");
		}
	
</script>

	<table id="noticeGrid"></table>
	
	<div id="noticeFrame"></div>
	
</html>