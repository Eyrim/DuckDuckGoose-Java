package com.duckduckgoose.app.controllers;

import com.duckduckgoose.app.models.database.Honk;
import com.duckduckgoose.app.models.database.Member;
import com.duckduckgoose.app.models.view.MemberViewModel;
import com.duckduckgoose.app.models.view.MembersViewModel;
import com.duckduckgoose.app.services.HonkService;
import com.duckduckgoose.app.services.MemberService;
import com.duckduckgoose.app.util.AuthHelper;
import com.duckduckgoose.app.util.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {

    private final MemberService memberService;

    private final HonkService honkService;

    @Autowired
    public MemberController(MemberService memberService, HonkService honkService) {
        this.memberService = memberService;
        this.honkService = honkService;
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public ModelAndView getMembersPage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam (value = "page", required = false) Integer page
    ) {
        Page<Member> members;
        Pageable pageRequest = PaginationHelper.getPageRequest(page);
        members = memberService.getMembers(search, pageRequest);


        MembersViewModel membersViewModel = new MembersViewModel(members, search, filter);
        return new ModelAndView("members", "model", membersViewModel);
    }

    @RequestMapping(value = "/member/{username}", method = RequestMethod.GET)
    public ModelAndView getMemberPage(
            @PathVariable String username,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam (value = "page", required = false) Integer page
    ) {
        Page<Member> members;
        Pageable pageRequest = PaginationHelper.getPageRequest(page);
        members = memberService.getMembers(search, pageRequest);


        MembersViewModel membersViewModel = new MembersViewModel(members, search, filter);
        return new ModelAndView("members", "model", membersViewModel);
    }
}
