package com.saumon.gui;

import com.saumon.core.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.*;

public class LoginView {

    private boolean loginMode = true;

    public Parent build() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + App.BG + ";");

        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(440);
        card.setPadding(new Insets(48, 48, 48, 48));
        card.setStyle("-fx-background-color: " + App.SIDEBAR + "; -fx-background-radius: 10;");

        // ── Logo ─────────────────────────────────────────────────────────────
        Label logo = new Label("UNSTEAMED");
        logo.setStyle("-fx-text-fill: " + App.ACCENT + "; -fx-font-size: 30; -fx-font-weight: bold;");

        Label sub = new Label("Game Store");
        sub.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 14;");

        // ── Mode toggle ───────────────────────────────────────────────────────
        Button btnLogin    = new Button("Login");
        Button btnRegister = new Button("Register");
        applyTabStyle(btnLogin, true);
        applyTabStyle(btnRegister, false);
        HBox tabs = new HBox(0, btnLogin, btnRegister);
        tabs.setAlignment(Pos.CENTER);

        // ── Fields ────────────────────────────────────────────────────────────
        TextField usernameField = styledField("Username");
        TextField emailField    = styledField("Email");
        PasswordField passField = styledPass("Password");
        PasswordField confField = styledPass("Confirm Password");

        // ── Error label ───────────────────────────────────────────────────────
        Label error = new Label();
        error.setStyle("-fx-text-fill: #ff5555; -fx-font-size: 12;");
        error.setWrapText(true);
        error.setMaxWidth(340);

        // ── Action button ─────────────────────────────────────────────────────
        Button actionBtn = new Button("LOGIN");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        styleActionBtn(actionBtn);

        // ── Form container ────────────────────────────────────────────────────
        VBox form = new VBox(12);
        VBox.setVgrow(form, Priority.ALWAYS);

        Runnable buildLogin = () -> {
            form.getChildren().setAll(emailField, passField, actionBtn, error);
            actionBtn.setText("LOGIN");
        };
        Runnable buildRegister = () -> {
            form.getChildren().setAll(usernameField, emailField, passField, confField, actionBtn, error);
            actionBtn.setText("REGISTER");
        };
        buildLogin.run();

        btnLogin.setOnAction(e -> {
            loginMode = true;
            applyTabStyle(btnLogin, true);
            applyTabStyle(btnRegister, false);
            error.setText("");
            buildLogin.run();
        });
        btnRegister.setOnAction(e -> {
            loginMode = false;
            applyTabStyle(btnLogin, false);
            applyTabStyle(btnRegister, true);
            error.setText("");
            buildRegister.run();
        });

        // ── Actions ───────────────────────────────────────────────────────────
        actionBtn.setOnAction(e -> {
            error.setText("");
            if (loginMode) {
                String email = emailField.getText().trim();
                String pass  = passField.getText();
                if (email.isEmpty() || pass.isEmpty()) { error.setText("Veuillez remplir tous les champs."); return; }
                User user = App.userRepo.getUserByEmail(email);
                if (user != null && user.getPassword().equals(pass)) {
                    App.setCurrentUser(user);
                    App.showMain();
                } else {
                    error.setText("Email ou mot de passe incorrect.");
                }
            } else {
                String username = usernameField.getText().trim();
                String email    = emailField.getText().trim();
                String pass     = passField.getText();
                String conf     = confField.getText();
                if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) { error.setText("Veuillez remplir tous les champs."); return; }
                if (!pass.equals(conf)) { error.setText("Les mots de passe ne correspondent pas."); return; }
                if (App.userRepo.getUserByEmail(email) != null) { error.setText("Un compte avec cet email existe déjà."); return; }
                User newUser = new User(0, username, email, pass, new Date(), new ArrayList<>(), new HashMap<>());
                App.userRepo.registerUser(newUser);
                App.setCurrentUser(newUser);
                App.showMain();
            }
        });

        card.getChildren().addAll(logo, sub, tabs, form);
        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);
        return root;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyTabStyle(Button btn, boolean active) {
        String base = "-fx-font-size: 14; -fx-padding: 9 28; -fx-cursor: hand; -fx-border-color: transparent;";
        if (active) {
            btn.setStyle(base + "-fx-background-color: " + App.ACCENT + "; -fx-text-fill: white; -fx-background-radius: 4;");
        } else {
            btn.setStyle(base + "-fx-background-color: " + App.CARD + "; -fx-text-fill: " + App.DIM + "; -fx-background-radius: 4;");
        }
    }

    private void styleActionBtn(Button btn) {
        String s = "-fx-background-color: " + App.ACCENT + "; -fx-text-fill: white; "
                 + "-fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12; "
                 + "-fx-background-radius: 5; -fx-cursor: hand;";
        btn.setStyle(s);
        btn.setOnMouseEntered(e -> btn.setStyle(s.replace(App.ACCENT, "#3ab0ff")));
        btn.setOnMouseExited(e  -> btn.setStyle(s));
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle("-fx-background-color: " + App.CARD + "; -fx-text-fill: " + App.TEXT
                + "; -fx-prompt-text-fill: " + App.DIM + "; -fx-padding: 10; "
                + "-fx-background-radius: 5; -fx-font-size: 13;");
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }

    private PasswordField styledPass(String prompt) {
        PasswordField f = new PasswordField();
        f.setPromptText(prompt);
        f.setStyle("-fx-background-color: " + App.CARD + "; -fx-text-fill: " + App.TEXT
                + "; -fx-prompt-text-fill: " + App.DIM + "; -fx-padding: 10; "
                + "-fx-background-radius: 5; -fx-font-size: 13;");
        f.setMaxWidth(Double.MAX_VALUE);
        return f;
    }
}

