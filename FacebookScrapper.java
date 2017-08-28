/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookscrapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author sakib
 */
public class FacebookScrapper extends Application {
    
    ArrayList<Element> storyElements = new ArrayList<Element>();
    int post_no = 0;
    
    @Override
	public void start(Stage stage) throws Exception {
		WebView myWebView = new WebView();
		WebEngine engine = myWebView.getEngine();
		
                crap();
		Button btn = new Button("Load Google");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				engine.load("https://www.google.com");
			}
		});
		
		Button btn2 = new Button("Execute JavaScript");
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				engine.executeScript("window.location = \"https://www.youtube.com/\";");
			}
		});
		
		Button btn3 = new Button("Next");
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
                                if(post_no == 4){
                                    post_no = -1;
                                }
                                Document doc = storyElements.get(++post_no).ownerDocument();
				engine.loadContent(doc.toString());
			}
		});
                
                Button btn5 = new Button("Previous");
		btn5.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
                                if(post_no == 0){
                                    post_no = 5;
                                }
                                Document doc = storyElements.get(--post_no).ownerDocument();
				engine.loadContent(doc.toString());
			}
		});
		
		Button btn4 = new Button("Reload");
		btn4.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				engine.reload();
			}
		});
		
		engine.setUserAgent("Mozilla/5.0 (Linux; U; Android 4.4.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		
		
		VBox root = new VBox();
		root.getChildren().addAll(myWebView, btn3, btn5);
		
		Scene scene = new Scene(root, 800, 500);
		stage.setScene(scene);
		
		stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void crap() throws IOException{
        
        // TODO code application logic here
        String cssQuery; 
        
        Connection.Response response = 
                Jsoup.connect("http://mbasic.facebook.com")
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .method(Connection.Method.GET)
                .followRedirects(true)
                .execute();

        //parse the document from response
        Document document = response.parse();
//        System.out.println(document);

        //get cookies
        Map<String, String> mapCookies = response.cookies();
        cssQuery = "input";
        Elements inputs = document.select(cssQuery);
        System.out.println(inputs.size());
        
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        for(Element input : inputs){
            names.add(input.attr("name"));
            values.add(input.attr("value"));
        }
        
        System.out.println(Arrays.toString(names.toArray()) + Arrays.toString(values.toArray()));
        
        Elements form = document.select("form");
        String postURL = form.attr("action");
        System.out.println(postURL);
        
        
        response = Jsoup.connect(postURL)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .method(Connection.Method.POST)
                .data(names.get(0), values.get(0))
                .data(names.get(1), values.get(1))
                .data(names.get(2), values.get(2))
                .data(names.get(3), values.get(3))
                .data(names.get(4), values.get(4))
                .data(names.get(5), "email")
                .data(names.get(6), "password")
                .data(names.get(7), values.get(7))
                .data(names.get(8), values.get(8))
                .cookies(mapCookies)
                .followRedirects(true)
                .execute();
        
        
        document = response.parse();
//        System.out.println(document);
        
        
        mapCookies = response.cookies();
        System.out.println("cookies"+Arrays.toString(mapCookies.entrySet().toArray()));
        cssQuery = "input";
        inputs = document.select(cssQuery);
        System.out.println(inputs.size());
        
        names = new ArrayList<String>();
        values = new ArrayList<String>();
        for(Element input : inputs){
            names.add(input.attr("name"));
            values.add(input.attr("value"));
        }
        
        System.out.println(Arrays.toString(names.toArray()) + Arrays.toString(values.toArray()));
        
        form = document.select("form");
        postURL = form.attr("action");
        System.out.println(postURL);
        Element not_now = form.get(0).nextElementSibling();
        postURL = not_now.attr("href");
        postURL = "http://mbasic.facebook.com" + postURL;
        System.out.println(postURL);
        
        
//        Connection con = Jsoup.connect(postURL)
//                .userAgent("Mozilla/5.0")
//                .timeout(10 * 1000)
//                .method(Connection.Method.GET)
//                .cookies(mapCookies);
//        
//        document = Jsoup.parse(new String(con.execute().bodyAsBytes(),"UTF-8"));
//        System.out.println(document);
        
        
        
        response = Jsoup.connect(postURL)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .method(Connection.Method.GET)
                .cookies(mapCookies)
                .followRedirects(true)
                .execute();

        //parse the document from response
        document = response.parse();
//        mapCookies.clear();
//        mapCookies.putAll(response.cookies());
        //System.out.println(document);
        
        
        
        response = Jsoup.connect("https://mbasic.facebook.com/groups/143463626153063")
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .method(Connection.Method.GET)
                .cookies(mapCookies)
                .followRedirects(true)
                .execute();

        //parse the document from response
        document = response.parse();
        
        BufferedWriter  writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter("C:\\Users\\sakib\\Desktop\\java\\a.html"));
            writer.write(document.toString());

        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }
        finally{
            writer.close();
        }
        
//      finished putting group feed in a.html
        
        Elements links = document.select("a[href]");
        ArrayList<String> stories = new ArrayList<String>();
        
        for(Element link : links){
            if(link.text().matches("Full Story")){
                stories.add("https://mbasic.facebook.com" + link.attr("href"));
            }
        }
        
        System.out.println(stories.size() + Arrays.toString(stories.toArray()));
        //        System.exit(0);
        try {
            
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i=0;
        for(String link : stories){
            System.out.println("cookies" + Arrays.toString(mapCookies.entrySet().toArray()));
            System.out.println(link);
            response = Jsoup.connect(link)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .method(Connection.Method.GET)
                .followRedirects(true)
                .cookies(mapCookies)
                .execute();

            document = response.parse();
            storyElements.add(document.body());
            try
            {
                writer = new BufferedWriter( new FileWriter("C:\\Users\\sakib\\Desktop\\java\\stories"+i+".html"));
                writer.write(document.toString());

            }
            catch ( IOException e)
            {
                e.printStackTrace();
                
            }
            finally{
                writer.close();
                i++;
            }
            
        }
        
        for(Element story : storyElements){
            String person = story.getElementsByTag("h3").get(0).getElementsByTag("strong").get(0).text();
            String post = story.getElementsByTag("h3").get(0).parent().nextElementSibling().getAllElements().text();
            System.out.println(person + " | "+post);
            break;
        }
        

//        //get cookies
//        mapCookies = response.cookies();
//        世界你好
//        
//        document = Jsoup.connect("https://mbasic.facebook.com").get();
//        System.out.println(document);
//        cssQuery = "input[name=\"email\"]";
//        Elements email = document.select(cssQuery);
//        cssQuery = "input[name=\"pass\"]";
//        Elements pass = document.select(cssQuery);
//        System.out.println(email.get(0).attr("class") + " | " + pass.get(0).attr("class"));
    }
    
    
}
