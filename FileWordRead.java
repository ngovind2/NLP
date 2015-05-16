//package calculatenr;

//import java.io.File;
import java.io.*;
import java.util.LinkedList;
import java.util.Iterator;

public class FileWordRead{

    private LinkedList<String> tokens;
    
    // constructor which starts reading with the beginning of the file
    public FileWordRead(String name) throws java.io.IOException
    {
        tokens = new LinkedList<String>();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(name));

        // now parse the input file and put all tokens into the linked list
        
        int ch = in.read();
        int position = 0;
  
        boolean  endOfFile = false;
        
        while ( ch != -1 )
        {
            while ( isWhiteSpace((char) ch) ){
                ch = in.read();
                position++;
                
                if ( ch == -1 ){
                    endOfFile = true;
                    break;
                }
            }
            
            if ( ! endOfFile )
            {
                StringBuffer buf = new StringBuffer();
                buf.append( Character.toLowerCase((char) ch));  
            
                if ( !isLetter((char) ch ) ){  // token is a single non-letter character 
                    ch = in.read();
                    position++;
                }   
                else {
                    ch = in.read();
                    position++;
                
                    while ( isLetterNumberSpecialChar_((char) ch) )
                    {  // token starts with a letter and consits of consequitive letters and numbers
                        buf.append((char) ch);
             
                        ch = in.read();
                        position++;
                    }
                }
            
                if ( buf.charAt(0) >=  'a' && buf.charAt(0) <=  'z' )
                    tokens.addLast(buf.toString());            
                //System.out.print(buf.toString());
                //System.out.print("#");
            }
        }
        
        in.close();        
    }

    public Iterator<String> getIterator(){
        return(tokens.listIterator());
    }
    
            
    // this function checks if the input character is a letter
    private boolean isLetter( char in ){
        char nextChar = Character.toLowerCase((char) in);
        if ( (nextChar >= 'a' && nextChar <= 'z')  )
            return (true);
        else return(false);
    }

    private boolean isWhiteSpace( char in ){
        char nextChar = Character.toLowerCase((char) in);
        if ( (nextChar >= 33 && nextChar <= 126)  )
            return (false);
        else return(true);
    }

    // this function checks if the input character is a letter or a number
    private boolean isLetterNumberSpecialChar_( char in ){
        char nextChar = Character.toLowerCase((char) in);
        if ( (nextChar >= 'a' && nextChar <= 'z') || (nextChar >= '0' && (nextChar <= '9')) || nextChar == '_')
            return (true);
        else return(false);
    }

}





