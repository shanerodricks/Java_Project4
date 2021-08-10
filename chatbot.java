import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ChatBot
{
    private final int FILLER_CHANCE = 20;
    InputSource inputSource;
    OutputDestination outputDestination;
    String inputText;
    Random random = new Random();


    public ChatBot(InputSource inputSource, OutputDestination outputDestination)
    {
        inputText = "";
        this.inputSource = inputSource;
        this.outputDestination = outputDestination;
    }

    public void chat()
    {
        while (isStillChatting())
        {
            getInput();
            if (isStillChatting())
                respond();
        }
    }

    private void getInput()
    {
        System.out.print("> ");
        inputText = inputSource.nextInput();
    }

    private void respond()
    {
        // send the user's input text to Language's processInput method.
        // Language will analyze it and be ready to create a response
        Language.processInput(inputText);

        // first, try to get Language to create a response based on keywords
        Language.tryToCreateAKeywordResponse();

        // sometimes, we can't find a keyword in the text to respond to
        // if we couldn't make a keyword response and  still need to make a response
        if (Language.stillNeedAResponse())
        {

            // create a response that doesn't use keywords (pick a random
            // noun or verb or adjective and respond based on that
            createNonKeywordResponse();
        }

        // show the response
        outputDestination.output(Language.getResponseText());
    }

    private void createNonKeywordResponse()
    {
    	// you should also be able to respond with some filler response "um, ok, sure" or whatever
        // you can get filler words using Language.getFillerText().  you can string them together
        // to create random unique filler responses of varying lengths

    	// one third of the time, use a filler word
    	// get a random number [0,2]
    	int num = random.nextInt(3);
    	// if num is 2, then print filler text
    	if(num == 2) {
        	System.out.print(Language.getFillerText() + " ");
    	}

        // you should also sometimes respond with "keep talking" phrases.  you can get
        // these by calling Language.getContinueText(), which will return a random "keep talking"
        // phrase.  you can combine these with filler responses, too.  "um, ok.  tell me more"

    	num = random.nextInt(5);
    	if(num == 4) {
    		System.out.print(Language.getContinueText() + " ");
    	}

        // pick a part of speech to respond to: noun, verb, adjective
        // you can use Language.inputHasNoun(), Language.inputHasVerb(),
        // or Language.inputHasAdjective() to see if you can use those when
        // you decide which to talk about.  you can randomly select one, or
        // come up with something more realistic

        // for that part of speech, you can call Language.createGeneralNounResponse(),
        // Language.createGeneralVerbResponse(), or Language.createGeneralAdjectiveResponse()

    	// get nouns, verbs, and adjectives from user input
//    	System.out.println(Language.getFoundPartsOfSpeech());
    	String[] nouns = getNouns(Language.getFoundPartsOfSpeech());
//    	System.out.println(Arrays.toString(nouns));
    	String[] verbs = getVerbs(Language.getFoundPartsOfSpeech());
//    	System.out.println(Arrays.toString(verbs));
    	String[] adjs = getAdjectives(Language.getFoundPartsOfSpeech());
//    	System.out.println(Arrays.toString(adjs));


    	if(Language.inputHasAdjective()){
    		// we can use a adj replacement
    		if(nouns.length > 0 && adjs.length > 0) {
    			Language.setResponseText("How do you feel about " + adjs[0] + " " + nouns[0] + "?");
    		}
    		else {
    			Language.createGeneralAdjectiveResponse();
    		}
    	}
    	else if(Language.inputHasVerb()) {
    		// we can use a verb replacement
    		if(verbs.length > 0 && nouns.length > 0) {
    			Language.setResponseText("How do you feel about " + verbs[0] + " with " + nouns[0] + "?");
    		}
    		else {
    			Language.createGeneralVerbResponse();
    		}
    	}
    	else if(Language.inputHasNoun()) {
    		// we can use a noun replacement
    		Language.createGeneralNounResponse();
    	}

        // finally, if you can't make sense of the input (no noun, verb, or adjective to respond
        // to), you can create a confusion response using Language.createConfusionResponse()

    	else {
    		Language.createConfusionResponse();
    	}
    }

	private String[] getNouns(String s) {
		int startBrace = s.indexOf("[");
		int endBrace = s.indexOf("]");
		if(endBrace - startBrace == 1) {
			String[] empty = new String[0];
			return empty;
		}
		String list = s.substring(startBrace + 1, endBrace);
		String[] tokens = list.split(", ");
		String[] vals = new String[tokens.length];
		int i = 0;
		for(String token : tokens) {
			// remove whitespace
			token = token.substring(0,token.indexOf(" "));
			vals[i++] = token;
		}
		return vals;
	}

	private String[] getVerbs(String s) {
		int startBrace = s.indexOf("[", s.indexOf("[") + 1);
		int endBrace = s.indexOf("]", s.indexOf("]") + 1);
		if(endBrace - startBrace == 1) {
			String[] empty = new String[0];
			return empty;
		}
		String list = s.substring(startBrace + 1, endBrace);
		String[] tokens = list.split(", ");
		String[] vals = new String[tokens.length];
		int i = 0;
		for(String token : tokens) {
			// remove whitespace
			token = token.substring(0,token.indexOf(" "));
			vals[i++] = token;
		}
		return vals;
	}

    private String[] getAdjectives(String s) {
    	int startBrace = s.indexOf("[", s.indexOf("[", s.indexOf("[") + 1) + 1);
		int endBrace = s.indexOf("]", s.indexOf("]", s.indexOf("]") + 1) + 1);
		if(endBrace - startBrace == 1) {
			String[] empty = new String[0];
			return empty;
		}
		String list = s.substring(startBrace + 1, endBrace);
		String[] tokens = list.split(", ");
		String[] vals = new String[tokens.length];
		int i = 0;
		for(String token : tokens) {
			vals[i++] = token;
		}
		return vals;
	}



	private boolean isStillChatting()
    {
        if (inputText.equals("bye"))
            return false;
        return true;
    }
}
