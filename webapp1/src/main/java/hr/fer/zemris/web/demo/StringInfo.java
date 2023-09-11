package hr.fer.zemris.web.demo;

public class StringInfo {

	private String text;
	
	public StringInfo() {
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getLength() {
		return text==null ? 0 : text.length();
	}
	
	public String getCharAt(long index) {
		if(text==null || index<0 || index>=text.length()) return "";
		return String.valueOf(text.charAt((int)index));
	}
	
	public StringInfo getReversed() {
		StringInfo rev = new StringInfo();
		if(text==null || text.isEmpty()) {
			rev.setText(text);
		} else {
			char[] arr = text.toCharArray();
			char[] arr2 = new char[arr.length];
			for(int i = 0; i < arr.length; i++) {
				arr2[arr.length-1-i] = arr[i];
			}
			rev.setText(new String(arr2));
		}
		return rev;
	}
}