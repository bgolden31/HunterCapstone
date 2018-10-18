package registeration;

public class User {
	private int user_id;
	private String username;
	private String password;
	private String email;
	private int age;
	private String name;
	
	

	public User() {
		user_id = 0;
		username = "";
		password = "";
		email = "";
		age = 0;
		name = "";
	}
	
	public User(int user_id, String username, String password, String email, int age, String name) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.age = age;
		this.name = name;
		
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
