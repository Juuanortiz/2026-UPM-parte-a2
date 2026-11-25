package es.upm.grise.subscriptionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.upm.grise.subscriptionService.exceptions.ExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NonExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NullUserException;
import es.upm.grise.subscriptionService.exceptions.UserDoesNotHaveEmailException;

class SubscriptionServiceTest {
	EmailService email = Mockito.mock(EmailService.class);

	@Test
	public void smokeTest() {}

	@Test
	public void testAddSubscriberNull() throws NullUserException {
		SubscriptionService service = new SubscriptionService(email);
		assertThrows(NullUserException.class, () -> {
			service.addSubscriber(null);
		});
	}

	@Test
	public void testAddSubscriberExisting() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		SubscriptionService service = new SubscriptionService(email);
		User user = new User(Delivery.LOCAL, "john.doe@example.com");
		service.addSubscriber(user);
		assertThrows(ExistingUserException.class, () -> {
			service.addSubscriber(user);
		});
	}
	

	@Test
	public void testAddSubscriberNoEmail() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		SubscriptionService service = new SubscriptionService(email);
		User user = new User(Delivery.LOCAL, null);
		assertThrows(UserDoesNotHaveEmailException.class, () -> {
			service.addSubscriber(user);
		});
	}

	

	@Test
	public void testRemoveSubscriber() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException, NonExistingUserException {
		SubscriptionService service = new SubscriptionService(email);
		User user = new User(Delivery.LOCAL, "john.doe@example.com");
		service.addSubscriber(user);
		service.removeSubscriber(user);
		assertThrows(NonExistingUserException.class, () -> {
			service.removeSubscriber(user);
		});
	}

	@Test
	public void testRemoveSubscriberNull() throws NullUserException {
		SubscriptionService service = new SubscriptionService(email);
		assertThrows(NullUserException.class, () -> {
			service.removeSubscriber(null);
		});
	}

	@Test 
	public void testRemoveSubscriberNonExisting() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		SubscriptionService service = new SubscriptionService(email);
		User user = new User(Delivery.LOCAL, "john.doe@example.com");

		assertThrows(NonExistingUserException.class, () -> {
			service.removeSubscriber(user);
		});

	}

	@Test 
	public void testSendMessage() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		SubscriptionService service = new SubscriptionService(email);
		User user1 = new User(Delivery.LOCAL, "john.doe@example.com");
		User user2 = new User(Delivery.LOCAL, "jane.doe@example.com");

		service.addSubscriber(user1);
		service.addSubscriber(user2);
		assertEquals(0,service.sendMessage( new Message(999,"Hello, subscribers!")));

	}


	@Test
	public void testSendMessageWithDiscarded() throws NullUserException, ExistingUserException, UserDoesNotHaveEmailException {
		SubscriptionService service = new SubscriptionService(email);
		User user1 = new User(Delivery.LOCAL, "john.doe@example.com");
		User user2 = new User(Delivery.DO_NOT_DELIVER, "jane.doe@example.com");
		

		service.addSubscriber(user1);
		service.addSubscriber(user2);
		assertEquals(1, service.sendMessage( new Message(999,"Hello, subscribers!")));
	}



}

