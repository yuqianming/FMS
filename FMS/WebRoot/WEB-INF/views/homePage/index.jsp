<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<jsp:include page="../inc.jsp"></jsp:include>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="edge"/>

<style type="text/css">
	/* 去掉拖行按钮 */
	/* div.panel-tool{
		display:none; 
	} */
	
	#u326_div{
		  /* position:absolute;
		  left:40px;
		  top:60px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(0, 204, 255, 1);
		  
		  /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#00CCFF,endColorstr=#00CCFF);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
	
	 #u337_div {
		  /* position:absolute;
		  left:120px;
		  top:60px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(246, 195, 53, 1);
		  
		   /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#FFF6C335,endColorstr=#FFF6C335);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
			
	#u344_div {
		  /* position:absolute;
		  left:200px;
		  top:60px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(255, 145, 70, 1);
		  
		   /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#FFFF9146,endColorstr=#FFFF9146);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
	
	#u351_div {
		  /* position:absolute;
		  left:40px;
		  top:140px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(255, 145, 70, 1);
		  
		   /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#FFFF9146,endColorstr=#FFFF9146);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
	
	#u358_div {
		  /* position:absolute;
		  left:120px;
		  top:140px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(247, 122, 167, 1);
		  
		   /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#FFF77AA7,endColorstr=#FFF77AA7);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
	
	#u365_div {
		  /* position:absolute;
		  left:200px;
		  top:140px; */
		  float:left;
		  margin:8% 10.5%;
		  width:40px;
		  height:40px;
		  background:inherit;
		  background-color:rgba(151, 185, 252, 1);
		  
		  /* 兼容ie8 */
		  filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#FF97B9FC,endColorstr=#FF97B9FC);
		  
		  border:none;
		  border-radius:3px;
		  -moz-box-shadow:none;
		  -webkit-box-shadow:none;
		  box-shadow:none;
	}
	
	.home_div_text{
		margin:1% 0% 0% -6%;
		width:80px;
		height:15px;
		line-height:15px;
		font-size: 12px;
		text-align: center;
		
		/* 兼容ie8 */
		position:absolute;
		z-index:1;
	}
	
	.customHome{
		float:left;
		width: 30%;
		text-align: left;
		line-height: 30px;
	}
	
</style>

<!-- 动态echart -->
<script type="text/javascript">
	//销项检查汇总及超额付款检查汇总及已付款情况统计
	var option1 = null;
	//成本单账龄分析
	var option2 = null;
	//仓储核对汇总
	var option3 = null;
</script>

<!-- 动态echart -->
<script type="text/javascript">

	$(function(){
		
		option1 = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        //data:['销项成本单金额','销项已付款金额','销项不为0项目个数','销项已付款款项目个数']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            //data : ['随州','恩施','黄冈','咸宁','荆州','黄石','十堰','孝感','宜昌','武汉']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            name : '金额（万元）'
		        },{
		            type : 'value',
		            name : '个数（个）'
		        }
		    ],
		    series : [
		        {
		            //name:'销项成本单金额',
		            type:'bar',
		            //data:[50,50,50,50,50,50,50,50,50,50],
				    itemStyle:{
		                normal:{
		                    color:'rgba(0, 204, 255, 1)'
		                }
		            }
		        },
		        {
		            //name:'销项已付款金额',
		            type:'bar',
		            //stack: '广告',
		            //data:[100,100,100,100,100,100,100,100,100,100],
			        itemStyle:{
		                normal:{
		                    color:'rgba(151, 185, 252, 1)'
		                }
		            }
		        },
		        {
		            //name:'销项不为0项目个数',
		            type:'line',
		            //stack: '广告',
		            yAxisIndex:1,
		            //data:[5,6,7,8,9,8,7,6,5,5]
		        },
		        {
		            //name:'销项已付款款项目个数',
		            type:'line',
		            yAxisIndex:1,
		            //stack: '广告',
		            //data:[15,16,17,18,19,20,19,18,17,16]
		        }
		    ]
		};
	});
	
</script>

