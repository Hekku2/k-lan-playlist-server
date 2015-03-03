package net.kokkeli.data.dto;

public enum Role {
    NONE(0),
    ANYNOMOUS(1),
    USER(2),
    ADMIN(3);
    
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
