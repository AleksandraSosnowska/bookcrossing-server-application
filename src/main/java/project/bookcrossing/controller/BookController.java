package project.bookcrossing.controller;

import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bookcrossing.dto.book.BookDataDTO;
import project.bookcrossing.dto.book.BookResponseDTO;
import project.bookcrossing.entity.*;
import project.bookcrossing.exception.CustomException;
import project.bookcrossing.service.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private HistoryUsersService historyUsersService;
	@Autowired
	private BookHistoryService bookHistoryService;
	@Autowired
	private FavouriteBooksService favouriteBooksService;
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "/all")
	@ApiOperation(value = "${BookController.getAll}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The book doesn't exist")})
	public List<BookResponseDTO> getAll() {
		List<Book> books = bookService.getAllBooks();
		List<BookResponseDTO> response = new ArrayList<>();
		for (Book book : books) {
			response.add(modelMapper.map(book, BookResponseDTO.class));
		}
		return response;
	}

	@GetMapping(value = "/id/{id}")
	@ApiOperation(value = "${BookController.searchById}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The user doesn't exist")})
	public BookResponseDTO searchById(@ApiParam("Id") @PathVariable long id) {
		return modelMapper.map(bookService.searchById(id), BookResponseDTO.class);
	}

	@GetMapping(value = "/title&category/{title}/{category}")
	@ApiOperation(value = "${BookController.searchByTitleCategory}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The user doesn't exist")})
	public List<BookResponseDTO> searchByTitleCategory(@ApiParam("Title") @PathVariable String title,
											   @ApiParam("Category") @PathVariable String category) {
		List<Book> books = bookService.searchByTitleCategory(title, category);
		List<BookResponseDTO> response = new ArrayList<>();
		for (Book book : books) {
			response.add(modelMapper.map(book, BookResponseDTO.class));
		}
		return response;
	}

	@GetMapping(value = "/title/{title}")
	@ApiOperation(value = "${BookController.searchByTitle}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The user doesn't exist")})
	public List<BookResponseDTO> searchByTitle(@ApiParam("Title") @PathVariable String title) {
		List<Book> books = bookService.searchByTitle(title);
		List<BookResponseDTO> response = new ArrayList<>();
		for (Book book : books) {
			response.add(modelMapper.map(book, BookResponseDTO.class));
		}
		return response;
	}

	@GetMapping(value = "/category/{category}")
	@ApiOperation(value = "${BookController.searchByCategory}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The user doesn't exist")})
	public List<BookResponseDTO> searchByCategory(@ApiParam("Category") @PathVariable String category) {
		List<Book> books = bookService.searchByCategory(category);
		List<BookResponseDTO> response = new ArrayList<>();
		for (Book book : books) {
			response.add(modelMapper.map(book, BookResponseDTO.class));
		}
		return response;
	}


	@GetMapping(value = "/user/{userId}")
	@ApiOperation(value = "${BookController.getByUser}", response = BookResponseDTO.class)
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 404, message = "The user doesn't exist")})
	public List<BookResponseDTO> getByUser(@ApiParam("User") @PathVariable long userId) {
		List<BookResponseDTO> books = new ArrayList<>();
		List<HistoryUsers> historyUsers = historyUsersService.getByCurrentUserKey(userId);
		for (HistoryUsers item : historyUsers){
			BookHistory bookHistory = bookHistoryService.searchById(item.getId_historyUsers().getId_history());
			Book book = bookService.getBookByHistory(bookHistory);
			books.add(modelMapper.map(book, BookResponseDTO.class));
		}
		if (books.isEmpty()){
			throw new CustomException("The book doesn't exist", HttpStatus.NOT_FOUND);
		} else {
			return books;
		}
	}

	@PostMapping("/create/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	@ApiOperation(value = "${BookController.create}")
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied"), //
			@ApiResponse(code = 422, message = "Username is already in use")})
	public BookResponseDTO create(@ApiParam("Book") @RequestBody BookDataDTO book,
								  @ApiParam("User") @PathVariable long userId) {
		Book savedBook = bookService.create(modelMapper.map(book, Book.class));
		long historyId = savedBook.getHistory().getId_history();
		historyUsersService.createHistoryUsers(userId, historyId, "firstUser");
		return modelMapper.map(savedBook, BookResponseDTO.class);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	@ApiOperation(value = "${BookController.update}")
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied")})
	public BookResponseDTO update(@ApiParam("Update Book") @RequestBody BookDataDTO book) {
		return modelMapper.map(bookService.update(modelMapper.map(book, Book.class)), BookResponseDTO.class);
	}

	@PutMapping("/updateHired/{bookId}")
	@ApiOperation(value = "${BookController.updateLastHired}")
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied")})
	public BookResponseDTO updateLastHired(@ApiParam("Update Book") @PathVariable long bookId) {
		return modelMapper.map(bookService.updateLastHired(bookId), BookResponseDTO.class);
	}

	@DeleteMapping(value = "/{bookId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	@ApiOperation(value = "${BookController.delete}", authorizations = { @Authorization(value="apiKey") })
	@ApiResponses(value = {//
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied"), //
			@ApiResponse(code = 404, message = "The book doesn't exist"), //
			@ApiResponse(code = 500, message = "Expired or invalid JWT token")})
	public long delete(@ApiParam("BookId") @PathVariable long bookId) {
		Book book = bookService.searchById(bookId);
		long historyId = book.getHistory().getId_history();

		//delete records from history_users
		historyUsersService.deleteByHistory(historyId);

		//delete records from book_history
		bookHistoryService.deleteHistory(historyId);

		FavouritesKey key = new FavouritesKey(bookId);
		//delete records from favourites_books
		favouriteBooksService.deleteByKey(key);

		//delete book
		bookService.delete(bookId);
		return bookId;
	}
}
