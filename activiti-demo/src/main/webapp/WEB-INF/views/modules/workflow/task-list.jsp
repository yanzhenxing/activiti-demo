<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/global.jsp"%>
	<%@ include file="/WEB-INF/views/include/meta.jsp" %>
	<%@ include file="/WEB-INF/views/include/include-base-styles.jsp" %>
	<title>待办任务列表</title>

	<script src="${ctx }/static/js/common/jquery.js" type="text/javascript"></script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#message').hide('slow');
		}, 5000);
		</script>
	</c:if>
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>任务ID</th>
				<th>任务名称</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>任务创建时间</th>
				<th>办理人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tasks }" var="task">
				<tr>
					<td>${task.id }</td>
					<td>${task.name }</td>
					<td>${task.processInstanceId }</td>
					<td>${task.processDefinitionId }</td>
					<td>${task.createTime }</td>
					<td>${task.assignee }</td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="btn" href="${ctx}/workflow/task/claim/${task.id}"><i class="icon-eye-open"></i>签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<a class="btn" href="${ctx}/workflow/task/handle/${task.id}"><i class="icon-user"></i>办理</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>