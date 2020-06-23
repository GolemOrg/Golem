package net.golem.packs;

public class ResourcePack {

	private String id;

	private String version;

	public ResourcePack(String id, String version) {
		this.id = id;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public long getSize() {
		return 0;
	}
}
