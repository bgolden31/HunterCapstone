package registration;

public class UserHistory {
	private String username;
	private String author;
	private String recipeName;
	
	public UserHistory() {
		username = "";
		author = "";
		recipeName = "";
	}
	public UserHistory(String username, String author, String recipeName) {
		this.username = username;
		this.author = author;
		this.recipeName = recipeName;
	}
	
	 public String getUsername() {
		  return username;
	}

	 public String getRecipeName() {
		  return recipeName;
	}

	public String getAuthor() {
		  return author;
	}

	// Setter Methods 

	public void setUsername(String username) {
		 this.username = username;
	}

	public void setRecipeName(String recipeName) {
		 this.recipeName = recipeName;
	}

	public void setAuthor(String author) {
		  this.author = author;
	}
}
