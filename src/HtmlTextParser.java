import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class HtmlTextParser {

    public static boolean isGood(char ch, char[] bad){
        for(char c : bad){
            if(ch == c){
                return false;
            }
        }
        return true;
    }

    public static Map<String, Integer> wordMap(String text, char[] chars){
        Map<String, Integer> map = new TreeMap<>();
        int start = 0;
        int end = 0;
        boolean inWord = false;
        char[] string = text.toCharArray();
        for(int i = 0 ; i < string.length; i++){
            if(isGood(string[i],chars)){
                if(inWord){
                    end++;
                }
                else {
                    start = i;
                    end = i;
                    inWord = true;
                }
            }
            else {
                if(inWord){
                    String s = new String(Arrays.copyOfRange(string,start,end+1));
                    map.put(s,map.get(s) == null ? 1 : map.get(s) + 1);
                    inWord = false;
                }
            }
        }
        if(inWord){
            String s = new String(Arrays.copyOfRange(string,start,end+1));
            map.put(s,map.get(s) == null ? 1 : map.get(s) + 1);
        }
        return map;
    }

    public static void main(String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        String url;
        Document document;
        try {
            writer.write("Enter the URL: ");
            writer.flush();
            url = reader.readLine();
            document = Jsoup.connect(url).get();
        } catch (IOException e){
            return;
        }

        PrintWriter pw;
        try {
            pw = new PrintWriter(document.title() + ".html");
            pw.write(document.html());
            pw.close();
        }catch (FileNotFoundException e){
            e.fillInStackTrace();
        }


        String text = document.text();
        char chars[] = new char[]{' ',',','.','!','?','-','"',';',':','[',']','(',')','«','»','\n','\r','\t'};
        Map<String, Integer> map = wordMap(text, chars);

        try {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
                writer.flush();
            }
            writer.close();
            reader.close();
        } catch (IOException e){
            e.getMessage();
        }
    }
}
