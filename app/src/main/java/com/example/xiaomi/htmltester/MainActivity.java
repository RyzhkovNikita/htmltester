package com.example.xiaomi.htmltester;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setText(getString(R.string.app_name));

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener((View view) -> {
            new Thread(() -> {
                try {
                    MainActivity.this.runOnUiThread(() -> {
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        progressBar.setIndeterminate(true);
                    });
                    Document document = Jsoup.connect("https://students.bmstu.ru/schedule/6e9f2bac-2359-11e7-a181-005056960017").get();
                    Log.d(TAG, "onCreate: " + document.title());
                    Elements tables = document.getElementsByClass("hidden-lg");
                    Log.d(TAG, "gotTables");
                    String pare, pare2, time, dayOfWeek, room, room2, teacher, teacher2, type, type2;
                    boolean isPared;
                    for (org.jsoup.nodes.Element table : tables) {
                        Elements elements = table.getElementsByTag("tr");
                        Log.d(TAG, "gotLines");
                        for (org.jsoup.nodes.Element element : elements) {
                            pare = pare2 = time = dayOfWeek = room = room2 = teacher = teacher2 = type = type2 = "";
                            isPared = false;
                            Elements tdTags = element.getElementsByTag("td");
                            for (org.jsoup.nodes.Element elem : tdTags) {
                                if (elem.hasClass("text-nowrap")) {
                                    time = elem.text();
                                } else if (elem.hasClass("text-info")) {
                                    pare = elem.getElementsByTag("span").text();
                                    isPared = true;
                                    Elements attributes = elem.getElementsByTag("i");
                                    if (attributes.size() != 0) {
                                        type = attributes.get(0).text();
                                        room = attributes.get(1).text();
                                        teacher = attributes.get(2).text();
                                    }
                                } else if (elem.hasClass("text-success")) {
                                    pare2 = elem.getElementsByTag("span").text();
                                    isPared = true;
                                    Elements attributes = elem.getElementsByTag("i");
                                    if (attributes.size() != 0) {
                                        type2 = attributes.get(0).text();
                                        room2 = attributes.get(1).text();
                                        teacher2 = attributes.get(2).text();
                                    }
                                } else if (elem.hasAttr("colspan")) {
                                    pare = elem.getElementsByTag("span").text();
                                    Elements attributes = elem.getElementsByTag("i");
                                    if (attributes.size() != 0) {
                                        type = attributes.get(0).text();
                                        room = attributes.get(1).text();
                                        teacher = attributes.get(2).text();
                                    }
                                }
                            }
                            Log.d(TAG, "TIME: " + time);
                            Log.d(TAG, type + pare + " ___ " + room + " ___ " + teacher);
                            Log.d(TAG, type2 + pare2 + " ___ " + room2 + " ___ " + teacher2);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    Log.e(TAG, "Интернета нет");
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: " + e.toString());
                }
                MainActivity.this.runOnUiThread(() -> {
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setIndeterminate(false);
                });
            }).start();
        });


    }
}
