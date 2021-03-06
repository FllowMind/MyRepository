package hebernate;
// Generated 2016-6-27 19:09:28 by Hibernate Tools 3.4.0.CR1

/**
 * Musicinfo generated by hbm2java
 */
public class Musicinfo implements java.io.Serializable {

	private MusicinfoId id;
	private String musicTitle;
	private String musicSize;
	private String musicUrl;
	private Integer hotIndex;
	private Integer albumId;

	public Musicinfo() {
	}

	public Musicinfo(MusicinfoId id) {
		this.id = id;
	}

	public Musicinfo(MusicinfoId id, String musicTitle, String musicSize, String musicUrl, Integer hotIndex,
			Integer albumId) {
		this.id = id;
		this.musicTitle = musicTitle;
		this.musicSize = musicSize;
		this.musicUrl = musicUrl;
		this.hotIndex = hotIndex;
		this.albumId = albumId;
	}

	public MusicinfoId getId() {
		return this.id;
	}

	public void setId(MusicinfoId id) {
		this.id = id;
	}

	public String getMusicTitle() {
		return this.musicTitle;
	}

	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}

	public String getMusicSize() {
		return this.musicSize;
	}

	public void setMusicSize(String musicSize) {
		this.musicSize = musicSize;
	}

	public String getMusicUrl() {
		return this.musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public Integer getHotIndex() {
		return this.hotIndex;
	}

	public void setHotIndex(Integer hotIndex) {
		this.hotIndex = hotIndex;
	}

	public Integer getAlbumId() {
		return this.albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

}
