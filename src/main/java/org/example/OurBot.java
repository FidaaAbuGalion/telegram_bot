package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.util.*;

public class OurBot extends TelegramLongPollingBot  {
    private List <Long> chatId ;
    private QuoteModel quoteModel;

    private static final String COVID_API_URL = "https://disease.sh/v3/covid-19";
    private static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke/Any";
    private static final String CAT_FACTS_API_URL = "https://cat-fact.herokuapp.com/facts/random?amount=3";
    private static final String QUOTES_API_URL = "https://api.quotable.io/random";
    private static final String NASA_API_URL = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String NASA_API_URL_KEY = "ki9sn5FIlNReREkZYgoqPbPH4DnMw1sqnzLy5WiR";


    public OurBot (){
        this.chatId = new ArrayList<>() ;
    }

    @Override
    public String getBotToken() {
        return "6143807633:AAFe2Ag8edG7Qi_jZpasAUsKVq-7y5NjUhE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId);
            if (!this.chatId.contains(chatId)) {
                theStartOfConversation(chatId);
                this.chatId.add(chatId);
            } else {
                botMenu(chatId);
            }
         } else if (update.hasCallbackQuery()) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        if (data.equals(" Yes")) {
            botMenu(chatId);
        }else if (data.equals(" No")){
            sendMessage.setChatId(chatId);
            sendMessage.setText("Thank you and goodbye!");
            send(sendMessage);
        }
        else {
            userOption(callbackQuery);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Noor64fbot";
    }

    private void theStartOfConversation(long chatId){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        InlineKeyboardButton ButtonYes = new InlineKeyboardButton(" Yes");
        ButtonYes.setCallbackData(" Yes");
        InlineKeyboardButton ButtonNo = new InlineKeyboardButton("No");
        ButtonNo.setCallbackData(" No");
        List<InlineKeyboardButton> NoYesButtons = Arrays.asList(ButtonYes, ButtonNo);
        List<List<InlineKeyboardButton>> keyboardYesNo = Arrays.asList(NoYesButtons);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboardYesNo);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Welcome! As the best bot around, I offer a wide range of options for you to explore." + "\n" +
                "do you want to try");
        send(sendMessage);
    }

    private void botMenu(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        InlineKeyboardButton Button1 = new InlineKeyboardButton(" 1") ;
        Button1.setCallbackData(" 1");
        InlineKeyboardButton Button2 = new InlineKeyboardButton(" 2") ;
        Button2.setCallbackData(" 2");
        InlineKeyboardButton Button3 = new InlineKeyboardButton(" 3") ;
        Button3.setCallbackData(" 3");
        InlineKeyboardButton Button4 = new InlineKeyboardButton(" 4") ;
        Button4.setCallbackData(" 4");
        InlineKeyboardButton Button5 = new InlineKeyboardButton(" 5") ;
        Button5.setCallbackData(" 5");
        List<InlineKeyboardButton> topRow = Arrays.asList(Button1 , Button2 , Button3 , Button4 , Button5);
        List<List<InlineKeyboardButton>> keyboard = Arrays.asList(topRow) ;
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup() ;
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Here are my services: \n" +
                "\n" +
                "1 - Enjoy a good laugh with some funny jokes.\n" +
                "2 - Embark on an interstellar journey with information about space.\n" +
                "3 - Discover fascinating random facts about cats.\n" +
                "4 - Stay informed with statistical data about the Corona virus.\n" +
                "5 - Explore some famous quotes.\n" +
                "Feel free to choose an option, and I'll be ready to assist you! " +
                "");
        send(sendMessage);
    }

    public  void  send(SendMessage sendMessage){

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void userOption(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (data.equals(" 1")) {
            sendJoke(chatId);
        }else if (data.equals(" 2")){
            sendAPODModel(chatId);
        }else if (data.equals(" 3")){
            sendCatFacts(chatId);
        }else if (data.equals(" 4")){
            sendCoronaVirusStats(chatId);
        }else if (data.equals(" 5")){
            sendRandomQuote(chatId);
        }
    }

    private void sendJoke(long chatId) {
        SendMessage sendMessage = null;
        try {
            HttpResponse<String> response = Unirest.get(JOKE_API_URL).asString();
            String json = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JokeModel jokeModel = objectMapper.readValue(json, JokeModel.class);
            String jokeText = extractJokeText(jokeModel);
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(jokeText);
            send(sendMessage);
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }

    private String extractJokeText(JokeModel jokeModel) {
        StringBuilder jokeText = new StringBuilder();
        if (jokeModel.getJoke() != null) {
            jokeText.append(jokeModel.getJoke());
        } else {
            jokeText.append(jokeModel.getSetup()).append("\n");
            jokeText.append(jokeModel.getDelivery());
        }
        return jokeText.toString();
    }
    private void sendAPODModel(long chatId) {
        try {

            String apiUrl = NASA_API_URL + NASA_API_URL_KEY;

            HttpResponse<String> response = Unirest.get(apiUrl).asString();
            String json = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            APODModel apodModel = objectMapper.readValue(json, APODModel.class);

            String title = apodModel.getTitle();
            String explanation = apodModel.getExplanation();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Title: " + title + "\nExplanation: " + explanation);
            send(sendMessage);


        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }


    private void sendCatFacts(long chatId) {
        try {
            String catFactsApiUrl = CAT_FACTS_API_URL;
            HttpResponse<String> catFactsResponse = Unirest.get(catFactsApiUrl).asString();
            String catFactsJson = catFactsResponse.getBody();

            JSONArray catFactsArray = new JSONArray(catFactsJson);
            List<String> catFactsList = new ArrayList<>();

            for (int i = 0; i < catFactsArray.length(); i++) {
                JSONObject factObject = catFactsArray.getJSONObject(i);
                String catFact = factObject.getString("text");
                catFactsList.add(catFact);
            }

            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Cat Facts:\n");

            for (int i = 0; i < catFactsList.size(); i++) {
                messageBuilder.append("- ").append(catFactsList.get(i)).append("\n");
            }

            String message = messageBuilder.toString();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            send(sendMessage);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    private void sendCoronaVirusStats(long chatId) {
        try {
            HttpResponse<String> response = Unirest.get(COVID_API_URL + "/all").asString();
            String json = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode data = objectMapper.readTree(json);

            int totalCases = data.get("cases").asInt();
            int totalDeaths = data.get("deaths").asInt();
            int totalRecovered = data.get("recovered").asInt();

            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Global COVID-19 Statistics:\n");
            messageBuilder.append("Total Cases: ").append(totalCases).append("\n");
            messageBuilder.append("Total Deaths: ").append(totalDeaths).append("\n");
            messageBuilder.append("Total Recovered: ").append(totalRecovered).append("\n");
            String message = messageBuilder.toString();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            send(sendMessage);
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }



    private void sendRandomQuote(long chatId) {
        SendMessage sendMessage = null;
        try {
            HttpResponse<String> response = Unirest.get(QUOTES_API_URL).asString();
            String json = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            QuoteModel quoteModel = objectMapper.readValue(json, QuoteModel.class);
            String quoteText = extractQuoteText(quoteModel);
            sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(quoteText);
            send(sendMessage);
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }
    private String extractQuoteText(QuoteModel quoteModel) {

        StringBuilder quoteText = new StringBuilder();

            quoteText.append(quoteModel.getAuthor()).append("\n");
            quoteText.append(quoteModel.getContent()).append("\n");

        return quoteText.toString();
    }



}
