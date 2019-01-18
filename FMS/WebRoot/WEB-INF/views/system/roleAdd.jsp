<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">

	$.extend($.fn.validatebox.defaults.rules, {
		noNullBlank: {  
	        validator: function (value, param) {
	        	value = $.trim(value);
	        	return value!="";
	        },  
	        message: '不能只输入空格！'  
	    }
	});

	var idsTmp = []; //页面选取值 临时集合
	
	var allIdsTmp = []; ////当前页面的所有值
	var resourceTree;
	var userTree;
	$(function() {
		
		//菜单树
		resourceTree = $('#resourceTree').tree({
			url : '${ctx}/roleAdmin/listMenuTree',
			parentField : 'pid',
			lines : true,
			checkbox : true,
			onBeforeLoad: function (node, param) {
                param.typeStr= $('input[name="type"]:checked ').val();
    		},
			onLoadSuccess : function(node, data) {
				//默认展开所有
				resourceTree.tree("expandAll"); 
			},
			onClick : function(node) {
				//alert("click");
				resourceTree.tree("toggle", node.target);  //展开节点
			},
			//onlyLeafCheck : true, //只在叶节点前显示 checkbox
			cascadeCheck : true  //级联选取
		});
		
		//可选的用户树
		userTree = $('#userTree').tree({
			url:'${ctx}/roleAdmin/listUserTree',
			queryParams: {org_id:'${sessionInfo.orgId}' },
			checkbox : true,
			onLoadSuccess : function(node, data) {
				
				//默认展开所有
				resourceTree.tree("expandAll"); 
				
				//保存当前页面所有值
				if (data && data.length > 0) {
					allIdsTmp = [];
					for ( var i = 0; i < data.length; i++) {
						allIdsTmp.push(data[i].id);
					}
				}
				
				//自动勾选角色对应的模块
				var ids = $.stringToList($('#userIds').val());
				if (ids.length > 0) {
					
					for ( var i = 0; i < ids.length; i++) {
						if (userTree.tree('find', ids[i])) {
							userTree.tree('check', userTree.tree('find', ids[i]).target);
						}
						
					}
				}
				if (idsTmp.length > 0) {
					for ( var i = 0; i < idsTmp.length; i++) {
						if (userTree.tree('find', idsTmp[i])) {
							userTree.tree('check', userTree.tree('find', idsTmp[i]).target);
						}
					}
				}
			}
		});
		

		$('#roleAddForm').form({
			url : '${ctx}/roleAdmin/add',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				var checknodes = resourceTree.tree('getChecked');
				var ids = [];
				if (checknodes && checknodes.length > 0) {
					for ( var i = 0; i < checknodes.length; i++) {
						ids.push(checknodes[i].id);
					}
				}
				$('#resourceIds').val(ids);
				
				//根据userTree设置userIds
				var checknodes1 = userTree.tree('getChecked');
				var ids1 = [];
				if (checknodes1 && checknodes1.length > 0) {
					for ( var i = 0; i < checknodes1.length; i++) {
						ids1.push(checknodes1[i].id);
					}
				}
				
				if (idsTmp.length > 0) {
					for ( var i = 0; i < idsTmp.length; i++) {
						
						var hasSame = hasSameIdUnitId(allIdsTmp, idsTmp[i]);
						var hasSelect = hasSelectIdUnitId(checknodes1, idsTmp[i]);
						
						if(hasSame){
							// 在当前页面所有值里面 并且 是页面选取值
							if(hasSelect){
								ids1.push(idsTmp[i]);
							}

						}else{
							// 没有在当前页面所有值里面
							ids1.push(idsTmp[i]);
						}
					}
				}
				
				$('#userIds').val(ids1);
				
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
					parent.$.messager.alert('提示', result.msg, 'info');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
		
		setTimeout(function(){
			initCombotree();
		},30);
	});
	
	function initCombotree()
	{
		$("#org_id").combotree({
			url:'${ctx}/departAdmin/departList',
			parentField : 'pid',
			lines : true,
			editable:false,
			onSelect:function(rec)
			{
				//parent.$.messager.alert('提示',rec.id, 'info');
				idsTmp=[];
				var params = {
			      org_id: rec.id
			    };
		        userTree.tree("options").queryParams = params;
		        userTree.tree('reload');
			}
		});
		var org_id='${sessionInfo.orgId}';
		$('#org_id').combotree('setValue',org_id);
	}
	
	function searchFun() {
		progressLoad();
		//条件查询的时候，把当前页面选取的值，放入临时集合
		saveCurrentSelectedUserListToTmp();
		
		//后台java代码赋值的时候，本身已经使用了一次解码，所以页面上编码两次
		var urlStr = '${ctx}/roleAdmin/listUserTreeBySearch?user_id='
				+ $('#user_id_search').val()
				+ "&user_name=" + $('#user_name_search').val();
		
		userTree.tree('options').url = urlStr;    
		userTree.tree('reload'); 
		progressClose();
	}
	
	function cleanFun() {
		
		//条件查询的时候，把当前页面选取的值，放入临时集合
		saveCurrentSelectedUserListToTmp();	
		progressLoad();
		$('#user_id_search').val('');
		$('#user_name_search').val('');	
		var urlStr = '${ctx}/roleAdmin/listUserTree';
		userTree.tree('options').url = urlStr;
		userTree.tree('reload'); 
		progressClose();
		
	}
	
	function saveCurrentSelectedUserListToTmp(){
		var checknodes = userTree.tree('getChecked');
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				idsTmp.push(checknodes[i].id);
			}
		}
	}
	
	function hasSameIdUnitId(allIds, id){
		var hasSame = false;
		
		if (allIds.length > 0) {
			for ( var i = 0; i < allIds.length; i++) {
				if(allIds[i] == id){
					hasSame = true;
					break;
				}
			}
		}
		
		return hasSame;
	}
	
	function hasSelectIdUnitId(nodes, id){
		var hasSelect = false;
		
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				if(nodes[i].id == id){
					hasSelect = true;
					break;
				}
			}
		}
		
		return hasSelect;
	}