<!-- 动态echart -->
<script type="text/javascript">

	$(function(){
		
		option2 = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        //data:['销项成本单金额','销项已付款金额','销项不为0项目个数','销项已付款款项目个数']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            //data : ['随州','恩施','黄冈','咸宁','荆州','黄石','十堰','孝感','宜昌','武汉']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            name : '金额（万元）'
		        }
			],
		    series : [
		        {
		            //name:'销项成本单金额',
		            type:'bar',
		            //data:[50,50,50,50,50,50,50,50,50,50],
				    itemStyle:{
		                normal:{
		                    color:'rgba(0, 204, 255, 1)'
		                }
		            }
		        },
		        {
		            //name:'销项已付款金额',
		            type:'bar',
		            //stack: '广告',
		            //data:[100,100,100,100,100,100,100,100,100,100],
			        itemStyle:{
		                normal:{
		                    color:'rgba(151, 185, 252, 1)'
		                }
		            }
		        },
		        {
		            //name:'销项已付款金额',
		            type:'bar',
		            //stack: '广告',
		            //data:[100,100,100,100,100,100,100,100,100,100],
			        itemStyle:{
		                normal:{
		                    color:'rgba(151, 185, 252, 1)'
		                }
		            }
		        },
		        {
		            //name:'销项已付款金额',
		            type:'bar',
		            //stack: '广告',
		            //data:[100,100,100,100,100,100,100,100,100,100],
			        itemStyle:{
		                normal:{
		                    color:'rgba(151, 185, 252, 1)'
		                }
		            }
		        }
		    ]
		};
	});
	
</script>


