package project.bookcrossing.dto.user;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

import project.bookcrossing.entity.Role;

public class UserDataDTO {

	@ApiModelProperty(position = 0)
	private String username;
	@ApiModelProperty(position = 1)
	private String email;
	@ApiModelProperty(position = 2)
	private String password;
	@ApiModelProperty(position = 3)
	String[] roles;
	@ApiModelProperty(position = 4)
	private String firstName;
	@ApiModelProperty(position = 5)
	private String lastName;
	@ApiModelProperty(position = 6)
	private String city;
	@ApiModelProperty(position = 7)
	private String province;
	@ApiModelProperty(position = 8)
	private long phoneNumber;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDataDTO{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", roles=" + Arrays.toString(roles) +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", phoneNumber=" + phoneNumber +
				'}';
	}
}
