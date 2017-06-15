package com.github.jotask.kimo.util.notification;

import java.util.Random;

/**
 * Message
 *
 * @author Jose Vives Iznardo
 * @since 29/04/2017
 */
public enum Message {

    n01("Kimo!", "I can't wait to see you again!"),
    n02("Kimo!", "I'm hot!"),
    n03("Kimo!", "I fucking love you!"),
    n04("Kimo!", "I Love You"),
    n05("Kimo!", "Master want his blowjob!"),
    n06("Kimo!", "Master wants you!"),
    n07("Kimo!", "Are you being a good girl?"),
    n08("Kimo!", "Doggy tonight?"),
    n09("Kimo!", "Viva Mexico!"),
    n10("Kimo!", "Doggy tonight?"),
    n11("Kimo!", "Master want his girl")
    ;

    final String title;
    final String text;

    Message(String title, String text) {
        this.title = title;
        this.text = text;
    }

    static Message getRandom(){ return Message.values()[new Random().nextInt(Message.values().length)]; }

}
