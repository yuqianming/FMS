<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- <c:set var="noticeContent" value="${noticeContent}"/> --%>

<title></title>
<head>
<style type="text/css">
	.td_right{
		text-align: right;
	}
</style>


</head>

	<!-- 公告弹窗 -->
	<%-- <div id="noticewin" class="easyui-window" title="
													<div style='font-size:15px;margin:0% 30%;text-align:center;width:30%;'>公告</div>
													" style="padding:5%;width:60%;height:18%" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false"> --%>
	
	
	<div id="noticeGrid" class="easyui-layout">	
	
		<table class="grid">
			<tr>
				<td class="td_right" style="width: 15%;">
					标题：
				</td>
				<td style="width: 85%;">
					<%-- <input id="noticeTitle" class="easyui-textbox" style="width: 100%;" name="" value="${noticeTitle}" readonly="readonly"> --%>
					<c:out value="${noticeTitle}" default="${noticeTitle}"/>
				</td>	
			</tr>
			<tr>
				<td class="td_right">
					发布日期：
				</td>
				<td>
					<%-- <input id="noticeTime" class="easyui-textbox" style="width:100%;" name="" value="${noticeTime}" readonly="readonly"> --%>
					<c:out value="${noticeTime}" default="${noticeTime}"/>
				</td>	
			</tr>
			<tr>
				<td class="td_right">发布部门：</td>
				<td>
					<%-- <input id="orgName" class="easyui-textbox" style="width:100%;" name="" value="${orgName}" readonly="readonly"> --%>
					<c:out value="${orgName}" default="${orgName}"/>
				</td>	
			</tr>
			<tr>
				<td class="td_right">发布人：</td>
				<td>
					<%-- <input id="orgName" class="easyui-textbox" style="width:100%;" name="" value="${orgName}" readonly="readonly"> --%>
					<c:out value="${userName}" default="${userName}"/>
				</td>	
			</tr>
			<tr>
				<td class="td_right">内容：</td>
				<td>
					<!-- <input class="easyui-textbox" data-options="multiline:true" id="noticeContent" style="width:300px;height:250px;"></input> -->
					<textarea id="noticeContent" style="width:100%;height:250px;border: 0;overflow: auto;cursor:default;" readonly="readonly">${noticeContent}</textarea>
				</td>
			</tr>
			<!-- <tr>
				<td colspan="2">
					<div style="margin-left: 35%;width:30%; ">
						<a href="#" class="easyui-linkbutton" style="width:100%;height: 20px;background-color: red" onclick="noticewinClose()">关闭</a>
					</div>
				</td>
			</tr> -->
		</table>
	
	</div>	