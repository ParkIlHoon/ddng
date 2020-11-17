package com.ddng.adminuibootstrap.modules.customer.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping("/customer")
public class CustomerController
{
    private static final String BASE_PATH = "/Users/1hoon/workspace/file_ddng";

    @GetMapping("/search")
    public String searchCustomerForm (Model model)
    {
        return "customer/main";
    }

    @GetMapping("/new")
    public String newCustomerForm (Model model)
    {
        return "customer/new-customer";
    }

    @GetMapping("/family")
    public String familyForm (Model model)
    {
        return "customer/family";
    }

    @GetMapping("/**")
    public ResponseEntity getFile (HttpServletRequest request) throws Exception
    {
        String filePath = BASE_PATH + File.separator + StringUtils.cleanPath(request.getRequestURI().replace("/customer/", ""));

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
