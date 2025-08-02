package com.roha.movies.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Layout
// tag::snippet[]
public class AppLayoutBasic extends AppLayout {

    public AppLayoutBasic() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Ik wil nu naar de film");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }
    // end::snippet[]

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Me", "/me",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Cities", "/cities", VaadinIcon.CART.create())
//                new SideNavItem("Customers", "/customers", VaadinIcon.USER_HEART.create()),
//                new SideNavItem("Products", "/products", VaadinIcon.PACKAGE.create()),
//                new SideNavItem("Documents", "/documents", VaadinIcon.RECORDS.create()),
//                new SideNavItem("Tasks", "/tasks", VaadinIcon.LIST.create()),
//                new SideNavItem("Analytics", "/analytics", VaadinIcon.CHART.create()));
        );
        return sideNav;
    }

    // tag::snippet[]
}
// end::snippet[]
