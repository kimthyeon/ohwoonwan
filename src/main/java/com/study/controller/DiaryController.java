package com.study.controller;

import com.study.entity.Diary;
import com.study.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryRepository diaryRepository;

    @GetMapping("/list")
    public String list(@RequestParam(required = false, defaultValue = "") String searchTitle,
                            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Model model) {
        Page<Diary> diarys = diaryRepository.findByTitleContaining(searchTitle, pageable);
        int startPage = Math.max(1, diarys.getPageable().getPageNumber() - 4);
        int endPage = Math.min(diarys.getTotalPages(), diarys.getPageable().getPageNumber() + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute(   "diarys", diarys);

        return "diarylist";
    }

    @GetMapping("/write")
    public String writeView() {
        return "diarywrite";
    }

    @PostMapping("/write")
    public String write(Diary diary, Model model) {
        diaryRepository.save(diary);

        model.addAttribute("message", "글 작성이 완료되었습니다!");
        model.addAttribute("searchUrl", "/diary/list");
        return "message";
    }

    @GetMapping("/view/{id}")
    public String diaryView(@PathVariable("id") Integer id, Model model) {
        Diary diary = diaryRepository.findById(id).get();
        model.addAttribute("diary", diary);
        return "diaryview";
    }

    @GetMapping("/delete")
    public String delete(Integer id, Model model) {
        diaryRepository.deleteById(id);
        model.addAttribute("message", "글 삭제 성공!");
        model.addAttribute("searchUrl", "/diary/list");
        return "message";
    }

    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Model model) {
        Diary diary = diaryRepository.findById(id).get();
        model.addAttribute("diary", diary);
        return "diarymodify";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, Diary diary, Model model) {
        Diary modified = diaryRepository.findById(id).get();

        modified.setTitle(diary.getTitle());
        modified.setContent(diary.getContent());

        diaryRepository.save(modified);

        model.addAttribute("message", "글 수정이 완료되었습니다!");
        model.addAttribute("searchUrl", "/diary/list");

        return "message";
    }
}