</script>

<div class="easyui-layout" data-options="fit:true,border:false" >
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;" >
		<form id="roleAddForm" method="post">
		    <div style="width:35%;float:left">
		      	<table class="grid">
					<tr>
						<td nowrap>角色名称</td>
						<td><input name="role_name" style="width:200px;" type="text" placeholder="请输入角色名称" class="easyui-validatebox" data-options="required:true" value="" maxlength="20"></td>
					</tr>
					<tr>
						<td>所属机构</td>
						<td><input id="org_id" name="org_id" style="width:200px;" class="easyui-combobox" class="easyui-validatebox" data-options="required:true"></td>
					</tr>
					<tr>
						<td>备注</td>
						<td><textarea name="remark" rows="5" style="width:200px;" maxlength="100"></textarea></td>
					</tr>
				  <tr>	
					<td nowrap>菜单列表</td>
					<td>
					  <ul id="resourceTree" style="overflow:auto;height:230px;"></ul>
					  <input id="resourceIds" name="resourceIds" type="hidden" />
					</td>
				  </tr>
				</table>
		    </div>
		    <div style="display:inline-block;width:65%">
		       <table class="grid">
		         <tr>
		           <td>
		              <div  style="line-height:50px;text-align:center;">
		               &nbsp;用户ID:&nbsp;<input style="width:100px;" id="user_id_search" name="user_id" placeholder="请输入用户ID"/>&nbsp;&nbsp;
					   &nbsp;用户姓名:&nbsp;<input style="width:100px;" id="user_name_search" name="user_name" placeholder="请输入用户姓名"/>&nbsp;&nbsp;&nbsp;&nbsp;
					   <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchFun();">查询</a>&nbsp;&nbsp;
					   <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="cleanFun();">清空</a>
		              </div>
		           </td>
		         </tr>
		         <tr>
		           <td>
		              <ul id="userTree" style="overflow:auto;height:335px;-webkit-padding-start: 20px;-webkit-margin-before: 0;-webkit-margin-after: 0;"></ul>
					  <input id="userIds" name="userIds" type="hidden"/>
		           </td>
		         </tr>
			  </table>
		    </div>
		</form>
	</div>
</div>