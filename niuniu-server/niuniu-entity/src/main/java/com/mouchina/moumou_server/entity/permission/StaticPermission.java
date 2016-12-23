package com.mouchina.moumou_server.entity.permission;

import java.io.Serializable;

public class StaticPermission implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8263274100687260222L;
	private Long id;
	private String permissionCode;
	private String permissionName;
	private String permissionAction;
	private String permissionNote;
	private Long permissionParent;
	private String permissionType;
	private byte permissionMenu;
	private String permissionIcon;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionAction() {
		return permissionAction;
	}

	public void setPermissionAction(String permissionAction) {
		this.permissionAction = permissionAction;
	}

	public String getPermissionNote() {
		return permissionNote;
	}

	public void setPermissionNote(String permissionNote) {
		this.permissionNote = permissionNote;
	}

	public Long getPermissionParent() {
		return permissionParent;
	}

	public void setPermissionParent(Long permissionParent) {
		this.permissionParent = permissionParent;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public byte getPermissionMenu() {
		return permissionMenu;
	}

	public void setPermissionMenu(byte permissionMenu) {
		this.permissionMenu = permissionMenu;
	}

	public String getPermissionIcon() {
		return permissionIcon;
	}

	public void setPermissionIcon(String permissionIcon) {
		this.permissionIcon = permissionIcon;
	}
}