<script type="text/javascript">

	/* 页面初始化 */
	$(function(){
		/* 默认关闭选择弹窗 */
		$('#customHomewin').window('close');
		$('#noticewin').window('close');
		$('#filewin').window('close');
		
		/* 快捷方式---start  */
		//自定义首页-弹窗复选框及Echart初始化
		//progressLoad();
		$.ajax({
                url:"${ctx}/home/initEchartInfo",
                type:"post",
                data:{},
                beforeSend: function () {
                	progressLoad(7);
    			},
                success:function(data){
                	var jsonObj = $.parseJSON(data);
                	
                	progressClose(7);
                	
                	if (jsonObj.success == true) {
                		for (var i = 0; i < jsonObj.obj.length; i++) {
                			$("input[type='checkbox'][name='customHome'][value='"+jsonObj.obj[i].custom_id+"']").attr('checked', 'checked');
						}
                		
                		//初始化echart
                    	initCustomCheckbox();
					}
                	
                },
                error:function(e){
                    parent.$.messager.alert('错误',"初始化图表异常",'error');
                }
            });  
		/* 快捷方式---end  */
		
		
		/* 公告/文件---start  */
		//公告树
		/* $('#noticett').tree({
			url: "${ctx}/home/getNoticeTree",
			method: 'post',
			onBeforeLoad: function(node, param){
				progressLoad(8);
		    },
		    onLoadSuccess: function(node, data){
		    	progressClose(8);
		    },
			loadFilter: function(data){
				return data.obj;
		    },
			onClick: function(node){
				
				//$('#noticewin').window('open');
				
				parent.$.modalDialog({
					title : "公告展示弹窗",
					width : 800,
					height : 500,
					href : "${ctx}/home/noticeView?id="+node.id
				});
				
			}
		}); */
		
		//样式调整
		var noticeGrid = $('#noticeGrid').datagrid({
			url : '${ctx}' + '/home/getNoticeGrid',
			striped : true,    //隔行变色
			fitColumn : true, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			singleSelect : true, //设置为 true，则只允许选中一行。
			//sortName : 'pay_amt',
			//sortOrder : 'desc',
			border : false,
			nowrap : false,
			showHeader : false,
			onClickRow : function(rowIndex,rowData){
				parent.$.modalDialog({
					title : "公告展示",
					width : 800,
					height : 500,
					href : "${ctx}/home/noticeView?id="+rowData.id
				});
			},
			columns : [[{
				width : '55%',
				field : 'notice_title',
				align:'left',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				}
			},{
				width : '45%',
				field : 'notice_time',
				align:'right',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				}
			}] ]
		});
		
		//文件树
		/* $('#filett').tree({
			url: "${ctx}/home/getFileTree",
			method: 'post',
			onBeforeLoad: function(node, param){
				progressLoad(9);
		    },
		    onLoadSuccess: function(node, data){
		    	progressClose(9);
		    },
			loadFilter: function(data){
				return data.obj;
		    },
			onClick: function(node){
				
				$('#fileName').html(node.text);
				
				//$("#fileName").textbox('setValue',node.text);
				//$('#fileId').textbox('setValue',node.id);
				
				$('#fileId').val(node.id);
				
				$('#filewin').window('open');
			}
		}); */
		
		//样式调整
		var fileGrid = $('#fileGrid').datagrid({
			url : '${ctx}' + '/home/getFileGrid',
			striped : true,    //隔行变色
			fitColumn : true, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			border : false,
			nowrap : false,
			showHeader : false,
			singleSelect : true, //设置为 true，则只允许选中一行。
			onClickRow : function(rowIndex,rowData){
				$('#fileName').html(rowData.file_name);
				$('#fileId').val(rowData.id);
				
				$('#filewin').window('open');
			},
			columns : [[{
				width : '55%',
				field : 'file_name',
				align:'left',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				}
			},{
				width : '45%',
				field : 'create_time',
				//align:'right',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				}
			}] ]
		});
		/* 公告/文件/更新日志---end  */
		
		
		/* 已付款供应商前10---start  */
		//已付款供应商前10
		/* $('#payOfftt').tree({
			url: "${ctx}/home/getPayOffTree",
			method: 'post',
			loadFilter: function(data){
				return data.obj;
		    },
			onClick: function(node){
				alert(node.text);  // alert node text property when clicked
			}
		}); */
		var payOffGrid = $('#payOffGrid').datagrid({
								url : '${ctx}' + '/home/getPayOffGrid',
								striped : true,    //隔行变色
								fitColumn : true, //为true的时候没有竖状滚动条
								fit : true, //适应整个屏幕
								singleSelect : true, //设置为 true，则只允许选中一行。
								nowrap : false,
								showHeader : false,
								//sortName : 'pay_amt',
								//sortOrder : 'desc',
								columns : [[{
									width : '60%',
									//title : '供应商名称',
									field : 'supplier_name',
									align:'left'
								},{
									width : '40%',
									//title : '金额',
									field : 'format_pay_amt',
									align:'right'
								}] ],
								/* onLoadSuccess: function(data){
									
							          var panel = $(this).datagrid('getPanel');   
							          var tr = panel.find('div.datagrid-body tr');
							          tr.css({
											"height": "100%"
							          });
							          tr.each(function(){
							              var td = $(this).children('td[field="supplier_name"]');   
							              td.children("div").css({
											"height": "17.3px",
											"line-height": "17.3px",
											//"font-size": "12px",
											//"padding": "5px",
							              });
							              td = $(this).children('td[field="format_pay_amt"]');
							              td.children("div").css({
							              	"height": "17.3px",
							              	"line-height": "17.3px",
											//"font-size": "12px",
											//"padding": "5px",
							              });
							          });  
							      } */
							});
		/* 已付款供应商前10---end  */
		
		
		/* 数据导入操作记录/更新日志---start  */
		//数据导入操作记录
		/* $('#dataImporttt').tree({
			url: "${ctx}/home/getDataImportTree",
			method: 'post',
			onBeforeLoad: function(node, param){
				progressLoad(10);
		    },
		    onLoadSuccess: function(node, data){
		    	progressClose(10);
		    },
			loadFilter: function(data){
				return data.obj;
		    },
			onClick: function(node){
				//alert(node.text);  // alert node text property when clicked
			}
		}); */
		
		//样式调整
		var dataImportGrid = $('#dataImportGrid').datagrid({
			url : '${ctx}' + '/home/getDataImportGrid',
			striped : true,    //隔行变色
			fitColumn : true, //为true的时候没有竖状滚动条
			fit : true, //适应整个屏幕
			border : false,
			nowrap : false,
			singleSelect : true, //设置为 true，则只允许选中一行。
			showHeader : false,
			columns : [[{
				width : '55%',
				field : 'file_name',
				align:'left',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				},
				formatter: function(value,row,index){
					return value;
				}
			},{
				width : '45%',
				field : 'log_time',
				align:'right',
				styler : function(value, row, index) {
					return 'border-left:0;border-right:0;';
				}
			}] ]
		});
		
		/* $('#updateContenttt').tree({
			url: "${ctx}/home/getUpdateContentTree",
			method: 'post',
			onBeforeLoad: function(node, param){
				progressLoad();
		    },
		    onLoadSuccess: function(node, data){
		    	progressClose();
		    },
			loadFilter: function(data){
				return data.obj;
		    },
			onClick: function(node){
				alert(node.text);  // alert node text property when clicked
			}
		}); */
		
		/* 数据导入操作记录---end  */
		
	})

	/* 快捷方式---start  */
	// 快捷方式跳转
	function addTab(title,url,icon,menu_id) {
		parent.addTab(title,url,icon,menu_id);
	};
	
	//自定义首页弹窗
	function customHomePage(){
		$('#customHomewin').window('open');
	}
	
	//确定按钮
	function customHomeConfirm(){
		
		if ($('input[type=checkbox]:checked').length > 2) {
			parent.$.messager.alert('提示',"该图表最多只能展示2个，请重新选择",'info');
			return false;
		}
		
		var customIds="";
		$.each($('input:checkbox:checked'),function(){
			//customIds.push(parseInt($(this).val()));
			//customIds.push($(this).val());
			customIds+=parseInt($(this).val());
		})
		
		$.ajax({
            url:"${ctx}/home/updateCustom",
            type:"post",
           	data:{'customIds':customIds},
            //data:JSON.stringify(customIds),
            beforeSend: function () {
            	progressLoad(10);
			},
            success:function(data){
            	progressClose(10);
            	
            	var jsonObj = $.parseJSON(data);
            	if (jsonObj.success == true) {
            		initCustomCheckbox();
            		$('#customHomewin').window('close');
				}
            	
            },
            error:function(e){
                parent.$.messager.alert('错误',"更新自定义异常",'error');
            }
        });
		
		//$('#customHomewin').window('close');
	}
	
	//初始化用户自定义选项
	function initCustomCheckbox(){
		var index = 1;
		$.each($('input:checkbox:checked'),function(){
			
				if ($(this).val() == "1") {
					//销项检查汇总
					getEchartData(['销项成本单金额','销项已付款金额','销项不为0项目个数','销项已付款款项目个数'],index,$(this).val(),$(this).next().text());
				} else if ($(this).val() == "2") {
					//销项检查汇总
					getEchartData(['超额付款项目金额','超额已付款项目金额','超额付款项目个数','超额已付款项目个数'],index,$(this).val(),$(this).next().text());
				} else if ($(this).val() == "3") {
					//已付款情况统计
					getEchartData(['已付款金额','签字未付金额','已付款项目个数','签字未付款项目个数'],index,$(this).val(),$(this).next().text());
				} else if ($(this).val() == "4") {
					//已销项检查
					//getEchartData(['销项成本单金额','销项已付款金额','销项不为0项目个数','销项已付款款项目个数'],index,$(this).val(),$(this).next().text());
				} else if ($(this).val() == "5") {
					//仓储数据核对
					//getEchartData(['销项成本单金额','销项已付款金额','销项不为0项目个数','销项已付款款项目个数'],index,$(this).val(),$(this).next().text());
				}
				
			index++;
			
		});
	}
	
	//获取echart数据
	function getEchartData(titleList,index,optionId,optionText){
		
		//$.ajaxSettings.async = false;//同步为false
		$.ajax({
            url:"${ctx}/home/getEchartData",
            //url:url,
            type:"post",
           	data:{'optionId':optionId},
           	beforeSend: function () {
            	progressLoad(optionId);
			},
            success:function(data){
            	progressClose(optionId);
            	
            	//$.messager.progress('close');// 关闭进度条
            	
            	//动态切换标题
    			$("#echarShow"+index+" span").html(optionText);
    			
    			//动态切换echart
    			var dom = document.getElementById("container"+index);
    			if (dom) {
    				var myChart = echarts.init(dom);
    				//var optionTemp = eval("option"+optionId);
    				
    				var optionTemp = null;
    				if (optionId == '1' || optionId == '2' || optionId == '3') {
    					optionTemp = option1;
    				} else if (optionId == '4') {
    					optionTemp = option2;
					}
    				
	            	var jsonObj = $.parseJSON(data);
	            	
	            	if (jsonObj.success == true) {
	            		
	            		//销项检查汇总及超额付款检查汇总及已付款情况统计
	            		if (optionId == '1' || optionId == '2' || optionId == '3') {
	            			//title
		            		optionTemp.legend.data = titleList;
		            		
		            		//分公司
		            		optionTemp.xAxis[0].data = jsonObj.obj[0];
		            		//个数
		            		optionTemp.series[0].data = jsonObj.obj[1];
		            		optionTemp.series[0].name = titleList[0];
		            		//个数
		            		optionTemp.series[1].data = jsonObj.obj[2];
		            		optionTemp.series[1].name = titleList[1];
		            		//金额
		            		optionTemp.series[2].data = jsonObj.obj[3];
		            		optionTemp.series[2].name = titleList[2];
		            		//金额
		            		optionTemp.series[3].data = jsonObj.obj[4];
		            		optionTemp.series[3].name = titleList[3];
		            		
		            		//成本单账龄分析
						} else if (optionId == '4') {
							
							/* //title
							optionTemp.legend.data = titleList;
							
							//分公司
							for (var i = 0; i < jsonObj.obj[0].length; i++) {
								if (jsonObj.obj[0]%5 == 0) {
									optionTemp.xAxis[0].data[i] = jsonObj.obj[0][i];
								}
							}
		            		
		            		//金额1
		            		optionTemp.series[0].data = jsonObj.obj[1];
		            		optionTemp.series[0].name = titleList[0];
		            		//金额2
		            		optionTemp.series[1].data = jsonObj.obj[2];
		            		optionTemp.series[1].name = titleList[1];
		            		//金额3
		            		optionTemp.series[2].data = jsonObj.obj[3];
		            		optionTemp.series[2].name = titleList[2];
		            		//金额4
		            		optionTemp.series[3].data = jsonObj.obj[4];
		            		optionTemp.series[3].name = titleList[3];
		            		//金额5
		            		optionTemp.series[4].data = jsonObj.obj[5];
		            		optionTemp.series[4].name = titleList[4]; */
		            		
		            		//仓储核对汇总
						} else if (optionId == '5') {
							
						}
	            		
	            		if (optionTemp && typeof optionTemp === "object") {
	    					myChart.setOption(optionTemp, true);
	    				}
	            		
					}
    			}
            },
            error:function(e){
                parent.$.messager.alert('错误',"获取图表异常",'error');
            }
        });
		//$.ajaxSettings.async = true;
		
	}
	
	//自定义首页取消按钮 
	function customHomeCancel(){
		$('#customHomewin').window('close');
	}
	/* 快捷方式---end  */
	
	
	//文件下载按钮 
	function filewinDown(){
		//$('#filewin').window('close');
		//var id=$("#fileId").textbox('getValue');
		
		//var id = $("#fileId").html();
		var id = $("#fileId").val();
		
		//var id=row.id;
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/files/queryFile",//路径  
			//dataType : "json",
			data : {
				id:id
			},//数据，这里使用的是Json格式进行传输  
			beforeSend: function () {
            	progressLoad(11);
			},
			success : function(result) {//返回数据根据结果进行相应的处理  
				//$.messager.progress('close');
				progressClose(11);
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
	/* 公告/文件/更新日志---end  */
	
	/* 更多彈窗---start  */
	function moreWin(flag){
		var titleText = "";
		var url = "";
		
		if (flag == 1) {
			url = "${ctx}/home/noticeWin";
			titleText = "公告";
		} else if (flag == 2) {
			url = "${ctx}/home/fileWin";
			titleText = "文件";
		} else if (flag == 3) {
			url = "${ctx}/home/dataImportWin";
			titleText = "数据导入操作记录";
		}
		
		parent.$.modalDialog({
			title : titleText,
			width : 830,
			height : 600,
			href : url
		});
		
	}
	/* 更多彈窗---end  */
</script>


<!-- 切换更多方法 -->
<script type="text/javascript">

	$(function(){
		$('#tt1').tabs({
			onSelect:function(title,index){
				if (index == 0) {
					$("#tb1 a").attr("onClick","moreWin(1)");
				} else if (index == 1) {
					$("#tb1 a").attr("onClick","moreWin(2)");
				}
			}
		});
	});
</script>


<!-- 重写加载条-->
<script type="text/javascript">

	/**
	 * 
	 * 重写进度条
	 * 
	 * **/
	function progressLoad(selectId){
	    $("<div id=\"datagrid-mask"+selectId+"\" class=\"datagrid-mask\" style=\"position:absolute;z-index: 9999;\"></div>").css({display:"block",width:"100%",height:$(document.body).height()}).appendTo("body");  
	    /*$(window).width()兼容ie8*/
	    
	    var winWidth = (($(window).width() - 190) / 2);
	    var winHeight = (($(window).height() - 45) / 2)
	    $("<div id=\"datagrid-mask-msg"+selectId+"\" class=\"datagrid-mask-msg\" style=\"position:absolute;z-index: 9999;\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:winWidth,top:winHeight});
	}
	
	/**
	 * 
	 * @requires jQuery
	 * 
	 * 页面加载加载进度条关闭
	 * **/
	function progressClose(selectId){
		$("#datagrid-mask"+selectId).remove();  
		$("#datagrid-mask-msg"+selectId).remove();
	}

</script>


</head>

<%-- <script type="text/javascript" src="${ctx}/jslib/echarts.js" charset="utf-8"></script> --%>

<body>
	<div id="main" title="首页" class="easyui-layout" data-options="fit:true,border:false" ondragstart='return false'>
	
	<!-- 文件下载 -->
	<div style="display:none"><form id="export" method="post"></form></div>
	
		<div style="overflow: auto; width: 100%;height: 100%;">
		
			<div id="cc1" class="easyui-layout" style="width:100%;height:50%;">
			
				<div title="
					<img id='u326_img' src='${ctx}/images/u326.png' style='position:absolute;left:0px;top:0px;width:24px;height:24px;border:0;'>
					<font style='color:red;margin-left:24px'>快捷操作</font>
					" data-options="region:'west',collapsible:false" style="width:25%;border:rgba(204, 204, 204, 1) solid 1px;">
					
					<div style="">
					   <div id="u326_div">
							<%-- <a href="#" id="" onClick="customHomePage()"><img id='u330_img' src='${ctx}/images/u330.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
							<a href="#" id="" onClick="customHomePage()"><img id='u330_img' src='${ctx}/images/u330.png' style='width:40px;height:40px;border:0;'></a>
							<div class="home_div_text">自定义首页</div>
						</div>
						
						<c:set var="menuList" value="${sessionInfo.treeList}"/>
						<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
						  <c:set var="childList" value="${menu.children}"/>
						     <c:forEach items="${childList}" var="child">
						     	<c:if test="${child.id == 'FN0301'}">
									<div id="u337_div">
										<%-- <a href="#" id="" onClick="addTab('订单测试','/orderTest/index','','FN0301')"><img id='u337_img' src='${ctx}/images/u337.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
										<a href="#" id="" onClick="addTab('订单测试','/orderTest/index','','FN0301')"><img id='u337_img' src='${ctx}/images/u337.png' style='width:40px;height:40px;border: 0'></a>
										<div class="home_div_text">订单测试</div>
									</div>
								</c:if>
								<c:if test="${child.id == 'FN0302'}">
									<div id="u344_div">
										<%-- <a href="#" id="" onClick="addTab('项目测试','/projectTest/index','','FN0302')"><img id='u344_img' src='${ctx}/images/u344.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
										<a href="#" id="" onClick="addTab('项目测试','/projectTest/index','','FN0302')"><img id='u344_img' src='${ctx}/images/u344.png' style='width:40px;height:40px;border: 0'></a>
										<div class="home_div_text">项目测试</div>
									</div>
								</c:if>
								<c:if test="${child.id == 'FN0703'}">
									<div id="u351_div">
										<%-- <a href="#" id="" onClick="addTab('超额付款检查','/overPay/index','','FN0703')"><img id='u351_img' src='${ctx}/images/u351.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
										<a href="#" id="" onClick="addTab('超额付款检查','/overPay/index','','FN0703')"><img id='u351_img' src='${ctx}/images/u351.png' style='width:40px;height:40px;border: 0'></a>
										<div class="home_div_text">超额付款检查</div>
									</div>
								</c:if>
								<c:if test="${child.id == 'FN0702'}">
									<div id="u358_div">
										<%-- <a href="#" id="" onClick="addTab('已销项检查','/cancelProjPay/index','','FN0702')"><img id='u358_img' src='${ctx}/images/u358.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
										<a href="#" id="" onClick="addTab('已销项检查','/cancelProjPay/index','','FN0702')"><img id='u337_img' src='${ctx}/images/u337.png' style='width:40px;height:40px;border: 0'></a>
										<div class="home_div_text">已销项检查</div>
									</div>
								</c:if>
								<c:if test="${child.id == 'FN0704'}">
									<div id="u365_div">
										<%-- <a href="#" id="" onClick="addTab('仓储数据核对','/storeCheck/index','','FN0704')"><img id='u365_img' src='${ctx}/images/u365.png' style='margin:7px;width:25px;height:25px;border:0;'></a> --%>
										<a href="#" id="" onClick="addTab('仓储数据核对','/storeCheck/index','','FN0704')"><img id='u365_img' src='${ctx}/images/u365.png' style='width:40px;height:40px;border: 0'></a>
										<div class="home_div_text">仓储数据核对</div>
									</div>
								</c:if>
						     </c:forEach>
						</c:forEach>
						
					</div>
					
				</div>
				
				<div id="echarShow1" data-options="region:'center'" style="padding:5px;border:rgba(204, 204, 204, 1) solid 1px;overflow: auto;">
					<span style="padding-left:20px;color:black;font-size: 15px;"></span>
					
					<div id="container1" style="height: 90%"></div>
					
				</div>
				
			    <div title="
					<img id='u241_img' src='${ctx}/images/u241.png' style='position:absolute;left:0px;top:0px;width:25px;height:28px;'>
					<font style='color:red;margin-left:25px;'>已付款供应商前10</font>
					" data-options="region:'east',collapsible:false" style="width:25%;border:rgba(204, 204, 204, 1) solid 1px;overflow: auto;">
					
					<!-- <ul style="color: black" id="payOfftt"></ul> -->
					
					<table id="payOffGrid" data-options="fit:true,border:false"></table>
					
			    </div>
			    
			</div>
			
			<div id="cc2" class="easyui-layout" style="width:100%;height:50%;border:0;">
			
				<div data-options="region:'west',collapsible:false" style="width:25%;height:100%;border:0;">
				
					<div id="tt1" class="easyui-tabs" data-options="fit:true,tools:'#tb1'">
					
						<div title="
									<img id='u175_img' src='${ctx}/images/u175.png' style='position:absolute;left:5px;top:0px;width:25px;height:23px;border:0;'>
									<font style='color:red;margin-left:20px;'>公告</font>
									" style="color:red;border:rgba(204, 204, 204, 1) solid 1px;overflow: auto;">
							
							<!-- <ul style="color: black" id="noticett"></ul> -->
							
							<table id="noticeGrid" data-options="fit:true,border:false"></table>
							
						</div>
						
						<div title="
									<img id='u182_img' src='${ctx}/images/u182.png' style='position:absolute;left:5px;top:5px;width:16px;height:18px;border:0;'>
									<font style='color:red;margin-left:15px;'>文件</font>
									" style="color:red;border:rgba(204, 204, 204, 1) solid 1px;overflow: auto;">
						
							<!-- <ul style="color: black" id="filett"></ul> -->
							
							<table id="fileGrid" data-options="fit:true,border:false"></table>
							
						</div>
						
					</div>
					
					<div id="tb1">
						<a href="#" class="easyui-linkbutton" plain="true" iconCls="" onClick="moreWin(1)">更多......</a>
					</div>
					
				</div>
				
				<div id="echarShow2" data-options="region:'center'" style="padding:5px;border:rgba(204, 204, 204, 1) solid 1px;">
					<span style="padding-left:20px;color:black;font-size: 15px;"></span>
					
					<div id="container2" style="height: 90%"></div>
					
				</div>
				
				<div data-options="region:'east',collapsible:false" style="width:25%;height:100%;border:0;">
			
					<div id="tt2" class="easyui-tabs" data-options="fit:true,tools:'#tb2'">
						
						<div title="
									<img id='u318_img' src='${ctx}/images/u318.png' style='position:absolute;left:5px;top:5px;width:20px;height:20px;border:0;'>
									<font style='color:red;margin-left:20px;'>数据导入操作记录</font>
									" style="color:black;border:rgba(204, 204, 204, 1) solid 1px;overflow: auto;">
							
							<!-- <ul style="color: black" id="dataImporttt"></ul> -->
							
							<table id="dataImportGrid" data-options="fit:true,border:false"></table>
					
						</div>
							
						<%-- <div title="
									<img id='u182_img' src='${ctx}/images/u182.png' style='position:absolute;left:5px;top:5px;width:16px;height:18px;'>
									<font style='color:red;font-size:8px;margin-left:20px;'>更新内容日志</font>
									" style="padding:10px;color:black;border:rgba(204, 204, 204, 1) solid 1px; overflow: auto;">
									
							<ul style="color: black" id="updateContenttt"></ul>
									
							<div>
							  <h3>更新内容日志：</h3>
							  <div>
							    <h4>2018-10-22</h4>
							    1、导入报错修改本地文件后直接点击确定还是报错BUG，修复为导入报错后需重新选择上传文件<br>
							    2、修复角色管理页面查询按钮无效BUG<br>
							    3、修复角色管理页面不能按照所属机构排序BUG<br>
							    4、修复新增用户的ID存在空格的BUG<br>
							    5、将系统管理员的密码设置复杂<br>
							    <h4>2018-10-24</h4>
							    1、修复仓储核对明细查询BUG<br>
							    2、修复仓储交易日志上传BUG
							    <h4>2018-10-29</h4>
							    1、功能优化，项目测试和订单测试中在"符合性测试"和"建设方式"中间插一列"本次付款比例"<br>
							    2、修复权限控制BUG，非系统管理员只能将自己拥有的权限功能开放给其他用户
							  </div>
							</div>
						</div> --%>
					</div>
					
					<div id="tb2">
						<a href="#" class="easyui-linkbutton" plain="true" iconCls="" onClick="moreWin(3)">更多......</a>
					</div>
					
				</div>
		    </div>
		    
		</div>
	</div>
	
	
	<!-- 以下为弹窗内容 -->
	<!-- 自定义首页弹窗 -->
	<div id="customHomewin" class="easyui-dialog" title="<font style='font-size:15px;margin: 43%;'>设置首页</font>" style="width:455px;height:200px" data-options="modal:true">
	
		<div class="easyui-layout" data-options="border:false" style="margin:20px 25px; height:50%;">
			<div data-options="region:'center',border:false" >
				<form action="">
					<c:set var="menuList" value="${sessionInfo.treeList}"/>
					<c:forEach items="${menuList}" var="menu" varStatus="">
					  <c:set var="childList" value="${menu.children}"/>
					     <c:forEach items="${childList}" var="child">
					     	<%-- <c:if test="${child.id == 'FN0301'}">
								<div class="customHome">
									<input type="checkbox" name="customHome" value="1"><span id="checkText1">已付款供应商排名</span>
								</div>
							</c:if> --%>
							<c:if test="${child.id == 'FN0801'}">
								<div class="customHome">
									<input type="checkbox" style="vertical-align: middle;" name="customHome" value="1"><span id="checkText2">销项检查汇总</span>
								</div>
							</c:if>
							<c:if test="${child.id == 'FN0802'}">
								<div class="customHome">
									<input type="checkbox" style="vertical-align: middle;" name="customHome" value="2"><span id="checkText3">超额付款检查汇总</span>
								</div>
							</c:if>
							<c:if test="${child.id == 'FN0803'}">
								<div class="customHome">
									<input type="checkbox" style="vertical-align: middle;" name="customHome" value="3"><span id="checkText6">已付款情况统计</span>
								</div>
							</c:if>
							<c:if test="${child.id == 'FN0804'}">
								<div class="customHome">
									<input type="checkbox" style="vertical-align: middle;" name="customHome" value="4"><span id="checkText4">成本单账龄分析</span>
								</div>
							</c:if>
							<c:if test="${child.id == 'FN0805'}">
								<div class="customHome">
									<input type="checkbox" style="vertical-align: middle;" name="customHome" value="5"><span id="checkText5">仓储核对汇总</span>
								</div>
							</c:if>
							
						</c:forEach>
					</c:forEach>
			
				</form>
			</div>
	    </div>
	    
	    <div class="easyui-layout" data-options="border:false" style="height:50%;margin: 0px 10px;">
	    	<div data-options="region:'west',border:false" style="width: 150px">&nbsp;</div>
			<div data-options="region:'center',border:false" style="">
				<a href="#" id="" style="position:absolute;width: 100px;height: 30px;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="customHomeConfirm()">确定</a>
			</div>
			<div data-options="region:'east',border:false" style="width: 150px">&nbsp;</div>
	    </div>
		
	</div>
	
	<!-- 文件下载弹窗 -->
	<div id="filewin" class="easyui-window" title="
												<div style='font-size:15px;margin:0% 30%;text-align:center;width:30%;'>文件下载</div>
												" style="padding:35px; width:450px;height:210px"  data-options="modal:true,collapsible:false,minimizable:false,maximizable:false">
		
		<div class="easyui-layout" data-options="border:false" style="height:70%;">
			<div data-options="region:'center',border:false">
				<div id="fileName" style="width: 100%;text-align: center;font-size: 15px;">
					<c:out value="${fileName}" default="${fileName}"/>
				</div>
				<!-- <input id="fileName" class="easyui-textbox" style="width: 100%;" name="fileName" readonly="readonly"> -->
			</div>
		</div>
				
		<!-- <div hidden="hidden" id="fileId" style="width: 100%;text-align: center;"></div> -->
		<input id="fileId" type="hidden">
				
		<div class="easyui-layout" data-options="border:false" style="height:30%;">
			<div data-options="region:'west',border:false" style="width: 130px">&nbsp;</div>
			<div data-options="region:'center',border:false" >
				<a href="#" id="" style="position:absolute;width: 100px;height: 30px;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="filewinDown()">下载</a>
			</div>
			<div data-options="region:'east',border:false" style="width: 130px">&nbsp;</div>
		</div>
		
		<!-- <div style="width:35%; margin: 10% 30%;">
			<a href="#" class="easyui-linkbutton" data-options="" style="width:100%;height: 20px;background-color: red" onclick="filewinDown()">下载</a>
			<a href="#" class="easyui-linkbutton" data-options="" style="width:48%;height: 20px;background-color: red" onclick="filewinCancel()">取消</a>
		</div> -->
	</div>
	
	
</body>
</html>