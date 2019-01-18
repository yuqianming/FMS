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
   /* $.canSign=false;
	var buttonList=${sessionInfo.buttonList};
	for(var i=0;i<buttonList.length;i++)
	{
		if(buttonList[i].id=='BT04010')
		{
			$.canSign=true;
		}
	}*/
	</script>
<style type="text/css">
.datagrid-row-selected {
    background: #CC0000;
    color:#fff;
}
.tb{
padding-left:20px;
padding-top:10px;
}
.tb td{
width:150px;
font-size:15px;
font-weight:bold;
text-align:right;
}
</style>
<title></title>

	<script type="text/javascript">
	var addGrid;
	var subGrid;
	var detailGrid;
	var IsCheckFlag = true;
	var type;
	var mode;
	var interval;
	var addLoad=false;
	var subLoad=false;
	$(function() {
		addGrid = $('#addGrid').datagrid({
			striped : true,    //隔行变色
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			//title:"项目信息列表",
			showFooter:true,
			columns : [[{
				width : '100',
				title : '类型',
				field : 'type',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '仓储有核算无',
				field : 'store_amt_txt',
				align:'right'
			}, {
				width : '150',
				title : '核算有仓储无',
				field : 'deal_amt_txt',
				align:'right'
			}, {
				width : '250',
				title : '订单相同金额不同（仓储 - 核算）',
				field : 'all_amt_txt',
				align:'right'
			}] ],
			onLoadSuccess:function(){
				 $("#addGrid").datagrid("clearSelections");
				 addLoad=true;
			},
		    onClickCell:function(rowIndex, field, value){
		         IsCheckFlag=false;
		         if(field=='type')
		         {
		        	 return;
		         }
		         //取消addGrid各个单元格的样式
		         var trs = $('#addGrid').prev().find('div.datagrid-body').find('tr');
		         for(var i=0;i<trs.length;i++)
		         {
		        	 for(var j=0;j<trs[i].cells.length;j++)
		        	 {
		        		 $(trs[i].cells[j]).removeAttr("style");
		        	 }
		         }
		         
		         //取消subGrid各个单元格的样式
		         var trs_sub = $('#subGrid').prev().find('div.datagrid-body').find('tr');
		         for(var i=0;i<trs_sub.length;i++)
		         {
		        	 for(var j=0;j<trs_sub[i].cells.length;j++)
		        	 {
		        		 $(trs_sub[i].cells[j]).removeAttr("style");
		        	 }
		         }
		         
		         var cells=trs[rowIndex].cells;
		         for(var i=0;i<cells.length;i++)
		         {
		        	 if($(cells[i]).attr('field')==field)
		        	 {
		        		 cells[i].style.cssText='background:#CC0000;color:#fff';
		        	 }
		         }
		         
		         var tt=addGrid.datagrid('getRows')[rowIndex];
		         type=tt.type;
		         mode=field;
		         viewDetail(tt.type,field);
		     },
		     onSelect: function (rowIndex, rowData) {
		         /*if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             poolGrid.datagrid("unselectRow", rowIndex);
		         }*/
		         addGrid.datagrid("unselectRow", rowIndex);
		     },                    
		     onUnselect: function (rowIndex, rowData) {
		         if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             addGrid.datagrid("selectRow", rowIndex);
		         }
		     }
		});
		
		subGrid = $('#subGrid').datagrid({
			striped : true,    //隔行变色
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			//title:"项目信息列表",
			showFooter:true,
			columns : [[{
				width : '100',
				title : '类型',
				field : 'type',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '仓储有核算无',
				field : 'store_amt_txt',
				align:'right'
			}, {
				width : '150',
				title : '核算有仓储无',
				field : 'deal_amt_txt',
				align:'right'
			}, {
				width : '250',
				title : '订单相同金额不同（仓储 - 核算）',
				field : 'all_amt_txt',
				align:'right'
			}] ],
			onLoadSuccess:function(){
				 $("#subGrid").datagrid("clearSelections");
				 subLoad=true;
			},
		    onClickCell:function(rowIndex, field, value){
		         IsCheckFlag=false;
		         if(field=='type')
		         {
		        	 return;
		         }
		         //取消addGrid各个单元格的样式
		         var trs = $('#subGrid').prev().find('div.datagrid-body').find('tr');
		         for(var i=0;i<trs.length;i++)
		         {
		        	 for(var j=0;j<trs[i].cells.length;j++)
		        	 {
		        		 $(trs[i].cells[j]).removeAttr("style");
		        	 }
		         }
		         //取消addGrid各个单元格的样式
		         var trs_add = $('#addGrid').prev().find('div.datagrid-body').find('tr');
		         for(var i=0;i<trs_add.length;i++)
		         {
		        	 for(var j=0;j<trs_add[i].cells.length;j++)
		        	 {
		        		 $(trs_add[i].cells[j]).removeAttr("style");
		        	 }
		         }
		         
		         var cells=trs[rowIndex].cells;
		         for(var i=0;i<cells.length;i++)
		         {
		        	 if($(cells[i]).attr('field')==field)
		        	 {
		        		 cells[i].style.cssText='background:#CC0000;color:#fff';
		        	 }
		         }
		         
		         var tt=subGrid.datagrid('getRows')[rowIndex];
		         type=tt.type;
		         mode=field;
		         viewDetail(tt.type,field);
		     },
		     onSelect: function (rowIndex, rowData) {
		         /*if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             poolGrid.datagrid("unselectRow", rowIndex);
		         }*/
		         subGrid.datagrid("unselectRow", rowIndex);
		     },                    
		     onUnselect: function (rowIndex, rowData) {
		         if (!IsCheckFlag) {
		             IsCheckFlag = true;
		             subGrid.datagrid("selectRow", rowIndex);
		         }
		     }
		});
		
		detailGrid = $('#detailGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumn : false, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			//idField : 'supplier_code',
			sortName : 'order_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [20, 50, 100, 200 ],
			showFooter:true,
			//title:"供应商明细信息列表",
			columns : [[{
				width : '100',
				title : '日期',
				field : 'month',
				sortable : true,
				align:'center'
			},{
				width : '250',
				title : '订单号或入库单号',
				field : 'order_code',
				sortable : true,
				align:'center'
			},{
				width : '200',
				title : '厂家',
				field : 'supplier_name',
				sortable : true,
				align:'center'
			}, {
				width : '150',
				title : '核算系统金额',
				field : 'deal_amt',
				align:'right'
			}, {
				width : '150',
				title : '仓储系统金额',
				field : 'store_amt',
				align:'right'
			}, {
				width : '150',
				title : '差异金额（仓储-核算）',
				field : 'all_amt',
				align:'right'
			}]],
			onLoadSuccess:function(){
				 $("#detailGrid").datagrid("clearSelections");
			}
		});
		
		
		$("#checkAll").change(function() {
			/*if($('#checkAll').is(':checked')) {
				parent.$.messager.alert('提示', "选中了全部核对过程", 'info');
			}*/
			if(type==''||type==undefined||mode==''||mode==undefined)
			{
				parent.$.messager.alert('提示', "请选择'加项'或者'减项'中的某项记录！", 'info');
				return;
			}
			viewDetail(type,mode);
		});
		
		$("#hideZero").change(function() {
			/*if($('#checkAll').is(':checked')) {
				parent.$.messager.alert('提示', "选中了全部核对过程", 'info');
			}*/
			if(type==''||type==undefined||mode==''||mode==undefined)
			{
				parent.$.messager.alert('提示', "请选择'加项'或者'减项'中的某项记录！", 'info');
				return;
			}
			viewDetail(type,mode);
		});
	});
	
	function startTest()
	{
		type='';
		mode='';
		$("#checkAll").attr("checked", false);
		$("#hideZero").attr("checked", false);
		detailGrid.datagrid('loadData', { total: 0, rows: [] });
		var startMonth=$("#startMonth").val();
		var endMonth=$("#endMonth").val();
		
		if(startMonth=='')
		{
			parent.$.messager.alert('提示', '起始年月不能为空！', 'info');
			return;
		}
		else if(startMonth<'201801')
		{
			parent.$.messager.alert('提示', '起始年月不能小于201801！', 'info');
			return;
		}
		
		if(endMonth!='')
		{
			if(endMonth<startMonth)
			{
				parent.$.messager.alert('提示', '终止年月不能小于起始年月！', 'info');
				return;
			}
		}
		
		var addUrl='${ctx}/storeCheck/addGrid';
		var addParams=addGrid.datagrid('options').queryParams;
		addParams.startMonth=startMonth;
		addParams.endMonth=endMonth;
		addGrid.datagrid('reload',addUrl);
		var subUrl='${ctx}/storeCheck/subGrid';
		var subParams=subGrid.datagrid('options').queryParams;
		subParams.startMonth=startMonth;
		subParams.endMonth=endMonth;
		subGrid.datagrid('reload',subUrl);
		interval=setInterval(function(){
			if(addLoad&&subLoad)
			{
				poolData();
			}
		},2000);
	}
	
	function poolData()
	{
		var startMonth=$("#startMonth").val();
		var endMonth=$("#endMonth").val();
		$.ajax({
			type : "POST",
			url : "${ctx}/storeCheck/poolData",
			data : {
				startMonth:startMonth,
				endMonth:endMonth
			},  
			success : function(result) {
				result=$.parseJSON(result);
				$("#deal_amt_txt").text(result.deal_amt_txt);
				$("#store_amt_txt").text(result.store_amt_txt);
				$("#dvalue_txt").text(result.dvalue_txt);
				$("#deal_amt_adj_txt").text(result.deal_amt_adj_txt);
				$("#store_amt_adj_txt").text(result.store_amt_adj_txt);
				$("#dvalue_adj_txt").text(result.dvalue_adj_txt);
				clearInterval(interval);
				addLoad=false;
				subLoad=false;
			}
		});
	}
	
	function viewDetail(type,mode)
	{
		var isAll=false;
		if($('#checkAll').is(':checked')) {
			isAll=true;
		}
		var hideZero=false;
		if($('#hideZero').is(':checked')) {
			hideZero=true;
		}
		var startMonth=$("#startMonth").val();
		var endMonth=$("#endMonth").val();
		var detailUrl='${ctx}/storeCheck/detailGrid';
		var detailParams=detailGrid.datagrid('options').queryParams;
		detailParams.startMonth=startMonth;
		detailParams.endMonth=endMonth;
		detailParams.type=type;
		detailParams.mode=mode;
		detailParams.isAll=isAll;
		detailParams.hideZero=hideZero;
		detailGrid.datagrid('reload',detailUrl);
	}
	
	function exportDetailData()
	{
		var isAll=false;
		if($('#checkAll').is(':checked')) {
			isAll=true;
		}
		var hideZero=false;
		if($('#hideZero').is(':checked')) {
			hideZero=true;
		}
		var startMonth=$("#startMonth").val();
		var endMonth=$("#endMonth").val();
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/storeCheck/createDetailExcel",//路径  
			//dataType : "json",
			data : {
				startMonth:startMonth,
				endMonth:endMonth,
				type:type,
				mode:mode,
				isAll:isAll,
				hideZero:hideZero
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/storeCheck/exportDetailExcel"
					}); 
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	</script>
</head>

