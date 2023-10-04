package com.duckduckgoose.app.controllers;

import com.duckduckgoose.app.models.database.Honk;
import com.duckduckgoose.app.models.database.Member;
import com.duckduckgoose.app.models.request.HonkRequest;
import com.duckduckgoose.app.models.view.HonksViewModel;
import com.duckduckgoose.app.services.HonkService;
import com.duckduckgoose.app.util.AuthHelper;
import com.duckduckgoose.app.util.PaginationHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HonkController {

    private final HonkService honkService;

    @Autowired
    public HonkController(HonkService honkService) {
        this.honkService = honkService;
    }

    @RequestMapping(value = "/honks", method = RequestMethod.GET)
    public ModelAndView getHonksPage(
            @RequestParam (value = "search", required = false) String search,
            @RequestParam (value = "filter", required = false) String filter,
            @RequestParam (value = "page", required = false) Integer page,
            RedirectAttributes redirectAttributes
    ) {
        Page<Honk> honks;
        Pageable pageRequest = PaginationHelper.getPageRequest(page);
        if (filter != null && filter.equals("followedUsers")) {
            if (!AuthHelper.isAuthenticated()) {
                redirectAttributes.addFlashAttribute("redirected", true);
                return new ModelAndView("redirect:/login");
            }
            Member followerMember = AuthHelper.getAuthenticatedMember();
            honks = honkService.getFollowedMemberHonks(followerMember, search, pageRequest);
        } else {
            honks = honkService.getHonks(search, pageRequest);
        }

        HonksViewModel honksViewModel = new HonksViewModel(honks, search, filter);
        return new ModelAndView("honks", "model", honksViewModel);
    }

    @RequestMapping(value = "/honk", method = RequestMethod.GET)
    public ModelAndView getHonkCreationPage() {
        return new ModelAndView("honk", "honkRequest", new HonkRequest());
    }

    @RequestMapping(value = "/honk", method = RequestMethod.POST)
    public ModelAndView onHonkSubmit(
            @Valid HonkRequest honkRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("honk", "honkRequest", honkRequest);
        }
        Member author = AuthHelper.getAuthenticatedMember();
        honkService.createHonk(author, honkRequest);
        redirectAttributes.addFlashAttribute("flashMessage", "Honk posted successfully.");
        return new ModelAndView("redirect:/honks");
    }
}
