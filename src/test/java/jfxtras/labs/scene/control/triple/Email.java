package jfxtras.labs.scene.control.triple;

public class Email {
	private String name;
	private String emailAddress;
	private boolean primary;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	public Email(String name, String emailAddress, boolean primary) {
		super();
		this.name = name;
		this.emailAddress = emailAddress;
		this.primary = primary;
	}
	@Override
	public String toString() {
		return "Email [name=" + name + ", emailAddress=" + emailAddress + ", primary=" + primary + "]";
	}
}
