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
	$.canExport = true;
	$.canSearch=true;
	/*var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT01010')
		{
			$.canExport=true;
		}
		else if(buttonList[i].id=='BT01011')
		{
			$.canSearch=true;
		}
	}*/
	
	</script>
	
<title></title>

	<script type="text/javascript">
	
	var dataGrid;
	var url ='${ctx}/costReport/dataGrid';
	$(function() {
		buttonHandle();
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			//pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			columns : [ [ {
				width : '200',
				title : '公司名称',
				field : 'org_name',
				align:'center'
			},{
				width : '100',
				title : '建设方式',
				field : 'build_mode',
				align:'center'
			},{
				width : '120',
				title : '0~3月',
				field : 'account_amt0',
				align:'right'
			} ,{
				width : '120',
				title : '4~6月',
				field : 'account_amt1',
				align:'right'
			},{
				width : '120',
				title : '7~9月',
				field : 'account_amt2',
				align:'right'
			} ,{
				width : '120',
				title : '10~12月',
				field : 'account_amt3',
				align:'right'
			},{
				width : '120',
				title : '13~24月',
				field : 'account_amt4',
				align:'right'
			},{
				width : '120',
				title : '25月以上',
				field : 'account_amt5',
				align:'right'
			}] ],
			onLoadSuccess:function(data){
				var org_name='';
				var rows=data.rows;
				var index=0;
				var count=0;
				var merges = [];
				for(var i=0;i<rows.length;i++)
				{
					count++;
					var row=rows[i];
					if(org_name=='')
					{
						org_name=row.org_name;
					}
					else
					{
						if(org_name!=row.org_name)
						{
							org_name=row.org_name;
							merges.push({index:index,rowspan:count-1})
							index=i;
							count=1;
						}
					}
				}
				merges.push({index:index,rowspan:count});
				
				for(var i=0; i<merges.length; i++){
					$(this).datagrid('mergeCells',{
						index: merges[i].index,
						field: 'org_name',
						rowspan: merges[i].rowspan
					});
				}
				
			}
		});
		initCombobxSelect();
	});

	function buttonHandle()
	{
		if(!$.canSearch)
		{
			$('#btnSearch').hide();
		}
		if(!$.canExport)
		{
			$('#btnExport').hide();
		}
	}
	
	function searchData()
	{
		var queryParams=dataGrid.datagrid('options').queryParams;
		queryParams.startMonth=$("#startMonth").val();
		queryParams.endMonth=$("#endMonth").val();
		if($('#no_cancel').is(':checked')) {
			queryParams.no_cancel=true;
		}
		else
		{
			queryParams.no_cancel=false;
		}
		queryParams.asset_trans_sts=$("#asset_trans_sts").combobox("getValue");
		queryParams.assemble_sts=$("#assemble_sts").combobox("getValue");
		queryParams.doc_type=$("#doc_type").combobox("getValue");
		dataGrid.datagrid('reload',url);
	}
	
	
	function exportData()
	{
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/costReport/createExcel",//路径  
			//dataType : "json",
			data : {
				startMonth:$("#startMonth").val(),
				endMonth:$("#endMonth").val(),
				no_cancel:$('#no_cancel').is(':checked'),
				asset_trans_sts:$("#asset_trans_sts").combobox("getValue"),
				assemble_sts:$("#assemble_sts").combobox("getValue"),
				doc_type:$("#doc_type").combobox("getValue")
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/costReport/exportExcel"
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
	<div data-options="region:'center',border:false" data-options="fit:true,border:false">
		<table id="dataGrid" data-options="fit:false,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;">
		</table>
	</div>
	
	<div id="tb" style="padding:3px;">
		<span>起始年月：</span>
		<input type="text" id="startMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span>终止年月：</span>
		<input type="text" id="endMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="no_cancel" style="vertical-align: middle;" name="no_cancel" type="checkbox"/>
		<span>不含销项</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span>是否转资：</span>
		<select class="easyui-combobox" id="asset_trans_sts" style="width:80px;height:21px;" data-options="editable:false" panelHeight="auto">
		     <option value ="">全部</option>
		     <option value ="未转资">未转资</option>
		     <option value ="已转资">已转资</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>是否装配：</span>
		<select class="easyui-combobox" id="assemble_sts" style="width:80px;height:21px;" data-options="editable:false" panelHeight="auto">
		     <option value ="">全部</option>
		     <option value ="未装配">未装配</option>
		     <option value ="已装配">已装配</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>单据类型：</span>
		<select class="easyui-combobox" id="doc_type" style="width:80px;height:21px;" data-options="editable:false" panelHeight="auto">
		     <option value ="">全部</option>
		     <option value ="设备类">设备类</option>
		     <option value ="费用类">费用类</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnStart" class="easyui-linkbutton" data-options="iconCls:'icon-start'" onclick="searchData();">开始分析</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>