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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecipeApplicationTests {
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private IngredientService ingredientService;
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
	@Order(3)
	void getKeywords() {
		List<Keyword> keywords = recipeService.getKeywords();
		assertEquals(keywords.size() , 6);
		int found = 0;
		for (Keyword keyword : keywords) {
			if(keyword.getName().equals("Plat en sauce")) found++;
			if(keyword.getName().equals("Chaud"))  found++;
			if(keyword.getName().equals("Viande"))  found++;
			if(keyword.getName().equals("Lactose"))  found++;
			if(keyword.getName().equals("Froid"))  found++;
			if(keyword.getName().equals("Grillade"))  found++;
		};
		assertEquals(found, 6);
	}

	@Test
	@Order(4)
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
	@Order(5)
	void deleteCommentByAdmin() {
		addComment();
		Recipe existingRecipe1 = recipeService.getRecipe(testRecipe1.getId()).get(0);
		Comment createdComment1 = existingRecipe1.getCommentList().get(0);
		recipeService.deleteComment(createdComment1.getId(),adminUser,existingRecipe1);

		Recipe ExistingRecipe1_withDeletedComment = recipeService.getRecipe(testRecipe1.getId()).get(0);
		assertEquals(ExistingRecipe1_withDeletedComment.getCommentList().size() , 0);
	}

	@Test
	@Order(5)
	void deleteCommentByChef() {
		addComment();
		Recipe existingRecipe1 = recipeService.getRecipe(testRecipe1.getId()).get(0);
		Comment createdComment1 = existingRecipe1.getCommentList().get(0);
		recipeService.deleteComment(createdComment1.getId(),chef1User,existingRecipe1);

		Recipe ExistingRecipe1_withDeletedComment = recipeService.getRecipe(testRecipe1.getId()).get(0);
		assertEquals(ExistingRecipe1_withDeletedComment.getCommentList().size() , 0);
	}

	@Test
	@Order(6)
	void deleteCommentByNotOwnerChef() {
		addComment();
		Boolean failure = false;
		Recipe existingRecipe1 = recipeService.getRecipe(testRecipe1.getId()).get(0);
		Comment createdComment1 = existingRecipe1.getCommentList().get(0);

		try{
			recipeService.deleteComment(createdComment1.getId(),chef2User,existingRecipe1);
		} catch (Exception e){
			Recipe ExistingRecipe1_withDeletedComment = recipeService.getRecipe(testRecipe1.getId()).get(0);
			assertEquals(ExistingRecipe1_withDeletedComment.getCommentList().size() , 1);
			failure = true;
		}
		assertEquals(failure,true);

	}
	@Test
	@Order(5)
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
	@Order(6)
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
	@Order(7)
	void cantDeleteRecipeByNotOwnerChef() {
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

	@Test
	@Order(8)
	void getIngredients() {
		List<Ingredient> ingredients = ingredientService.getIngredients();
		assertEquals(ingredients.size() , 5);
		int found = 0;
		for (Ingredient ingredient : ingredients) {
			if(ingredient.getName().equals("Boeuf")) found++;
			if(ingredient.getName().equals("Navet")) found++;
			if(ingredient.getName().equals("Carotte")) found++;
			if(ingredient.getName().equals("Crème")) found++;
			if(ingredient.getName().equals("Veau")) found++;
		};
		assertEquals(found , 5);
	}
	@Test
	@Order(9)
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
	void createRecipeWithNewIngredient() {
		assertEquals(ingredientService.getIngredients().size() , 5);
		Ingredient newIngredient1 = new Ingredient("newIngredient1");
		newIngredient1.setId(0L);
		Ingredient newIngredient2 = new Ingredient("newIngredient2");
		newIngredient2.setId(0L);

		Recipe testRecipe = new Recipe();
		testRecipe.setName("Test Recipe");
		testRecipe.setDescription("This is a test recipe");
		testRecipe.setInstructions("1. Test instruction 1\n2. Test instruction 2");
		testRecipe.addIngredient(newIngredient1,"1q");
		testRecipe.addIngredient(newIngredient2,"1L");
		testRecipe.setId(0L);
		testRecipe = recipeService.createRecipe(testRecipe, adminUser);

		List<Recipe> recipes = recipeService.getRecipe(testRecipe.getId());
		assertEquals(recipes.size() , 1);

		Recipe savedRecipe = recipes.get(0);
		assertEquals(savedRecipe.getName(), "Test Recipe");
		assertEquals(savedRecipe.getInstructions(),"1. Test instruction 1\n2. Test instruction 2");
		assertEquals(savedRecipe.getDescription(), "This is a test recipe");
		assertEquals(savedRecipe.getRecipesIngredients().size(), 2);

		int found = 0;
		assertEquals(savedRecipe.getRecipesIngredients().size(), 2);

		for(RecipeIngredient ri  : savedRecipe.getRecipesIngredients()) {
			if(ri.getIngredient().getName().equals("newIngredient1") && ri.getQuantity().equals("1q")) found++;
			if(ri.getIngredient().getName().equals("newIngredient2") && ri.getQuantity().equals("1L")) found++;
		}
		assertEquals(found, 2);
		assertEquals(ingredientService.getIngredients().size() , 7);
	}

	@Test
	@Order(11)
	void createRecipeWithExistingIngredient() {
		assertEquals(ingredientService.getIngredients().size() , 5);
		Ingredient existingIngredient1 = ingredientService.getIngredients().get(0);
		Ingredient existingIngredient2 = ingredientService.getIngredients().get(1);

		Recipe testRecipe = new Recipe();
		testRecipe.setName("Test Recipe");
		testRecipe.setDescription("This is a test recipe");
		testRecipe.setInstructions("1. Test instruction 1\n2. Test instruction 2");
		testRecipe.addIngredient(existingIngredient1,"42");
		testRecipe.addIngredient(existingIngredient2,"43");
		testRecipe.setId(0L);

		testRecipe = recipeService.createRecipe(testRecipe, adminUser);

		List<Recipe> recipes = recipeService.getRecipe(testRecipe.getId());
		assertEquals(recipes.size() , 1);

		Recipe savedRecipe = recipes.get(0);
		assertEquals(savedRecipe.getName(), "Test Recipe");
		assertEquals(savedRecipe.getInstructions(),"1. Test instruction 1\n2. Test instruction 2");
		assertEquals(savedRecipe.getDescription(), "This is a test recipe");
		assertEquals(savedRecipe.getRecipesIngredients().size(), 2);

		int found = 0;
			for(RecipeIngredient ri  : savedRecipe.getRecipesIngredients()) {
			if(ri.getIngredient().getName().equals(existingIngredient1.getName()) && ri.getQuantity().equals("42")) found++;
			if(ri.getIngredient().getName().equals(existingIngredient2.getName()) && ri.getQuantity().equals("43")) found++;
		}
		assertEquals(found, 2);
		List<Ingredient> ingredientList = ingredientService.getIngredients();
		assertEquals(ingredientService.getIngredients().size() , 5);
	}

	Recipe generateRecipeWithExistingIngredient() {
		assertEquals(ingredientService.getIngredients().size() , 5);
		Ingredient existingIngredient1 = ingredientService.getIngredients().get(0);
		Ingredient existingIngredient2 = ingredientService.getIngredients().get(1);

		Recipe testRecipe = new Recipe();
		testRecipe.setName("Test Recipe");
		testRecipe.setDescription("This is a test recipe");
		testRecipe.setInstructions("1. Test instruction 1\n2. Test instruction 2");
		testRecipe.addIngredient(existingIngredient1,"42");
		testRecipe.addIngredient(existingIngredient2,"43");
		testRecipe.setId(0L);

		testRecipe = recipeService.createRecipe(testRecipe, adminUser);
		return testRecipe;
	}

	@Test
	@Order(12)
	void editExistingRecipe() {
		Recipe existingRecipe = generateRecipeWithExistingIngredient();

		Ingredient existingIngredient2 = ingredientService.getIngredients().get(1);
		Ingredient existingIngredient3 = ingredientService.getIngredients().get(2);
		Ingredient existingIngredient4 = ingredientService.getIngredients().get(3);

		Ingredient newIngredient1 = new Ingredient("newIngredient2");
		newIngredient1.setId(0L);

		existingRecipe.setName("Test Recipe Edited");
		existingRecipe.setDescription("This is a test recipe Edited");
		existingRecipe.setInstructions("1. Test instruction 1\n2. Test instruction 2 Edited");
		existingRecipe.emptyIngredients();
		existingRecipe.addIngredient(existingIngredient2,"44");
		existingRecipe.addIngredient(existingIngredient3,"45");
		existingRecipe.addIngredient(existingIngredient4,"46");
		existingRecipe.addIngredient(newIngredient1,"47");


		existingRecipe = recipeService.updateRecipe(existingRecipe.getId(),existingRecipe, adminUser);

		List<Recipe> recipes = recipeService.getRecipe(existingRecipe.getId());
		assertEquals(recipes.size() , 1);

		Recipe editedRecipe = recipes.get(0);
		assertEquals(editedRecipe.getName(), "Test Recipe Edited");
		assertEquals(editedRecipe.getInstructions(),"1. Test instruction 1\n2. Test instruction 2 Edited");
		assertEquals(editedRecipe.getDescription(), "This is a test recipe Edited");
		assertEquals(editedRecipe.getRecipesIngredients().size(), 4);

		int found = 0;
		for(RecipeIngredient ri  : editedRecipe.getRecipesIngredients()) {
			if(ri.getIngredient().getName().equals(existingIngredient2.getName()) && ri.getQuantity().equals("44")) found++;
			if(ri.getIngredient().getName().equals(existingIngredient3.getName()) && ri.getQuantity().equals("45")) found++;
			if(ri.getIngredient().getName().equals(existingIngredient4.getName()) && ri.getQuantity().equals("46")) found++;
			if(ri.getIngredient().getName().equals(newIngredient1.getName()) && ri.getQuantity().equals("47")) found++;
		}
		assertEquals(found, 4);
		assertEquals(ingredientService.getIngredients().size() , 6);
	}

	@Test
	@Order(13)
	void editExistingRecipeByAnotherChef() {
		Recipe existingRecipe = generateRecipeWithExistingIngredient();
		Ingredient existingIngredient1 = ingredientService.getIngredients().get(0);
		Ingredient existingIngredient2 = ingredientService.getIngredients().get(1);
		Ingredient existingIngredient3 = ingredientService.getIngredients().get(2);
		Ingredient existingIngredient4 = ingredientService.getIngredients().get(3);

		Ingredient newIngredient1 = new Ingredient("newIngredient2");
		newIngredient1.setId(0L);

		existingRecipe.setName("Test Recipe Edited");
		existingRecipe.setDescription("This is a test recipe Edited");
		existingRecipe.setInstructions("1. Test instruction 1\n2. Test instruction 2 Edited");
		existingRecipe.emptyIngredients();
		existingRecipe.addIngredient(existingIngredient2,"44");
		existingRecipe.addIngredient(existingIngredient3,"45");
		existingRecipe.addIngredient(existingIngredient4,"46");
		existingRecipe.addIngredient(newIngredient1,"47");

		Boolean failure = false;
		try {
			existingRecipe = recipeService.updateRecipe(existingRecipe.getId(), existingRecipe, chef1User);
		} catch(Exception e) {
			failure = true;
			List<Recipe> recipes = recipeService.getRecipe(existingRecipe.getId());
			assertEquals(recipes.size() , 1);

			Recipe editedRecipe = recipes.get(0);
			assertEquals(editedRecipe.getName(), "Test Recipe");
			assertEquals(editedRecipe.getInstructions(),"1. Test instruction 1\n2. Test instruction 2");
			assertEquals(editedRecipe.getDescription(), "This is a test recipe");
			assertEquals(editedRecipe.getRecipesIngredients().size(), 2);

			int found = 0;
			for(RecipeIngredient ri  : editedRecipe.getRecipesIngredients()) {
				if(ri.getIngredient().getName().equals(existingIngredient1.getName()) && ri.getQuantity().equals("42")) found++;
				if(ri.getIngredient().getName().equals(existingIngredient2.getName()) && ri.getQuantity().equals("43")) found++;
			}
			assertEquals(found, 2);
			assertEquals(ingredientService.getIngredients().size() , 5);
		}
		assertEquals(failure,true);
	}
}