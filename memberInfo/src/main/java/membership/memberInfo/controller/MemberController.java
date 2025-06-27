package membership.memberInfo.controller;

import membership.memberInfo.domain.Member;
import membership.memberInfo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/new")
    public String form(Model model){
        model.addAttribute("member", new Member());
        return "members/form";
    }

    @PostMapping("/members")
    public String save(@ModelAttribute Member member){
        memberService.save(member);
        return "redirect:/members/list";
    }

    @GetMapping("/members/list")
    public String list(Model model){
        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }

    @GetMapping("/members/{id}/edit")
    // PathVariable : 위의 id를 그대로 불러올 수 있는
    public String editForm(@PathVariable Long id, Model model){
        Member member = memberService.findById(id).orElseThrow();
        model.addAttribute("member", member);   // edit.html에 있는 object의 이름과 동일
        return "members/edit";
    }

    @PostMapping("/members/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Member member){
        member.setId(id);
        memberService.save(member);
        return "redirect:/members/list";
    }

    @PostMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id){
        memberService.deleteById(id);
        return "redirect:/members/list";
    }
}
