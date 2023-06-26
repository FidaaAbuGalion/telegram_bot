package org.example;

import apiModel.APODModel;
import apiModel.JokeModel;
import apiModel.QuoteModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.*;

public class OurBot extends TelegramLongPollingBot  {
    private List <Long> chatId ;
    private QuoteModel quoteModel;
    private Map<Long , Lead> leadMap ;
    private LinkedList<Lead> theLeads = new LinkedList<>() ;
    private Lead LeadHere ;
    protected String mostActiveUser;

    private static final String COVID_API_URL = "https://disease.sh/v3/covid-19";
    private static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke/Any";
    private static final String CAT_FACTS_API_URL = "https://cat-fact.herokuapp.com/facts/random?amount=3";
    private static final String QUOTES_API_URL = "https://api.quotable.io/random";
    private static final String NASA_API_URL = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String NASA_API_URL_KEY = "ki9sn5FIlNReREkZYgoqPbPH4DnMw1sqnzLy5WiR";


    public OurBot (){
        this.chatId = new ArrayList<>() ;
        this.leadMap = new HashMap<>() ;
    }

    @Override
    public String getBotToken() {
        return "6143807633:AAFe2Ag8edG7Qi_jZpasAUsKVq-7y5NjUhE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        /*long chatId1 = update.getMessage().getChatId() ;
       Lead lead = this.leadMap.get(chatId1) ;
       LeadHere = lead ;
        if (lead == null){
            //first time
           lead = new Lead(chatId1);
           this.leadMap.put(chatId1 , lead) ;
           theLeads.push(lead);
        }*/

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
        SaveTheVisitor(update);
    }
    private void SaveTheVisitor(Update update){    //new
        long chatId1 = update.getMessage().getChatId() ;
        Lead lead = this.leadMap.get(chatId1) ;
        LeadHere = lead ;
        if (lead == null){
            //first time
            lead = new Lead(chatId1);
            this.leadMap.put(chatId1 , lead) ;
            theLeads.push(lead);
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
        int numChoices = 0;
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        LinkedList<Integer> visitorChoices = new LinkedList<>();

        if (data.equals(" 1") && numChoices < 3) {
            sendJoke(chatId);
            visitorChoices.push(1);
            LeadHere.setTheNumberOfTheServicesYouUse(visitorChoices);
            numChoices++;
        } else if (data.equals(" 2") && numChoices < 3) {
            sendAPODModel(chatId);
            visitorChoices.push(2);
            LeadHere.setTheNumberOfTheServicesYouUse(visitorChoices);
            numChoices++;
        } else if (data.equals(" 3") && numChoices < 3) {
            sendCatFacts(chatId);
            visitorChoices.push(3);
            LeadHere.setTheNumberOfTheServicesYouUse(visitorChoices);
            numChoices++;
        } else if (data.equals(" 4") && numChoices < 3) {
            sendCoronaVirusStats(chatId);
            visitorChoices.push(4);
            LeadHere.setTheNumberOfTheServicesYouUse(visitorChoices);
            numChoices++;
        } else if (data.equals(" 5") && numChoices < 3) {
            sendRandomQuote(chatId);
            visitorChoices.push(5);
            LeadHere.setTheNumberOfTheServicesYouUse(visitorChoices);
            numChoices++;
        }

        Map<Long, LinkedList<Integer>> userChoices = (Map<Long, LinkedList<Integer>>) LeadHere.getTheNumberOfTheServicesYouUse();
        mostActiveUser = findMostActiveUser(userChoices);


    }
    public LinkedList<String> StringTheServicesHeDoes(Lead lead) {   //ني بتبين اش البعلوت الي سواهن اليوزر
        LinkedList<String> TheServices = new LinkedList<>() ;
        TheServices.push(lead.getName());
        for (int i = 0; i < lead.getTheNumberOfTheServicesYouUse().size(); i++) {
            if (lead.getTheNumberOfTheServicesYouUse().get(i) == 1){
                TheServices.set(i,"jokes")  ;
            }
            if (lead.getTheNumberOfTheServicesYouUse().get(i) == 2){
                TheServices.set(i,"facts about the space")  ;
            }
            if (lead.getTheNumberOfTheServicesYouUse().get(i) == 3){
                TheServices.set(i,"facts about cats")  ;
            }
            if (lead.getTheNumberOfTheServicesYouUse().get(i) == 4){
                TheServices.set(i,"facts about corona virus")  ;
            }
            if (lead.getTheNumberOfTheServicesYouUse().get(i) == 5){
                TheServices.set(i,"option 5")  ;
            }
        }
        return TheServices ;
    }

    public LinkedList<String> allTheServicesWeDid (){  //هني بترجع كل الخدمات الي تمت من اول ما اشتغل التطبيق
        LinkedList<String> allTheService = new LinkedList<>();
        for (Lead theLead : theLeads) {
            for (int i = 0; i < theLead.getTheNumberOfTheServicesYouUse().size(); i++) {
                if (theLead.getTheNumberOfTheServicesYouUse().get(i) == 1) {
                    allTheService.set(i, "jokes");
                }
                if (theLead.getTheNumberOfTheServicesYouUse().get(i) == 2) {
                    allTheService.set(i, "facts about the space");
                }
                if (theLead.getTheNumberOfTheServicesYouUse().get(i) == 3) {
                    allTheService.set(i, "facts about cats");
                }
                if (theLead.getTheNumberOfTheServicesYouUse().get(i) == 4) {
                    allTheService.set(i, "facts about corona virus");
                }
                if (theLead.getTheNumberOfTheServicesYouUse().get(i) == 5) {
                    allTheService.set(i, "option 5");
                }
            }
        }
        return allTheService ;
    }

    public LinkedList<String> namesThePeople(){    // بترجع اسماء كل الي حكو معها
        LinkedList<String> theNames = new LinkedList<>() ;
        for (Lead lead : theLeads ) {
            theNames.push(lead.getName()) ;
        }
        return theNames ; //hi//

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

            int connectTimeout = 5000;
            int socketTimeout = 10000;

            Unirest.setTimeouts(connectTimeout, socketTimeout);
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

    public static String findMostPopularActivity() {
        Map<String, Integer> activityCount = new HashMap<>();

        List<String> activities = new ArrayList<>();
        activities.add("jokes");
        activities.add("space");
        activities.add("cat facts");
        activities.add("corona virus");
        activities.add("quotes");
        for (String activity : activities) {
            activityCount.put(activity, activityCount.getOrDefault(activity, 0) + 1);
        }

        String mostPopularActivity = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : activityCount.entrySet()) {
            String activity = entry.getKey();
            int count = entry.getValue();

            if (count > maxCount) {
                mostPopularActivity = activity;
                maxCount = count;
            }
        }

        return mostPopularActivity;
    }
    public static String findMostActiveUser(Map<Long, LinkedList<Integer>> userChoices) {
        String mostActiveUser = null;
        int maxServicesUsed = 0;

        for (Map.Entry<Long, LinkedList<Integer>> entry : userChoices.entrySet()) {
            Long userId = entry.getKey();
            LinkedList<Integer> choices = entry.getValue();
            int numServicesUsed = choices.size();

            if (numServicesUsed > maxServicesUsed) {
                mostActiveUser = userId.toString();
                maxServicesUsed = numServicesUsed;
            }
        }

        return mostActiveUser;
    }

}