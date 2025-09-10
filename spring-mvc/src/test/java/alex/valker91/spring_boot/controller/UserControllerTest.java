package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.User;
import alex.valker91.spring_boot.model.impl.UserImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookingFacade bookingFacade;

	@Test
	void getUsersMainPage_returnsTemplate() throws Exception {
		mockMvc.perform(get("/users/main/page"))
				.andExpect(status().isOk())
				.andExpect(view().name("users/main_page"));
	}

	@Test
	void createUser_success_redirectsToUser() throws Exception {
		User created = new UserImpl(123L, "Alice", "alice@example.com");
		when(bookingFacade.createUser(any(User.class))).thenReturn(created);

		mockMvc.perform(post("/users/create")
					.param("id", "0")
					.param("name", "Alice")
					.param("email", "alice@example.com"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users/123"));
	}

	@Test
	void createUser_validationError_returnsFormWithErrors() throws Exception {
		when(bookingFacade.createUser(any(User.class))).thenThrow(new BadRequestException(java.util.List.of("name required")));

		mockMvc.perform(post("/users/create")
					.param("id", "0")
					.param("name", "")
					.param("email", "alice@example.com"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("users/new_user"))
				.andExpect(model().attributeExists("errors"));
	}

	@Test
	void getUser_rendersUserView() throws Exception {
		when(bookingFacade.getUserById(123)).thenReturn(new UserImpl(123, "Bob", "b@example.com"));

		mockMvc.perform(get("/users/123"))
				.andExpect(status().isOk())
				.andExpect(view().name("users/user"))
				.andExpect(model().attributeExists("user"));
	}
}