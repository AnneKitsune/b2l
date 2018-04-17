package net.supercraft.B2L.Log;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Logger{
    public String name = "DefaultLogger";
    public boolean printName = true;
    public boolean printDate = true;
    public boolean printLevel = true;
    
    private PrintStream printStream = System.out;
    public Logger(){
        
    }
    public Logger(String name){
        this.name = name;
    }
    public void log(String msg,LogLevel level){
        StringBuilder out = new StringBuilder();
        if(printName){
            out.append(name);
            out.append(" ");
        }
        if(printDate){
            out.append(getCurrentFormattedTime());
            out.append(" ");
        }
        if(printLevel){
            out.append(level.name);
            out.append(" ");
        }
        printStream.append(out);
    }
    public String getCurrentFormattedTime(){
        return getCurrentFormattedTime("yyyy-MM-dd HH:mm:ss");
    }
    public String getCurrentFormattedTime(String format){
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(cal);
    }
}