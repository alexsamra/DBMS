import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Frame {
    private byte[] content;
    private boolean dirty;
    private boolean pinned;
    private int blockID;

    public Frame(){
        this.content = new byte[4000];
        this.dirty = false;
        this.pinned = false;
        this.blockID = -1;
    }

    /**
     * Gets the record within a block
     * @param i
     * @return String of specified record
     */
    public String getRecord(int i){
        byte[] recordArray = Arrays.copyOfRange(this.content, (i - 1) * 40, i * 40);
        return new String(recordArray, StandardCharsets.UTF_8);
    }

    /**
     * Alters specified record within a block
     * @param i
     * @param record
     */
    public void changeRecord(int i, String record){
        System.out.println(i + " " + record);
        byte[] newRecord = record.getBytes();
        for(int j = 0; j < 40; j++){
            this.content[j + (40 * (i-1))] = newRecord[j];
        }
        this.dirty = true;
    }

    public int getBlockID(){
        return this.blockID;
    }

    public void setBlockID(int x){
        this.blockID = x;
    }

    public String getContent(){
        return new String(this.content, StandardCharsets.UTF_8);
    }

    public void setContent(String x){
        this.content = x.getBytes();
    }
    public boolean isDirty(){
        return this.dirty;
    }

    public void setDirty(Boolean x){
        this.dirty = x;
    }

    public boolean isPinned(){
        return this.pinned;
    }

    public void setPinned(Boolean x){
        this.pinned = x;
    }
}
