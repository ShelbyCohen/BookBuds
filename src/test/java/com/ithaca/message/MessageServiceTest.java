package com.ithaca.message;

import com.ithaca.user.User;
import com.ithaca.user.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ThreadRepository threadRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    public void findThreadTest() {
        Long one = 1L;
        User user1 = new User("David", "pass", "q", "a");
        User user2 = new User("Nick", "pass", "q", "a");
        Thread thread = new Thread();
        thread.getUsers().add(user1);
        thread.getUsers().add(user2);
        Message message = new Message(user1, thread, "Message to Nick", "time");
        thread.getMessages().add(message);
        user1.getThreads().add(thread);

        when(userRepository.findOne(one)).thenReturn(user1);
        when(userRepository.findByName("Nick")).thenReturn(user2);

        Assert.assertTrue(messageService.findThread(one, "Nick").getUsers().contains(user1));
        Assert.assertTrue(messageService.findThread(one, "Nick").getUsers().contains(user2));

        // test return null
        when(userRepository.findByName("Nick")).thenReturn(user1);
        Assert.assertNull(messageService.findThread(one, "Nick"));
    }

    @Test
    public void createTest() {
        Long one = 1L;
        User user1 = new User("David", "pass", "q", "a");
        User user2 = new User("Nick", "pass", "q", "a");
        Thread thread = new Thread();
        thread.getUsers().add(user1);
        thread.getUsers().add(user2);
        Message message = new Message(user1, thread, "Message to Nick", "time");
        thread.getMessages().add(message);
        user1.getThreads().add(thread);

        when(userRepository.findOne(one)).thenReturn(user1);
        when(userRepository.findByName("Nick")).thenReturn(user2);
        when(threadRepository.save(thread)).thenReturn(thread);

        Assert.assertTrue(messageService.create(one, "Nick", "A message to Nick").getUsers().contains(user1));
    }

    @Test
    public void editTest() {
        Long one = 1L;
        User user = new User("David", "pass", "q", "a");
        Thread thread = new Thread();
        thread.getUsers().add(user);
        Message message = new Message(user, thread, "Message to Nick", "time");
        thread.getMessages().add(message);
        user.getThreads().add(thread);
        user.getMessages().add(message);

        when(userRepository.findOne(one)).thenReturn(user);
        when(messageRepository.findOne(one)).thenReturn(message);

        Assert.assertEquals("new message", messageService.edit(one, one, "new message").getMessages().get(0).getText());
    }

    @Test
    public void deleteTest() {
        Long one = 1L;
        User user = new User("David", "pass", "q", "a");
        Thread thread = new Thread();
        thread.getUsers().add(user);
        Message message = new Message(user, thread, "Message to Nick", "time");
        thread.getMessages().add(message);
        user.getThreads().add(thread);
        user.getMessages().add(message);

        when(userRepository.findOne(one)).thenReturn(user);
        when(messageRepository.findOne(one)).thenReturn(message);

        Assert.assertEquals(true, messageService.delete(one, one));
        Assert.assertEquals(false, messageService.delete(one, 2L));
    }
}
