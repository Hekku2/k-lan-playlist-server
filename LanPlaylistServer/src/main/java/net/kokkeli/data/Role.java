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
}
