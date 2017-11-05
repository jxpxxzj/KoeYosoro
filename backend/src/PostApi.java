import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Base64;

public class PostApi {
    static final String username = "ce1c407d-3f78-4369-bb3b-bd640e50d40f";
    static final String password = "0d5Mf5VMpFs1";
    static final String serviceURL = "https://stream.watsonplatform.net/speech-to-text/api/v1/recognize?model=zh-CN_BroadbandModel";

    static String postToIbm(byte[] audioFile) {
        try {
            URL url = new URL(serviceURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // TODO: change wav
            connection.addRequestProperty("Content-Type", "audio/wav");
            connection.addRequestProperty("Transfer-Encoding", "chunked");
            String encoding = Base64.getEncoder().encodeToString(new String(username+":"+password).getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.connect();

            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());

            ds.write(audioFile);
            ds.flush();
            ds.close();

            int resultCode=connection.getResponseCode();
            if(HttpURLConnection.HTTP_OK==resultCode){
                StringBuffer sb=new StringBuffer();
                BufferedReader responseReader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                String readLine;
                while((readLine=responseReader.readLine())!=null){
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                return sb.toString();
            }
            else {
                System.out.println(connection.getResponseMessage());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
