package com.tunnelnetwork.KpOnlineStore.User;

import com.tunnelnetwork.KpOnlineStore.Models.User;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ConfirmationTokenService confirmationTokenService;

	private final EmailSenderService emailSenderService;

	void sendConfirmationMail(String userMail, String token) {

		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setSubject("Mail Confirmation Link!");
		mailMessage.setFrom("tunnelnetworkkpop@gmail.com");
		mailMessage.setText(
				"Thank you for registering. Please click the link to activate your account. " + "http://localhost:8080/sign-up/confirm?token="
						+ token);

		emailSenderService.sendEmail(mailMessage);
	}

	void sendResetPasswordMail(String userEmail) {

		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		final ConfirmationToken confirmationToken = new ConfirmationToken(userRepository.findByEmail(userEmail).get());

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		mailMessage.setTo(userEmail);
		mailMessage.setSubject("Password Reset");
		mailMessage.setFrom("tunnelnetworkkpop@gmail.com");
		mailMessage.setText(
				"Please click the link to reset the password of your account. " + "http://localhost:8080/change-password/confirm?token="
						+ confirmationToken.getConfirmationToken());

		emailSenderService.sendEmail(mailMessage);
	}

	void confirmResetPassword(ConfirmationToken confirmationToken, String newPassword) {

		final User user = confirmationToken.getUser();

		user.setPassword(bCryptPasswordEncoder.encode(newPassword));

		userRepository.save(user);

		confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		final Optional<User> optionalUser = userRepository.findByEmail(email);

		return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));

	}

	public void signUpUser(User user) {

		final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		user.setPassword(encryptedPassword);

		userRepository.saveAndFlush(user);

		final ConfirmationToken confirmationToken = new ConfirmationToken(user);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());

	}

	void confirmUser(ConfirmationToken confirmationToken) {

		final User user = confirmationToken.getUser();

		user.setEnabled(true);

		userRepository.save(user);

		confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

	}

	public boolean doesUserExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}
}