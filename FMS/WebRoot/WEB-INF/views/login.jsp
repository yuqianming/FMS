<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工程财务管理辅助系统</title>
<link rel="icon" href="${ctx}/style/css/project.ico" type="image/x-icon">
<style type="text/css">
  .input_login{
    width: 519px;
    height: 45px;
    font-family: '微软雅黑';
    font-weight: 400;
    font-style: normal;
    font-size: 16px;
    text-decoration: none;
    color: #000000;
    text-align: center;
    border-color: transparent;
    outline-style: none;
  }
  #logo{
  display:block;
  float:left;
  }
  .titleName{
  	width:100%;
  	text-align:center;
  	line-height:100px;
  	font-size:32px;
  	font-weight: 700;
  	padding-right:20%;
  	box-sizing:border-box;
  }
  .bottomTitle{
  display:block;
  width:100%;
  text-align:center;
  }
  .user_id,.password{
  	width: 585px;
  	position:relative;
  	overflow: hidden;
  	padding-top:25px;
  	left: 5px;
  }
  #user_id,#password{
  width:80%;
  float:left;
  position:relative;
  display:block;
  margin-left:10%;
  background-color:#ffffff!important;
  opacity: 1;
  z-index:9;
  border:0px;
  }
  .userimg{
   width:32px;
   height:32px;
   float:left;
   display:block;
   position:absolute;
   left:15%;
   top:33px;
   
   z-index:10;
  }
  
  .bottomDiv{
	  width:140px;
	  height:40px;
	  text-align:center;
	  line-height:40px;
	  font-size:16px;
	  position:absolute;
	  top:200px;
	  left: 300px;
	  margin-left:-70px;
	  background:#fff;
	  border-radius: 5px;
	  background-color: rgba(255, 255, 255, 1);
	  border-width: 1px;
      border-style: solid;
      border-color: rgba(121, 121, 121, 1);
      cursor: pointer;
  }
  
  .background{
    width:100%;
    height:100%;
    overflow:hidden;
    background-image: url('../style/images/background.png');
    background-repeat: no-repeat;
    background-size:cover;
    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
        src='../style/images/background.png',
        sizingMethod='scale');
    -ms-filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(
        src='../style/images/background.png',
        sizingMethod='scale');
  }
</style>
<script type="text/javascript" src="${ctx}/jslib/easyui1.4.2/jquery.min.js"  charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/jslib/easyui1.4.2/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript">
$(function() {
	$('#loginform').form({
	    url:'${ctx}/admin/login',
	    onSubmit : function() {
	    	//progressLoad();
			var isValid = $(this).form('validate');
			if(!isValid){
				progressClose();
			} 
			return isValid;
		},
	    success:function(result){
	    	result = $.parseJSON(result);
	    	//progressClose();
	    	if (result.success) {
	    		window.location.href='${ctx}/admin/index';
	    	}else{
	    		$.messager.show({
	    			title:'提示',
	    			msg:'<div class="light-info"><div class="light-tip icon-tip"></div><div>'+result.msg+'</div></div>',
	    			showType:'show'
	    		});
	    	}
	    }
	});
});

function submitForm(){
	$('#loginform').submit();
}

document.onkeydown=function(e)
{
	var event = e||window.event;
	var code = event.keyCode||event.which||event.charCode
	if(code == 13)
	{
		$('#loginform').submit();
	}
}
</script>
</head>
<body class="easyui-layout" ondragstart='return false' onselectstart ='return false' onselect='document.selection.empty()'>
    <div data-options="region:'north'" style="width:100%;height:100px;overflow:hidden;">
		<img id="logo" style="width:300px;height:80px;padding-left:50px;padding-top:5px" src="${ctx}/style/images/logo.jpg"/>
		<div class="titleName">
                                       中国铁塔股份有限公司湖北分公司
        </div>
	</div><!-- style="width:100%;height:100%;overflow:hidden;background-image: url('../style/images/background.png');background-repeat: no-repeat;background-size:cover" -->
	<div data-options="region:'center'" class="background">
	  <!--<img style="width:100%;height:100%;" src="${ctx}/style/images/background.png"/>-->
	  <div style="position: absolute;left:400px;top:-40px;width: 721px;height: 94px;font-family: '微软雅黑 Bold', '微软雅黑';font-weight: 700;font-style: normal;font-size: 72px;color: #D70C18;">
	    <p><span>工程财务管理辅助系统</span></p>
	  </div>
	  <div style="position: absolute;
    left: 500px;
    top: 180px;
    width: 585px;
    height: 257px;
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(121, 121, 121, 1);
    border-radius: 0px;
    -moz-box-shadow: none;
    -webkit-box-shadow: none;
    box-shadow: none;
    font-family: '微软雅黑';
    font-weight: 400;
    font-style: normal;">
    <div style="width:100%;height:100%;position: absolute;top:0;left:0;background-color: rgba(242, 242, 242, 1);"> </div>
    	 <form id="loginform" method="post">
    	   	 <div class="user_id">
    	    <img class="userimg" src="${ctx}/style/images/u12.png" />
	        <input id="user_id" name="user_id" class="input_login" type="text" value="" />
	      </div>
	      <div class="password">
	         <img class="userimg" src="${ctx}/style/images/u14.png" />
	         <input id="password" name="password" class="input_login" type="password" value="" />
	      </div>
	       <div class="bottomDiv" onclick="submitForm()" >登录</div>
	     </form>
	  </div>
	</div>

    <div data-options="region:'south'" style="width:100%;height:50px;text-align:cneter;">
        <p><span class="bottomTitle">®Copyright 版权相关信息。</span></p>  
	</div>
</body>
</html>