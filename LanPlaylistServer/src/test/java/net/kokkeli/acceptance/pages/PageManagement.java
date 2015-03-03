package net.kokkeli.acceptance.pages;

import net.kokkeli.settings.ISettings;

import org.openqa.selenium.WebDriver;

public class PageManagement extends BasePage{

    public PageManagement(ISettings settings, WebDriver driver) {
        super(settings, driver, "management");
    }
}
