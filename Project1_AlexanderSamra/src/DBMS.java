import java.util.Scanner;

public class DBMS {
    private static BufferPool bp;

    public static void main(String[] args) {
        int size = 0;
        int mult = 1;
        for(int i = args.length-1; i >= 0; i--){
            size += Integer.parseInt(args[i]) * mult;
            mult *= 10;
        }
        //System.out.println(size);
        bp = new BufferPool(size);
        System.out.println("The program is ready for the next command");
        String commandLine;
        String command = "";
        Scanner scanner = new Scanner(System.in);

        while(!command.equals("END")){
            commandLine = scanner.nextLine();
            command = commandLine.substring(0, commandLine.indexOf(' '));
            switch (command){
                case "GET":
                    get(commandLine);
                    break;
                case "SET":
                    set(commandLine);
                    break;
                case "PIN":
                    pin(commandLine);
                    break;
                case "UNPIN":
                    unpin(commandLine);
                    break;
                default:
                    command = "END";
            }

            System.out.println("The program is ready for the next command");
        }
    }

    /**
     * Parses users input and runs the get function on the BufferPool
     * Prints the result of the function
     * @param command
     */
    public static void get(String command){
        int start = command.indexOf(' ') + 1;
        int record = Integer.parseInt(command.substring(start));
        int block = record / 100 + 1;
        record = record % 100;
        int frame = bp.inBuffer(block);
        String response = bp.getFrameContent(block, record);
        if(frame != -1){
            System.out.println("Block in BufferPool in frame " + frame);
            System.out.println(response);
        }
        else if (response != null){
            frame = bp.inBuffer(block);
            System.out.println("Block retrieved from disk and is now in frame " + frame);
            System.out.println(response);
        }
        else{
            System.out.println("Block unable to be retrieved from disk");
        }
    }

    /**
     * Parses users input and runs the set function on the BufferPool
     * Prints the result of the function
     * @param command
     */
    public static void set(String command){
        int start = command.indexOf(' ') + 1;
        String subString = command.substring(start);
        start = subString.indexOf(' ');
        int record = Integer.parseInt(subString.substring(0, start));
        start++;
        String newRecord = subString.substring(start);
        int block = record / 100 + 1;
        record = record % 100;
        int frame = bp.inBuffer(block);
        if(frame != -1){
            bp.getBuffers()[frame].changeRecord(record, newRecord);
            System.out.println("Successfully retrieved from memory in frame " + frame);
        }
        else if(bp.getFrameContent(block, record) != null){
            frame = bp.inBuffer(block);
            bp.getBuffers()[frame].changeRecord(record, newRecord);
            System.out.println("Successfully retrieved from disk and is now in frame " + frame);
        }
        else{
            System.out.println("Unsuccessful due to no free frames");
        }
    }

    /**
     * Parses users input and runs the pin function on the BufferPool
     * Prints the result of the function
     * @param command
     */
    public static void pin(String command){
        int start = command.indexOf(' ') + 1;
        int block = Integer.parseInt(command.substring(start));
        int frame = bp.inBuffer(block);
        if(frame != -1){
            if(bp.getBuffers()[frame].isPinned()){
                System.out.println("Frame " + frame + " is already pinned");
            }
            else{
                bp.getBuffers()[frame].setPinned(true);
                System.out.println("Frame " + frame + " is now pinned");
            }
        }
        else if(bp.getFrameContent(block, 0) != null){
            frame = bp.inBuffer(block);
            bp.getBuffers()[frame].setPinned(true);
            System.out.println("Frame " + frame + " is now pinned after being retrieved from the disk");
        }
        else{
            System.out.println("Unsuccessful due to no free frames");
        }
    }

    /**
     * Parses users input and runs the unpin function on the BufferPool
     * Prints the result of the function
     * @param command
     */
    public static void unpin(String command){
        int start = command.indexOf(' ') + 1;
        int block = Integer.parseInt(command.substring(start));
        int frame = bp.inBuffer(block);
        if(frame != -1){
            if(bp.getBuffers()[frame].isPinned()){
                bp.getBuffers()[frame].setPinned(false);
                System.out.println("Frame " + frame + " is now unpinned");
            }
            else{
                System.out.println("Frame " + frame + " is already unpinned");
            }
        }
        else{
            System.out.println("No block present to unpin");
        }
    }
}
