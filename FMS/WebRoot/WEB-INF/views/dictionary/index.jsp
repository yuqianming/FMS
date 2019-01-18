<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
  .nv{
     list-style-type:none;
     -webkit-padding-start: 0px;
  }
  .nv li{
   line-height:35px;
   border: 1px solid #CCCCCC;
   text-align:center;
   margin: 0 0 -1px 0px;
   cursor: pointer;
   width:220px;
   //margin-left:-42px;
  }
  li:hover { 
    background-color:#CC0000;
    color:#FFF;
    opacity:0.2;
  }
</style>

</head>
<body>
  <div class="easyui-layout" data-options="fit:true,border:false">
     <div data-options="region:'west',border:false" style="width:250px;height:100%; overflow: hidden;border: 1px solid #CCCCCC;">
        <div style="line-height:20px;width:220px;margin:15px;text-align:center;font-size:14px;background:#c3bfbf;">字典类型</div>
        <div style="width:220px;margin: 30px 15px 15px 15px;">
           <ul class="nv" >
			   <c:set var="menuList" value="${sessionInfo.childList}"/>
			   <c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
			           <li onClick="showPage('${menu.url}','${menu.id}')"><a><span>${menu.text}</span></a></li>
			   </c:forEach>
			</ul>
        </div>
     </div>
     <div data-options="region:'center',fit:false,border:false" style="blackground:blue;">
       <iframe id="itemIframe" style="border:0;width:100%;height:100%;"></iframe>
     </div>
  </div>
  
  
  
</body>
<script type="text/javascript">
  function showPage(url,menu_id)
  {
	  src='${ctx}' + url + '?menu_id='+menu_id;
	  $("#itemIframe").attr("src",src);
	  //$("#itemIframe").height(window.innerHeight-5);
	  //$(this).siblings().removeClass("active");
	  //$(this).addClass("active");
  }
  $(".nv").on("click","li",function(){
	  $(this).siblings().removeClass("active");
	  $(this).addClass("active");
  })
</script>
</html>