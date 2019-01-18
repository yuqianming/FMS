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
            if(buttonList[i].id=='BT02010')
			{
				$.canImport=true;
			}
            else if(buttonList[i].id=='BT02011')
			{
				$.canExport=true;
			}
		}
	</script>
	
<title></title>

	<script type="text/javascript">
	var url='${ctx}' + '/costSheet/dataGrid';
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
			idField : 'busi_date',
			sortName : 'busi_date',
			sortOrder : 'asc',
			pageSize : 20,
			pageList : [ 20, 50, 100, 200],
			title:"成本信息列表",
			showFooter:true,
			columns : [[
			     {title:'<input id="busiDateResultSearch" type="text" class="Wdate" style="width:90px;padding:0;border:0;" onclick="WdatePicker({readOnly:true,dateFmt:\'yyyy-MM-dd\',onpicked:function(){bindLeaveEvent();},oncleared:function(){bindLeaveEvent();}})" readOnly="readOnly"/> '},
			     {title:'<input id="orderCodeResultSearch" type="text" onchange="bindLeaveEvent()" style="width:140px;padding:0;border:0;"/>'},
			     {title:''},
			     {title:'<input id="supplierNameResultSearch" type="text" onchange="bindLeaveEvent()" style="width:230px;padding:0;border:0;"/>'},
			     {title:'<input id="supplierCodeResultSearch" type="text" onchange="bindLeaveEvent()" style="width:110px;padding:0;border:0;"/>'},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:'<input id="projectCodeResultSearch" type="text" onchange="bindLeaveEvent()" style="width:140px;padding:0;border:0;"/>'},
			     {title:'<input id="projectNameResultSearch" type="text" onchange="bindLeaveEvent()" style="width:160px;padding:0;border:0;"/>'},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:'<input id="documentaryResultSearch" type="text" onchange="bindLeaveEvent()" style="width:90px;padding:0;border:0;"/>'},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:''},
			     {title:'<input id="accountCodeResultSearch" type="text" onchange="bindLeaveEvent()" style="width:70px;padding:0;border:0;"/>'},
			     {title:'<input id="accountNameResultSearch" type="text" onchange="bindLeaveEvent()" style="width:110px;padding:0;border:0;"/>'},
			     {title:''},
			     {title:'<input id="voucherNoResultSearch" type="text" onchange="bindLeaveEvent()" style="width:110px;padding:0;border:0;"/>'},
			     {title:''},
			 ],[ 
			     {width : '100',title:'业务日期',field : 'busi_date',align:'center',sortable : true} , 
			     {width : '150',title:'采购订单号',field : 'order_code',align:'center',sortable : true} , 
			     {width : '80',title:'订单来源',field : 'source_type',align:'center',sortable : true} , 
			     {width : '250',title:'供应商名称',field : 'supplier_name',align:'center',sortable : true} , 
			     {width : '120',title:'供应商编号',field : 'supplier_code',align:'center',sortable : true}, 
			     {width : '150',title:'物料编码',field : 'material_code',align:'center',sortable : true}, 
			     {width : '150',title:'生产厂家',field : 'manufacturer',align:'center',sortable : true}, 
			     {width : '120',title:'规格编号',field : 'spec_code',align:'center',sortable : true}, 
			     {width : '120',title:'物料名称',field : 'material_name',align:'center',sortable : true}, 
			     {width : '150',title:'物资服务编码',field : 'service_code',align:'center',sortable : true}, 
			     {width : '60',title:'计量单位',field : 'unit',align:'center',sortable : true}, 
			     {width : '60',title:'数量',field : 'quantity',align:'center',sortable : true}, 
			     {width : '80',title:'不含税单价',field : 'unit_price_e_tax',align:'right',sortable : true}, 
			     {width : '100',title:'不含税总金额',field : 'total_amt_e_tax',align:'right',sortable : true}, 
			     {width : '120',title:'当期成本确认金额',field : 'period_cost_amt',align:'right',sortable : true}, 
			     {width : '80',title:'入账金额',field : 'account_amt',align:'right',sortable : true}, 
			     {width : '150',title:'工程项目编码',field : 'project_code',align:'center',sortable : true}, 
			     {width : '180',title:'工程项目名称',field : 'project_name',align:'center',sortable : true}, 
			     {width : '150',title:'站址编号',field : 'address_code',align:'center',sortable : true}, 
			     {width : '150',title:'站址名称',field : 'address_name',align:'center',sortable : true}, 
			     {width : '80',title:'核算组织',field : 'accounting_org',align:'center',sortable : true}, 
			     {width : '80',title:'单据类型',field : 'doc_type',align:'center',sortable : true}, 
			     {width : '100',title:'制单人',field : 'documentary',align:'center',sortable : true}, 
			     {width : '80',title:'是否装配',field : 'assemble_sts',align:'center',sortable : true}, 
			     {width : '80',title:'是否分摊',field : 'allocation_sts',align:'center',sortable : true}, 
			     {width : '80',title:'是否转资',field : 'asset_trans_sts',align:'center',sortable : true}, 
			     {width : '100',title:'时间戳',field : 'timestamp',align:'center',sortable : true}, 
			     {width : '80',title:'科目编号',field : 'account_code',align:'center',sortable : true}, 
			     {width : '120',title:'科目名称',field : 'account_name',align:'center',sortable : true}, 
			     {width : '100',title:'订单类型',field : 'order_type',align:'center',sortable : true}, 
			     {width : '120',title:'凭证编号',field : 'voucher_no',align:'center',sortable : true}, 
			     {width : '150',title:'交易明细行号',field : 'detail_no',align:'center',sortable : true}
			     ]],
				 onLoadSuccess:function(){
						$("#dataGrid").datagrid("clearSelections");
						initTip();
					    $(".datagrid .panel-title").each(function(i,e){
					    	if($(e).html()=='成本信息列表')
					    	{
					    		$(this).append('<label class="l-tips">最后一次导入：<span id="tip"></span></label>');
					    	}
					    });
					}
		});
		
		dataGrid.datagrid('reload',url);

		/*bindEnterEvent('listIdResultSearch');
		//bindEnterEvent('busiDateResultSearch');
		bindEnterEvent('orderCodeResultSearch');
		bindEnterEvent('supplierNameResultSearch');
		bindEnterEvent('supplierCodeResultSearch');
		bindEnterEvent('projectCodeResultSearch');
		bindEnterEvent('projectNameResultSearch');
		bindEnterEvent('documentaryResultSearch');
		bindEnterEvent('accountCodeResultSearch');
		bindEnterEvent('accountNameResultSearch');
		bindEnterEvent('voucherNoResultSearch');*/
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
	
	function bindLeaveEvent()
	{
		var queryParams=$("#dataGrid").datagrid('options').queryParams;
		var busiDateResultSearch=$("#busiDateResultSearch").val();
		var orderCodeResultSearch=$("#orderCodeResultSearch").val();
		var supplierNameResultSearch=$("#supplierNameResultSearch").val();
		var supplierCodeResultSearch=$("#supplierCodeResultSearch").val();
		var projectCodeResultSearch=$("#projectCodeResultSearch").val();
		var projectNameResultSearch=$("#projectNameResultSearch").val();
		var documentaryResultSearch=$("#documentaryResultSearch").val();
		var accountCodeResultSearch=$("#accountCodeResultSearch").val();
		var accountNameResultSearch=$("#accountNameResultSearch").val();
		var voucherNoResultSearch=$("#voucherNoResultSearch").val();
		if(orderCodeResultSearch.length>50)
		{
			$.messager.alert('提示','采购订单号长度不能超过50');
			return;
		}
		if(supplierNameResultSearch.length>100)
		{
			$.messager.alert('提示','供应商名称长度不能超过100');
			return;
		}
		if(supplierCodeResultSearch.length>30)
		{
			$.messager.alert('提示','供应商编码长度不能超过30');
			return;
		}
		if(projectCodeResultSearch.length>30)
		{
			$.messager.alert('提示','项目编码长度不能超过30');
			return;
		}
		if(projectNameResultSearch.length>100)
		{
			$.messager.alert('提示','项目名称长度不能超过100');
			return;
		}
		if(documentaryResultSearch.length>20)
		{
			$.messager.alert('提示','制作人长度不能超过20');
			return;
		}
		if(accountCodeResultSearch.length>10)
		{
			$.messager.alert('提示','科目编码长度不能超过10');
			return;
		}
		if(accountNameResultSearch.length>50)
		{
			$.messager.alert('提示','科目名称长度不能超过50');
			return;
		}
		if(voucherNoResultSearch.length>20)
		{
			$.messager.alert('提示','凭证编号长度不能超过20');
			return;
		}
		queryParams.busi_date=busiDateResultSearch;
		queryParams.order_code=orderCodeResultSearch;
		queryParams.supplier_name=supplierNameResultSearch;
		queryParams.supplier_code=supplierCodeResultSearch;
		queryParams.project_code=projectCodeResultSearch;
		queryParams.project_name=projectNameResultSearch;
		queryParams.documentary=documentaryResultSearch;
		queryParams.account_code=accountCodeResultSearch;
		queryParams.account_name=accountNameResultSearch;
		queryParams.voucher_no=voucherNoResultSearch;
		dataGrid.datagrid('reload');
	}
	
	/*function bindEnterEvent(id)
	{
		$('#'+id).keydown(function(e){
			if(e.keyCode == 13)
			{
				$('#'+id).change();
			}
		});
	}*/
	
	function popImportDialog()
	{
		var val = $('input:radio[name="import_type"]:checked').val();
		$("#isFull").val(val);
		if($("input[type='checkbox']").is(':checked'))
		{
			$("#isSynchronize").val("1");
		}
		else
		{
			$("#isSynchronize").val("0");
		}
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
								}, 2000);  
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
		//$('#importForm').submit();
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
			url : "${ctx}/costSheet/createExcel",//路径  
			//dataType : "json",
			data : {
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					$("#export").form('submit',{
						url:"${ctx}/costSheet/exportExcel"
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
			data-options="title:'成本单批量导入',
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
					<td nowrap>成本单上传:
					 <input id="tableName" name="tableName" type="hidden" value="t_cost"/>
					 <input id="isFull" name="isFull" type="hidden"/>
					 <input id="isSynchronize" name="isSynchronize" type="hidden"/>
					</td>
					<td id="file_td"><input id="upFile" name="upFile"  style="width: 100%;" type="file" accept=".xls,.xlsx" value="选择文件" /></td>	
				</tr>
			</table>
		</form>
     </div>

	<div data-options="region:'center',fit:false,border:false" style="overflow:hidden">
		<table id="dataGrid" data-options="fit:true,border:false,toolbar:'#tb'" class="easyui-datagrid"  style="width:100%;height:100%;"></table>
	</div>
	
	<div id="tb" style="padding:3px;">
	    <input style="vertical-align: text-bottom;" name="import_type" type="radio" value="0" checked/>
		<span>增量导入</span>&nbsp;&nbsp;
		<input style="vertical-align: text-bottom;" name="import_type" type="radio" value="1"/>
		<span>全量导入</span>&nbsp;&nbsp;
		<input style="vertical-align: middle;" name="synchronize" type="checkbox"/>
		<span>同步数据字典（科目/供应商）</span>&nbsp;&nbsp;
		<a href="#" id="btnImport" class="easyui-linkbutton" data-options="iconCls:'icon-import'" onclick="popImportDialog()">批量导入</a>
		<a href="#" id="btnExport" style="position:absolute;right:30px;" class="easyui-linkbutton" data-options="iconCls:'icon-export'" onclick="exportData()">导出Excel</a>
	</div>
</body>
</html>