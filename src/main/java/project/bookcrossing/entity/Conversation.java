package project.bookcrossing.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "conversation_seq", allocationSize = 100)
public class Conversation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id_conversation;

	@ManyToMany
	@JoinTable(
			name = "user_conversation",
			joinColumns = @JoinColumn(name = "id_conversation"),
			inverseJoinColumns = @JoinColumn(name = "id_user"))
	private List<User> conversationUsers;

	public Conversation() {
	}

	public Conversation(User firstUser, User secondUser) {
		this.conversationUsers = new ArrayList<>();
		this.conversationUsers.add(0, firstUser);
		this.conversationUsers.add(1, secondUser);
	}

	public long getId_conversation() { return this.id_conversation; }

	public User getFirstUser() {
		return conversationUsers.get(0);
	}

	public void setFirstUser(User firstUser) {
		this.conversationUsers.set(0, firstUser);
	}

	public User getSecondUser() {
		return conversationUsers.get(1);
	}

	public void setSecondUser(User secondUser) {
		this.conversationUsers.set(1, secondUser);
	}

	@Override
	public String toString() {
		return "Conversation{" +
				"id_conversation=" + id_conversation +
				", conversationUsers=" + conversationUsers +
				'}';
	}
}
