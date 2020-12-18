package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerDto;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;

/**
 * <h1>고객 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController
{
    private final ServiceProperties serviceProperties;
    private final CustomerTemplate customerTemplate;

    /**
     * 고객 조회 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String searchForm (Model model)
    {
        return "customer/search/main";
    }

    /**
     * 고객 등록 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/register")
    public String registerForm (Model model)
    {
        model.addAttribute("registerForm", new RegisterForm());
        return "customer/register/main";
    }

    /**
     * 고객 등록 액션 요청
     * @param registerForm
     * @param errors
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public String registerAction (@Valid @ModelAttribute RegisterForm registerForm, Errors errors) throws Exception
    {
        if (errors.hasErrors())
        {
            return "customer/register/main";
        }
        customerTemplate.createCustomer(registerForm);
        return "redirect:/customer/register";
    }

    /**
     * 가족 관리 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/family")
    public String familyForm (Model model)
    {
        return "customer/family/main";
    }

    /**
     * 고객 사진 요청
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/**")
    public ResponseEntity getFile (HttpServletRequest request) throws Exception
    {
        String filePath = serviceProperties.getFile() + File.separator + StringUtils.cleanPath(request.getRequestURI().replace("/customer/", ""));

        File file = new File(filePath);

        if (file.exists())
        {
            String contentType = "";
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            request.getServletContext().getMimeType(file.getAbsolutePath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
