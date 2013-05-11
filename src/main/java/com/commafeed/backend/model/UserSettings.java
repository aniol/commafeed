package com.commafeed.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERSETTINGS")
@SuppressWarnings("serial")
public class UserSettings extends AbstractModel {

	public enum ReadingMode {
		all, unread
	}

	public enum ReadingOrder {
		asc, desc
	}

	public enum ViewMode {
		title, expanded
	}

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReadingMode readingMode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReadingOrder readingOrder;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ViewMode viewMode;

	@Column(length = 4)
	private String language;

	private boolean showRead;
	private boolean scrollMarks;
	private boolean socialButtons;

	@Lob
	@Column(length = Integer.MAX_VALUE)
	private String customCss;

	public ReadingMode getReadingMode() {
		return readingMode;
	}

	public void setReadingMode(ReadingMode readingMode) {
		this.readingMode = readingMode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCustomCss() {
		return customCss;
	}

	public void setCustomCss(String customCss) {
		this.customCss = customCss;
	}

	public ReadingOrder getReadingOrder() {
		return readingOrder;
	}

	public void setReadingOrder(ReadingOrder readingOrder) {
		this.readingOrder = readingOrder;
	}

	public boolean isShowRead() {
		return showRead;
	}

	public void setShowRead(boolean showRead) {
		this.showRead = showRead;
	}

	public boolean isSocialButtons() {
		return socialButtons;
	}

	public void setSocialButtons(boolean socialButtons) {
		this.socialButtons = socialButtons;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	public void setViewMode(ViewMode viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isScrollMarks() {
		return scrollMarks;
	}

	public void setScrollMarks(boolean scrollMarks) {
		this.scrollMarks = scrollMarks;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
