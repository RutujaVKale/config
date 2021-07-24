package configurationHandling;

public class SorterDetail {
	private String sorter;
	private String chute;
	private String divert;
	private String flag;
	
	public String getSorter() {
		return sorter;
	}

	public void setSorter(String sorter) {
		this.sorter = sorter;
	}

	public String getChute() {
		return chute;
	}

	public void setChute(String chute) {
		this.chute = chute;
	}

	public String getDivert() {
		return divert;
	}

	public void setDivert(String divert) {
		this.divert = divert;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	SorterDetail(){
		
	}

	public SorterDetail(String sorter, String chute, String divert, String flag) {
		super();
		this.sorter = sorter;
		this.chute = chute;
		this.divert = divert;
		this.flag = flag;
	}
	

}