<body class="easyui-layout">
      <div style="display:none"><form id="export" method="post"></form></div>
      <div data-options="region:'north'" style="height:100px;">
		<div style="height:30px;padding-top:5px;padding-left:20px;">
		  <span>起始年月：</span>
		  <input type="text" id="startMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <span>终止年月：</span>
		  <input type="text" id="endMonth" style="width:100px;" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyyMM'})" readOnly="readOnly">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <a href="#" id="btnStart" class="easyui-linkbutton" data-options="iconCls:'icon-start'" onclick="startTest()">开始测试</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <!--<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="detailExport()">导出Excel</a>-->
		</div>
		<div style="height:70px;background:#e2dfdf;">
		  <table class="tb">
		    <tr>
		      <td>核算系统余额：</td>
		      <td><label id="deal_amt_txt"></label></td>
		      <td>仓储系统余额：</td>
		      <td><label id="store_amt_txt"></label></td>
		      <td>差额：</td>
		      <td><label id="dvalue_txt"></label></td>
		    </tr>
		    <tr>
		      <td>调整后核算余额：</td>
		      <td><label id="deal_amt_adj_txt"></label></td>
		      <td>调整后仓储余额：</td>
		      <td><label id="store_amt_adj_txt"></label></td>
		      <td>差额：</td>
		      <td><label id="dvalue_adj_txt"></label></td>
		    </tr>
		  </table>
		</div>
	  </div>
	  <div data-options="region:'center',border:false">
	   <div  class="easyui-layout" style="width:100%;height:100%;">
	    <div data-options="region:'north',border:false" style="height:160px;">
	     <div  class="easyui-layout" style="width:100%;height:160px;">
	      <div data-options="region:'center'" style="height:160px;width:50%;">
	         <table id="addGrid" data-options="fit:false,border:false,toolbar:'#tb1'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	      </div>
	      <div data-options="region:'east'" style="height:160px;width:50%;">
	         <table id="subGrid" data-options="fit:false,border:false,toolbar:'#tb2'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	      </div>
	     </div>
	    </div>
	    <div data-options="region:'center',border:false" style="height:100%;width:100%;">
	      <table id="detailGrid" data-options="fit:true,border:false,toolbar:'#tb3'"></table>
	    </div>
	   </div>
	  </div>
	  <div id="tb1" style="padding:3px;line-height:25px;background-color:#fff;">
		<span style="padding-left:20px;color:#9e9898;">加项</span>
	  </div>
	  <div id="tb2" style="padding:3px;line-height:25px;background-color:#fff;">
		<span style="padding-left:20px;color:#9e9898;">减项</span>
	  </div>
	 <div id="tb3" style="padding:3px 3px 3px 20px;line-height:26px;background-color:#fff;">
		<input id="checkAll" style="vertical-align: middle;" name="checkAll" type="checkbox"/>
		<span>全部核对过程</span>&nbsp;&nbsp;&nbsp;
		<input id="hideZero" style="vertical-align: middle;" name="hideZero" type="checkbox"/>
		<span>隱藏金额为0的记录</span>&nbsp;&nbsp;&nbsp;
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportDetailData()">导出Excel</a>
	</div>
</body>
</html>