<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">

	var resourceTree;
	var userTree;
	
	$(function() {
		
		resourceTree = $('#resourceTree').tree({
			url : '${ctx}/roleAdmin/listMenuTree',
			parentField : 'pid',
			lines : true,
			checkbox : true,
			onLoadSuccess : function(node, data) {
				
				//查看页面，禁用勾选点击
				$(this).find('span.tree-checkbox').unbind().click(function(){ 
					return false;
				});
				
				//默认展开所有
				resourceTree.tree("expandAll"); 
				
				//自动勾选角色对应的模块
				var ids = $.stringToList($('#resourceIds').val());;
				if (ids.length > 0) {
					for ( var i = 0; i < ids.length; i++) {
						if (resourceTree.tree('find', ids[i])) {
							resourceTree.tree('check', resourceTree.tree('find', ids[i]).target);
						}
					}
				}				
			},
			onClick : function(node) {
				//alert("click");
				resourceTree.tree("toggle", node.target);  //展开节点
			},
			//onlyLeafCheck : true, //只在叶节点前显示 checkbox
			cascadeCheck : true  //级联选取
		});
		
		userTree = $('#userTree').tree({
			url : '${ctx}/roleAdmin/listUserTreeByRole?role_id='+ $('#role_id').val(),
			onLoadSuccess : function(node, data) {
				
				//查看页面，禁用勾选点击
				$(this).find('span.tree-checkbox').unbind().click(function(){ 
					return false;
				});
				
				//默认展开所有
				userTree.tree("expandAll"); 
				
				//自动勾选角色对应的模块
				var ids = $.stringToList($('#userIds').val());;
				if (ids.length > 0) {
					for ( var i = 0; i < ids.length; i++) {
						if (userTree.tree('find', ids[i])) {
							userTree.tree('check', userTree.tree('find', ids[i]).target);
						}
					}
				}				
			}
		});

	});

</script>

<div class="easyui-layout" data-options="fit:true,border:false" >
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;" >
			<div style="width:35%;float:left">
		      	<table class="grid">
					<tr>
						<td nowrap>角色名称<input id="role_id" type="hidden" value="${role.role_id}"></td>
						<td>${role.role_name}</td>
					</tr>
					<tr>
						<td>所属机构</td>
						<td>${role.org_name}</td>
					</tr>
					<tr>
						<td>备注</td>
						<td>${role.remark}</td>
					</tr>
					<tr>
					  <td>创建时间</td>
					  <td><fmt:formatDate value="${role.create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				  </tr>
				  <tr>	
					<td nowrap>菜单列表</td>
					<td>
					  <ul id="resourceTree" style="overflow:auto;height:290px;"></ul>
					  <input id="resourceIds" name="resourceIds" type="hidden" value="${role.resourceIds}"/>
					</td>
				  </tr>
				</table>
		    </div>
		    <div style="display:inline-block;width:65%">
		       <table class="grid">
		         <tr>
		           <td>
		              <ul id="userTree" style="overflow:auto;height:425px;-webkit-padding-start: 20px;-webkit-margin-before: 0;-webkit-margin-after: 0;"></ul>
					  <input id="userIds" name="userIds" type="hidden" value="${role.userIds}"/>
		           </td>
		         </tr>
			  </table>
		    </div>
	</div>
</div>