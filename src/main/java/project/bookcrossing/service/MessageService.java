package project.bookcrossing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import project.bookcrossing.dto.message.MessageResponseDTO;
import project.bookcrossing.entity.Conversation;
import project.bookcrossing.entity.Message;
import project.bookcrossing.entity.User;
import project.bookcrossing.exception.CustomException;
import project.bookcrossing.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public MessageResponseDTO createMessage(Message message, User user, Conversation conversation) {
		if (conversation.getFirstUser().getId() != user.getId() &&
				conversation.getSecondUser().getId() != user.getId() ) {
			throw new CustomException("User doesn't belong to this conversation", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		message.setSender(user);
		message.setConversation(conversation);
		message = messageRepository.save(message);
		return new MessageResponseDTO(
				message.getId_message(), message.getContent(), message.getDate(),
				user.getUsername(), conversation.getId_conversation());
	}

	public List<MessageResponseDTO> searchByConversation(Conversation conversation) {
		List<Message> messages = messageRepository.findByConversationOrderByDateAsc(conversation);
		List<MessageResponseDTO> response = new ArrayList<>();
		for (Message message : messages) {
			MessageResponseDTO _message = new MessageResponseDTO(
					message.getId_message(), message.getContent(), message.getDate(),
					message.getSender().getUsername(), message.getConversation().getId_conversation());
			response.add(_message);
		}
		return response;
	}

	private List<Message> findByConversation(Conversation conversation) {
		return messageRepository.findByConversationOrderByDateAsc(conversation);
	}

	public Message searchLastByConversation(Conversation conversation) {
		List<Message> allMessages = this.findByConversation(conversation);
		return allMessages.get(allMessages.size() - 1);
	}

	public void deleteMessage(long messageId){
		this.setSender(messageId);
		messageRepository.deleteById(messageId);
	}

	private void setSender(long messageId) {
		Optional<Message> _message = messageRepository.findByIdMessage(messageId);
		if (_message.isEmpty()) {
			throw new CustomException("The message doesn't exist", HttpStatus.NOT_FOUND);
		}
		_message.get().setSender(null);
		Message message = _message.get();
		messageRepository.save(message);
	}

	public void deleteByConversation(Conversation conversation){
		List<Message> messages = messageRepository.getAllByConversation(conversation);
		if (messages != null && !messages.isEmpty()) {
			for (Message item : messages) {
				deleteMessage(item.getId_message());
			}
		}
	}
}
