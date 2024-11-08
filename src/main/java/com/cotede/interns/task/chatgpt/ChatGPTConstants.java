package com.cotede.interns.task.chatgpt;

public class ChatGPTConstants {


    public static final String CHAT_GPT_PROMPT = """
            
            Generate a JSON object with a case scenario (2 lines scenario), 4 characters (just one of them is the criminal), and a paragraph explaining why the character is the criminal (just one of the characters is the criminal). The scenario can be a murder, theft, blackmail or any kind of criminal case and it should be an interesting story. Just Send To me The structure without any additional word should look like this:\

            {
              "id": null,
              "scenario": "",
              "answerParagraph": "",
              "characters": [
                {
                  "id": null,
                  "name": "",
                  "age": null,
                  "gender": true or false,
                  "occupation": "",
                  "isCriminal": true or false,
                  "alibi": "",
                  "motive": ""
                },
                {
                  "id": null,
                  "name": "",
                  "age": null,
                  "gender": true or false,
                  "occupation": "",
                  "isCriminal": true or false,
                  "alibi": "",
                  "motive": ""
                },
                {
                  "id": null,
                  "name": "",
                  "age": null,
                  "gender": true or false,
                  "occupation": "",
                  "isCriminal": true or false,
                  "alibi": "",
                  "motive": ""
                },
                {
                  "id": null,
                  "name": "",
                  "age": null,
                  "gender": true or false,
                  "occupation": "",
                  "isCriminal": true or false,
                  "alibi": "",
                  "motive": ""
                }
              ]
            }""";
}
