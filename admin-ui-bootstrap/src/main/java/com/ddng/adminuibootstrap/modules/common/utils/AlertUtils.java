package com.ddng.adminuibootstrap.modules.common.utils;

import com.ddng.adminuibootstrap.modules.common.enums.AlertType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AlertUtils {

    public static final String MESSAGE_ATTR_KEY = "message";
    public static final String ALERT_TYPE_ATTR_KEY = "alertType";

    private AlertUtils() {
    }

    public static RedirectAttributes alert(RedirectAttributes redirectAttributes, AlertType alertType, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, alertType);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }


    public static RedirectAttributes alertInfo(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, AlertType.INFO);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }

    public static RedirectAttributes alertSuccess(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, AlertType.SUCCESS);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }

    public static RedirectAttributes alertFail(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, AlertType.WARNING);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }

    public static RedirectAttributes alertError(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, AlertType.DANGER);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }

    public static RedirectAttributes alertPrimary(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(ALERT_TYPE_ATTR_KEY, AlertType.PRIMARY);
        redirectAttributes.addFlashAttribute(MESSAGE_ATTR_KEY, message);
        return redirectAttributes;
    }

    public static void alertInfo(Model model, String message) {
        model.addAttribute(ALERT_TYPE_ATTR_KEY, AlertType.INFO);
        model.addAttribute(MESSAGE_ATTR_KEY, message);
    }

    public static void alertSuccess(Model model, String message) {
        model.addAttribute(ALERT_TYPE_ATTR_KEY, AlertType.SUCCESS);
        model.addAttribute(MESSAGE_ATTR_KEY, message);
    }

    public static void alertFail(Model model, String message) {
        model.addAttribute(ALERT_TYPE_ATTR_KEY, AlertType.WARNING);
        model.addAttribute(MESSAGE_ATTR_KEY, message);
    }

    public static void alertError(Model model, String message) {
        model.addAttribute(ALERT_TYPE_ATTR_KEY, AlertType.DANGER);
        model.addAttribute(MESSAGE_ATTR_KEY, message);
    }

    public static void alertPrimary(Model model, String message) {
        model.addAttribute(ALERT_TYPE_ATTR_KEY, AlertType.PRIMARY);
        model.addAttribute(MESSAGE_ATTR_KEY, message);
    }
}
