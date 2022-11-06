package com.sportyshoes.controller;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sportyshoes.model.Product;
import com.sportyshoes.model.User;
import com.sportyshoes.service.ProductService;
import com.sportyshoes.service.PurchaseReportService;
import com.sportyshoes.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseReportService purchaseReportService;

	@PostMapping("/signup")
	public @ResponseBody String register(@RequestBody(required = false) User user) {
		if (user == null) {
			return "Enter Valid User Details - User details should not be Null";
		}else if(user.getUserName() == null || user.getUserPassword()== null || user.getUserEmail() == null) {
			return "Enter Valid User Details - All the fields(Name, Password, Email) are mandatory";
		}
		int strength = 10;
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
		String encodedPassword = bCryptPasswordEncoder.encode(user.getUserPassword());
		user.setUserPassword(encodedPassword);
		user.setUserName(user.getUserName().toLowerCase());
		userService.signUp(user);
		return "Signed Up Successfully!";
	}

	@PostMapping("/{userId}/buy/{productName}")
	@Transactional
	public @ResponseBody String buyProductByName(@PathVariable(name = "userId") int userID,
			@PathVariable(name = "productName") String productName) {
		Optional<Product> product = productService.getProductByName(productName);
		if (product.isPresent()) {
			Optional<User> user = userService.getSignedUpUserById(userID);
			if (user.isPresent()) {
				User user2 = user.get();
				user2.addProduct(product.get());
				Product product2 = product.get();
				product2.addUser(user.get());
				userService.saveUserWithProduct(user2);
				productService.addProduct(product2);
				purchaseReportService.savePurchaseReport(product2.getProductName(), product2.getCategory(),
						product2.getProductPrice(), user2.getUserName(), user2.getUserEmail(), new Date());
				return "You have successfully bought : " + product.get().getProductName();
			} else {
				return "User Not Found! to buy the Product";
			}
		}
		return "Product Not Found!";
	}
}
