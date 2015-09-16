<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html  lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部署流程</title>
</head>
<body>
	<form action="${ctx}/workflow/deploy" method="post" enctype="multipart/form-data">
			<input type="file" name="file">
			<input type="submit" value="Submit">
	</form>
</body>
</html>