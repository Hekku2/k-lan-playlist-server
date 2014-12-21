package net.kokkeli.resources;

public enum AuthenticationErrorHandling {
    DEFAULT(0),
    RETURN_CODE(1);
    
    private final int id;
    
    /**
     * Creates AuthenticationErrorHandling with given id.
     * @param id
     */
    private AuthenticationErrorHandling(int id){
        this.id = id;
    }
    
    /**
     * Id of AuthenticationErrorHandling
     * @return
     */
    public int getId(){
        return id;
    }
    
    /**
     * Returns AuthenticationErrorHandling with given id.
     * @param id Id of role
     * @return AuthenticationErrorHandling with given id.
     */
    public static AuthenticationErrorHandling getAuthenticationErrorHandling(int id){
        
        for (AuthenticationErrorHandling r : AuthenticationErrorHandling.values()) {
            if (r.getId() == id){
                return r;
            }
        }
        throw new IndexOutOfBoundsException("There was no AuthenticationErrorHandling with given Id.");
    }
}
