package net.kokkeli.data;

public enum Role {
    NONE(0),
    USER(1),
    ADMIN(2);
    
    private final int id;
    
    /**
     * Creates role with given id.
     * @param id
     */
    private Role(int id){
        this.id = id;
    }
    
    /**
     * Id of Role
     * @return
     */
    public int getId(){
        return id;
    }
    
    /**
     * Returns Role with given id.
     * @param id Id of role
     * @return Role with given id.
     */
    public static Role getRole(int id){
        
        for (Role r : Role.values()) {
            if (r.getId() == id){
                return r;
            }
        }
        throw new IndexOutOfBoundsException("There was no role with given Id.");
    }
}
