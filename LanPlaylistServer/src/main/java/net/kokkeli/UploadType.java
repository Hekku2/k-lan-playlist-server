package net.kokkeli;

public enum UploadType {
    NONE(0, ""),
    UPLOAD(1, "upload"),
    VLC(2, "vlc"),
    YOUTUBEDL(3, "youtubeDl");
    
    private final int id;
    private final String text;
    private UploadType(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId(){
        return id;
    }
    
    public String getText(){
        return text;
    }

    public static UploadType getUploadType(int id){
        
        for (UploadType type : UploadType.values()) {
            if (type.getId() == id){
                return type;
            }
        }
        throw new IndexOutOfBoundsException("There was no UploadType with given Id.");
    }
    
    public static UploadType getUploadType(String text){
        
        for (UploadType type : UploadType.values()) {
            if (type.getText().equals(text)){
                return type;
            }
        }
        throw new IndexOutOfBoundsException("There was no UploadType with given text " + text);
    }
}
