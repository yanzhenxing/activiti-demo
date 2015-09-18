<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html  lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程列表</title>
</head>
<body>
	<table>
		<thead>
			<tr>
				<td>流程key</td>
				<td>版本号</td>
				<td></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processDefinitions }" var="processDefinition">
			<tr>
				<td>${processDefinition.key }</td>
				<td>${processDefinition.version }</td>
				<td><a href="${ctx }/workflow/process/start?"></a></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>