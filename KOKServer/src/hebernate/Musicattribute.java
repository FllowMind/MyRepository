package hebernate;
// Generated 2016-6-27 19:09:28 by Hibernate Tools 3.4.0.CR1

/**
 * Musicattribute generated by hbm2java
 */
public class Musicattribute implements java.io.Serializable {

	private String musicId;
	private String language;
	private String style;
	private int year;
	private String mood1;
	private String mood2;
	private String mood3;
	private String scene1;
	private String scene2;
	private String scene3;
	private String scene4;
	private String scene5;

	public Musicattribute() {
	}

	public Musicattribute(String musicId, String language, String style, int year, String mood1, String scene1) {
		this.musicId = musicId;
		this.language = language;
		this.style = style;
		this.year = year;
		this.mood1 = mood1;
		this.scene1 = scene1;
	}

	public Musicattribute(String musicId, String language, String style, int year, String mood1, String mood2,
			String mood3, String scene1, String scene2, String scene3, String scene4, String scene5) {
		this.musicId = musicId;
		this.language = language;
		this.style = style;
		this.year = year;
		this.mood1 = mood1;
		this.mood2 = mood2;
		this.mood3 = mood3;
		this.scene1 = scene1;
		this.scene2 = scene2;
		this.scene3 = scene3;
		this.scene4 = scene4;
		this.scene5 = scene5;
	}

	public String getMusicId() {
		return this.musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMood1() {
		return this.mood1;
	}

	public void setMood1(String mood1) {
		this.mood1 = mood1;
	}

	public String getMood2() {
		return this.mood2;
	}

	public void setMood2(String mood2) {
		this.mood2 = mood2;
	}

	public String getMood3() {
		return this.mood3;
	}

	public void setMood3(String mood3) {
		this.mood3 = mood3;
	}

	public String getScene1() {
		return this.scene1;
	}

	public void setScene1(String scene1) {
		this.scene1 = scene1;
	}

	public String getScene2() {
		return this.scene2;
	}

	public void setScene2(String scene2) {
		this.scene2 = scene2;
	}

	public String getScene3() {
		return this.scene3;
	}

	public void setScene3(String scene3) {
		this.scene3 = scene3;
	}

	public String getScene4() {
		return this.scene4;
	}

	public void setScene4(String scene4) {
		this.scene4 = scene4;
	}

	public String getScene5() {
		return this.scene5;
	}

	public void setScene5(String scene5) {
		this.scene5 = scene5;
	}

}
