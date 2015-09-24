package com.quauq.yanzhenxing.activiti.vo;

/**
 * 待办任务vo
 * 
 * @author yanzhenxing
 * @createDate 2015年9月23日
 */
public class TaskVo {

	private String taskId;
	private String procInsId;
	private String procName;
	private boolean needClaim;
	private String applyUserId;

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public boolean getNeedClaim() {
		return needClaim;
	}

	public void setNeedClaim(boolean needClaim) {
		this.needClaim = needClaim;
	}

}
