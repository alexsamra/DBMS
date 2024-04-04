import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BufferPool {
    private Frame[] buffers;

    /**
     * Returns the array of frames in the BufferPool
     * @return
     */
    public Frame[] getBuffers() {
        return this.buffers;
    }

    /**
     * Creates a BufferPool of length defined by size
     * @param size
     */
    public BufferPool(int size){
        this.buffers = new Frame[size];
        for(int i = 0; i < size; i++){
            this.buffers[i] = new Frame();
        }
    }

    /**
     * checks if a specific blockID is in any of the frames in the BufferPool
     * @param block
     * @return frame number or -1 if not found
     */
    public int inBuffer(int block){
        for(int i = 0; i < this.buffers.length; i++){
            if(this.buffers[i].getBlockID() == block){
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds record of specified block
     * If not in BufferPool calls method to try to retrive from disk
     * @param block
     * @param record
     * @return String of record
     */
    public String getFrameContent(int block, int record){
        int frameNumber = inBuffer(block);
        if(frameNumber != -1){
            return this.buffers[frameNumber].getRecord(record);
        }
        return getBlockFromFile(block, record);
    }

    /**
     * Calls method to find free Frame
     * If found updates the Frame with relevant information
     * @param block
     * @param record
     * @return record or null if no free Frame availible
     */
    private String getBlockFromFile(int block, int record){
        int free = findFreeFrame();
        if(free != -1){
            String fileName = "Project1/F" + block + ".txt";
            try {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                this.buffers[free].setContent(content);
                this.buffers[free].setBlockID(block);
                return this.buffers[free].getRecord(record);
            } catch (IOException e) {
                System.out.println("An error occurred");
            }
        }
        return null;
    }

    /**
     * Determines if any Frames are empty
     * If not calls method to remove a Frame
     * @return Frame number or -1 if no frames can be found or removed
     */
    private int findFreeFrame(){
        for(int i = 0; i < this.buffers.length; i++){
            if(buffers[i].getBlockID() == -1){
                return i;
            }
        }
        return removeFrame();
    }

    /**
     * Removes Frame that is unpinned
     * If Dirty calls method to update disk
     * @return frame removed or -1 if no frame is removed
     */
    private int removeFrame(){
        for(int i = 0; i < this.buffers.length; i++){
            if(!buffers[i].isPinned()){
                if(buffers[i].isDirty()){
                    updateFile(i);
                    buffers[i].setDirty(false);
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * Updates Disk of specified Frame before Frame is removed
     * @param frame
     */
    private void updateFile(int frame){
        String fileName = "Project1/F" + buffers[frame].getBlockID() + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName, false);
            fileWriter.write(buffers[frame].getContent());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
