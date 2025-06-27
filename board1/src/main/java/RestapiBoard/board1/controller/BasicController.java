package RestapiBoard.board1.controller;

import RestapiBoard.board1.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasicController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/form")
    public String formPage() {
        return "form";
    }

// -> list1과 맞음
//    @GetMapping("/list")
//    public String formPage1() {
//        return "list";
//    }

    // list와 맞음
    @GetMapping("/list")
    public String formPage1(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "list";
    }

    @GetMapping("/update")
    // 파라미터로 날아오는 아이디로 수정
    public String updatePage(@RequestParam Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "update";
    }
    
// list의 update/2 주소 이용하는 방법. 이게 더 많이 쓰임
//    @GetMapping("/update/{id}")
//    public String updatePage(@PathVariable Long id, Model model) {
//        model.addAttribute("board", boardService.findById(id));
//        return "update";
//    }

 // list의 detail/2 주소 이용하는 방법. 이게 더 많이 쓰임
    @GetMapping("detail/{id}")
    public String detailPage(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "detail";
    }

}
