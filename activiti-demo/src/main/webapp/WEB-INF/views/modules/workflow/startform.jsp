<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html  lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>启动流程</title>
</head>
<body>
	<form action="${ctx}/workflow/process/start" method="post">
			<input type="hidden" name="key" value="${key}">
			请假开始时间：<input type="text" name="startDate"><br>
			请假结束时间：<input type="text" name="endDate"><br>
			请假理由：<input type="text" name="reason"><br>
			<input type="submit" value="申请">
	</form>
</body>
</html>