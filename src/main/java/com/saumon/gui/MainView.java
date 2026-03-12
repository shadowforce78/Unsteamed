package com.saumon.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {

    private BorderPane root;
    private Button activeNav;

    public Parent build() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + App.BG + ";");
        root.setLeft(buildSidebar());
        showCatalog();
        return root;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: " + App.SIDEBAR + ";");

        // ── Header ──
        VBox header = new VBox(4);
        header.setPadding(new Insets(24, 16, 20, 16));
        header.setStyle("-fx-border-color: transparent transparent #2a3d52 transparent; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("UNSTEAMED");
        logo.setStyle("-fx-text-fill: " + App.ACCENT + "; -fx-font-size: 18; -fx-font-weight: bold;");

        Label welcome = new Label("Bonjour, " + App.getCurrentUser().getUsername());
        welcome.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");
        welcome.setWrapText(true);

        header.getChildren().addAll(logo, welcome);

        // ── Nav buttons ──
        VBox nav = new VBox(4);
        nav.setPadding(new Insets(16, 10, 16, 10));

        Button btnCatalog = navBtn("  Catalogue de jeux", false);
        Button btnLibrary = navBtn("  Ma Bibliotheque", false);
        Button btnHelp    = navBtn("  Aide", false);

        btnCatalog.setOnAction(e -> { setActive(btnCatalog); showCatalog(); });
        btnLibrary.setOnAction(e -> { setActive(btnLibrary); showLibrary(); });
        btnHelp.setOnAction(e    -> { setActive(btnHelp);    showHelp();    });

        nav.getChildren().addAll(btnCatalog, btnLibrary, btnHelp);

        // ── Spacer ──
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // ── Logout ──
        Button btnLogout = new Button("  Deconnexion");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6b6b; "
                         + "-fx-font-size: 13; -fx-padding: 10 16; -fx-cursor: hand; "
                         + "-fx-alignment: CENTER_LEFT; -fx-background-radius: 6;");
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(btnLogout.getStyle().replace("transparent", "#2a1e1e")));
        btnLogout.setOnMouseExited(e  -> btnLogout.setStyle(btnLogout.getStyle().replace("#2a1e1e", "transparent")));
        btnLogout.setOnAction(e -> App.logout());

        VBox bottom = new VBox(btnLogout);
        bottom.setPadding(new Insets(8, 10, 20, 10));
        bottom.setStyle("-fx-border-color: #2a3d52 transparent transparent transparent; -fx-border-width: 1 0 0 0;");

        sidebar.getChildren().addAll(header, nav, spacer, bottom);

        // Select Catalogue by default
        setActive(btnCatalog);
        return sidebar;
    }

    private Button navBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        applyNavStyle(btn, active);
        btn.setOnMouseEntered(e -> { if (btn != activeNav) btn.setStyle(btn.getStyle().replace("transparent", App.CARD_HVR)); });
        btn.setOnMouseExited(e  -> { if (btn != activeNav) applyNavStyle(btn, false); });
        return btn;
    }

    private void applyNavStyle(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: " + App.ACCENT + "; -fx-text-fill: white; "
                       + "-fx-font-size: 13; -fx-padding: 10 16; -fx-cursor: hand; "
                       + "-fx-background-radius: 6;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + App.TEXT + "; "
                       + "-fx-font-size: 13; -fx-padding: 10 16; -fx-cursor: hand; "
                       + "-fx-background-radius: 6;");
        }
    }

    private void setActive(Button btn) {
        if (activeNav != null) applyNavStyle(activeNav, false);
        activeNav = btn;
        applyNavStyle(btn, true);
    }

    // ── Content switching ─────────────────────────────────────────────────────

    private void setContent(Node content) {
        root.setCenter(content);
    }

    private void showCatalog() {
        setContent(new CatalogView().build());
    }

    private void showLibrary() {
        setContent(new LibraryView().build());
    }

    private void showHelp() {
        setContent(new HelpView().build());
    }
}

