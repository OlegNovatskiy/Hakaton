package com.Bot.service;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.Bot.components.services.ActionDAO;

/**
 * Service that manage a telegram bot
 * 
 * @author olegnovatskiy
 *
 */
@Service
public class TelegramService extends TelegramLongPollingBot {

	private static final String BOTTOKEN = "314392470:AAE-JC1mnHCfkA3G7tw4GJ86Yy0hRA2xiMw";
	private static final String BOTUSERNAME = "XPKbot";

	private static Logger log = Logger.getLogger(TelegramService.class);

	static {
		ApiContextInitializer.init();
	}

	@Autowired
	private ReplacementService replacementService;

	@Autowired
	private ActionDAO actionDAO;

	/**
	 * Initialization a bot of telegram
	 */
	@PostConstruct
	public void initBot() throws TelegramApiException {
		log.info("Innitialization Bot)");
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(this);
		} catch (TelegramApiException e) {
			log.error("Error in getDomain ---  " + e.getMessage());
			throw new TelegramApiException(e);
		}
	}

	/**
	 * This method will be called when an Update is received by your bot
	 * 
	 * @param update
	 *            - updates that happened in telegram
	 */
	@Override
	public void onUpdateReceived(Update update) {

		Message receiveMessage = update.getMessage();

		switch (receiveMessage.getText()) {

		case "Привіт":
		case "Привіт)":
		case "Привіт!":
		case "Прівет":
		case "Прівет!":
		case "Прівет)":
			sendTextMessage(receiveMessage, "Привіт)))");
			return;
		case "help":
			Set<String> actions = actionDAO.getActions();
			sendTextMessage(receiveMessage, "Заміни група");
			for (String action : actions) {
				sendTextMessage(receiveMessage, action);
			}
			return;

		}

		if (receiveMessage.getText().length()>6 && receiveMessage.getText().substring(0, 6).equals("Заміни")) {

			System.out.println(receiveMessage.getText().substring(0, 6));
			System.out.println(receiveMessage.getText().substring(7, 13));
			
			
			String replacement = replacementService.getReplacement(receiveMessage.getText().substring(7, 13));
			sendTextMessage(receiveMessage, replacement);
			return;
		}

		Set<String> actions = actionDAO.getActions();

		if (actions.contains(receiveMessage.getText())) {
			String resultAction = actionDAO.getActionValue(receiveMessage.getText());
			sendTextMessage(receiveMessage, resultAction);

		} else {
			sendTextMessage(receiveMessage,
					"Я незнаю що ти хочеш!!! Використай команду 'help' для відображення всіх можливих звереннь.");
		}

	}

	/**
	 * Method send a text messages to telegram
	 * 
	 * @param receivedMessage
	 * @param textNewMessage
	 * @throws TelegramApiException
	 */
	private void sendTextMessage(Message receivedMessage, String textNewMessage) {

		SendMessage messageForSend = new SendMessage();
		messageForSend.setChatId(receivedMessage.getChatId().toString());
		messageForSend.setText(textNewMessage);

		try {
			sendMessage(messageForSend);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}

	}

	

	@Override
	public String getBotUsername() {
		return BOTUSERNAME;
	}

	@Override
	public String getBotToken() {
		return BOTTOKEN;
	}

}
