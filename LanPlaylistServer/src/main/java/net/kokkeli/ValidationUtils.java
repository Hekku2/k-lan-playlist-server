package net.kokkeli;

/**
 * Collection for different static validation utils.
 * @author Hekku2
 *
 */
public final class ValidationUtils {
    /**
     * Note: [\d\w]* is not good enough, it doesn't accept ö or ä.
     */
    private static final String REGEX_ONLY_LETTER_AND_NUMBERS = "[\\p{L}\\p{N} ]*";
    
    private static final String REGEX_USERNAME = "[a-zA-Z0-9]+";
    
    /**
     * Matches <, > and &#
     */
    private static final String REGEX_GENERAL_INPUT = ".*(&#|<|>).*"; 
    
    /**
     * Checks if given string contains only numbers and letters.
     * @param input input string. If it's null, false is returned.
     * @return True, if given string contains only numbers and letters.
     */
    public static boolean containsOnlyNumbersAndLettersAndWhiteSpace(String input){
        if (input == null)
            return false;
        
        return input.matches(REGEX_ONLY_LETTER_AND_NUMBERS);
    }
    
    /**
     * Checks if string contains only white space charachters.
     * @param input Input string. If it's null, true is returned.
     * @return True, if input contains only white space.
     */
    public static boolean isEmpty(String input){
        if (input == null)
            return true;
        
        return input.trim().length() == 0;
    }
    
    /**
     * Checks if username is valid. Username can only contain small and big letters and numbers.
     * @param username Username. If it's null, false is returned.
     * @return True, if username is valid.
     */
    public static boolean isValidUsername(String username){
        if (username == null)
            return false;
        
        return username.matches(REGEX_USERNAME) && username.length() <= 255;
    }
    
    /**
     * Validates general input
     * @param input
     * @return
     */
    public static boolean isValidInput(String input){
        if (isEmpty(input))
            return false;
        
        return !input.matches(REGEX_GENERAL_INPUT) && input.length() <= 255;
    }
    
    /**
     * Hidden constructor.
     */
    private ValidationUtils(){}
}
