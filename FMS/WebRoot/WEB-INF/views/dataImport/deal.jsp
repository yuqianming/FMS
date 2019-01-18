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
		$.canImport = false;
		$.canExport = false;
		var buttonList=${sessionInfo.buttonList};
		for(var i=0;i<buttonList.length;i++)
		{
            if(buttonList[i].id=='BT02030')
			{
				$.canImport=true;
			}
            else if(buttonList[i].id=='BT02031')
			{
				$.canExport=true;
			}
		}
	</script>
	
<title></title>

	<script type="text/javascript">
	var url='${ctx}' + '/deal/dataGrid';
	var dataGrid;
	$(function() {
		buttonHandle();
		$('#importDialog').dialog('close');
		dataGrid = $('#dataGrid').datagrid({
			striped : true,    //隔行变色
			rownumbers : true, //显示序号
			pagination : true,
			singleSelect : true,
			fitColumns : true, //为true的时候没有竖状滚动条
			fit : false, //适应整个屏幕
			idField : 'project_code',
			sortName : 'project_code',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [ 20, 20, 50, 100, 200],
			title:"仓储交易信息列表",  
			columns : [[
                 {width : '150',title : '项目编码',field : 'project_code',sortable : true,align:'center'},
                 {width : '450',title : '项目名称',field : 'project_name',sortable : true,align:'center'},
                 {width : '250',title : '采购订单号',field : 'order_code',sortable : true,align:'center'},
                 {width : '220',title : '批次号',field : 'batch_no',sortable : true,align:'center'},
                 {width : '120',title : 'PMS物料编码',field : 'material_code',sortable : true,align:'center'},
                 {width : '150',title : '物料名称',field : 'material_name',sortable : true,align:'center'},
                 {width : '120',title : '业务类型',field : 'busi_type',sortable : true,align:'center'},
                 {width : '80',title : '操作类型',field : 'opration_type',sortable : true,align:'center'},
                 {width : '120',title : '供应商编码',field : 'supplier_code',sortable : true,align:'center'},
                 {width : '150',title : '供应商名称',field : 'supplier_name',sortable : true,align:'center'},
                 {width : '80',title : '交易前数量',field : 'quantity_org',sortable : true,align:'center'},
                 {width : '80',title : '交易数量',field : 'quantity_txn',sortable : true,align:'center'},
                 {width : '80',title : '交易后数量',field : 'quantity_left',sortable : true,align:'center'},
                 {width : '150',title : '总金额（产品净额）',field : 'total_amt_e_tax',sortable : true,align:'right'},
                 {width : '200',title : '含税总金额（产品净额+税额）',field : 'total_amt_i_tax',sortable : true,align:'right'},
                 {width : '200',title : '创建时间',field : 'create_time',sortable : true,align:'center'},
                 {width : '120',title : '是否进行明细核对',field : 'is_check',align:'center'},
                 {width : '250',title : '备注',field : 'remark',align:'center'}
			     ]],
				 onLoadSuccess:function(){
						$("#dataGrid").datagrid("clearSelections");
						initTip();
					    $(".datagrid .panel-title").each(function(i,e){
					    	if($(e).html()=='仓储交易信息列表')
					    	{
					    		$(this).append('<label class="l-tips">最后一次导入：<span id="tip"></span></label>');
					    	}
					    });
					}
		});
		
		dataGrid.datagrid('reload',url);
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
		if(!$.canImport)
		{
			$('#btnImport').hide();
		}
		if(!$.canExport)
		{
			$('#btnExport').hide();
		}
	}
	
	
	function popImportDialog()
	{
		var val = $('input:radio[name="import_type"]:checked').val();
		$("#isFull").val(val);
		$('#importDialog').dialog('open');
	}
	
	function importFun()
	{
		if(!fileIsEmptyOrNot('upFile'))
		{
			parent.$.messager.alert('提示','文件路径不能为空！','info');
			return;
		}
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
	
	function importCancel()
	{
		$('#upFile').val('');
		$('#importDialog').dialog('close');
	}
	
	function exportData()
	{
		//$.messager.progress({msg:'正在下载中，请耐心等候...',interval:800});
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/deal/createExcel",//路径  
			//dataType : "json",
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/deal/exportExcel"
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
    <div id="importDialog" style="height:200px;width:400px;" class="easyui-dialog" 
			data-options="title:'仓库操作批量导入',
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
					<td nowrap>仓库操作上传:
					 <input id="tableName" name="tableName" type="hidden" value="t_store_deal"/>
					 <input id="isFull" name="isFull" type="hidden"/>
					</td>
					<td id="file_td"><input id="upFile" name="upFile" style="width: 100%;" type="file" accept=".xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>

	<div data-options="region:'center',fit:false,border:false">
		<table id="dataGrid" data-options="fit:true,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	    <input style="vertical-align: text-bottom;" name="import_type" type="radio" value="0" checked/>
		<span>增量导入</span>&nbsp;&nbsp;
		<input style="vertical-align: text-bottom;" name="import_type" type="radio" value="1"/>
		<span>全量导入</span>&nbsp;&nbsp;
		<a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">批量导入</a>
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>