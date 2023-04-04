package recipe.demo.Recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import recipe.demo.Recipe.recipe.*;
import recipe.demo.Recipe.security.auth.AuthenticationService;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecipeApplicationTests {
	@Autowired
	private RecipeService recipeService;

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationService authService;
	private User adminUser;
	private User regularUser;
	private User chef1User;
	private User chef2User;

	private Recipe testRecipe1;
	private Recipe testRecipe2;
	@BeforeEach
	void setUp() {
		adminUser = new User();
		adminUser.setFirstname("admin_Firstname");
		adminUser.setLastname("admin_Lastname");
		adminUser.setEmail("admin_email");
		adminUser.setPassword("admin_password");
		adminUser.setRole(Role.ADMIN);
		adminUser.setId(0);
		adminUser = authService.register(adminUser);

		regularUser = new User();
		regularUser.setFirstname("regularUser_Firstname");
		regularUser.setLastname("regularUser_Lastname");
		regularUser.setEmail("regularUser_email");
		regularUser.setPassword("regularUser_password");
		regularUser.setRole(Role.USER);
		regularUser.setId(0);
		regularUser = authService.register(regularUser);

		chef1User = new User();
		chef1User.setFirstname("chef1User_Firstname");
		chef1User.setLastname("chef1User_Lastname");
		chef1User.setEmail("chef1User_email");
		chef1User.setPassword("chef1User_password");
		chef1User.setRole(Role.CHEF);
		chef1User.setId(0);
		chef1User = authService.register(chef1User);

		chef2User = new User();
		chef2User.setFirstname("chef2User_Firstname");
		chef2User.setLastname("chef2User_Lastname");
		chef2User.setEmail("chef2User_email");
		chef2User.setPassword("chef2User_password");
		chef2User.setRole(Role.CHEF);
		chef2User.setId(0);
		chef2User = authService.register(chef2User);

		testRecipe1 = new Recipe();
		testRecipe1.setName("Test Recipe 1");
		testRecipe1.setDescription("This is a test recipe");
		testRecipe1.setInstructions("1. Test instruction 1\n2. Test instruction 2");
		testRecipe1 = recipeService.createRecipe(testRecipe1, chef1User);

		testRecipe2 = new Recipe();
		testRecipe2.setName("Test Recipe 2");
		testRecipe2.setDescription("This is a test recipe");
		testRecipe2.setInstructions("1. Test instruction 1\n2. Test instruction 2");
		testRecipe2 = recipeService.createRecipe(testRecipe2, chef2User);


	}

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void getRecipes() {
		Recipe readTestRecipe1 = null;
		Recipe readTestRecipe2 = null;
		List<Recipe> recipes = recipeService.getRecipes();
		assertEquals(recipes.size() , 2);
		for (Recipe recipe : recipes) {
			if(recipe.getName().equals("Test Recipe 1")) readTestRecipe1 = recipe;
			if(recipe.getName().equals("Test Recipe 2")) readTestRecipe2 = recipe;
		};

		assertNotNull(readTestRecipe1);
		assertNotNull(readTestRecipe2);
		assertEquals(readTestRecipe1.getName(), testRecipe1.getName());
		assertEquals(readTestRecipe1.getDescription(), testRecipe1.getDescription());
		assertEquals(readTestRecipe1.getInstructions(), testRecipe1.getInstructions());
		assertEquals(readTestRecipe2.getName(), testRecipe2.getName());
		assertEquals(readTestRecipe2.getInstructions(), testRecipe2.getInstructions());
		assertEquals(readTestRecipe2.getDescription(), testRecipe2.getDescription());

		recipes = recipeService.getRecipe(testRecipe2.getId());
		assertEquals(recipes.size() , 1);
		assertEquals(recipes.get(0).getName(), testRecipe2.getName());
		assertEquals(recipes.get(0).getDescription(), testRecipe2.getDescription());
		assertEquals(recipes.get(0).getInstructions(), testRecipe2.getInstructions());
	}

	@Test
	@Order(10)
	void addComment() {
		Date date = new Date();
		Comment newComment = new Comment();
		newComment.setId(0L);
		newComment.setCreationDate(date);
		newComment.setTitle("comment1_title");
		newComment.setContent("comment1_content");
		newComment.setCreator(chef1User);
		recipeService.addCommentToRecipe(testRecipe1.getId(),newComment);

		Recipe ExistingRecipe1 = recipeService.getRecipe(testRecipe1.getId()).get(0);

		assertEquals(ExistingRecipe1.getCommentList().size() , 1);
		Comment createdComment1 = ExistingRecipe1.getCommentList().get(0);
		assertEquals(createdComment1.getTitle(), newComment.getTitle());
		assertEquals(createdComment1.getContent(), newComment.getContent());
		assertEquals(createdComment1.getCreator().getId(), chef1User.getId());
		assertTrue((createdComment1.getCreationDate().getTime() - date.getTime()) < 1000);

	}
	@Test
	@DirtiesContext
	@Order(40)
	void deleteRecipeByAdmin() {
		Recipe readTestRecipe1 = null;
		Recipe readTestRecipe2 = null;
		recipeService.deleteRecipe(testRecipe1.getId(),adminUser);

		List<Recipe> recipes = recipeService.getRecipes();
		assertEquals(recipes.size() , 1);
		for (Recipe recipe : recipes) {
			if(recipe.getName().equals("Test Recipe 1")) readTestRecipe1 = recipe;
			if(recipe.getName().equals("Test Recipe 2")) readTestRecipe2 = recipe;
		};

		assertNull(readTestRecipe1);
		assertNotNull(readTestRecipe2);
	}

	@Test
	@DirtiesContext
	@Order(41)
	void deleteRecipeByChef() {
		Recipe readTestRecipe1 = null;
		Recipe readTestRecipe2 = null;
		recipeService.deleteRecipe(testRecipe1.getId(),chef1User);

		List<Recipe> recipes = recipeService.getRecipes();
		assertEquals(recipes.size() , 1);
		for (Recipe recipe : recipes) {
			if(recipe.getName().equals("Test Recipe 1")) readTestRecipe1 = recipe;
			if(recipe.getName().equals("Test Recipe 2")) readTestRecipe2 = recipe;
		};
		assertNull(readTestRecipe1);
		assertNotNull(readTestRecipe2);
	}

	@Test
	@DirtiesContext
	@Order(42)
	void cantDeleteRecipeOfAnotherChef() {
		Recipe readTestRecipe1 = null;
		Recipe readTestRecipe2 = null;
		Boolean failure = false;
		try{
			recipeService.deleteRecipe(testRecipe1.getId(),chef2User);
		} catch (Exception e){
			List<Recipe> recipes = recipeService.getRecipes();
			assertEquals(recipes.size() , 2);
			for (Recipe recipe : recipes) {
				if(recipe.getName().equals("Test Recipe 1")) readTestRecipe1 = recipe;
				if(recipe.getName().equals("Test Recipe 2")) readTestRecipe2 = recipe;
			};
			assertNotNull(readTestRecipe1);
			assertNotNull(readTestRecipe2);
			failure = true;
		}
		assertEquals(failure,true);
	}


}
