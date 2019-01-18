<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
<link rel="stylesheet" href="${ctx}/style/css/menu.css" type="text/css">
<style>
#u22_img {
    position: absolute;
    left: 50px;
    top: 10px;
    width: 268px;
    height: 80px;
}
#u25 {
    position: absolute;
    left: 498px;
    top: 0px;
    width: 321px;
    white-space: nowrap;
    font-family: '微软雅黑 Bold', '微软雅黑';
    font-weight: 700;
    font-style: normal;
    color: #D70C18;
    font-size: 32px;
    text-align: left;
}
</style>
<script type="text/javascript">
	var index_layout;
	var index_tabs;
	var index_tabsMenu;
	var layout_west_tree;
	var layout_west_tree_url = '';
	
	var sessionInfo_userId = '${sessionInfo.id}';
	if (sessionInfo_userId) {//如果没有登录,直接跳转到登录页面
		//layout_west_tree_url = '${ctx}/resource/tree';
	}else{
		window.location.href='${ctx}/admin/index';
	}
	$(function() {
		$("#editDialog").dialog('close');
		index_layout = $('#index_layout').layout({
			fit : true
		});
		
		index_tabs = $('#index_tabs').tabs({
			fit : true,
			border : false,
			tools : [{
				iconCls : 'icon-home',
				handler : function() {
					index_tabs.tabs('select', 0);
				}
			}, {
				iconCls : 'icon-del',
				handler : function() {
					var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
					var tab = index_tabs.tabs('getTab', index);
					if (tab.panel('options').closable) {
						index_tabs.tabs('close', index);
					}
				}
			} ]
		});
	});
	
	function addTab(title,url,icon,menu_id) {
		var iframe = '<iframe class="itemIframe" src="${ctx}' + url + '?menu_id='+menu_id+'" frameborder="0" style="border:0;width:100%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : title,
			closable : true,
			iconCls : icon,
			content : iframe,
			border : false,
			fit : true
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
		$(".itemIframe").height(window.innerHeight-170);
	}
	
	function logout(){
		$.messager.confirm('提示','确定要退出?',function(r){
			if (r){
				progressLoad();
				$.post( '${ctx}/admin/logout', function(result) {
					if(result.success){
						progressClose();
						window.location.href='${ctx}/admin/index';
					}
				}, 'json');
			}
		});
	}
	
	function resetPassword()
	{
		$("#editDialog").dialog('open');
	}
	
	function editFun()
	{
		var newPassword=$("#newPassword").val();
		var newPassword2=$("#newPassword2").val();
		if(newPassword!=newPassword2)
		{
			parent.$.messager.alert('提示','新密码与确认密码不一致！','info');
			return;
		}
		progressLoad();
		$.ajax({
			type : "POST", //提交方式  
			url : "${ctx}/admin/resetPassword",//路径  
			//dataType : "json",
			data : {
				user_id:$("#user_id").val(),
				password:$("#oldPassword").val(),
				newPassword:$("#newPassword").val()
			},//数据，这里使用的是Json格式进行传输  
			success : function(result) {//返回数据根据结果进行相应的处理  
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示',result.msg,'info');
					$('#editDialog').dialog('close');
					dataGrid.datagrid('load');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	}
	
	function editCancel()
	{
		$('#editDialog').dialog('close');
	}
</script>
</head>
<body>
<div id="editDialog" style="height:350px;width:400px;" class="easyui-dialog" 
			data-options="title:'修改密码',
						modal:true,
						buttons:[{
							text:'确定',
							iconCls:'icon-ok',
							handler:function(){editFun()}
						},{
							text:'取消',
							iconCls:'icon-no',
							handler:function(){editCancel()}
						}]">
	     <form id="addForm" enctype="multipart/form-data" method="post">
			<table class="grid">
				<tr>
					<td nowrap>用户ID:</td>
					<td><input id="user_id" name="user_id" style="width:200px;" type="text" disabled="disabled" value="${sessionInfo.id}"></td>	
				</tr>
				<tr>
					<td>原密码:</td>
					<td><input id="oldPassword" name="oldPassword" style="width:200px;" type="password"  class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>	
				</tr>
				<tr>
					<td>新密码:</td>
					<td><input id="newPassword" name="newPassword" style="width:200px;" type="password"  class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>		
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input id="newPassword2" name="newPassword2" style="width:200px;" type="password"  class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>		
				</tr>
			</table>
		</form>
     </div>

	<div style=" overflow: hidden;height:100px;" >
		<img id="u22_img" class="img " src="${ctx}/style/images/logo.jpg"/>
		<div id="u25" class="text" style="visibility: visible;">
        <p><span>工程财务管理辅助系统</span></p>
          </div>
		
		<img id="u12_img" style="position: absolute;left: 1003px;top: 34px;" src="${ctx}/style/images/u12.png"/>
		<div style="position: absolute;left: 1043px;top: 34px;text-align:center;line-height: 32px;">
			${sessionInfo.orgName}&nbsp;&nbsp;&nbsp;&nbsp;${sessionInfo.loginname}
    	</div>
    	<div style="float:right;padding-right:20px;padding-top:15px;">
    	    <ul style="list-style:none;">
		      <li style="padding-bottom:5px;"><a href="javascript:void(0)" onclick="resetPassword()">修改密码</a></li>
		      <li><a href="javascript:void(0)" onclick="logout()">安全退出</a></li>
		    </ul>
    	</div>
	</div>
	<div id="index_layout">
		<!--<div id="daohang" data-options="region:'north'" style="height: 40px;background-color: rgba(0, 0, 0, 1);overflow: hidden;overflow-x:auto;color: #FFFFFF;">-->
			<!--<div class="well well-small" style="padding: 5px 5px 5px 5px;">
				<ul id="layout_west_tree"></ul>
			</div>-->
		<div id="daohang" class="menu radius" data-options="region:'north'" >
			<ul class="nav">
			   <c:set var="menuList" value="${sessionInfo.treeList}"/>
			   <c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
			     <li class="tab dropdown"><a class="tablink arwlink">${menu.text}</a>
			      <c:set var="childList" value="${menu.children}"/>
			       <ul style="list-style-type:none;-webkit-padding-start: 0px;">
			         <c:forEach items="${childList}" var="child">
			           <li><a><span onClick="addTab('${child.text}','${child.url}','','${child.id}')">${child.text}</span></a></li>
			         </c:forEach>
			       </ul>
			     </li>
			   </c:forEach>
			</ul>
		</div>
		<div data-options="region:'center'" style="overflow: hidden;">
			<div id="index_tabs" style="overflow: hidden;">
				<div title="首页" data-options="border:false" style="overflow: hidden;">
					<div style="padding:10px 0 10px 10px">
						<h2>系统介绍</h2>
						<div class="light-info">
							<div class="light-tip icon-tip"></div>
							<div>工程财务管理辅助系统。</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		        
	</div>
	<!--[if lte IE 7]>
	<div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet Explorer 8</a> 或以下浏览器：
	<a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
	<![endif]-->
	
	<style>
		/*ie6提示*/
		#ie6-warning{width:100%;position:absolute;top:0;left:0;background:#fae692;padding:5px 0;font-size:12px}
		#ie6-warning p{width:960px;margin:0 auto;}
	</style>
</body>
</html>