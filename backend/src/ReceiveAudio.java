import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import org.json.*;

@WebServlet
@MultipartConfig
public class ReceiveAudio extends HttpServlet {
    ServletContext servletContext;
    ArrayList<String>[] words;
    HttpSession session;

    @Override
    public void init() throws ServletException {
        servletContext = getServletContext();
        if(servletContext.getAttribute("words")==null) {
            words = new ArrayList[3];
            for(int i=0; i<3; i++)
                words[i] = new ArrayList<>();
            try {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("easy.txt"), "utf-8"));
                while((line = br.readLine())!=null) {
                    words[0].add(line);
                    //System.out.println(line);
                }
                br = new BufferedReader(new InputStreamReader(new FileInputStream("mid.txt"), "utf-8"));
                while((line = br.readLine())!=null) {
                    words[1].add(line);
                }
                br = new BufferedReader(new InputStreamReader(new FileInputStream("hard.txt"), "utf-8"));
                while((line = br.readLine())!=null) {
                    words[2].add(line);
                }
                servletContext.setAttribute("words", words);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            words = (ArrayList<String>[])servletContext.getAttribute("words");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        session = request.getSession();
        System.out.println("receive POST FILE");
        
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "http://10.1.1.27");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        PrintWriter pw = response.getWriter();

        int idx1=0;
        if(request.getHeader("Difficulty")!=null)
            idx1 = java.lang.Integer.parseInt(request.getHeader("Difficulty"));
        int idx2 = new Random().nextInt(words[idx1].size());

        Part filePart = request.getPart("audio-blob");
        InputStream fileContent = filePart.getInputStream();
        ByteArrayOutputStream af = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int i = 0;
        while((i = fileContent.read(b,0,1024))>0){
            af.write(b,0,i);
        }

        if(new String(af.toByteArray()).equals("null")) {
            JSONObject njo = new JSONObject();
            njo.put("next", words[idx1].get(idx2));
            njo.put("result", new JSONArray());
            System.out.println(njo);
            pw.print(njo.toString());
            session.setAttribute("last", words[idx1].get(idx2));
            System.out.println("First connection, "+ words[idx1].get(idx2));
        }
        else {
            String ret = PostApi.postToIbm(af.toByteArray());
            JSONObject readRet = new JSONObject(ret);
            JSONArray ja = readRet.getJSONArray("results");
            if(ja.length()==0) {
                JSONObject njo = new JSONObject();
                njo.put("next", session.getAttribute("last"));
                njo.put("result", new JSONArray());
                System.out.println(njo);
                pw.print(njo.toString());
                System.out.println(session.getAttribute("last"));
            }
            else {
                String sb = (String)ja.getJSONObject(0).getJSONArray("alternatives").getJSONObject(0).get("transcript");
                sb = sb.replaceAll(" ", "");
                boolean[] sab = LCS.getSameOfB((String)session.getAttribute("last"), sb);
                System.out.printf("Last: %s, This: %s\n", session.getAttribute("last"), sb);
                JSONObject out = new JSONObject();
                JSONArray out1 = new JSONArray();
                for(int ii=0; ii<sab.length; ii++) {
                    JSONObject temp = new JSONObject();
                    temp.put("character", ""+sb.charAt(ii));
                    temp.put("right", sab[ii]);
                    out1.put(temp);
                }
                out.put("result", out1);
                out.put("next", words[idx1].get(idx2));
                session.setAttribute("last", (String)words[idx1].get(idx2));
                pw.print(out.toString());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("please POST");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://10.1.1.27");
        resp.setHeader("Access-Control-Allow-Headers", "Difficulty");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
