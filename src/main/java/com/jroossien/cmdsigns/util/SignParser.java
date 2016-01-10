package com.jroossien.cmdsigns.util;

import com.jroossien.cmdsigns.config.messages.Msg;
import com.jroossien.cmdsigns.config.messages.Param;
import com.jroossien.cmdsigns.signs.SignTemplate;

import java.util.ArrayList;
import java.util.List;

public class SignParser {

    private List<Argument> arguments = new ArrayList<Argument>();
    private String error = null;

    public SignParser(SignTemplate template, String[] signText) {
        //Go through each line.
        for (int i = 0; i < 4; i++) {
            String syntax = template.getSyntax(i);
            String text = Util.stripAllColor(signText[i]);
            char[] syntaxChars = syntax.toCharArray();
            char[] textChars = text.toCharArray();

            String placeholder = null; //Gets set when a { is found and gets set back to null at a }
            Argument argument = null; //Gets set when a } is found and the syntax is set.
            int textCharIndex = 0; //The character index of the sign text that we are pasing.
            for (char ch : syntaxChars) {
                if (ch == '{') {
                    //Start setting the syntax of the placeholder.
                    placeholder = "";
                    continue;
                }
                if (ch == '}') {
                    //Create an argument based on the syntax of the placeholder.
                    argument = new Argument(placeholder);
                    continue;
                }
                if (placeholder != null) {
                    //Add character to placeholder syntax till a } is found.
                    placeholder += ch;
                    continue;
                }

                if (argument == null) {
                    //Match regular characters. (in between, before or after placeholder syntax.)
                    if (ch != textChars[textCharIndex]) {
                        error = Msg.INVALID_SYNTAX.getMsg(true, true, Param.P("{found-char}", textChars[textCharIndex]), Param.P("{expected-char}", ch),
                                Param.P("{line}", i+1), Param.P("{syntax}", template.getSyntax(i)));
                        return;
                    }
                    textCharIndex++;
                } else {
                    //Get the text value for the current placeholder syntax.
                    String match = "";
                    do {
                        if (textCharIndex < textChars.length) {
                            match += textChars[textCharIndex++];
                        }
                    } while (textCharIndex < textChars.length && textChars[textCharIndex] != ch);
                    textCharIndex++;

                    argument.setValue(match);

                    arguments.add(argument);
                    argument = null;
                }
            }
            //If there are remaining characters and the argument hasn't been set parse those.
            if (argument != null) {
                String match = "";
                while (textCharIndex < textChars.length) {
                    match += textChars[textCharIndex];
                    textCharIndex++;
                }
                argument.setValue(match);
                arguments.add(argument);
            }
        }
    }

    public boolean isValid() {
        return error == null;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public String getError() {
        return error;
    }

}
