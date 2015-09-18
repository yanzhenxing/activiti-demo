/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.quauq.yanzhenxing.activiti.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色实体
 * 
 * @author yanzhenxing
 * @createDate 2015年9月17日
 */
@Entity
@Table(name = "role")
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;// 角色id

	@Column(name = "name")
	private String name; // 角色名称

	@Column(name = "type")
	private String type;// 权限类型

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "del_flag")
	private boolean delFlag;
	
	public Role() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}

}
