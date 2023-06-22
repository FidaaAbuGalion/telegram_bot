package org.example;

import org.glassfish.grizzly.strategies.LeaderFollowerNIOStrategy;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NoorBot extends TelegramLongPollingBot  {
    private List <Long> chatId ;


    public NoorBot (){
        this.chatId = new ArrayList<>() ;
    }

    @Override
    public String getBotToken() {
        return "6143807633:AAFe2Ag8edG7Qi_jZpasAUsKVq-7y5NjUhE";
    }
//    private long getChatId(Update update){
//        long chatId = 0 ;
//        if (update.getMessage() != null){
////            chatId
//        }
//    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        long chatId = update.getMessage().getChatId() ;
//        Lead lead = new Lead() ;  //شوفي محلكات لييد هي محلكا فيها معلومات الشخص الي فات يحكي مع البوت بنسجله حسب التشات تبعه موجود الشرح عن شاي فلدرس الموكلات
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (!this.chatId.contains(chatId)){

                sendMessage.setText(" do you support me for my office ?");
                InlineKeyboardButton ButtonYes = new InlineKeyboardButton(" Yes");
                ButtonYes.setCallbackData(" Yes");
                InlineKeyboardButton ButtonNo = new InlineKeyboardButton("No");
                ButtonNo.setCallbackData(" No");
                List<InlineKeyboardButton> NoYesButtons = Arrays.asList(ButtonYes, ButtonNo);
                List<List<InlineKeyboardButton>> keyboardYesNo = Arrays.asList(NoYesButtons);
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(keyboardYesNo);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                sendMessage.setText("choose from the option");  //هني بتجيه الرساله مع زرين لازم اذا حط no توقف التوخناه واذا حط yes يطلب منه اسمه وهويته ويسجلهن ف lead وكذي بنحفظ كل يوزر وبعدين بتجيه الرساله الي تحت

            sendMessage.setText("Welcome, you are now with the best bot. I have a lot of options for you\n" +
                    "1: Some of the funny jokes\n" +
                    "2: Random facts about cats\n" +
                    "3: Information about countries\n" +
                    "4: Statistical information about the Corona virus\n" +
                    "5: Information about space " + "\n" +
                    "do you want to ask ?");
            this.chatId.add(chatId) ;
        }else {
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
            sendMessage.setText("choose from the option");  //هني لازم يختار رقم الي وديا يسر عنده حسب طلبه بندبيس له
            //كل الاشياء الي هني هنه الاشياء الي لازم البوت يسويها كلشي بينكتب هني ودايما لازم يكون في اضافات على محلكات BotActivities

        }




//        sendMessage.setChatId(update.getMessage().getChatId());
//        sendMessage.setText("fout");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "Noor64fbot";
    }
}
