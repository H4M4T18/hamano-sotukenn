package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Video;

@Controller
public class VideoController {

    private List<Video> videos = new ArrayList<>();

    // コンストラクタでデータをロード
    public VideoController() {
        loadData();
        // データ件数を確認
        System.out.println("読み込んだ動画の数: " + videos.size());
    }

    // TSV ファイルから動画データをロードするメソッド
    private void loadData() {
        try {
            // resourcesフォルダから安全に読み込む
            ClassPathResource resource = new ClassPathResource("nhkforschool history banngumi.tsv");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                br.readLine(); // ヘッダをスキップ
                String line;
                while ((line = br.readLine()) != null) {
                    String[] cols = line.split(",", -1);
                    if (cols.length > 10) {
                        String title = cols[2];
                        String grade = cols[7];
                        String url = cols[5];
                        String keyword = cols[11];
                        videos.add(new Video(grade, title, url));

                        // 読み込み確認用（任意）
                        // System.out.println("読み込んだ動画: " + title + " | 学年: " + grade + " | URL: " + url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 学年一覧ページ
    @GetMapping("/")
    public String showGrades(Model model) {
        Set<String> grades = videos.stream()
                .map(Video::getGrade)
                .filter(g -> g != null && !g.isEmpty())
                .collect(Collectors.toCollection(TreeSet::new));
        model.addAttribute("grades", grades);
        return "index"; // templates/index.html を返す
    }

    // 選択した学年の動画一覧ページ
    @GetMapping("/videos")
    public String showVideos(@RequestParam("grade") String grade, Model model) {
        List<Video> filtered = videos.stream()
                .filter(v -> v.getGrade().equals(grade))
                .collect(Collectors.toList());
        model.addAttribute("grade", grade);
        model.addAttribute("videos", filtered);
        return "videos"; // templates/videos.html を返す
    }
}
