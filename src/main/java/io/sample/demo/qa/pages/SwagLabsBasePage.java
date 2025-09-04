package io.sample.demo.qa.pages;

import com.microsoft.playwright.Page;
import io.sample.demo.qa.commons.WebBasePage;

public sealed class SwagLabsBasePage extends WebBasePage permits SwagLabsCartPage,
        SwagLabsCheckoutCompletePage, SwagLabsCheckoutOverviewPage, SwagLabsCheckoutPage,
        SwagLabsHomePage, SwagLabsLoginPage, SwagLabsProductPage {

    public SwagLabsBasePage(Page basePage) {
        super(basePage);
    }
}