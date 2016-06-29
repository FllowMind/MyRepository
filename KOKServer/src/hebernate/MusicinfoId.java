package hebernate;
// Generated 2016-6-27 19:09:28 by Hibernate Tools 3.4.0.CR1

/**
 * MusicinfoId generated by hbm2java
 */
public class MusicinfoId implements java.io.Serializable {

	private String musicId;
	private int artistId;

	public MusicinfoId() {
	}

	public MusicinfoId(String musicId, int artistId) {
		this.musicId = musicId;
		this.artistId = artistId;
	}

	public String getMusicId() {
		return this.musicId;
	}

	public void setMusicId(String musicId) {
		this.musicId = musicId;
	}

	public int getArtistId() {
		return this.artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MusicinfoId))
			return false;
		MusicinfoId castOther = (MusicinfoId) other;

		return ((this.getMusicId() == castOther.getMusicId()) || (this.getMusicId() != null
				&& castOther.getMusicId() != null && this.getMusicId().equals(castOther.getMusicId())))
				&& (this.getArtistId() == castOther.getArtistId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getMusicId() == null ? 0 : this.getMusicId().hashCode());
		result = 37 * result + this.getArtistId();
		return result;
	}

}
