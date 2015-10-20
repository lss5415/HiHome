package com.zykj.hihome.data;

public class Anniversary {
	
	/**
	 * 纪念日
	 */
		private static final long serialVersionUID = 1L;

		private String id;
		private String title;// 纪念日名称
		private String content;// 纪念日内容
		private String date;// 纪念日日期
		private String imgsrc;// 纪念日图片
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getImgsrc() {
			return imgsrc;
		}
		public void setImgsrc(String imgsrc) {
			this.imgsrc = imgsrc;
		}

		
		

}
