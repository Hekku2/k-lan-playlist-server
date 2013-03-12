package net.kokkeli;

import org.junit.Assert;

import org.junit.Test;

public class TestValidationUtis {
    
    @Test
    public void testContainsOnlyNumbersAndLettersAndWhitespace(){
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace(""));
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("Jeah"));
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("9"));
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("343asdfsdGGfds"));
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("ööääÖÄ"));
        Assert.assertTrue(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("Pelle Hermanni"));
        
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("gsdgd\n"));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("gsd\td"));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace(null));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("<html>"));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("_"));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("%#"));
        Assert.assertFalse(ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace("("));
    }
    
    @Test
    public void testIsEmptyWorks(){
        Assert.assertTrue(ValidationUtils.isEmpty(""));
        Assert.assertTrue(ValidationUtils.isEmpty(" "));
        Assert.assertTrue(ValidationUtils.isEmpty(null));
        Assert.assertTrue(ValidationUtils.isEmpty("\t"));
        Assert.assertTrue(ValidationUtils.isEmpty("\n"));

        Assert.assertFalse(ValidationUtils.isEmpty("H"));
        Assert.assertFalse(ValidationUtils.isEmpty("Harmi"));
        Assert.assertFalse(ValidationUtils.isEmpty("       \tH"));
    }
    
    @Test
    public void testIsValidUsername(){
        Assert.assertTrue(ValidationUtils.isValidUsername("ghjjk"));
        Assert.assertTrue(ValidationUtils.isValidUsername("543"));
        Assert.assertTrue(ValidationUtils.isValidUsername("543dfasfd"));
        Assert.assertTrue(ValidationUtils
                .isValidUsername("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        Assert.assertFalse(ValidationUtils.isValidUsername(""));
        Assert.assertFalse(ValidationUtils.isValidUsername("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        Assert.assertFalse(ValidationUtils.isValidUsername(" fdasf"));
        Assert.assertFalse(ValidationUtils.isValidUsername("    "));
        Assert.assertFalse(ValidationUtils.isValidUsername("_"));
        Assert.assertFalse(ValidationUtils.isValidUsername(null));
    }
}
